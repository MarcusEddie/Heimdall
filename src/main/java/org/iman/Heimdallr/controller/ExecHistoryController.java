package org.iman.Heimdallr.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.iman.Heimdallr.config.annotations.JsonParam;
import org.iman.Heimdallr.constants.enums.ErrorCode;
import org.iman.Heimdallr.constants.enums.TestCaseState;
import org.iman.Heimdallr.entity.ExecHistory;
import org.iman.Heimdallr.entity.ExecHistoryFailureDetails;
import org.iman.Heimdallr.entity.TaskQueue;
import org.iman.Heimdallr.exception.DataConversionException;
import org.iman.Heimdallr.service.ExecHistoryFailureDetailsService;
import org.iman.Heimdallr.service.ExecHistoryService;
import org.iman.Heimdallr.utils.BeanUtils;
import org.iman.Heimdallr.utils.ControllerUtils;
import org.iman.Heimdallr.vo.ExecHistoryFailureDetailsVo;
import org.iman.Heimdallr.vo.ExecHistoryVo;
import org.iman.Heimdallr.vo.PageInfo;
import org.iman.Heimdallr.vo.Pagination;
import org.iman.Heimdallr.vo.Response;
import org.iman.Heimdallr.vo.TaskQueueVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping("/history")
public class ExecHistoryController {

    private static final Logger log = LoggerFactory.getLogger(ExecHistoryController.class);
    
    @Resource
    private ExecHistoryService execHistoryService;
    @Resource
    private ExecHistoryFailureDetailsService execHistoryFailureDetailsService;
    
    @PostMapping("getHistoryByParams")
    public Response<List<ExecHistoryVo>> getHistoryByParams(@JsonParam ExecHistoryVo params,
            @JsonParam PageInfo pageInfo) {
        System.out.println("GET HISOTORY BY PARAMS " + params.toString());
        Response<List<ExecHistoryVo>> resp = new Response<List<ExecHistoryVo>>();
        try {
            Pagination<ExecHistory> cases = execHistoryService.getByParams(params, pageInfo.toPage());
            List<ExecHistoryVo> vos = new ArrayList<ExecHistoryVo>();
            if (!CollectionUtils.sizeIsEmpty(cases.getList())) {
                Iterator<ExecHistory> it = cases.getList().iterator();
                while (it.hasNext()) {
                    ExecHistory plan = (ExecHistory) it.next();
                    ExecHistoryVo cpObj = packageVo(plan);
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

    @PostMapping("reEnqueueById")
    public Response<TaskQueueVo> reEnqueueById(@RequestBody ExecHistoryVo req) {
        Response<TaskQueueVo> resp = new Response<TaskQueueVo>();
        System.out.println("re enqueue BY ID" + req.toString());
        try {
            Optional<TaskQueue> delRs = execHistoryService.reEnqueue(req);
            if (delRs.isPresent()) {
                TaskQueueVo vo = BeanUtils.copy(delRs.get(), TaskQueueVo.class).get();
                resp.setData(vo);
            }
        } catch (DataConversionException e) {
            resp = ControllerUtils.encapsulateErrCode(ErrorCode.DATA_CONVERSION_FAILURE);
        }

        return resp;
    }
    
    @PostMapping("getFailureDetailsByHistoryId")
    public Response<List<ExecHistoryFailureDetailsVo>> getFailureDetailsByHistoryId(
            @JsonParam ExecHistoryFailureDetailsVo params, @JsonParam PageInfo pageInfo) {
        Response<List<ExecHistoryFailureDetailsVo>> resp = new Response<List<ExecHistoryFailureDetailsVo>>();
        System.out.println("GetFailureDetailsByHistoryId BY ID" + params.toString());

        try {
            Pagination<ExecHistoryFailureDetails> logs = execHistoryFailureDetailsService
                    .getByParams(params, pageInfo.toPage());
            List<ExecHistoryFailureDetailsVo> rs = new ArrayList<ExecHistoryFailureDetailsVo>();
            if (!CollectionUtils.sizeIsEmpty(logs.getList())) {
                Iterator<ExecHistoryFailureDetails> it = logs.getList().iterator();
                while (it.hasNext()) {
                    ExecHistoryFailureDetails details = (ExecHistoryFailureDetails) it.next();
                    ExecHistoryFailureDetailsVo cpObj = BeanUtils
                            .copy(details, ExecHistoryFailureDetailsVo.class).get();
                    cpObj.setCaseErrorVal(prettyLog(details.getCaseError()));
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
    
    private ExecHistoryVo packageVo(ExecHistory tc) throws DataConversionException {
        ExecHistoryVo cpObj = BeanUtils.copy(tc, ExecHistoryVo.class).get();
        cpObj.setDetailsVal(prettyLog(tc.getDetails()));
        
        cpObj.setState(TestCaseState.valueOf(tc.getEnabled()));
        return cpObj;
    }
    
    private String prettyLog(JsonNode node) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = new JsonParser().parse(node.toString()).getAsJsonObject();
        return gson.toJson(object);
    }
    
}
