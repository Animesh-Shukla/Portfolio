package com.animesh_shukla.portfolio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class PortfolioController {
    @GetMapping("/")
    public String showIndexPage(Model model)
    {
        model.addAttribute("title", "Index");
        return "main";
    }

    @GetMapping("/projects")
    public String showProjectsPage(Model model)
    {
        model.addAttribute("title", "Projects");
        return "main";
    }

    @GetMapping("/contact")
    public String showContactPage(Model model)
    {
        model.addAttribute("title", "Contact");
        return "main";
    }

    @GetMapping("/resume")
    public String showResumePage(Model model)
    {
        model.addAttribute("title", "Resume");
        return "main";
    }


}
