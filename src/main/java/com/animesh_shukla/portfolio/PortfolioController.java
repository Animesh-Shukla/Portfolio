package com.animesh_shukla.portfolio;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.animesh_shukla.model.Contact;
import com.animesh_shukla.service.send_email;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;

@Controller
public class PortfolioController {

    private final send_email emailService;

    @Autowired
    public PortfolioController(send_email emailService) {
        this.emailService = emailService;
    }

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
        
        // send email with contact data
        try {
            emailService.sendContactNotification(contactData);
            model.addAttribute("message", "Thank you for contacting us! We will get back to you soon.");
        } catch (MailException ex) {
            // log and inform user
            System.err.println("Failed to send contact email: " + ex.getMessage());
            model.addAttribute("message", "Your message was received but failed to send notification (admins).");
        }
        
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
