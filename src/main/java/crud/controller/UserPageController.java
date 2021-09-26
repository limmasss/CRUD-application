package crud.controller;

import crud.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class UserPageController {

    @GetMapping
    public String userPage(Model model){
        User user = (User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }
}
