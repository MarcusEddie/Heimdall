package org.iman.Heimdallr.actuator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.iman.Heimdallr.constants.enums.TaskState;
import org.iman.Heimdallr.constants.enums.TestType;
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.ApiTestCase;
import org.iman.Heimdallr.entity.Page;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.service.TaskQueueService;
import org.iman.Heimdallr.service.TestPlanService;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.TestPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class Actuator {

    @Autowired
    private TaskQueueService taskQueueService;
    @Autowired
    private TestPlanService testPlanService;
    @Autowired
    private APIService apiService;

    @Scheduled(fixedRate = 5000)
    public void execute() {
        try {
            Optional<TaskQueue> task = taskQueueService.requestATask(TestType.API_TEST);
            if (task.isPresent()) {
                TestPlanVo plan = new TestPlanVo(task.get().getPlanId());
                Page page = new Page();
                page.setCurrent(1);
                page.setPageSize(Integer.MAX_VALUE);
                Pagination<ApiTestCase> cases = testPlanService.getAPITestCasesByPlanId(plan, page);
                if (task.get().getTaskState().equals(TaskState.DELAYED)) {
                    exec(cases.getList());
                } else {
                    waitAndExec(task.get(), cases.getList());
                }
            }
        } catch (DataConversionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    call apiName with parameters and headers and get resultName
//    1 .... many
//    setup parameter is resultName.parameter
//    setup parameter is parameter
//    setup parameter is global.parameter
//    1 .... many
//    send request
//    receive response
//    assert parameter is parameter
//    1 .... many
//    query dbconnName with SQL
//    assert parameter is parameter
//    1 .... many
//    save parameter as global parameter
    private void exec(List<ApiTestCase> cases) {
        if (!CollectionUtils.sizeIsEmpty(cases)) {
            Iterator<ApiTestCase> it = cases.iterator();
            while (it.hasNext()) {
                ApiTestCase tstCase = (ApiTestCase) it.next();
                if (StringUtils.isNotBlank(tstCase.getSteps())) {
                    String[] steps = tstCase.getSteps().split("\n");
                    for (int i = 0; i < steps.length; i++) {
                        String command = steps[i];
                        if (command.indexOf("call") == 0) {
                            if (command.indexOf("with") > 0 && command.indexOf("get") > 0) {
                                doCallAnApi(tstCase.getApiId(),
                                        tstCase.getParameters().get("parameters"),
                                        tstCase.getParameters().get("header"));
                            } else if (command.indexOf("with") > 0 && command.indexOf("get") < 0) {
                                doCallAnApiWithoutRtn(tstCase.getApiId(),
                                        tstCase.getParameters().get("parameters"),
                                        tstCase.getParameters().get("header"));
                            }
                        }

                    }
                }
            }
        }
    }

    private JsonNode doCallAnApi(Long apiId, JsonNode params, JsonNode headers) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        ApiDeclaration api = apiService.getById(apiId).get();
        
        return node;
    }

    private void doCallAnApiWithoutRtn(Long apiId, JsonNode params, JsonNode headers) {

    }

    private void doPost (ApiDeclaration api, String requestBody) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(api.getUrl());
        StringEntity entity = new StringEntity(requestBody, "UTF-8");
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json;charset=utf8");
        CloseableHttpResponse resp = null;
        try {
            resp = client.execute(post);
            HttpEntity respEntity = resp.getEntity();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private void waitAndExec(TaskQueue task, List<ApiTestCase> cases) {
        while (true) {
            if (task.getTriggerTime().equals(LocalDateTime.now())) {
                break;
            }
        }

        exec(cases);
    }

}
