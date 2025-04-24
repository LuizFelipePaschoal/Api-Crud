package com.example.usuarioapi.repository;

import com.example.usuarioapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<Usuario, UUID> {
}