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
import org.iman.Heimdallr.entity.AppStructure;
import org.iman.Heimdallr.entity.DBConnectionInfo;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.AppStructureService;
import org.iman.Heimdallr.service.DBConnectionInfoService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.DBConnection;
import org.iman.Heimdallr.vo.DBConnectionInfoVo;
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
@RequestMapping("/dbConn")
public class DataBaseController {

    private static final Logger log = LoggerFactory.getLogger(DataBaseController.class);
    
    @Resource
    private DBConnectionInfoService dbConnectionService;
    @Resource
    private AppStructureService appStructureService;
    
    @PostMapping("getDBConnByAppId")
    public Response<List<DBConnectionInfoVo>> getDBConnByAppId(
            @RequestBody DBConnectionInfoVo req) {
        Response<List<DBConnectionInfoVo>> resp = new Response<List<DBConnectionInfoVo>>();
        Optional<List<DBConnectionInfo>> rs = dbConnectionService
                .getConnInfoByAppId(req.getAppId());
        List<DBConnectionInfoVo> vos = new ArrayList<DBConnectionInfoVo>();
        try {
            if (rs.isPresent()) {
                Iterator<DBConnectionInfo> it = rs.get().iterator();
                while (it.hasNext()) {
                    DBConnectionInfo info = (DBConnectionInfo) it.next();
                    DBConnectionInfoVo vo = BeanUtils.copy(info, DBConnectionInfoVo.class).get();
                    vos.add(vo);
                }
            }

            resp.setData(vos);
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }

    @PostMapping("getDBConnById")
    public Response<DBConnectionInfoVo> getDBConnById(@RequestBody DBConnectionInfoVo req) {
        Response<DBConnectionInfoVo> resp = new Response<DBConnectionInfoVo>();
        Optional<DBConnectionInfo> info = dbConnectionService.getById(req.getId());
        try {
            if (info.isPresent()) {
                Optional<DBConnectionInfoVo> vo = BeanUtils.copy(info.get(),DBConnectionInfoVo.class);
                resp.setData(vo.get());
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }
    
    @PostMapping("getDBConnByParams")
    public Response<List<DBConnectionInfoVo>> getDBConnByParams(
            @JsonParam DBConnectionInfoVo params, @JsonParam PageInfo pageInfo) {
        Response<List<DBConnectionInfoVo>> resp = new Response<List<DBConnectionInfoVo>>();
        System.out.println("GET DB CONNECTIONS BY PARAMS === > " + params.toString());
        try {
            Pagination<DBConnectionInfo> conns = dbConnectionService.getByParams(params,
                    pageInfo.toPage());
            List<DBConnectionInfoVo> rs = new ArrayList<DBConnectionInfoVo>();
            if (!CollectionUtils.sizeIsEmpty(conns.getList())) {
                Iterator<DBConnectionInfo> it = conns.getList().iterator();
                while (it.hasNext()) {
                    DBConnectionInfo info = (DBConnectionInfo) it.next();
                    DBConnectionInfoVo vo = packageVo(info);
                    rs.add(vo);
                }
            }

            resp.setData(rs);
            resp.setTotal(conns.getTotal());
            resp.setCurrent(conns.getCurrent());
            resp.setPageSize(conns.getPageSize());
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }

    @PostMapping("deleteById")
    public Response<DBConnectionInfoVo> deleteById(@RequestBody DBConnectionInfoVo req) {
        Response<DBConnectionInfoVo> resp = new Response<DBConnectionInfoVo>();
        System.out.println("DELETE BY ID"+req.toString());
        try {
            Optional<DBConnectionInfo> delRs = dbConnectionService.delete(req);
            if (delRs.isPresent()) {
                DBConnectionInfoVo vo = BeanUtils.copy(delRs.get(), DBConnectionInfoVo.class).get();
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
            List<DBConnectionInfoVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = dbConnectionService.delete(vos);
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
    public Response<DBConnectionInfoVo> activateById(@RequestBody DBConnectionInfoVo req) {
        Response<DBConnectionInfoVo> resp = new Response<DBConnectionInfoVo>();
        System.out.println("ACTIVATE BY ID"+req.toString());
        try {
            Optional<DBConnectionInfo> rs = dbConnectionService.activate(req);
            if (rs.isPresent()) {
                DBConnectionInfoVo vo = BeanUtils.copy(rs.get(), DBConnectionInfoVo.class).get();
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
            List<DBConnectionInfoVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = dbConnectionService.activate(vos);
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
    public Response<DBConnectionInfoVo> deactivateById(@RequestBody DBConnectionInfoVo req) {
        Response<DBConnectionInfoVo> resp = new Response<DBConnectionInfoVo>();
        System.out.println("ACTIVATE BY IDS"+req.toString());
        try {
            Optional<DBConnectionInfo> rs = dbConnectionService.deactivate(req);
            if (rs.isPresent()) {
                DBConnectionInfoVo vo = BeanUtils.copy(rs.get(), DBConnectionInfoVo.class).get();
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
            List<DBConnectionInfoVo> vos = convertToTestCaseVo(req.get(Parameters.IDS).asText());
            Integer rs = dbConnectionService.deactivate(vos);
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
    
    @PostMapping("updateDBConnById")
    public Response<DBConnectionInfoVo> updateById(@RequestBody DBConnectionInfoVo req) {
        Response<DBConnectionInfoVo> resp = new Response<DBConnectionInfoVo>();
        System.out.println("UPDATE BY ID === >   " + req.toString());
        try {
            Optional<DBConnectionInfo> rs = dbConnectionService.updateById(req);
            if (rs.isPresent()) {
                Optional<DBConnectionInfoVo> vo = BeanUtils.copy(rs.get(), DBConnectionInfoVo.class);
                resp.setData(vo.get());
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
//        
        return resp;
    }
    
    @PostMapping("dbConnectionTest")
    public Response<String> dbConnectionTest(@RequestBody DBConnectionInfoVo req) {
        Response<String> resp = new Response<String>();
        System.out.println("TRY TO CONNECT A DATA BASE ----- >" + req.toString());
        DBConnection rs = dbConnectionService.testDBConnection(req);
        if (!rs.getStatus()) {
            resp.setErrorMsg(rs.getErrorMsg());
        }
        System.out.println(rs.toString());
        return resp;
    }
    
    @PostMapping("saveDBConnInfo")
    public Response<DBConnectionInfoVo> saveDBConnInfo(@RequestBody DBConnectionInfoVo req) {
        Response<DBConnectionInfoVo> resp = new Response<DBConnectionInfoVo>();
        System.out.println("SAVE A DB CONN INFO ===>" + req.toString());
        try {
            DBConnectionInfo rs = dbConnectionService.save(req);
            DBConnectionInfoVo vo = BeanUtils.copy(rs, DBConnectionInfoVo.class).get();
            resp.setData(vo);
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }
        
        return resp;
    }
    
    private List<DBConnectionInfoVo> convertToTestCaseVo(String idsVal) {
        List<DBConnectionInfoVo> rs = new ArrayList<DBConnectionInfoVo>();
        if (!StringUtils.isNotBlank(idsVal)) {
            return Collections.emptyList();
        }
        String[] ids = idsVal.split(",");
        try {
            for (int i = 0; i < ids.length; i++) {
                DBConnectionInfoVo vo = new DBConnectionInfoVo(Long.valueOf(ids[i]));
                rs.add(vo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert id from String to Long failed", e);
        }
        return rs;
    }
    
    private DBConnectionInfoVo packageVo(DBConnectionInfo declaration)
            throws DataConversionException {
        DBConnectionInfoVo cpObj = BeanUtils.copy(declaration, DBConnectionInfoVo.class).get();
        Optional<AppStructure> app = appStructureService.getById(cpObj.getAppId());
        if (app.isPresent()) {
            cpObj.setAppName(app.get().getName());
        }

        cpObj.setState(TestCaseState.valueOf(declaration.getEnabled()));
        return cpObj;
    }
}
