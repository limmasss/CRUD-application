package crud.controller;

import crud.exception.UserAlreadyExistsException;
import crud.models.Role;
import crud.models.User;
import crud.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.expression.Fields;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsersController {

    private UserService userService;

    @GetMapping("/")
    public String show(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("users", userService.getAll());
        model.addAttribute("admin", user);
        return "users/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "users/new";
    }

    @PostMapping("/")
    public String create(@RequestParam Map<String, String> form, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        validateRoles(form, bindingResult);
        try {
            userService.create(user);
        } catch (UserAlreadyExistsException e) {
            bindingResult.addError(new ObjectError("user", "User already exists."));
        }
        if (bindingResult.hasErrors()) {
            return "/users/new";
        }
        return "redirect:/admin/";
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
    public String update(@RequestParam Map<String, String> form, @ModelAttribute @Valid User user,
                         BindingResult bindingResult){
        validateRoles(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/users/edit";
        }
        for (Role role : Role.values()) {
            if (form.containsKey(role.name())) {
                user.getRoles().add(role);
            }
        }
        userService.update(user.getId(), user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/";
    }

    private void validateRoles(Map<String, String> form, BindingResult bindingResult) {
        Set<Role> roleSet = form.entrySet().stream()
                .filter(entry -> entry.getKey().contains("ROLE_"))
                .map(entry -> Role.valueOf(entry.getKey())).collect(Collectors.toSet());
        if (roleSet.isEmpty()) {
            bindingResult.addError(new FieldError("user", "roles", "Roles should not be empty"));
        }
    }
}
