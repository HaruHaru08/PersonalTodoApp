package com.example.personal_todo_app.repository;

import com.example.personal_todo_app.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findAll();
    Todo save(Todo todo);
    Optional<Todo> findById(Long id);
    void deleteById(Long id);
}
