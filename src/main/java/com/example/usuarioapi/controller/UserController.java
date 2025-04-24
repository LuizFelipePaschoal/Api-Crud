package com.example.usuarioapi.controller;

import com.example.usuarioapi.model.Usuario;
import com.example.usuarioapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAllUsers() {
        return usuarioService.listarTodos();
    }

    @PostMapping
    public Usuario createUser(@RequestBody Usuario user) {
        return usuarioService.criarUsuario(user);
    }

    @GetMapping("/{id}")
    public Usuario getUserById(@PathVariable UUID id) {
        return usuarioService.buscarPorId(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Usuario updateUser(@PathVariable UUID id, @RequestBody Usuario newUser) {
        return usuarioService.atualizarUsuario(id, newUser);
    }

    @PatchMapping("/{id}")
    public Usuario patchUser(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return usuarioService.atualizarParcial(id, updates);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        usuarioService.deletarUsuario(id);
    }
}
