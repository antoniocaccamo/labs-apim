package it.antoniocaccamo.labs.apim.resource.web;

import it.antoniocaccamo.labs.apim.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller @RequiredArgsConstructor @Slf4j
public class IndexController {


    private final BookService bookService;

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        log.info("displaying home page ...");
        model.addAttribute("appName", appName);
        model.addAttribute("books", bookService.findBooks());
        return "home";
    }

}
