/**
 * 
 */
package org.iman.Heimdallr.controller;

import javax.annotation.Resource;

import org.iman.Heimdallr.service.HelloWordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/")
public class IdxController {

    @Resource
    private HelloWordService helloWorldService;
    
    @RequestMapping("/index")
    public String index() {
        String name = helloWorldService.getById(1L);
        return "hello : " + name;
    }
}
