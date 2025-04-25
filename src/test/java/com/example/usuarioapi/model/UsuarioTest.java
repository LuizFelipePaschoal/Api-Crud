package com.example.usuarioapi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    // Testa se o método getFullName() retorna corretamente o nome completo do usuário
    @Test
    void deveRetornarNomeCompletoCorretamente() {
        Usuario usuario = new Usuario();
        usuario.setName("Maria");
        usuario.setLastName("Silva");

        // Chama o método que queremos testar
        String resultado = usuario.getFullName();

        // Verifica se o resultado é o esperado
        assertEquals("Maria Silva", resultado);
    }
}
