/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.iman.Heimdallr.config.annotations.JsonParam;
import org.iman.Heimdallr.constants.Parameters;
import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.constants.enums.ResultCheckMode;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.ApiTestCase;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.entity.DBConnectionInfo;
import org.iman.Heimdallr.entity.TestPlan;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.DBConnectionInfoService;
import org.iman.Heimdallr.service.TestPlanService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.TestPlanVo;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.ApiTestCaseVo;
import org.iman.Heimdallr.vo.PageInfo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/plan")
public class TestPlanController {

    private static final Logger log = LoggerFactory.getLogger(TestPlanController.class);

    @Resource
    private TestPlanService testPlanService;
    @Resource
    private APIService apiService;
    @Resource
    private AppStructureService appStructureService;
    @Resource
    private DBConnectionInfoService dbConnectionInfoService;

    @PostMapping("addOnePlan")
    public Response<TestPlanVo> addOneAPI(@RequestBody TestPlanVo req) {
        Response<TestPlanVo> resp = new Response<TestPlanVo>();

        System.out.println("Add one api:" + req.toString());
        try {
            TestPlan rs = testPlanService.save(req);
            Optional<TestPlanVo> rtn = BeanUtils.copy(rs, TestPlanVo.class);
            resp.setData(rtn.get());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }
    
    @PostMapping("getPlanByParams")
    public Response<List<TestPlanVo>> getPlanByParams(@JsonParam TestPlanVo params,
            @JsonParam PageInfo pageInfo) {
        System.out.println("GET page TEST CASE BY PRAMS: " + params.toString());
        Response<List<TestPlanVo>> resp = new Response<List<TestPlanVo>>();
        try {
            Pagination<TestPlan> cases = testPlanService.getByParams(params,
                    pageInfo.toPage());
            List<TestPlanVo> vos = new ArrayList<TestPlanVo>();
            if (!CollectionUtils.sizeIsEmpty(cases.getList())) {
                Iterator<TestPlan> it = cases.getList().iterator();
                while (it.hasNext()) {
                    TestPlan plan = (TestPlan) it.next();
                    TestPlanVo cpObj = packageVo(plan);
                    vos.add(cpObj);
                }
            }

            resp.setData(vos);
            resp.setTotal(cases.getTotal());
            resp.setCurrent(cases.getCurrent());
            resp.setPageSize(cases.getPageSize());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("getPlanById")
    public Response<TestPlanVo> getPlanById(@RequestBody TestPlanVo req) {
        Response<TestPlanVo> resp = new Response<TestPlanVo>();

        Optional<TestPlan> rs = testPlanService.getById(req.getId());
        try {
            if (rs.isPresent()) {
                Optional<TestPlanVo> rtn = BeanUtils.copy(rs.get(), TestPlanVo.class);
                resp.setData(rtn.get());
            }
//            else {
//                resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_IS_DELETED);
//            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }
    
    @PostMapping("updateById")
    public Response<TestPlanVo> updateById(@RequestBody TestPlanVo req) {
        Response<TestPlanVo> resp = new Response<TestPlanVo>();
        System.out.println("UPDATE BY ID === >   " + req.toString());
        try {
            Optional<TestPlan> rs = testPlanService.update(req);
            if (rs.isPresent()) {
                Optional<TestPlanVo> vo = BeanUtils.copy(rs.get(), TestPlanVo.class);
                resp.setData(vo.get());
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }
    
    @PostMapping("deleteById")
    public Response<TestPlanVo> deleteById(@RequestBody TestPlanVo req) {
        Response<TestPlanVo> resp = new Response<TestPlanVo>();
        System.out.println("DELETE BY ID" + req.toString());
        try {
            Optional<TestPlan> delRs = testPlanService.delete(req);
            if (delRs.isPresent()) {
                TestPlanVo vo = BeanUtils.copy(delRs.get(), TestPlanVo.class).get();
                resp.setData(vo);
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }

    @PostMapping("deleteByIds")
    public Response<Integer> deleteByIds(@RequestBody ObjectNode req) {
        Response<Integer> resp = new Response<Integer>();
        System.out.println("DELETE BY IDS" + req.toString());
        try {
            List<TestPlanVo> vos = convertToVo(req.get(Parameters.IDS).asText());
            Integer rs = testPlanService.delete(vos);
            resp.setData(rs);
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Data conversion failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("Parameter is invalid", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.PARAMETERS_ARE_INVALID);
        }
        return resp;
    }

    @PostMapping("activateById")
    public Response<TestPlanVo> activateById(@RequestBody TestPlanVo req) {
        Response<TestPlanVo> resp = new Response<TestPlanVo>();
        System.out.println("ACTIVATE BY ID" + req.toString());
        try {
            Optional<TestPlan> rs = testPlanService.activate(req);
            if (rs.isPresent()) {
                TestPlanVo vo = BeanUtils.copy(rs.get(), TestPlanVo.class).get();
                resp.setData(vo);
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }

    @PostMapping("activateByIds")
    public Response<Integer> activateByIds(@RequestBody ObjectNode req) {
        Response<Integer> resp = new Response<Integer>();
        System.out.println("ACTIVATE BY IDS" + req.toString());
        try {
            List<TestPlanVo> vos = convertToVo(req.get(Parameters.IDS).asText());
            Integer rs = testPlanService.activate(vos);
            resp.setData(rs);
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Data conversion failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("Parameter is invalid", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.PARAMETERS_ARE_INVALID);
        }

        return resp;
    }

    @PostMapping("deactivateById")
    public Response<TestPlanVo> deactivateById(@RequestBody TestPlanVo req) {
        Response<TestPlanVo> resp = new Response<TestPlanVo>();
        System.out.println("ACTIVATE BY IDS" + req.toString());
        try {
            Optional<TestPlan> rs = testPlanService.deactivate(req);
            if (rs.isPresent()) {
                TestPlanVo vo = BeanUtils.copy(rs.get(), TestPlanVo.class).get();
                resp.setData(vo);
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }

    @PostMapping("deactivateByIds")
    public Response<Integer> deactivateByIds(@RequestBody ObjectNode req) {
        Response<Integer> resp = new Response<Integer>();
        System.out.println("ACTIVATE BY IDS" + req.toString());
        try {
            List<TestPlanVo> vos = convertToVo(req.get(Parameters.IDS).asText());
            Integer rs = testPlanService.deactivate(vos);
            resp.setData(rs);
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Data conversion failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("Parameter is invalid", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.PARAMETERS_ARE_INVALID);
        }

        return resp;
    }

    @PostMapping("cronExplain")
    public Response<ObjectNode> cronExplain(@RequestBody ObjectNode req) {
        Response<ObjectNode> resp = new Response<ObjectNode>();
        System.out.println("cronExplain BY IDS" + req.toString());
        try {
            String cron = req.get(Parameters.CRON_EXPRESSION).asText();
            CronDefinition cronDefinition = CronDefinitionBuilder
                    .instanceDefinitionFor(CronType.QUARTZ);
            // Create a parser based on provided definition
            CronParser parser = new CronParser(cronDefinition);
            // Create a descriptor for a specific Locale
            CronDescriptor descriptor = CronDescriptor.instance(Locale.UK);
            String description = descriptor.describe(parser.parse(cron));
//             "1 2 0,12 3 4 ? 2021-2023"
            ObjectNode node = new ObjectMapper().createObjectNode();
            node.put("content", description);
            resp.setData(node);
        } catch (IllegalArgumentException e) {
            if (log.isErrorEnabled()) {
                log.error("Parameter is invalid", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.PARAMETERS_ARE_INVALID);
        }

        return resp;
    }
    
    @PostMapping("getAPITestCaseByPlanId")
    public Response<List<ApiTestCaseVo>> getTestCaseByPlanId(@JsonParam TestPlanVo params,
            @JsonParam PageInfo pageInfo) {
        System.out.println("GET page TEST CASE BY PRAMS: " + params.toString());
        Response<List<ApiTestCaseVo>> resp = new Response<List<ApiTestCaseVo>>();
        try {
            Pagination<ApiTestCase> cases = testPlanService.getAPITestCasesByPlanId(params,
                    pageInfo.toPage());
            List<ApiTestCaseVo> vos = new ArrayList<ApiTestCaseVo>();
            if (!CollectionUtils.sizeIsEmpty(cases.getList())) {
                Iterator<ApiTestCase> it = cases.getList().iterator();
                while (it.hasNext()) {
                    ApiTestCase plan = (ApiTestCase) it.next();
                    ApiTestCaseVo cpObj = packageAPITestCaseVo(plan);
                    vos.add(cpObj);
                }
            }

            resp.setData(vos);
            resp.setTotal(cases.getTotal());
            resp.setCurrent(cases.getCurrent());
            resp.setPageSize(cases.getPageSize());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    private TestPlanVo packageVo(TestPlan tc) throws DataConversionException {
        TestPlanVo cpObj = BeanUtils.copy(tc, TestPlanVo.class).get();
//        Optional<UiPage> page = uiPageService.getById(tc.getPageId());
//        if (page.isPresent()) {
//            UiPageVo vo = BeanUtils.copy(page.get(), UiPageVo.class).get();
//            Optional<AppStructure> app = appStructureService.getById(vo.getAppId());
//            if (app.isPresent()) {
//                vo.setAppName(app.get().getName());
//            }
//            Optional<AppStructure> module = appStructureService.getById(vo.getModuleId());
//            if (module.isPresent()) {
//                vo.setModuleName(module.get().getName());
//            }
//            Optional<AppStructure> func = appStructureService.getById(vo.getFunctionId());
//            if (func.isPresent()) {
//                vo.setFunctionName(func.get().getName());
//            }
//            cpObj.setPageId(vo.getId());
//            cpObj.setPage(vo);
//        } else {
//            // TODO: handle the api is not found
//        }

        cpObj.setState(TestCaseState.valueOf(tc.getEnabled()));
        return cpObj;
    }
    
    private ApiTestCaseVo packageAPITestCaseVo(ApiTestCase tc) throws DataConversionException {
        ApiTestCaseVo cpObj = BeanUtils.copy(tc, ApiTestCaseVo.class).get();
        Optional<ApiDeclaration> api = apiService.getById(tc.getApiId());
        if (api.isPresent()) {
            ApiDeclarationVo vo = BeanUtils.copy(api.get(), ApiDeclarationVo.class).get();
            Optional<AppStructure> app = appStructureService.getById(vo.getAppId());
            if (app.isPresent()) {
                vo.setAppName(app.get().getName());
            }
            Optional<AppStructure> module = appStructureService.getById(vo.getModuleId());
            if (module.isPresent()) {
                vo.setModuleName(module.get().getName());
            }
            Optional<AppStructure> func = appStructureService.getById(vo.getFunctionId());
            if (func.isPresent()) {
                vo.setFunctionName(func.get().getName());
            }
            
            cpObj.setApi(vo);
        } else {
            // TODO: handle the api is not found
        }

        if (ResultCheckMode.DB_DATA.equals(tc.getResultCheckMode())) {
            cpObj.setExpectedResult(null);
            Optional<DBConnectionInfo> connInfo = dbConnectionInfoService.getById(tc.getDbConnId());
            if (connInfo.isPresent()) {
                cpObj.setDbConnName(connInfo.get().getName());
            } else {
                // TODO: handle the connInfo is not found
            }
        } else {
            cpObj.setDbConnId(null);
            cpObj.setQuerySql(null);
        }
        
        cpObj.setState(TestCaseState.valueOf(tc.getEnabled()));
        return cpObj;
    }
    
    private List<TestPlanVo> convertToVo(String idsVal) {
        List<TestPlanVo> rs = new ArrayList<TestPlanVo>();
        if (!StringUtils.isNotBlank(idsVal)) {
            return Collections.emptyList();
        }
        String[] ids = idsVal.split(",");
        try {
            for (int i = 0; i < ids.length; i++) {
                TestPlanVo vo = new TestPlanVo(Long.valueOf(ids[i]));
                rs.add(vo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert id from String to Long failed", e);
        }
        return rs;
    }
    
//    public static void main(String[] args) {
//        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
//     // Create a parser based on provided definition
//        CronParser parser = new CronParser(cronDefinition);
//     // Create a descriptor for a specific Locale
//        CronDescriptor descriptor = CronDescriptor.instance(Locale.US);
//
//        // Parse some expression and ask descriptor for description
//        String description = descriptor.describe(parser.parse("1 2 0,12 3 4 ? 2021-2023"));
//        System.out.println(description);
//        // Description will be: "every 45 seconds"
//
////        1 2 0 4 * ? 
//        ExecutionTime executionTime = ExecutionTime.forCron(parser.parse("1 2 0 4 * ? "));
//        ZonedDateTime now = ZonedDateTime.now();
//        Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);
//        System.out.println(nextExecution.get().toLocalDateTime().toString());
//    }
}
