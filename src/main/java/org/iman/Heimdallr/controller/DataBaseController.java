/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.entity.DBConnectionInfo;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.DBConnectionInfoService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.DBConnectionInfoVo;
import org.iman.Heimdallr.vo.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/dbConn")
public class DataBaseController {

    @Resource
    private DBConnectionInfoService dbConnectionService;

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

}
