/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;

import org.iman.Heimdallr.DataNotFoundException;
import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.entity.TestCase;
import org.iman.Heimdallr.service.CaseGeneralInfoService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.Response;
import org.iman.Heimdallr.vo.TestCaseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/testcase")
public class TestCaseController {

    private static final Logger log = LoggerFactory.getLogger(TestCaseController.class);

    @Resource
    private CaseGeneralInfoService caseGeneralInfoService;

    @PostMapping("/saveOneCase")
    public Response<TestCaseVo> saveOneCase(@Validated @RequestBody TestCaseVo vo) {
        Response<TestCaseVo> resp = new Response<TestCaseVo>();
        System.out.println("Save One Case: " + vo.toString());

        try {
            TestCase rs = caseGeneralInfoService.saveOneCase(vo);
            vo = BeanUtils.copy(rs, TestCaseVo.class);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            if (log.isErrorEnabled()) {
                log.error("Convert TestCaseVo from TestCase failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        } catch (DataNotFoundException e1) {
            if (log.isErrorEnabled()) {
                log.error("Save single test case failed because of ", e1);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.REQUIRED_DATA_NOT_AVAILABLE);
        }
        resp.setData(vo);
        return resp.mkTime();
    }
    
    @PostMapping("saveMultiCases")
    public Response<TestCaseVo> saveMutilCases(@RequestBody ObjectNode req){
        Response<TestCaseVo> resp = new Response<TestCaseVo>();
        System.out.println("====>>>  save multi cases:" + req.toString());
        return resp;
    }
}
