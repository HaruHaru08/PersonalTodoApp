package com.example.personal_todo_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "todo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "content")
    private String content;

    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "status")
    private boolean status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "priority")
    private Priority priority;
}
