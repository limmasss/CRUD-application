package crud.controller;

import crud.models.User;
import crud.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private UserService userService;

    @GetMapping("/")
    public String show(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "users/new";
    }

    @PostMapping("/")
    public String create(@ModelAttribute("user") User user) {
        userService.create(user);
        return "redirect:/users/";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.get(id));
        return "users/show";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.get(id));
        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute User user){
        userService.update(user.getId(), user);
        return "redirect:/users/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users/";
    }
}
