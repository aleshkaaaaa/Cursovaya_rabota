package ru.alex.coursework.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.alex.coursework.service.MailService;
import ru.alex.coursework.entity.callFromUser;
import ru.alex.coursework.service.ResService;

import javax.validation.Valid;

@Controller
public class callController {
    @Autowired
    private MailService mailService;

    @Autowired
    private ResService resService;

    @GetMapping({"/index", "/"})
    public String getIndex(Model model){
        model.addAllAttributes(resService.checkTime());
        model.addAttribute("call", new callFromUser());
        return "index";
    }

    @PostMapping("/index")
    public String postIndex(@ModelAttribute("call") @Valid callFromUser call){
        mailService.sendEmail("MAIL TVOY", "Вопрос от " + call.getName(), "Имя: " + call.getName() + "\nEmail: " + call.getEmail() + "\nСообщение: " + call.getMessage());
        return "redirect:/index";
    }

}
