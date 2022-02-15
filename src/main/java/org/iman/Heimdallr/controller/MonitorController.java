/**
 * 
 */
package org.iman.Heimdallr.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iman.Heimdallr.constants.enums.HttpMethod;
import org.iman.Heimdallr.vo.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @GetMapping("getHttpMethods")
    public Response<List<HttpMethod>> getHttpMethods() {
        Response<List<HttpMethod>> resp = new Response<List<HttpMethod>>();
        resp.setData(new ArrayList<HttpMethod>(Arrays.asList(HttpMethod.values())));
        return resp;
    }
}
