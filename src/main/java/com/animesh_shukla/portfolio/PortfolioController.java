package com.animesh_shukla.portfolio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import com.animesh_shukla.portfolio.Model.Contact;

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
        model.addAttribute("contact", new Contact());
        return "main";
    }

    @PostMapping("/contact")
    public String handleContactForm(@ModelAttribute Contact contactData, Model model)
    {
        System.out.println("Name: " + contactData.getName());
        System.out.println("Email: " + contactData.getEmail());
        System.out.println("Phone: " + contactData.getPhone_number());
        System.out.println("Message: " + contactData.getMessage());
        
        // TODO: Save to database or send email
        
        model.addAttribute("title", "Contact");
        model.addAttribute("message", "Thank you for contacting us!");
        return "main";
    }

    @GetMapping("/resume")
    public String showResumePage(Model model)
    {
        model.addAttribute("title", "Resume");
        return "main";
    }
}
