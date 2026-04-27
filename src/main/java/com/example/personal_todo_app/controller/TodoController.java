package com.example.personal_todo_app.controller;

import com.example.personal_todo_app.model.OwnerForm;
import com.example.personal_todo_app.model.Todo;
import com.example.personal_todo_app.repository.TodoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class TodoController {
    private final TodoRepository repository;
    private final MessageSource messageSource;

    @GetMapping
    public String showWelcome(Model model) {
        if (!model.containsAttribute("ownerForm")) {
            model.addAttribute("ownerForm", new OwnerForm());
        }
        return "welcome";
    }

    @PostMapping("/owner")
    public String saveOwner(
            @Valid @ModelAttribute("ownerForm") OwnerForm ownerForm,
            BindingResult bindingResult,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "welcome";
        }
        session.setAttribute("ownerName", ownerForm.getName().trim());
        return "redirect:/todos";
    }

    @GetMapping("/todos")
    public String home(HttpSession session, Model model) {
        if (!hasOwner(session)) {
            return "redirect:/";
        }
        model.addAttribute("ownerName", session.getAttribute("ownerName"));
        model.addAttribute("todos", repository.findAll());
        return "todo";
    }

    @GetMapping("/add")
    public String add(HttpSession session, Model model) {
        if (!hasOwner(session)) {
            return "redirect:/";
        }
        model.addAttribute("todo", new Todo());
        return "form-add";
    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("todo") Todo todo,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Locale locale,
            Model model
    ) {
        if (!hasOwner(session)) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return todo.getId() == null ? "form-add" : "form-edit";
        }
        repository.save(todo);
        redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("todo.flash.saved", null, locale)
        );
        return "redirect:/todos";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, HttpSession session, Model model) {
        if (!hasOwner(session)) {
            return "redirect:/";
        }
        Optional<Todo> todo = repository.findById(id);
        if (todo.isEmpty()) {
            return "redirect:/todos";
        }
        model.addAttribute("todo", todo.get());
        return "form-edit";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Locale locale
    ) {
        if (!hasOwner(session)) {
            return "redirect:/";
        }
        repository.deleteById(id);
        redirectAttributes.addFlashAttribute(
                "message",
                messageSource.getMessage("todo.flash.deleted", null, locale)
        );
        return "redirect:/todos";
    }

    private boolean hasOwner(HttpSession session) {
        Object ownerName = session.getAttribute("ownerName");
        return ownerName instanceof String owner && !owner.isBlank();
    }
}
