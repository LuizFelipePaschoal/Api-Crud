// Pacote correto
package com.example.usuarioapi.service;

import com.example.usuarioapi.model.Usuario;
import com.example.usuarioapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    // Cria um mock do repositório
    @Mock
    private UserRepository userRepository;

    // Injeta o mock dentro do service real
    @InjectMocks
    private UsuarioService usuarioService;

    // Inicializa os mocks antes de cada teste
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosUsuarios() {
        // Simula que existem 2 usuários no repositório
        List<Usuario> lista = List.of(new Usuario(), new Usuario());
        when(userRepository.findAll()).thenReturn(lista);

        // Executa o método real
        List<Usuario> resultado = usuarioService.listarTodos();

        // Verifica se retornou os 2 usuários simulados
        assertEquals(2, resultado.size());

        // Verifica se o método findAll foi chamado 1 vez
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void deveCriarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");

        // Simula o salvamento
        when(userRepository.save(usuario)).thenReturn(usuario);

        Usuario salvo = usuarioService.criarUsuario(usuario);

        assertEquals("teste@email.com", salvo.getEmail());
        verify(userRepository, times(1)).save(usuario);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
    }

    @Test
    void deveRetornarOptionalVazioQuandoUsuarioNaoExiste() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarPorId(id);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAtualizarUsuarioQuandoIdExiste() {
        UUID id = UUID.randomUUID();
        Usuario existente = new Usuario();
        existente.setId(id);

        Usuario atualizado = new Usuario();
        atualizado.setName("Novo");
        atualizado.setLastName("Nome");
        atualizado.setEmail("novo@email.com");
        atualizado.setPassword("senha123");

        // Simula que encontrou o usuário
        when(userRepository.findById(id)).thenReturn(Optional.of(existente));
        // Simula que salvou o usuário
        when(userRepository.save(existente)).thenReturn(existente);

        Usuario resultado = usuarioService.atualizarUsuario(id, atualizado);

        assertEquals("Novo", resultado.getName());
        assertEquals("Nome", resultado.getLastName());
        assertEquals("novo@email.com", resultado.getEmail());
        assertEquals("senha123", resultado.getPassword());

        verify(userRepository).save(existente);
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarUsuarioInexistente() {
        UUID id = UUID.randomUUID();
        Usuario atualizado = new Usuario();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Espera que uma exceção seja lançada
        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.atualizarUsuario(id, atualizado);
        });
    }

    @Test
    void deveAtualizarParcialmenteUsuario() {
        UUID id = UUID.randomUUID();
        Usuario existente = new Usuario();
        existente.setId(id);

        // Simula atualizações parciais
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "NovoNome");
        updates.put("email", "novo@email.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existente));
        when(userRepository.save(existente)).thenReturn(existente);

        Usuario resultado = usuarioService.atualizarParcial(id, updates);

        assertEquals("NovoNome", resultado.getName());
        assertEquals("novo@email.com", resultado.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarParcialUsuarioInexistente() {
        UUID id = UUID.randomUUID();
        Map<String, Object> updates = Map.of("name", "Teste");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            usuarioService.atualizarParcial(id, updates);
        });
    }

    @Test
    void deveDeletarUsuario() {
        UUID id = UUID.randomUUID();

        usuarioService.deletarUsuario(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}
