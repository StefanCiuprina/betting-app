package com.example.application.data.repositories;

import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BettingTicketsRepository extends JpaRepository<User, Integer> {
}
