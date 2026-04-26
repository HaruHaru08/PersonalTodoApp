package com.example.personal_todo_app.controller;

import com.example.personal_todo_app.model.Todo;
import com.example.personal_todo_app.repository.TodoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            if(todo.getId() == null){
                return "form-add";
            }
            return "form-edit";
        }
        repository.save(todo);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("todo", repository.findById(id).get());
        return "form-edit";
    }
    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute(name = "todo") Todo todo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "form-edit";
        }
        repository.save(todo);
        return "redirect:/";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id) {
        repository.deleteById(id);
        return "redirect:/";
    }
}
