package com.security.securitybasic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 이승환
 * @since 2020-11-22
 */
@RestController
public class HomeController {

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello lsh!!";
    }

}
