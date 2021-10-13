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
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
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
@RequestMapping("/apis")
public class ApisController {

    private static final Logger log = LoggerFactory.getLogger(ApisController.class);

    @Resource
    private APIService apiService;
    @Resource
    private AppStructureService appStructureService;
    

    @PostMapping("getApisByAppId")
    public Response<List<ApiDeclarationVo>> getApisById(@RequestBody ApiDeclarationVo req) {
        Response<List<ApiDeclarationVo>> resp = new Response<List<ApiDeclarationVo>>();

        System.out.println("GET APIS BY APP ID: " + req.toString());
        List<ApiDeclaration> queryRs = apiService.getByParams(req);
        if (!CollectionUtils.sizeIsEmpty(queryRs)) {
            try {
                Iterator<ApiDeclaration> it = queryRs.iterator();
                List<ApiDeclarationVo> rs = new ArrayList<ApiDeclarationVo>();
                while (it.hasNext()) {
                    ApiDeclaration apiDeclaration = (ApiDeclaration) it.next();
                    Optional<ApiDeclarationVo> vo = BeanUtils.copy(apiDeclaration,
                            ApiDeclarationVo.class);
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

    @PostMapping("addOneApi")
    public Response<ApiDeclarationVo> addOneAPI(@RequestBody ApiDeclarationVo req) {
        Response<ApiDeclarationVo> resp = new Response<ApiDeclarationVo>();

        System.out.println("Add one api:" + req.toString());
        ApiDeclaration rs = apiService.saveOneAPI(req);
        try {
            Optional<ApiDeclarationVo> rtn = BeanUtils.copy(rs, ApiDeclarationVo.class);
            resp.setData(rtn.get());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }
    
    @PostMapping("getApiById")
    public Response<ApiDeclarationVo> getApiById(@RequestBody ApiDeclarationVo req) {
        Response<ApiDeclarationVo> resp = new Response<ApiDeclarationVo>();

        Optional<ApiDeclaration> rs = apiService.getById(req.getId());
        try {
            if (rs.isPresent()) {
                Optional<ApiDeclarationVo> rtn = BeanUtils.copy(rs.get(), ApiDeclarationVo.class);
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
    
    @PostMapping("getApisByParams")
    public Response<List<ApiDeclarationVo>> getApisByParams(@JsonParam ApiDeclarationVo params,
            @JsonParam PageInfo pageInfo) {
        Response<List<ApiDeclarationVo>> resp = new Response<List<ApiDeclarationVo>>();
        System.out.println("get apis by params: " + params.toString());
        try {
            Pagination<ApiDeclaration> apis = apiService.getByParams(params, pageInfo.toPage());
            List<ApiDeclarationVo> rs = new ArrayList<ApiDeclarationVo>();
            if (!CollectionUtils.sizeIsEmpty(apis.getList())) {
                Iterator<ApiDeclaration> it = apis.getList().iterator();
                while (it.hasNext()) {
                    ApiDeclaration apiDeclaration = (ApiDeclaration) it.next();
                    ApiDeclarationVo cpObj = packageVo(apiDeclaration);
                    rs.add(cpObj);
                }
            }
            
            resp.setData(rs);
            resp.setTotal(apis.getTotal());
            resp.setCurrent(apis.getCurrent());
            resp.setPageSize(apis.getPageSize());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }
    
    @PostMapping("deleteById")
    public Response<ApiDeclarationVo> deleteById(@RequestBody ApiDeclarationVo req) {
        Response<ApiDeclarationVo> resp = new Response<ApiDeclarationVo>();
        System.out.println("DELETE BY ID"+req.toString());
        try {
            Optional<ApiDeclaration> delRs = apiService.delete(req);
            if (delRs.isPresent()) {
                ApiDeclarationVo vo = BeanUtils.copy(delRs.get(), ApiDeclarationVo.class).get();
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
            List<ApiDeclarationVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = apiService.delete(vos);
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
    public Response<ApiDeclarationVo> activateById(@RequestBody ApiDeclarationVo req) {
        Response<ApiDeclarationVo> resp = new Response<ApiDeclarationVo>();
        System.out.println("ACTIVATE BY ID"+req.toString());
        try {
            Optional<ApiDeclaration> rs = apiService.activate(req);
            if (rs.isPresent()) {
                ApiDeclarationVo vo = BeanUtils.copy(rs.get(), ApiDeclarationVo.class).get();
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
            List<ApiDeclarationVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = apiService.activate(vos);
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
    public Response<ApiDeclarationVo> deactivateById(@RequestBody ApiDeclarationVo req) {
        Response<ApiDeclarationVo> resp = new Response<ApiDeclarationVo>();
        System.out.println("ACTIVATE BY IDS"+req.toString());
        try {
            Optional<ApiDeclaration> rs = apiService.deactivate(req);
            if (rs.isPresent()) {
                ApiDeclarationVo vo = BeanUtils.copy(rs.get(), ApiDeclarationVo.class).get();
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
            List<ApiDeclarationVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = apiService.deactivate(vos);
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
    public Response<ApiDeclarationVo> updateById(@RequestBody ApiDeclarationVo req) {
        Response<ApiDeclarationVo> resp = new Response<ApiDeclarationVo>();
        System.out.println("UPDATE BY ID === >   " + req.toString());
        try {
            Optional<ApiDeclaration> rs = apiService.updateById(req);
            if (rs.isPresent()) {
                Optional<ApiDeclarationVo> vo = BeanUtils.copy(rs.get(), ApiDeclarationVo.class);
                resp.setData(vo.get());
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    private ApiDeclarationVo packageVo(ApiDeclaration declaration) throws DataConversionException {
        ApiDeclarationVo cpObj = BeanUtils.copy(declaration, ApiDeclarationVo.class).get();
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
        
        cpObj.setState(TestCaseState.valueOf(declaration.getEnabled()));
        return cpObj;
    }
    
    private List<ApiDeclarationVo> convertToTestCaseVo(String idsVal) {
        List<ApiDeclarationVo> rs = new ArrayList<ApiDeclarationVo>();
        if (!StringUtils.isNotBlank(idsVal)) {
            return Collections.emptyList();
        }
        String[] ids = idsVal.split(",");
        try {
            for (int i = 0; i < ids.length; i++) {
                ApiDeclarationVo vo = new ApiDeclarationVo(Long.valueOf(ids[i]));
                rs.add(vo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert id from String to Long failed", e);
        }
        return rs;
    }
}
