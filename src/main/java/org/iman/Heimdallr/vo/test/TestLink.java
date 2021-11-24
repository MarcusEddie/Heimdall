package org.iman.Heimdallr.vo.test;

import org.iman.Heimdallr.vo.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/targetAPI")
public class TestLink {

    @PostMapping("apiOne")
    public Response<Param> getAPIone(@RequestBody Param link) {
        Response<Param> rsp = new Response<Param>();
        rsp.setSuccess(false);
        System.out.println("API ONE GET REQUIRONMENTS: " + link.toString());
        return rsp;
    }
}
