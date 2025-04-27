package com.example.usuarioapi.service;

import com.example.usuarioapi.model.Usuario;
import com.example.usuarioapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    public List<Usuario> listarTodos() {
        return userRepository.findAll();
    }

    public Usuario criarUsuario(Usuario usuario) {
        return userRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return userRepository.findById(id);
    }

    public Usuario atualizarUsuario(UUID id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        usuarioExistente.setName(usuarioAtualizado.getName());
        usuarioExistente.setLastName(usuarioAtualizado.getLastName());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setPassword(usuarioAtualizado.getPassword());

        return userRepository.save(usuarioExistente);
    }

    public Usuario atualizarParcial(UUID id, Map<String, Object> updates) {
        return userRepository.findById(id)
                .map(usuario -> {
                    updates.forEach((String key, Object value) -> {
                        switch (key) {
                            case "name" -> usuario.setName((String) value);
                            case "lastName" -> usuario.setLastName((String) value);
                            case "email" -> usuario.setEmail((String) value);
                            case "password" -> usuario.setPassword((String) value);
                            default -> throw new IllegalArgumentException("Campo inválido: " + key);
                        }
                    });
                    return userRepository.save(usuario);
                })
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado para atualização")); // <<-- CORREÇÃO AQUI
    }

    public void deletarUsuario(UUID id) {
        userRepository.deleteById(id);
    }
}
