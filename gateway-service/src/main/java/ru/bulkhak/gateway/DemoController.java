package ru.bulkhak.gateway;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;

@RestController
@RequestMapping("/")
public class DemoController {

    @GetMapping("/demo")
    public String hello() {
        return "hello";
    }
//
//    @PostMapping("/key")
//    public String hello() {
//        return ;
//    }
}
