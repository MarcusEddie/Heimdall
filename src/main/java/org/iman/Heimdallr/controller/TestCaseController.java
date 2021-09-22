/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.entity.MindRawData;
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.exception.DataNotFoundException;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.CaseGeneralInfoService;
import org.iman.Heimdallr.service.MindRawDataService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.MindRawDataVo;
import org.iman.Heimdallr.vo.PageInfo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.Response;
import org.iman.Heimdallr.vo.TestCaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/testcase")
public class TestCaseController {

    private static final Logger log = LoggerFactory.getLogger(TestCaseController.class);

    @Resource
    private CaseGeneralInfoService caseGeneralInfoService;
    @Resource
    private MindRawDataService mindRawDataService;
    @Resource
    private AppStructureService appStructureService;

    @PostMapping("/saveOneCase")
    public Response<TestCaseVo> saveOneCase(@Validated @RequestBody TestCaseVo req) {
        Response<TestCaseVo> resp = new Response<TestCaseVo>();

        Optional<TestCaseVo> vo = null;
        try {
            Optional<TestCase> rs = caseGeneralInfoService.saveOneCase(req);
            vo = BeanUtils.copy(rs, TestCaseVo.class);
        } catch (DataNotFoundException e1) {
            if (log.isErrorEnabled()) {
                log.error("Save single test case failed because of ", e1);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.REQUIRED_DATA_NOT_AVAILABLE);
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Conversion failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        resp.setData(vo.isPresent() ? vo.get() : null);
        return resp.mkTime();
    }

    @PostMapping("saveMultiCases")
    public Response<Integer> saveMutilCases(@RequestBody MindRawDataVo req) {
        Response<Integer> resp = new Response<Integer>();

        Optional<AppStructure> func = appStructureService.getById(req.getFunctionId());
        if (func.isEmpty()) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.REQUIRED_DATA_NOT_AVAILABLE);
            return resp;
        }
        Optional<AppStructure> module = appStructureService.getById(func.get().getRoot());
        if (module.isEmpty()) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.REQUIRED_DATA_NOT_AVAILABLE);
            return resp;
        }
        req.setModuleId(func.get().getRoot());
        req.setAppId(module.get().getRoot());
        ObjectNode node = new ObjectMapper().createObjectNode().putPOJO("data", req.getRawNode());
        req.setRawData(node.get("data").toString());

        try {
            Integer cnt = mindRawDataService.generateTestCases(req);
            resp.setData(cnt);
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Data conversion failed because of ", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }

    @PostMapping("/getRawDataByFunctionId")
    public Response<MindRawDataVo> getRawDataByFunctionId(@RequestBody MindRawDataVo req) {
        Response<MindRawDataVo> resp = new Response<MindRawDataVo>();
        Optional<MindRawData> raw = mindRawDataService.getByFunctionId(req.getFunctionId());
        if (raw.isPresent()) {
            try {
                Optional<MindRawDataVo> vo = BeanUtils.copy(raw.get(), MindRawDataVo.class);
                resp.setData(vo.isPresent() ? vo.get() : null);
            } catch (DataConversionException e) {
                if (log.isErrorEnabled()) {
                    log.error(e.getMessage());
                }
                resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
            }
        }

        return resp.mkTime();
    }

    @PostMapping("getTestCasesByParams")
    public Response<List<TestCaseVo>> getTestCasesByParams(@JsonParam TestCaseVo params,
            @JsonParam PageInfo pageInfo) {
        Response<List<TestCaseVo>> resp = new Response<List<TestCaseVo>>();

        try {
            Pagination<TestCase> rs = caseGeneralInfoService.getByParams(params, pageInfo.toPage());
            List<TestCaseVo> vos = new ArrayList<TestCaseVo>();
            if (!CollectionUtils.sizeIsEmpty(rs.getList())) {
                Iterator<TestCase> it = rs.getList().iterator();
                while (it.hasNext()) {
                    TestCase testCase = (TestCase) it.next();
                    Optional<TestCaseVo> vo = BeanUtils.copy(testCase, TestCaseVo.class);
                    if (vo.isPresent()) {
                        TestCaseVo vo2 = vo.get();
                        vo2.setState(TestCaseState.valueOf(testCase.getEnabled()));
                        vos.add(vo.get());
                    }
                }
            }
            resp.setCurrent(rs.getCurrent());
            resp.setTotal(rs.getTotal());
            resp.setPageSize(rs.getPageSize());
            resp.setData(vos);
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }

    @PostMapping("updateTestCase")
    public Response<TestCaseVo> updateTestCase(@RequestBody TestCaseVo req) {
        Response<TestCaseVo> resp = new Response<TestCaseVo>();
        try {
            Optional<TestCase> rs = caseGeneralInfoService.updateTestCase(req);
            if (rs.isPresent()) {
                Optional<TestCaseVo> vo = BeanUtils.copy(rs.get(), TestCaseVo.class);
                TestCaseVo vo2 = vo.get();
                vo2.setState(TestCaseState.valueOf(rs.get().getEnabled()));
                resp.setData(vo2);
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        return resp.mkTime();
    }

    @PostMapping("deactivateById")
    public Response<TestCaseVo> deactivateTestCaseById(@RequestBody TestCaseVo req) {
        Response<TestCaseVo> resp = new Response<TestCaseVo>();
        try {
            Optional<TestCase> rs = caseGeneralInfoService.deactivate(req);
            if (rs.isPresent()) {
                Optional<TestCaseVo> cpObj = BeanUtils.copy(rs.get(), TestCaseVo.class);
                resp.setData(cpObj.get());
            }
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Data conversion failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }

    @PostMapping("deactivateByIds")
    public Response<Integer> deactivateTestCaseByIds(@RequestBody ObjectNode req) {
        Response<Integer> resp = new Response<Integer>();
        List<TestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
        try {
            Integer rs = caseGeneralInfoService.deactivate(vos);
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

        return resp.mkTime();
    }

    @PostMapping("activateById")
    public Response<TestCaseVo> activateTestCaseById(@RequestBody TestCaseVo req) {
        Response<TestCaseVo> resp = new Response<TestCaseVo>();
        try {
            Optional<TestCase> rs = caseGeneralInfoService.activate(req);
            if (rs.isPresent()) {
                Optional<TestCaseVo> cpObj = BeanUtils.copy(rs.get(), TestCaseVo.class);
                resp.setData(cpObj.get());
            }
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Data conversion failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }

    @PostMapping("activateByIds")
    public Response<Integer> activateTestCaseByIds(@RequestBody ObjectNode req) {
        Response<Integer> resp = new Response<Integer>();
        List<TestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
        try {
            Integer rs = caseGeneralInfoService.activate(vos);
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

        return resp.mkTime();
    }

    @PostMapping("deleteById")
    public Response<TestCaseVo> deleteTestCaseById(@RequestBody TestCaseVo req) {
        Response<TestCaseVo> resp = new Response<TestCaseVo>();
        try {
            Optional<TestCase> rs = caseGeneralInfoService.delete(req);
            if (rs.isPresent()) {
                Optional<TestCaseVo> cpObj = BeanUtils.copy(rs.get(), TestCaseVo.class);
                resp.setData(cpObj.get());
            }
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Data conversion failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }

    @PostMapping("deleteByIds")
    public Response<Integer> deleteTestCaseByIds(@RequestBody ObjectNode req) {
        Response<Integer> resp = new Response<Integer>();
        try {
            List<TestCaseVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = caseGeneralInfoService.delete(vos);
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
        return resp.mkTime();
    }

    @GetMapping("getState")
    public Response<List<TestCaseState>> getTestCaseState() {
        Response<List<TestCaseState>> resp = new Response<List<TestCaseState>>();
        resp.setData(Arrays.asList(TestCaseState.values()));
        return resp.mkTime();
    }
    
    private List<TestCaseVo> convertToTestCaseVo(String idsVal) {
        List<TestCaseVo> rs = new ArrayList<TestCaseVo>();
        if (!StringUtils.isNotBlank(idsVal)) {
            return Collections.emptyList();
        }
        String[] ids = idsVal.split(",");
        try {
            for (int i = 0; i < ids.length; i++) {
                TestCaseVo vo = new TestCaseVo(Long.valueOf(ids[i]));
                rs.add(vo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert id from String to Long failed", e);
        }
        return rs;
    }
}
