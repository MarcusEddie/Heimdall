/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.entity.ApiDeclaration;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.APIService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.ApiDeclarationVo;
import org.iman.Heimdallr.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
