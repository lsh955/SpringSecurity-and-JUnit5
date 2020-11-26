package com.security.securitybasic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 이승환
 * @since 2020-11-22
 */
@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String hello() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(defaultValue = "false") Boolean error, Model model) {
        if(error) {
            model.addAttribute("errorMessage", "아이디나 패스워드가 올바르지 않습니다.");
        }
        return "login";
    }

}
