/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iman.Heimdallr.constants.enums.CasePriority;
import org.iman.Heimdallr.constants.enums.DBType;
import org.iman.Heimdallr.constants.enums.HttpMethod;
import org.iman.Heimdallr.constants.enums.ResultCheckMode;
import org.iman.Heimdallr.constants.enums.TestType;
import org.iman.Heimdallr.vo.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/generalApis")
public class GeneralController {

    @GetMapping("getHttpMethods")
    public Response<List<HttpMethod>> getHttpMethods() {
        Response<List<HttpMethod>> resp = new Response<List<HttpMethod>>();
        resp.setData(new ArrayList<HttpMethod>(Arrays.asList(HttpMethod.values())));
        return resp;
    }
    
    @GetMapping("getCasePriority")
    public Response<List<CasePriority>> getCasePriority() {
        Response<List<CasePriority>> resp = new Response<List<CasePriority>>();
        resp.setData(new ArrayList<CasePriority>(Arrays.asList(CasePriority.values())));
        return resp;
    }
    
    @GetMapping("getCaseCheckMode")
    public Response<List<ResultCheckMode>> getCaseCheckMethod() {
        Response<List<ResultCheckMode>> resp = new Response<List<ResultCheckMode>>();
        resp.setData(new ArrayList<ResultCheckMode>(Arrays.asList(ResultCheckMode.values())));
        return resp;
    }
    
    @GetMapping("getDBType")
    public Response<List<DBType>> getDBType() {
        Response<List<DBType>> resp = new Response<List<DBType>>();
        resp.setData(new ArrayList<DBType>(Arrays.asList(DBType.values())));
        return resp;
    }
    
    @GetMapping("getTestType")
    public Response<List<TestType>> getTestTypes(){
        Response<List<TestType>> resp = new Response<List<TestType>>();
        resp.setData(new ArrayList<TestType>(Arrays.asList(TestType.values())));
        return resp;
    }
}
