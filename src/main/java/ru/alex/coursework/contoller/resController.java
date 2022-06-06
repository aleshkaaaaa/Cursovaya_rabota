package ru.alex.coursework.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.alex.coursework.entity.User;
import ru.alex.coursework.service.ResService;
import ru.alex.coursework.repository.UserRepository;

@Controller
public class resController {

    @Autowired
    private ResService resService;
    @Autowired
    private UserRepository userRepository;

    public int whichBuilding(int number){
        if (number < 9)
            return 1;
        else if (number < 17)
            return 2;
        else if (number < 25)
            return 3;
        else if (number < 33)
            return 4;
        else
            return 5;
    }

    @GetMapping("/a_{number:\\d+}")
    public String addB(@PathVariable int number, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        int mesID = whichBuilding(number);
        if (user.isReserved()) {
            model.addAttribute("errorSetting" + mesID, true);
            model.addAttribute("errorMessage" + mesID, "Вы уже записались!");
        } else {
            boolean flag = resService.addRes(number);
            if (flag) {
                model.addAttribute("infoSetting" + mesID, true);
                model.addAttribute("infoMessage" + mesID, "Вы успешно записались! Информация отправлена на почту");
                user.setReserved(true);
                userRepository.save(user);
                resService.sendRes(user, number);
            } else {
                model.addAttribute("errorSetting", true);
                model.addAttribute("errorMessage", "Данное время уже занято!");
            }
        }
        model.addAllAttributes(resService.checkTime());
        return "redirect:/index";
    }



}
