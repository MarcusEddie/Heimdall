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
import org.iman.Heimdallr.constants.Parameters;
import org.iman.Heimdallr.constants.enums.AppLevel;
import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.entity.App;
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.AppVo;
import org.iman.Heimdallr.vo.FunctionVo;
import org.iman.Heimdallr.vo.ModuleVo;
import org.iman.Heimdallr.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/app")
public class AppController {

    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @Resource
    private AppStructureService appStructureService;

    @GetMapping("/getApps")
    public Response<List<AppVo>> getAppOptions() {
        java.lang.System.out.println("get System options");
        Response<List<AppVo>> resp = new Response<List<AppVo>>();
        List<AppStructure> apps = appStructureService.getStructures(AppLevel.APP, 0L, true);
        if (CollectionUtils.sizeIsEmpty(apps)) {
            return resp;
        }
        Iterator<AppStructure> it = apps.iterator();
        List<AppVo> rs = new ArrayList<AppVo>();
        while (it.hasNext()) {
            AppStructure appStructure = (AppStructure) it.next();
            AppVo vo = new AppVo(appStructure.getId());
            vo.setName(appStructure.getName());
            rs.add(vo);
        }

        resp.setData(rs);

        return resp.mkTime();
    }

    @PostMapping("getAppById")
    public Response<AppVo> getApptionById(@RequestBody ObjectNode req) {
        Response<AppVo> resp = new Response<AppVo>();
        Long id = req.get(Parameters.ID).asLong();
        Optional<AppStructure> rs = appStructureService.getById(id);
        if (rs.isEmpty()) {
            resp.setSuccess(false);
            resp.setErrorCode(ErrorCode.DATA_IS_DELETED.getCode());
            resp.setErrorMsg(ErrorCode.DATA_IS_DELETED.getMsg());
        }

        AppVo vo = new AppVo(rs.get().getId());
        vo.setName(rs.get().getName());
        resp.setData(vo);
        return resp.mkTime();
    }

    @PostMapping("/getModuleOptions")
    public Response<List<ModuleVo>> getModuleOptionsByApp(@RequestBody ObjectNode req) {
        java.lang.System.out.println("Get Modules 2  By System Id : " + req.toString());
        Response<List<ModuleVo>> resp = new Response<List<ModuleVo>>();
        Long appId = req.get(Parameters.APP_ID).asLong();

        List<AppStructure> modules = appStructureService.getStructures(AppLevel.MODULE, appId,
                true);
        if (CollectionUtils.sizeIsEmpty(modules)) {
            return resp;
        }
        Iterator<AppStructure> it = modules.iterator();
        List<ModuleVo> rs = new ArrayList<ModuleVo>();
        while (it.hasNext()) {
            AppStructure appStructure = (AppStructure) it.next();
            ModuleVo vo = new ModuleVo();
            vo.setId(appStructure.getId());
            vo.setName(appStructure.getName());
            rs.add(vo);
        }

        resp.setData(rs);

        return resp.mkTime();
    }

    @PostMapping("/getFuncOptions")
    public Response<List<FunctionVo>> getFuncOptionsByModule(@RequestBody ObjectNode req) {
        Response<List<FunctionVo>> resp = new Response<List<FunctionVo>>();
        Long moduleId = req.get(Parameters.MODULE_ID).asLong();
        List<AppStructure> functions = appStructureService.getStructures(AppLevel.FUNCTION,
                moduleId, true);
        if (CollectionUtils.sizeIsEmpty(functions)) {
            return resp;
        }

        Iterator<AppStructure> it = functions.iterator();
        List<FunctionVo> rs = new ArrayList<FunctionVo>();

        while (it.hasNext()) {
            AppStructure appStructure = (AppStructure) it.next();
            FunctionVo vo = new FunctionVo(appStructure.getId());
            vo.setName(appStructure.getName());
            rs.add(vo);
        }

        resp.setData(rs);
        return resp.mkTime();
    }

    @PostMapping("getFunctionById")
    public Response<FunctionVo> getFunctionById(@RequestBody ObjectNode req) {
        Response<FunctionVo> resp = new Response<FunctionVo>();
        Long functionId = req.get(Parameters.FUNCTION_ID).asLong();
        Optional<AppStructure> function = appStructureService.getById(functionId);
        if (function.isPresent()) {
            try {
                Optional<FunctionVo> vo = BeanUtils.copy(function.get(), FunctionVo.class);
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

    @PostMapping("/saveNewAppComponent")
    public Response<AppVo> saveStructure(@RequestBody ObjectNode req) {
        Response<AppVo> resp = new Response<AppVo>();

        App app = appStructureService.saveComponentTree(req.get(Parameters.APP_NAME).asText(),
                req.get(Parameters.MODULE_NAME).asText(),
                req.get(Parameters.FUNCTION_NAME).asText());

        try {
            Optional<AppVo> rs = BeanUtils.copy(app, AppVo.class);
            resp.setData(rs.isPresent() ? rs.get() : null);
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp.mkTime();
    }

    @PostMapping("/deleteAppComponent")
    public Response<Integer> deleteStructure(@RequestBody ObjectNode req) {
        Response<Integer> resp = new Response<Integer>();
        System.out.println(req.toString());
        Long appVal = req.get(Parameters.APP_ID).asLong();
        Long moduleVal = req.get(Parameters.MODULE_ID).asLong();
        Long functioVal = req.get(Parameters.FUNCTION_ID).asLong();

        if (appVal.compareTo(0L) == 0) {
            resp.setSuccess(false);
            resp.setErrorCode(ErrorCode.PARAMETERS_ARE_INVALID.getCode());
            resp.setErrorMsg(ErrorCode.PARAMETERS_ARE_INVALID.getMsg());
            return resp;
        }

        Integer effectedRows = appStructureService.deleteComponent(appVal, moduleVal, functioVal);
        resp.setData(effectedRows);

        return resp.mkTime();
    }

}
