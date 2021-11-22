/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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
import org.iman.Heimdallr.entity.ExecHistory;
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.entity.UiPage;
import org.iman.Heimdallr.entity.UiTestCase;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.CaseGeneralInfoService;
import org.iman.Heimdallr.service.DBConnectionInfoService;
import org.iman.Heimdallr.service.TaskQueueService;
import org.iman.Heimdallr.service.UiPageService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.TaskQueueVo;
import org.iman.Heimdallr.vo.UiPageVo;
import org.iman.Heimdallr.vo.UiTestCaseVo;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.ApiTestCaseVo;
import org.iman.Heimdallr.vo.PageInfo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.Response;
import org.iman.Heimdallr.vo.TestCaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/queue")
public class TaskQueueController {

    private static final Logger log = LoggerFactory.getLogger(TaskQueueController.class);

    @Resource
    private TaskQueueService taskQueueService;
    @Resource
    private APIService apiService;
    @Resource
    private AppStructureService appStructureService;
    @Resource
    private DBConnectionInfoService dbConnectionInfoService;
    @Resource
    private UiPageService uiPageService;
    @Resource
    private CaseGeneralInfoService caseGeneralInfoService;

    @PostMapping("addToQueue")
    public Response<TaskQueueVo> addToQueue(@RequestBody TaskQueueVo req) {
        Response<TaskQueueVo> resp = new Response<TaskQueueVo>();

        System.out.println("Add one api:" + req.toString());
        try {
            TaskQueue rs = taskQueueService.save(req);
            Optional<TaskQueueVo> rtn = BeanUtils.copy(rs, TaskQueueVo.class);
            resp.setData(rtn.get());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }

    @PostMapping("getQueueByParams")
    public Response<List<TaskQueueVo>> getQueueByParams(@JsonParam TaskQueueVo params,
            @JsonParam PageInfo pageInfo) {
        System.out.println("GET queue BY PRAMS: " + params.toString());
        Response<List<TaskQueueVo>> resp = new Response<List<TaskQueueVo>>();
        try {
            Pagination<TaskQueue> cases = taskQueueService.getByParams(params, pageInfo.toPage());
            List<TaskQueueVo> vos = new ArrayList<TaskQueueVo>();
            if (!CollectionUtils.sizeIsEmpty(cases.getList())) {
                Iterator<TaskQueue> it = cases.getList().iterator();
                while (it.hasNext()) {
                    TaskQueue plan = (TaskQueue) it.next();
                    TaskQueueVo cpObj = packageVo(plan);
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

//    @PostMapping("getPlanById")
//    public Response<TaskQueueVo> getPlanById(@RequestBody TaskQueueVo req) {
//        Response<TaskQueueVo> resp = new Response<TaskQueueVo>();
//
//        Optional<TaskQueue> rs = taskQueueService.getById(req.getId());
//        try {
//            if (rs.isPresent()) {
//                Optional<TaskQueueVo> rtn = BeanUtils.copy(rs.get(), TaskQueueVo.class);
//                resp.setData(rtn.get());
//            }
////            else {
////                resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_IS_DELETED);
////            }
//        } catch (DataConversionException e) {
//            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
//        }
//
//        return resp.mkTime();
//    }

//    @PostMapping("updateById")
//    public Response<TaskQueueVo> updateById(@RequestBody TaskQueueVo req) {
//        Response<TaskQueueVo> resp = new Response<TaskQueueVo>();
//        System.out.println("UPDATE BY ID === >   " + req.toString());
//        try {
//            Optional<TaskQueue> rs = taskQueueService.update(req);
//            if (rs.isPresent()) {
//                Optional<TaskQueueVo> vo = BeanUtils.copy(rs.get(), TaskQueueVo.class);
//                resp.setData(vo.get());
//            }
//        } catch (DataConversionException e) {
//            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
//        }
//
//        return resp;
//    }

    @PostMapping("deleteById")
    public Response<TaskQueueVo> deleteById(@RequestBody TaskQueueVo req) {
        Response<TaskQueueVo> resp = new Response<TaskQueueVo>();
        System.out.println("DELETE BY ID" + req.toString());
        try {
            Optional<ExecHistory> delRs = taskQueueService.delete(req);
            if (delRs.isPresent()) {
                TaskQueueVo vo = BeanUtils.copy(delRs.get(), TaskQueueVo.class).get();
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
            List<TaskQueueVo> vos = convertToVo(req.get(Parameters.IDS).asText());
            Integer rs = taskQueueService.delete(vos);
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
    public Response<TaskQueueVo> activateById(@RequestBody TaskQueueVo req) {
        Response<TaskQueueVo> resp = new Response<TaskQueueVo>();
        System.out.println("ACTIVATE BY ID" + req.toString());
        try {
            Optional<TaskQueue> rs = taskQueueService.activate(req);
            if (rs.isPresent()) {
                TaskQueueVo vo = BeanUtils.copy(rs.get(), TaskQueueVo.class).get();
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
            List<TaskQueueVo> vos = convertToVo(req.get(Parameters.IDS).asText());
            Integer rs = taskQueueService.activate(vos);
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
    public Response<TaskQueueVo> deactivateById(@RequestBody TaskQueueVo req) {
        Response<TaskQueueVo> resp = new Response<TaskQueueVo>();
        System.out.println("ACTIVATE BY IDS" + req.toString());
        try {
            Optional<TaskQueue> rs = taskQueueService.deactivate(req);
            if (rs.isPresent()) {
                TaskQueueVo vo = BeanUtils.copy(rs.get(), TaskQueueVo.class).get();
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
            List<TaskQueueVo> vos = convertToVo(req.get(Parameters.IDS).asText());
            Integer rs = taskQueueService.deactivate(vos);
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

    private TaskQueueVo packageVo(TaskQueue tc) throws DataConversionException {
        TaskQueueVo cpObj = BeanUtils.copy(tc, TaskQueueVo.class).get();
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

    private UiTestCaseVo packageUIVo(UiTestCase tc) throws DataConversionException {
        UiTestCaseVo cpObj = BeanUtils.copy(tc, UiTestCaseVo.class).get();
        Optional<UiPage> page = uiPageService.getById(tc.getPageId());
        if (page.isPresent()) {
            UiPageVo vo = BeanUtils.copy(page.get(), UiPageVo.class).get();
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
            cpObj.setPageId(vo.getId());
            cpObj.setPage(vo);
        } else {
            // TODO: handle the api is not found
        }

        List<TestCase> rs = caseGeneralInfoService
                .getByParams(new TestCaseVo(cpObj.getGeneralCaseId()));
        if (!CollectionUtils.sizeIsEmpty(rs)) {
            cpObj.setGeneralCaseName(rs.get(0).getName());
        }

        cpObj.setState(TestCaseState.valueOf(tc.getEnabled()));
        return cpObj;
    }

    private List<TaskQueueVo> convertToVo(String idsVal) {
        List<TaskQueueVo> rs = new ArrayList<TaskQueueVo>();
        if (!StringUtils.isNotBlank(idsVal)) {
            return Collections.emptyList();
        }
        String[] ids = idsVal.split(",");
        try {
            for (int i = 0; i < ids.length; i++) {
                TaskQueueVo vo = new TaskQueueVo(Long.valueOf(ids[i]));
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
