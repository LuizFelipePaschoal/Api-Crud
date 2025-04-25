package com.example.usuarioapi.controller;

// Importações das classes da aplicação e ferramentas de teste
import com.example.usuarioapi.model.Usuario;
import com.example.usuarioapi.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de teste para o UserController.
 * Usa MockMvc para simular requisições HTTP e Mockito para simular o serviço.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // Utilizado para simular chamadas HTTP à API

    // Cria um mock do UsuarioService que será injetado no controller
    @MockitoBean
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper; // Converte objetos Java para JSON
    private Usuario usuario; // Objeto padrão usado nos testes

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Instancia e popula um objeto de usuário para ser usado nos testes
        usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setName("João");
        usuario.setLastName("Silva");
        usuario.setEmail("joao@email.com");
        usuario.setPassword("123456");
    }

    // Testa o endpoint GET /users para listar todos os usuários
    @Test
    void deveListarTodosUsuarios() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
    }

    // Testa o endpoint GET /users/{id} para buscar usuário por ID
    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        when(usuarioService.buscarPorId(usuario.getId())).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/users/{id}", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    // Testa o endpoint POST /users para criar um novo usuário
    @Test
    void deveCriarUsuario() throws Exception {
        when(usuarioService.criarUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    // Testa o endpoint PUT /users/{id} para atualizar completamente um usuário
    @Test
    void deveAtualizarUsuario() throws Exception {
        when(usuarioService.atualizarUsuario(eq(usuario.getId()), any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/users/{id}", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João"));
    }

    // Testa o endpoint PATCH /users/{id} para atualização parcial
    @Test
    void deveAtualizarParcialmenteUsuario() throws Exception {
        Map<String, Object> updates = Map.of("name", "Carlos");
        usuario.setName("Carlos");

        when(usuarioService.atualizarParcial(eq(usuario.getId()), eq(updates))).thenReturn(usuario);

        mockMvc.perform(patch("/users/{id}", usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos"));
    }

    // Testa o endpoint DELETE /users/{id} para deletar um usuário
    @Test
    void deveDeletarUsuario() throws Exception {
        doNothing().when(usuarioService).deletarUsuario(usuario.getId());

        mockMvc.perform(delete("/users/{id}", usuario.getId()))
                .andExpect(status().isOk());
    }
}
