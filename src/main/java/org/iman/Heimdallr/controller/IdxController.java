/**
 * 
 */
package org.iman.Heimdallr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ey
 *
 */
@RestController
@RequestMapping("/")
public class IdxController {

    @RequestMapping("/index")
    public String index() {
        return "hello world";
    }
}
