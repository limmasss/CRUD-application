package crud.controller;

import crud.exception.UserAlreadyExistsException;
import crud.models.User;
import crud.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class LoginController {

    private UserService userService;

    @GetMapping
    public String mainPage(){
        return "/index";
    }

    @GetMapping("/signup")
    public String signUp(Model model){
        model.addAttribute("user", new User());
        return "/signup";
    }

    @PostMapping("/signup/")
    public String register(@ModelAttribute @Valid User user, BindingResult bindingResult){
        try {
            userService.create(user);
        } catch (UserAlreadyExistsException e) {
            bindingResult.addError(new ObjectError("user", "User already exists."));
        }
        if (bindingResult.hasGlobalErrors()) {
            return "signup";
        }
        return "redirect:/login";
    }
}
