/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.config.annotations.JsonParam;
import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.entity.DataHistory;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.DataHistoryService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.DataHistoryVo;
import org.iman.Heimdallr.vo.PageInfo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/actionLogs")
public class DataHistoryController {

    private static final Logger log = LoggerFactory.getLogger(DataHistoryController.class);
    @Resource
    private DataHistoryService dataHistoryService;

    @PostMapping("/getLogsByDataIdAndFunc")
    public Response<List<DataHistoryVo>> getLongsByIdAndFuncTag(@JsonParam DataHistoryVo params,
            @JsonParam PageInfo pageInfo) {
        System.out.println("get logs by dataid and func tang ===>"+ pageInfo.toString());
        Response<List<DataHistoryVo>> resp = new Response<List<DataHistoryVo>>();
        try {
            Pagination<DataHistory> logs = dataHistoryService.getLogsByDataIdAndFuncTag(
                    params.getDataId(), params.getTableName(), pageInfo.toPage());
            List<DataHistoryVo> rs = new ArrayList<DataHistoryVo>();
            if (!CollectionUtils.sizeIsEmpty(logs.getList())) {
                Iterator<DataHistory> it = logs.getList().iterator();
                while (it.hasNext()) {
                    DataHistory hist = (DataHistory) it.next();
                    DataHistoryVo cpObj = BeanUtils.copy(hist, DataHistoryVo.class).get();
                    if (null != hist.getOldData()) {
                        cpObj.setOldDataVal(prettyLog(hist.getOldData()));
                    }
                    if (null != hist.getNewData()) {
                        cpObj.setNewDataVal(prettyLog(hist.getNewData()));
                    }
                    System.out.println(cpObj.getOldDataVal());
                    rs.add(cpObj);
                }
            }

            resp.setData(rs);
            resp.setTotal(logs.getTotal());
            resp.setCurrent(logs.getCurrent());
            resp.setPageSize(logs.getPageSize());
        } catch (DataConversionException e) {
            if (log.isErrorEnabled()) {
                log.error("Query failed", e);
            }
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }
    
    private String prettyLog(JsonNode node) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = new JsonParser().parse(node.toString()).getAsJsonObject();
        return gson.toJson(object);
    }
}
