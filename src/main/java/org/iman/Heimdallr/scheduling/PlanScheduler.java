/**
 * 
 */
package org.iman.Heimdallr.scheduling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.iman.Heimdallr.constants.enums.HttpMethod;
import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TestType;
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.ApiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.entity.TestPlan;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.service.ExecHistoryService;
import org.iman.Heimdallr.service.TaskQueueService;
import org.iman.Heimdallr.service.TestPlanService;
import org.iman.Heimdallr.utils.ObjectUtils;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.ExecHistoryVo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TaskQueueVo;
import org.iman.Heimdallr.vo.TestPlanVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author ey
 *
 */
@Component
//@EnableScheduling
public class PlanScheduler {

    private static final Logger log = LoggerFactory.getLogger(PlanScheduler.class);

    @Autowired
    private TaskQueueService taskQueueService;
    @Autowired
    private TestPlanService testPlanService;
    @Autowired
    private APIService apiService;
    @Autowired
    private ExecHistoryService execHistoryService;

    private Map<String, String> globalVariable = new HashedMap<String, String>();

    @Scheduled(fixedRate = 5000)
    public void scheduling() {
        while (true) {
            globalVariable.clear();
            execute();
        }
    }

    private void execute() {
        try {
            Long start = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
            Optional<TaskQueue> task = taskQueueService.requestATask(TestType.API_TEST);
            if (task.isPresent()) {
                System.out.println(task.toString());
                TestPlanVo plan = new TestPlanVo(task.get().getPlanId());
                Page page = new Page();
                page.setCurrent(1);
                page.setPageSize(Integer.MAX_VALUE);
                Pagination<ApiTestCase> cases = testPlanService.getAPITestCasesByPlanId(plan, page);
//                System.out.println(cases.getList().size());
                Map<String, List<String>> errors = new HashedMap<String, List<String>>();
                if (task.get().getTriggerTime().isBefore(LocalDateTime.now())) {
                    errors = exec(cases.getList());
                } else {
                    errors = waitAndExec(task.get(), cases.getList());
                }
                Long period = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - start;
                responseToHub(task.get(), errors, period);
            } else {
                System.out.println("Dont find a ready task, try find one seconds later");
            }
            Thread.sleep(1000);
        } catch (DataConversionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Boolean responseToHub(TaskQueue task, Map<String, List<String>> errors, Long period) {
        ExecHistoryVo history = new ExecHistoryVo();
        history.setPlanId(task.getPlanId());
        history.setPlanName(task.getPlanName());
        if (null != errors && errors.size() == 0) {
            history.setTaskState(TaskState.SUCCESS);
        } else {
            history.setTaskState(TaskState.FAILED);
        }
        history.setTriggerTime(task.getTriggerTime());
        history.setPriority(task.getPriority());
        history.setTestType(task.getTestType());
        history.setType(task.getType());
        ObjectNode summary = new ObjectMapper().createObjectNode();
//        {"Total minutes has ran": 0," "Total success number of cases": 0, "Total nonexecution number of cases": 0}
        TestPlan plan = testPlanService.getById(task.getPlanId()).get();
        summary.put("Total number of cases", plan.getCaseSize());
        summary.put("Total failed number of cases", errors.size());
        summary.put("Total success number of cases", plan.getCaseSize() - errors.size());
        Long mins = period / 60000;
        if (period % 60000 != 0) {
            mins += 1;
        }
        summary.put("Total minutes has ran", mins);
        history.setDetails(summary);
        try {
            TaskQueueVo vo = new TaskQueueVo(task.getId());
            vo.setTaskState(history.getTaskState());
            taskQueueService.delete(vo);
            execHistoryService.save(history, errors);
            return true;
        } catch (DataConversionException e) {
            return false;
        }
    }

    private Map<String, List<String>> exec(List<ApiTestCase> cases) {
        Map<String, List<String>> finalRs = new HashedMap<String, List<String>>();
        if (!CollectionUtils.sizeIsEmpty(cases)) {
            Iterator<ApiTestCase> it = cases.iterator();
            Map<String, Object> map = new HashedMap<String, Object>();
            ObjectNode params = new ObjectMapper().createObjectNode();
            ObjectNode res = null;
            while (it.hasNext()) {
                ApiTestCase tstCase = (ApiTestCase) it.next();
                List<String> assertRs = new ArrayList<String>();
                if (StringUtils.isNotBlank(tstCase.getSteps())) {
                    String[] steps = tstCase.getSteps().split("\n");
                    for (int i = 0; i < steps.length; i++) {
                        System.out.println(steps[i]);
                        String[] command = steps[i].strip().split(" ");
                        if (command[0].equals("Call")) {
                            if (command[2].equals("With") & command[7].equals("Get")) {
                                JsonNode rs = doCallAnApi(command[1],
                                        tstCase.getParameters().get("Parameters"),
                                        tstCase.getParameters().get("Headers"));

                                map.put(command[command.length - 1].substring(1,
                                        command[command.length - 1].length() - 1), rs);
                                System.out.println(map);
                            } else if (command[2].equals("With") && command[7].equals("Get")) {
                                doCallAnApiWithoutRtn(command[1],
                                        tstCase.getParameters().get("Parameters"),
                                        tstCase.getParameters().get("Headers"));
                            }

                        } else if (command[0].strip().equals("Assign")) {
                            ObjectNode selfParams = doParseParams(command, tstCase.getParameters(),
                                    map, globalVariable);
                            params.putAll(selfParams);
                            System.out.println(" < " + params.toPrettyString());
                        } else if (command[0].strip().equals("Save")
                                && command[1].strip().equals("Global")) {
                            doSaveGlobalVariables(command, globalVariable, map);
                            System.out.println("globalVariable: " + globalVariable.toString());
                        } else if (command[0].strip().equals("Send")
                                && command[1].strip().equals("Request")) {
                            res = (ObjectNode) doSendReq(tstCase, params);
                            String[] respCommand = steps[i + 1].strip().split(" ");
                            String resultName = doReceiveResp(respCommand);
                            map.put(resultName, res);
                            System.out.println("final result " + map.toString());
                        } else if (command[0].strip().equals("Receive")
                                && command[1].strip().equals("Response")) {
                            System.out.println();
                        } else if (command[0].strip().equals("Assert")) {
//                        //Assert [finalResult.success]  Is true
                            // Assert [variable] = [value]
                            // Assert [variable] IsNot null
                            String result = "";
                            if (command[2].equals("Is") || command[2].equals("IsNot")) {
                                result = doAssertIsTrueAndFalse(command, res, map);
                            } else if (Integer.valueOf((command[2]).charAt(0)).compareTo(61) == 0
                                    || command[2].equals("!=")) {
                                result = doAssertEquals(command, map);
                            }
                            if (StringUtils.isNotBlank(result)) {
                                assertRs.add(result);
                            }

                            System.out.println("Assert is true: " + result);
                        }
                    }
                }
                finalRs.put(tstCase.getName(), assertRs);
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println(assertRs.toString());
            }

        }
        return finalRs;
    }

    private void doSaveGlobalVariables(String[] commands, Map<String, String> globalVariable,
            Map<String, Object> map) {
        String key = commands[2].substring(1, commands[2].length() - 1);
        if (commands[3].equals("=")) {
            globalVariable.put(key, commands[4].substring(1, commands[4].length() - 1));
            System.out.println("153 === " + globalVariable.toString());
        } else if (commands[3].equals("From")) {
            String apiKey = commands[4].substring(1, commands[4].indexOf("."));
            String rsKey = commands[4].substring(commands[4].indexOf(".") + 1,
                    commands[4].length() - 1);
            System.out.println("rskeys: " + rsKey + ", " + apiKey);
            globalVariable.put(key, ((JsonNode) map.get(apiKey)).get("data").get(rsKey).asText());
            System.out.println("159 === " + globalVariable.toString());
        }
    }

    private String doReceiveResp(String[] commands) {
        String rsName = commands[2].substring(1, commands[2].length() - 1);
        return rsName;
    }

    private JsonNode doSendReq(ApiTestCase tstCase, ObjectNode params) {
        JsonNode res = doCallAnApi(tstCase.getApiId(), params, null);
        return res;
    }

    private ObjectNode doParseParams(String[] commands, JsonNode parameters,
            Map<String, Object> map, Map<String, String> globalVariable) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        String key = commands[1].substring(1, commands[1].length() - 1);
        if (commands[3].indexOf(".") > 0 && commands[3].indexOf("Global") < 0) {
            String apiKey = commands[3].substring(1, commands[3].indexOf("."));
            String rsKey = commands[3].substring(commands[3].indexOf(".") + 1,
                    commands[3].length() - 1);
            System.out.println("rskeys: " + rsKey + ", " + apiKey);
            node.put(commands[1].substring(1, commands[1].length() - 1),
                    ((JsonNode) map.get(apiKey)).get("data").get(rsKey));
        } else if (commands[3].indexOf(".") > 0 && commands[3].indexOf("Global") == 1) {
            String rsKey = commands[3].substring(commands[3].indexOf(".") + 1,
                    commands[3].length() - 1);
            node.put(commands[1].substring(1, commands[1].length() - 1), globalVariable.get(rsKey));
            System.out.println("170 : " + rsKey);
        } else if (commands[3].indexOf(".") < 0) {
            System.out.println("params 152: " + parameters.toString());
            node.put(commands[1].substring(1, commands[1].length() - 1), parameters.get(key));
        }

        return node;
    }

    private JsonNode doCallAnApi(String apiName, JsonNode params, JsonNode headers) {
        JsonNode node = new ObjectMapper().createObjectNode();
        ApiDeclarationVo criteria = new ApiDeclarationVo();
        criteria.setName(apiName.substring(1, apiName.length() - 1));
        ApiDeclaration api = apiService.getByParams(criteria).get(0);
        if (api.getMethod().equals(HttpMethod.POST)) {
            node = doPost(api, params.toPrettyString());
        }
        return node;
    }

    private JsonNode doCallAnApi(Long apiId, JsonNode params, JsonNode headers) {
        JsonNode node = new ObjectMapper().createObjectNode();
        ApiDeclarationVo criteria = new ApiDeclarationVo(apiId);
        ApiDeclaration api = apiService.getByParams(criteria).get(0);
        if (api.getMethod().equals(HttpMethod.POST)) {
            node = doPost(api, params.toPrettyString());
        }
        return node;
    }

    private String doAssertEquals(String[] commands, Map<String, Object> map) {
        String key = commands[1].substring(1, commands[1].length() - 1);
        String respName = key.substring(0, key.indexOf("."));
        String fieldName = key.substring(key.indexOf(".") + 1);
        System.out.println("do assert ===== " + respName + " <>  " + fieldName + " <>  "
                + Integer.valueOf((commands[2]).charAt(0)));

        try {
            if (Integer.valueOf((commands[2]).charAt(0)).compareTo(61) == 0) {
                JsonNode resp = new ObjectMapper().readTree(map.get(respName).toString());
                Object realVal = resp.get(fieldName).asText();
                Object expectVal = commands[3].subSequence(1, commands[3].length() - 1);
                System.out.println(realVal.toString() + " === " + expectVal);
                if (!realVal.toString().contentEquals(expectVal.toString())) {
                    return logFailure(fieldName, expectVal, realVal, null);
                }
            } else {
                JsonNode resp = new ObjectMapper().readTree(map.get(respName).toString());
                Object realVal = resp.get(fieldName).asText();
                Object expectVal = commands[3].subSequence(1, commands[3].length() - 1);
                System.out.println(realVal + "===" + expectVal);
                System.out.println(realVal.toString().contentEquals(expectVal.toString()) + " === "
                        + realVal.toString().equals(expectVal.toString()));
                if (realVal.toString().contentEquals(expectVal.toString())) {

                    return logFailure(fieldName, expectVal, realVal, "not equals ");
                }
            }
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String doAssertIsTrueAndFalse(String[] commands, ObjectNode node,
            Map<String, Object> map) {
        String key = commands[1].substring(1, commands[1].length() - 1);
        String respName = key.substring(0, key.indexOf("."));
        String fieldName = key.substring(key.indexOf(".") + 1);
        System.out.println("do assert ===== " + respName + " <>  " + fieldName);
        try {
            if (commands[2].equals("Is")
                    && (commands[3].equals("true") || commands[3].equals("false"))) {
                JsonNode resp = new ObjectMapper().readTree(map.get(respName).toString());
                System.out.println(
                        resp.get(fieldName).asBoolean() + " === " + Boolean.valueOf(commands[3]));
                Boolean realVal = resp.get(fieldName).asBoolean();
                Boolean expectVal = Boolean.valueOf(commands[3]);
                if (realVal ^ expectVal) {
                    return logFailure(fieldName, expectVal, realVal, null);
                }
            } else if (commands[2].equals("IsNot")
                    && (commands[3].equals("true") || commands[3].equals("false"))) {
                JsonNode resp = new ObjectMapper().readTree(map.get(respName).toString());
                Boolean realVal = resp.get(fieldName).asBoolean();
                Boolean expectVal = Boolean.valueOf(commands[3]);
                System.out.println(realVal + " === " + expectVal + " -----" + (!expectVal));
                if (!expectVal) {
                    // isnot false : true & true
                    if (!(realVal && (!expectVal))) {
                        return logFailure(fieldName, !expectVal, realVal, null);
                    }
                } else {
                    // isnot true : false & false
                    if (!((!realVal) && expectVal)) {
                        return logFailure(fieldName, !expectVal, realVal, null);
                    }
                }
            } else if (commands[2].equals("Is") && commands[3].equals("Null")) {
                JsonNode resp = new ObjectMapper().readTree(map.get(respName).toString());
                System.out.println(
                        resp.get(fieldName).asBoolean() + " === " + Boolean.valueOf(commands[3]));
                Object realVal = resp.get(fieldName);
                if (ObjectUtils.isNotEmpty(realVal)) {
                    return logFailure(fieldName, "Null", realVal, null);
                }

            } else if (commands[2].equals("IsNot") && commands[3].equals("Null")) {
                JsonNode resp = new ObjectMapper().readTree(map.get(respName).toString());
                Object realVal = resp.get(fieldName);
                if (ObjectUtils.isEmpty(realVal)) {
                    return logFailure(fieldName, "Not Null", "Null", null);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String logFailure(String fieldName, Object expectVal, Object realVal, String tip) {
        StringBuilder rs = new StringBuilder();
        if (StringUtils.isNotBlank(tip)) {
            rs.append("Field").append("[").append(fieldName).append("] is expected for ")
                    .append(tip).append("\"").append(expectVal.toString()).append("\" but got \"")
                    .append(realVal.toString()).append("\"");
        } else {
            rs.append("Field").append("[").append(fieldName).append("] is expected for \"")
                    .append(expectVal.toString()).append("\" but got \"").append(realVal.toString())
                    .append("\"");
        }
        return rs.toString();
    }

    private JsonNode doPost(ApiDeclaration api, String requestBody) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(api.getUrl() + api.getPath());
        StringEntity entity = new StringEntity(requestBody, "UTF-8");
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json;charset=utf8");
        CloseableHttpResponse resp = null;
        JsonNode rs = new ObjectMapper().createObjectNode();
        try {
            resp = client.execute(post);
            HttpEntity respEntity = resp.getEntity();
            rs = new ObjectMapper().readTree(EntityUtils.toString(respEntity));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    private Map<String, List<String>> waitAndExec(TaskQueue task, List<ApiTestCase> cases) {
        while (true) {
            if (task.getTriggerTime().equals(LocalDateTime.now())
                    || task.getTriggerTime().isAfter(LocalDateTime.now())) {
                break;
            }
        }

        Map<String, List<String>> errors = exec(cases);
        return errors;
    }

    private void doCallAnApiWithoutRtn(String apiName, JsonNode params, JsonNode headers) {

    }
}
