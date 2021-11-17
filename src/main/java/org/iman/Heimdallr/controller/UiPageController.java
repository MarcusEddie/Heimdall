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
import org.iman.Heimdallr.entity.UiPage;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.UiPageService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.UiPageVo;
import org.iman.Heimdallr.vo.PageInfo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.Response;
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
@RequestMapping("uiPage")
public class UiPageController {

    private static final Logger log = LoggerFactory.getLogger(UiPageController.class);
    @Resource
    private UiPageService uiPageService;
    @Resource
    private AppStructureService appStructureService;
    
    @PostMapping("getPagesByAppId")
    public Response<List<UiPageVo>> getPagesById(@RequestBody UiPageVo req) {
        Response<List<UiPageVo>> resp = new Response<List<UiPageVo>>();

        System.out.println("GET PageS BY APP ID: " + req.toString());
        List<UiPage> queryRs = uiPageService.getByParams(req);
        if (!CollectionUtils.sizeIsEmpty(queryRs)) {
            try {
                Iterator<UiPage> it = queryRs.iterator();
                List<UiPageVo> rs = new ArrayList<UiPageVo>();
                while (it.hasNext()) {
                    UiPage page = (UiPage) it.next();
                    Optional<UiPageVo> vo = BeanUtils.copy(page, UiPageVo.class);
                    rs.add(vo.get());
                }
                resp.setData(rs);
            } catch (DataConversionException e) {
                if (log.isErrorEnabled()) {
                    log.error("Query failed", e);
                }
                resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
            }

        }
        
        return resp.mkTime();
    }

    @PostMapping("addOnePage")
    public Response<UiPageVo> addOnePage(@RequestBody UiPageVo req) {
        Response<UiPageVo> resp = new Response<UiPageVo>();

        System.out.println("Add one Page:" + req.toString());
        UiPage rs = uiPageService.saveOnePage(req);
        try {
            Optional<UiPageVo> rtn = BeanUtils.copy(rs, UiPageVo.class);
            resp.setData(rtn.get());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }
    
    @PostMapping("getPageById")
    public Response<UiPageVo> getPageById(@RequestBody UiPageVo req) {
        Response<UiPageVo> resp = new Response<UiPageVo>();

        Optional<UiPage> rs = uiPageService.getById(req.getId());
        try {
            if (rs.isPresent()) {
                Optional<UiPageVo> rtn = BeanUtils.copy(rs.get(), UiPageVo.class);
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
    
    @PostMapping("getPagesByParams")
    public Response<List<UiPageVo>> getPagesByParams(@JsonParam UiPageVo params,
            @JsonParam PageInfo pageInfo) {
        Response<List<UiPageVo>> resp = new Response<List<UiPageVo>>();
        System.out.println("get Pages by params: " + params.toString());
        try {
            Pagination<UiPage> pages = uiPageService.getByParams(params, pageInfo.toPage());
            List<UiPageVo> rs = new ArrayList<UiPageVo>();
            if (!CollectionUtils.sizeIsEmpty(pages.getList())) {
                Iterator<UiPage> it = pages.getList().iterator();
                while (it.hasNext()) {
                    UiPage page = (UiPage) it.next();
                    UiPageVo cpObj = packageVo(page);
                    rs.add(cpObj);
                }
            }
            
            resp.setData(rs);
            resp.setTotal(pages.getTotal());
            resp.setCurrent(pages.getCurrent());
            resp.setPageSize(pages.getPageSize());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }
    
    @PostMapping("deleteById")
    public Response<UiPageVo> deleteById(@RequestBody UiPageVo req) {
        Response<UiPageVo> resp = new Response<UiPageVo>();
        System.out.println("DELETE BY ID"+req.toString());
        try {
            Optional<UiPage> delRs = uiPageService.delete(req);
            if (delRs.isPresent()) {
                UiPageVo vo = BeanUtils.copy(delRs.get(), UiPageVo.class).get();
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
        System.out.println("DELETE BY IDS"+req.toString());
        try {
            List<UiPageVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = uiPageService.delete(vos);
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
    public Response<UiPageVo> activateById(@RequestBody UiPageVo req) {
        Response<UiPageVo> resp = new Response<UiPageVo>();
        System.out.println("ACTIVATE BY ID"+req.toString());
        try {
            Optional<UiPage> rs = uiPageService.activate(req);
            if (rs.isPresent()) {
                UiPageVo vo = BeanUtils.copy(rs.get(), UiPageVo.class).get();
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
        System.out.println("ACTIVATE BY IDS"+req.toString());
        try {
            List<UiPageVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = uiPageService.activate(vos);
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
    public Response<UiPageVo> deactivateById(@RequestBody UiPageVo req) {
        Response<UiPageVo> resp = new Response<UiPageVo>();
        System.out.println("ACTIVATE BY IDS"+req.toString());
        try {
            Optional<UiPage> rs = uiPageService.deactivate(req);
            if (rs.isPresent()) {
                UiPageVo vo = BeanUtils.copy(rs.get(), UiPageVo.class).get();
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
        System.out.println("ACTIVATE BY IDS"+req.toString());
        try {
            List<UiPageVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = uiPageService.deactivate(vos);
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
    
    @PostMapping("updateById")
    public Response<UiPageVo> updateById(@RequestBody UiPageVo req) {
        Response<UiPageVo> resp = new Response<UiPageVo>();
        System.out.println("UPDATE BY ID === >   " + req.toString());
        try {
            Optional<UiPage> rs = uiPageService.updateById(req);
            if (rs.isPresent()) {
                Optional<UiPageVo> vo = BeanUtils.copy(rs.get(), UiPageVo.class);
                resp.setData(vo.get());
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    private UiPageVo packageVo(UiPage page) throws DataConversionException {
        UiPageVo cpObj = BeanUtils.copy(page, UiPageVo.class).get();
        Optional<AppStructure> app = appStructureService.getById(cpObj.getAppId());
        if (app.isPresent()) {
            cpObj.setAppName(app.get().getName());
        }
        
        Optional<AppStructure> module = appStructureService.getById(cpObj.getModuleId());
        if (module.isPresent()) {
            cpObj.setModuleName(module.get().getName());
        }
        
        Optional<AppStructure> func = appStructureService.getById(cpObj.getFunctionId());
        if (func.isPresent()) {
            cpObj.setFunctionName(func.get().getName());
        }
        
        cpObj.setState(TestCaseState.valueOf(page.getEnabled()));
        return cpObj;
    }
    
    private List<UiPageVo> convertToTestCaseVo(String idsVal) {
        List<UiPageVo> rs = new ArrayList<UiPageVo>();
        if (!StringUtils.isNotBlank(idsVal)) {
            return Collections.emptyList();
        }
        String[] ids = idsVal.split(",");
        try {
            for (int i = 0; i < ids.length; i++) {
                UiPageVo vo = new UiPageVo(Long.valueOf(ids[i]));
                rs.add(vo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert id from String to Long failed", e);
        }
        return rs;
    }
}
