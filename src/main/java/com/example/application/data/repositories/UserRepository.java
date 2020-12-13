package com.example.application.data.repositories;

import com.example.application.data.entity.User;
import com.vaadin.flow.component.html.Span;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User getByUsername(String username);

    User getByEmail(String email);

    User getById(int id);

    @Transactional
    @Modifying
    void removeById(int id);
}
