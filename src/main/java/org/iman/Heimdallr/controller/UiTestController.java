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
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.UiTestCase;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.entity.UiPage;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.UiTestCaseService;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.CaseGeneralInfoService;
import org.iman.Heimdallr.service.DBConnectionInfoService;
import org.iman.Heimdallr.service.UiPageService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.UiTestCaseVo;
import org.iman.Heimdallr.vo.PageInfo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.Response;
import org.iman.Heimdallr.vo.TestCaseVo;
import org.iman.Heimdallr.vo.UiPageVo;
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
@RequestMapping("/uiTest")
public class UiTestController {

    private static final Logger log = LoggerFactory.getLogger(UiTestController.class);
    @Resource
    private UiTestCaseService uiTestCaseService;
    @Resource
    private UiPageService uiPageService;
    @Resource
    private DBConnectionInfoService dbConnectionInfoService;
    @Resource
    private CaseGeneralInfoService caseGeneralInfoService;
    @Resource
    private AppStructureService appStructureService;
    
    @PostMapping("saveUiTest")
    public Response<UiTestCaseVo> saveUiTest(@RequestBody UiTestCaseVo vo) {
        Response<UiTestCaseVo> resp = new Response<UiTestCaseVo>();
        System.out.println("SAVE PAGE TEST: " + vo.toString());
        try {
           UiTestCase testCase = uiTestCaseService.save(vo);
           Optional<UiTestCaseVo> voRs = BeanUtils.copy(testCase, UiTestCaseVo.class);
           resp.setData(voRs.get());
           
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("getUiTestCasesByParams")
    public Response<List<UiTestCaseVo>> getUiTestCasesByParams(@JsonParam UiTestCaseVo params,
            @JsonParam PageInfo pageInfo) {
        System.out.println("GET page TEST CASE BY PRAMS: " + params.toString());
        Response<List<UiTestCaseVo>> resp = new Response<List<UiTestCaseVo>>();
        try {
            Pagination<UiTestCase> cases = uiTestCaseService.getByParams(params,
                    pageInfo.toPage());
            List<UiTestCaseVo> vos = new ArrayList<UiTestCaseVo>();
            if (!CollectionUtils.sizeIsEmpty(cases.getList())) {
                Iterator<UiTestCase> it = cases.getList().iterator();
                while (it.hasNext()) {
                    UiTestCase testCase = (UiTestCase) it.next();
                    UiTestCaseVo cpObj = packageVo(testCase);
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
   
    @PostMapping("updateUiTestCaseDetails")
    public Response<UiTestCaseVo> updateUiTestCaseDetails(@RequestBody UiTestCaseVo vo) {
        System.out.println("updateUiTestCaseDetails: " + vo.toString());
        Response<UiTestCaseVo> resp = new Response<UiTestCaseVo>();
        
        try {
            UiTestCase updatedObj = uiTestCaseService.update(vo);
            resp.setData(BeanUtils.copy(updatedObj, UiTestCaseVo.class).get());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("delUiTestCaseById")
    public Response<UiTestCaseVo> delUiTestCaseById(@RequestBody UiTestCaseVo vo) {
        System.out.println("delUiTestCaseById: " + vo.toString());
        Response<UiTestCaseVo> resp = new Response<UiTestCaseVo>();
        
        try {
            Optional<UiTestCase> delRs = uiTestCaseService.delete(vo);
            if (delRs.isPresent()) {
                resp.setData(BeanUtils.copy(delRs.get(), UiTestCaseVo.class).get());
            }
            
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("delUiTestCaseByIds")
    public Response<Integer> delUiTestCaseByIds(@RequestBody ObjectNode req) {
        System.out.println("delUiTestCaseByIds: " + req.toString());
        Response<Integer> resp = new Response<Integer>();
        
        try {
            List<UiTestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = uiTestCaseService.delete(vos);
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
    
    @PostMapping("activateUiTestCaseById")
    public Response<UiTestCaseVo> activateUiTestCaseById(@RequestBody UiTestCaseVo vo) {
        System.out.println("activateUiTestCaseById: " + vo.toString());
        Response<UiTestCaseVo> resp = new Response<UiTestCaseVo>();
        
        try {
            Optional<UiTestCase> delRs = uiTestCaseService.activate(vo);
            if (delRs.isPresent()) {
                resp.setData(BeanUtils.copy(delRs.get(), UiTestCaseVo.class).get());
            }
            
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("activateUiTestCaseByIds")
    public Response<Integer> activateUiTestCaseByIds(@RequestBody ObjectNode req) {
        System.out.println("activateUiTestCaseByIds: " + req.toString());
        Response<Integer> resp = new Response<Integer>();
        
        List<UiTestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
        try {
            Integer rs = uiTestCaseService.activate(vos);
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
    
    @PostMapping("deactivateUiTestCaseById")
    public Response<UiTestCaseVo> deactivateUiTestCaseById(@RequestBody UiTestCaseVo vo) {
        System.out.println("deactivateUiTestCaseById: " + vo.toString());
        Response<UiTestCaseVo> resp = new Response<UiTestCaseVo>();
        
        try {
            Optional<UiTestCase> delRs = uiTestCaseService.deactivate(vo);
            if (delRs.isPresent()) {
                resp.setData(BeanUtils.copy(delRs.get(), UiTestCaseVo.class).get());
            }
            
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    @PostMapping("deactivateUiTestCaseByIds")
    public Response<Integer> deactivateUiTestCaseByIds(@RequestBody ObjectNode req) {
        System.out.println("deactivateUiTestCaseByIds: " + req.toString());
        Response<Integer> resp = new Response<Integer>();
        
        List<UiTestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
        try {
            Integer rs = uiTestCaseService.deactivate(vos);
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
    
    private UiTestCaseVo packageVo(UiTestCase tc) throws DataConversionException {
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

        List<TestCase> rs = caseGeneralInfoService.getByParams(new TestCaseVo(cpObj.getGeneralCaseId()));
        if (!CollectionUtils.sizeIsEmpty(rs)) {
            cpObj.setGeneralCaseName(rs.get(0).getName());
        }
        
        cpObj.setState(TestCaseState.valueOf(tc.getEnabled()));
        return cpObj;
    }
    
    private List<UiTestCaseVo> convertToTestCaseVo(String idsVal) {
        List<UiTestCaseVo> rs = new ArrayList<UiTestCaseVo>();
        if (!StringUtils.isNotBlank(idsVal)) {
            return Collections.emptyList();
        }
        String[] ids = idsVal.split(",");
        try {
            for (int i = 0; i < ids.length; i++) {
                UiTestCaseVo vo = new UiTestCaseVo(Long.valueOf(ids[i]));
                rs.add(vo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert id from String to Long failed", e);
        }
        return rs;
    }
}
