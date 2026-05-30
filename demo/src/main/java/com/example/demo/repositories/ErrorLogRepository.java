package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, String> {
}

