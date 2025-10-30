package com.animesh_shukla.portfolio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortfolioController {
    @GetMapping("/")
    public String showIndexPage()
    {
        return "index";
    }

    @GetMapping("/projects")
    public String showProjectsPage()
    {
        return "projects";
    }

    @GetMapping("/contact")
    public String showContactPage()
    {
        return "contact";
    }

    @GetMapping("/resume")
    public String showResumePage()
    {
        return "resume";
    }


}
