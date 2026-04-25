package com.example.personal_todo_app.controller;

import com.example.personal_todo_app.model.Todo;
import com.example.personal_todo_app.repository.TodoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class TodoController {
    private final TodoRepository repository;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("todos", repository.findAll());
        return "todo";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("todo", new Todo());
        return "form-add";
    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute(name = "todo") Todo todo,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return "form-add";
        }
        repository.save(todo);
        return "redirect:/";
    }
}
