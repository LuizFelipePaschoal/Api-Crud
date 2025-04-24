package com.example.usuarioapi.controller;

import com.example.usuarioapi.model.Usuario;
import com.example.usuarioapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Usuario> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public Usuario createUser(@RequestBody Usuario user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public Usuario getUserById(@PathVariable UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Usuario updateUser(@PathVariable UUID id, @RequestBody Usuario newUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(newUser.getName());
            user.setLastName(newUser.getLastName());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());
            return userRepository.save(user);
        }).orElseThrow();
    }

    @PatchMapping("/{id}")
    public Usuario patchUser(@PathVariable UUID id, @RequestBody Usuario userUpdates) {
        return userRepository.findById(id).map(user -> {
            if (userUpdates.getName() != null) user.setName(userUpdates.getName());
            if (userUpdates.getLastName() != null) user.setLastName(userUpdates.getLastName());
            if (userUpdates.getEmail() != null) user.setEmail(userUpdates.getEmail());
            if (userUpdates.getPassword() != null) user.setPassword(userUpdates.getPassword());
            return userRepository.save(user);
        }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userRepository.deleteById(id);
    }
}
