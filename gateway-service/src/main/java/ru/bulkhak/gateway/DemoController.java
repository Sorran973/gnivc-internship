package ru.bulkhak.gateway;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.RedirectView;

@RestController
@RequestMapping("/")
public class DemoController {

    @GetMapping("/demo")
    public String demo() {
        return "Hello demo";
    }

    @GetMapping("/user")
    public String user() {
        return "Hello User!";
    }
}
