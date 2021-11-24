package org.iman.Heimdallr.controller;

import org.iman.Heimdallr.vo.AppVo;
import org.iman.Heimdallr.vo.Response;
import org.iman.Heimdallr.vo.test.TestLink;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testLink")
public class TestLinkAPI {

    @PostMapping("getAppById")
    public Response<AppVo> getTestLinkById(@RequestBody AppVo vo) {
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println(vo.toString());
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------------");
        
        Response<AppVo> resp = new Response<AppVo>();
        AppVo rs = new AppVo(911911L);
        rs.setName("Tested for api test");
        resp.setData(rs);
        return resp;
    }
    
    @PostMapping("getAppById2")
    public Response<TestLink> getTestLinkById2(@RequestBody TestLink vo) {
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println(vo.toString());
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("-------------------------------------------------------------------------------------------------");
        
        Response<TestLink> resp = new Response<TestLink>();
        TestLink rs = new TestLink();
//        rs.setName("Tested for api test");
        resp.setData(rs);
        return resp;
    }
}
