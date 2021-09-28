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
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.service.ApiTestCaseService;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.CaseGeneralInfoService;
import org.iman.Heimdallr.service.DBConnectionInfoService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
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
@RequestMapping("/apiTest")
public class ApiTestController {

    private static final Logger log = LoggerFactory.getLogger(ApiTestController.class);
    @Resource
    private ApiTestCaseService apiTestCaseService;
    @Resource
    private APIService apiService;
    @Resource
    private DBConnectionInfoService dbConnectionInfoService;
    @Resource
    private CaseGeneralInfoService caseGeneralInfoService;
    @Resource
    private AppStructureService appStructureService;
    
    @PostMapping("saveApiTest")
    public Response<ApiTestCaseVo> saveApiTest(@RequestBody ApiTestCaseVo vo) {
        Response<ApiTestCaseVo> resp = new Response<ApiTestCaseVo>();
        System.out.println("SAVE API TEST: " + vo.toString());
        try {
           ApiTestCase testCase = apiTestCaseService.save(vo);
           Optional<ApiTestCaseVo> voRs = BeanUtils.copy(testCase, ApiTestCaseVo.class);
           resp.setData(voRs.get());
           
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("getApiTestCasesByParams")
    public Response<List<ApiTestCaseVo>> getApiTestCasesByParams(@JsonParam ApiTestCaseVo params,
            @JsonParam PageInfo pageInfo) {
        System.out.println("GET API TEST CASE BY PRAMS: " + params.toString());
        Response<List<ApiTestCaseVo>> resp = new Response<List<ApiTestCaseVo>>();
        try {
            Pagination<ApiTestCase> cases = apiTestCaseService.getByParams(params,
                    pageInfo.toPage());
            List<ApiTestCaseVo> vos = new ArrayList<ApiTestCaseVo>();
            if (!CollectionUtils.sizeIsEmpty(cases.getList())) {
                Iterator<ApiTestCase> it = cases.getList().iterator();
                while (it.hasNext()) {
                    ApiTestCase testCase = (ApiTestCase) it.next();
                    ApiTestCaseVo cpObj = packageVo(testCase);
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
   
    @PostMapping("updateApiTestCaseDetails")
    public Response<ApiTestCaseVo> updateApiTestCaseDetails(@RequestBody ApiTestCaseVo vo) {
        System.out.println("updateApiTestCaseDetails: " + vo.toString());
        Response<ApiTestCaseVo> resp = new Response<ApiTestCaseVo>();
        
        try {
            ApiTestCase updatedObj = apiTestCaseService.update(vo);
            resp.setData(BeanUtils.copy(updatedObj, ApiTestCaseVo.class).get());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("delApiTestCaseById")
    public Response<ApiTestCaseVo> delApiTestCaseById(@RequestBody ApiTestCaseVo vo) {
        System.out.println("delApiTestCaseById: " + vo.toString());
        Response<ApiTestCaseVo> resp = new Response<ApiTestCaseVo>();
        
        try {
            Optional<ApiTestCase> delRs = apiTestCaseService.delete(vo);
            if (delRs.isPresent()) {
                resp.setData(BeanUtils.copy(delRs.get(), ApiTestCaseVo.class).get());
            }
            
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("delApiTestCaseByIds")
    public Response<Integer> delApiTestCaseByIds(@RequestBody ObjectNode req) {
        System.out.println("delApiTestCaseByIds: " + req.toString());
        Response<Integer> resp = new Response<Integer>();
        
        try {
            List<ApiTestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = apiTestCaseService.delete(vos);
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
    
    @PostMapping("activateApiTestCaseById")
    public Response<ApiTestCaseVo> activateApiTestCaseById(@RequestBody ApiTestCaseVo vo) {
        System.out.println("activateApiTestCaseById: " + vo.toString());
        Response<ApiTestCaseVo> resp = new Response<ApiTestCaseVo>();
        
        try {
            Optional<ApiTestCase> delRs = apiTestCaseService.activate(vo);
            if (delRs.isPresent()) {
                resp.setData(BeanUtils.copy(delRs.get(), ApiTestCaseVo.class).get());
            }
            
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("activateApiTestCaseByIds")
    public Response<Integer> activateApiTestCaseByIds(@RequestBody ObjectNode req) {
        System.out.println("activateApiTestCaseByIds: " + req.toString());
        Response<Integer> resp = new Response<Integer>();
        
        List<ApiTestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
        try {
            Integer rs = apiTestCaseService.activate(vos);
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
    
    @PostMapping("deactivateApiTestCaseById")
    public Response<ApiTestCaseVo> deactivateApiTestCaseById(@RequestBody ApiTestCaseVo vo) {
        System.out.println("deactivateApiTestCaseById: " + vo.toString());
        Response<ApiTestCaseVo> resp = new Response<ApiTestCaseVo>();
        
        try {
            Optional<ApiTestCase> delRs = apiTestCaseService.deactivate(vo);
            if (delRs.isPresent()) {
                resp.setData(BeanUtils.copy(delRs.get(), ApiTestCaseVo.class).get());
            }
            
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("deactivateApiTestCaseByIds")
    public Response<Integer> deactivateApiTestCaseByIds(@RequestBody ObjectNode req) {
        System.out.println("deactivateApiTestCaseByIds: " + req.toString());
        Response<Integer> resp = new Response<Integer>();
        
        List<ApiTestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
        try {
            Integer rs = apiTestCaseService.deactivate(vos);
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
    
    private ApiTestCaseVo packageVo(ApiTestCase tc) throws DataConversionException {
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

        List<TestCase> rs = caseGeneralInfoService.getByParams(new TestCaseVo(cpObj.getGeneralCaseId()));
        if (!CollectionUtils.sizeIsEmpty(rs)) {
            cpObj.setGeneralCaseName(rs.get(0).getName());
        }
        
        cpObj.setState(TestCaseState.valueOf(tc.getEnabled()));
        return cpObj;
    }
    
    private List<ApiTestCaseVo> convertToTestCaseVo(String idsVal) {
        List<ApiTestCaseVo> rs = new ArrayList<ApiTestCaseVo>();
        if (!StringUtils.isNotBlank(idsVal)) {
            return Collections.emptyList();
        }
        String[] ids = idsVal.split(",");
        try {
            for (int i = 0; i < ids.length; i++) {
                ApiTestCaseVo vo = new ApiTestCaseVo(Long.valueOf(ids[i]));
                rs.add(vo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert id from String to Long failed", e);
        }
        return rs;
    }
}
