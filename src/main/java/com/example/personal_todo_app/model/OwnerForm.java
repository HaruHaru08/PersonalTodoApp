package com.example.personal_todo_app.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerForm {
    @NotBlank(message = "{owner.name.notBlank}")
    private String name;
}
