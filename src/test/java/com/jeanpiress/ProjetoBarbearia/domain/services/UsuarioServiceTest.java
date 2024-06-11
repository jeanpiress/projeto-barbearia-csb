package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioNovaSenhaInput;
import com.jeanpiress.ProjetoBarbearia.domain.exceptions.*;
import com.jeanpiress.ProjetoBarbearia.domain.model.Permissao;
import com.jeanpiress.ProjetoBarbearia.domain.model.Usuario;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
class UsuarioServiceTest {

    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;
    
    @Mock
    ProfissionalService profissionalService;
    
    @Mock
    ClienteService clienteService;

    @Mock
    PermissaoService permissaoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    Usuario usuarioRecepcao;
    Usuario usuarioProfissional;
    Usuario usuarioGerente;
    Usuario usuarioVazio;
    Cliente cliente;
    Profissional profissional;
    Permissao permissaoGerente;
    Permissao permissaoRecepcao;
    Permissao permissaoProfissional;
    Permissao permissaoCliente;
    UsuarioNovaSenhaInput usuarioNovaSenhaInput;

    Set<Permissao> permissoes = new HashSet<>();



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        permissaoGerente = new Permissao(1L, "GERENTE", null, null );
        permissaoRecepcao = new Permissao(2L, "RECEPCAO", null, null );
        permissaoProfissional = new Permissao(3L, "PROFISSIONAL", null, null);
        permissaoCliente = new Permissao(4L, "CLIENTE", null, null);

        profissional = new Profissional(1L, "João Silva", "João", "34999999999", null,
                OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), BigDecimal.ZERO, null, true, null);

        cliente = new Cliente(1L, "João", "34999999999", OffsetDateTime.parse("1991-11-13T00:00:00-03:00"), 
                null, BigDecimal.ZERO, null, null, 30, profissional, null);

        usuarioGerente = new Usuario(1L, "joao@csb.com", "123456", "joao", "GERENTE", null,
                null, permissoes );

        usuarioProfissional = new Usuario(2L, "joao@csb.com", "123456", "joao", "PROFISSIONAL", null,
                profissional, Set.of(permissaoCliente, permissaoProfissional));

        usuarioRecepcao = new Usuario(3L, "joao@csb.com", "123456", "joao", "RECEPCAO", null,
                null, permissoes );

        usuarioVazio = new Usuario(null, "joao@csb.com", "123456", "joao", null, null,
                null, null);

        usuarioNovaSenhaInput = new UsuarioNovaSenhaInput("joao@csb.com", "123456", "789123", "789123");
    }

    @Test
    public void deveBuscarUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioGerente));

        Usuario usuarioSalvo = usuarioService.buscarPorId(1L);

        assertEquals(usuarioSalvo, usuarioGerente);
        verify(usuarioRepository).findById(1L);
        verifyNoMoreInteractions(usuarioRepository);
    }


    @Test
    public void deveLancarUsuarioNaoEncontradoExceptionAoBuscarUsuarioPorId() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        UsuarioNaoEncontradoException exception = Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> {usuarioService.buscarPorId(2L);
                });

        assertEquals("Não existe um cadastro de usuario com codigo 2", exception.getMessage());

        verify(usuarioRepository).findById(2L);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    public void deveBuscarUsuarioPorEmail() {
        when(usuarioRepository.findByEmail("joao@csb.com")).thenReturn(Optional.of(usuarioGerente));

        Usuario usuarioSalvo = usuarioService.buscarPorEmail("joao@csb.com");

        assertEquals(usuarioSalvo, usuarioGerente);
        verify(usuarioRepository).findByEmail(any());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    public void deveLancarUsuarioNaoEncontradoExceptionAoBuscarUsuarioPorEmail() {
        when(usuarioRepository.findByEmail("joao@gmail.com")).thenReturn(Optional.empty());

        UsuarioNaoEncontradoException exception = Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> {usuarioService.buscarPorEmail("joao@gmail.com");
                });

        assertEquals("Não existe um cadastro de usuario com este email", exception.getMessage());

        verify(usuarioRepository).findByEmail(any());
        verifyNoMoreInteractions(usuarioRepository);
    }


    @Test
    public void deveAdicionarNovoUsuarioPartindoDeUmCliente() {
        when(usuarioRepository.existsByEmail(usuarioVazio.getEmail())).thenReturn(false);
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(permissaoService.buscarPorId(4L)).thenReturn(permissaoCliente);
        when(passwordEncoder.encode(usuarioVazio.getSenha())).thenReturn("encodedPassword");
        when(usuarioRepository.save(usuarioVazio)).thenReturn(usuarioVazio);

        Usuario usuarioSalvo = usuarioService.criarUsuarioClienteExistente(usuarioVazio, 1L);

        assertEquals(usuarioSalvo.getCliente(), cliente);
        assertEquals(usuarioSalvo.getMaiorPermissao(), "CLIENTE");
        assertEquals(usuarioSalvo.getPermissoes().size(), 1);
        assertEquals(usuarioSalvo.getSenha(), "encodedPassword");
        assertTrue(usuarioSalvo.getPermissoes().contains(permissaoCliente));
        assertNull(usuarioSalvo.getProfissional());

        verify(usuarioRepository).existsByEmail(usuarioVazio.getEmail());
        verify(clienteService).buscarPorId(1L);
        verify(permissaoService).buscarPorId(4L);
        verify(usuarioRepository).save(usuarioVazio);
        verifyNoMoreInteractions(usuarioRepository, clienteService, permissaoService);
    }

    @Test
    public void deveLancarEmailExistenteExceptionAoTentarCriarUsuarioComEmailExistente() {
        when(usuarioRepository.existsByEmail(usuarioVazio.getEmail())).thenReturn(true);

        EmailExistenteException exception = Assertions.assertThrows(EmailExistenteException.class,
                () -> {usuarioService.criarUsuarioClienteExistente(usuarioVazio, 1L);
                });

        assertEquals("Email já cadastrado", exception.getMessage());

        verify(usuarioRepository).existsByEmail(any());
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    public void deveCriarUsuarioSemRelacionamento(){
        usuarioVazio.setMaiorPermissao("GERENTE");
        when(usuarioRepository.existsByEmail(usuarioVazio.getEmail())).thenReturn(false);
        when(permissaoService.buscarPorId(1L)).thenReturn(permissaoGerente);
        when(permissaoService.buscarPorId(2L)).thenReturn(permissaoRecepcao);
        when(permissaoService.buscarPorId(3L)).thenReturn(permissaoProfissional);
        when(permissaoService.buscarPorId(4L)).thenReturn(permissaoCliente);
        when(passwordEncoder.encode(usuarioVazio.getSenha())).thenReturn("encodedPassword");
        when(usuarioRepository.save(usuarioVazio)).thenReturn(usuarioVazio);

        Usuario usuarioSalvo = usuarioService.criarUsuario(usuarioVazio);

        assertEquals(usuarioSalvo.getMaiorPermissao(), "GERENTE");
        assertEquals(usuarioSalvo.getSenha(), "encodedPassword");
        assertEquals(usuarioSalvo.getPermissoes().size(), 4);
        assertTrue(usuarioSalvo.getPermissoes().contains(permissaoGerente));
        assertTrue(usuarioSalvo.getPermissoes().contains(permissaoRecepcao));
        assertTrue(usuarioSalvo.getPermissoes().contains(permissaoProfissional));
        assertTrue(usuarioSalvo.getPermissoes().contains(permissaoCliente));
        assertNull(usuarioSalvo.getProfissional());
        assertNull(usuarioSalvo.getCliente());

        verify(usuarioRepository).existsByEmail(usuarioVazio.getEmail());
        verify(permissaoService).buscarPorId(1L);
        verify(permissaoService).buscarPorId(2L);
        verify(permissaoService).buscarPorId(3L);
        verify(permissaoService).buscarPorId(4L);
        verify(usuarioRepository).save(usuarioVazio);
        verifyNoMoreInteractions(usuarioRepository, clienteService, permissaoService);
    }

    @Test
    public void deveLancarPermissaoNaoEncontradaExceptionAoTentarCriarUsuarioSemRelacionamentoSemPassarPermissao() {
        PermissaoNaoEncontradaException exception = Assertions.assertThrows(PermissaoNaoEncontradaException.class,
                () -> {usuarioService.criarUsuario(usuarioVazio);
                });

        assertEquals("Permissao é obrigátorio", exception.getMessage());

    }

    @Test
    public void deveLancarPermissaoInvalidaExceptionAoTentarCriarUsuarioSemRelacionamentoComPermissaoInvalida() {
        usuarioVazio.setMaiorPermissao("BARBEIRO");

        PermissaoInvalidaException exception = Assertions.assertThrows(PermissaoInvalidaException.class,
                () -> {usuarioService.criarUsuario(usuarioVazio);
                });

        assertEquals("BARBEIRO não é um nome de permissao valido", exception.getMessage());

    }

    @Test
    public void deveAdicionarNovoUsuarioPartindoDeUmProfissional() {
        usuarioVazio.setMaiorPermissao("PROFISSIONAL");
        when(usuarioRepository.existsByEmail(usuarioVazio.getEmail())).thenReturn(false);
        when(profissionalService.buscarPorId(1L)).thenReturn(profissional);
        when(permissaoService.buscarPorId(3L)).thenReturn(permissaoProfissional);
        when(permissaoService.buscarPorId(4L)).thenReturn(permissaoCliente);
        when(passwordEncoder.encode(usuarioVazio.getSenha())).thenReturn("encodedPassword");
        when(usuarioRepository.save(usuarioVazio)).thenReturn(usuarioVazio);

        Usuario usuarioSalvo = usuarioService.criarUsuarioProfissionalExistente(usuarioVazio, 1L);

        assertEquals(usuarioSalvo.getProfissional(), profissional);
        assertEquals(usuarioSalvo.getMaiorPermissao(), "PROFISSIONAL");
        assertEquals(usuarioSalvo.getPermissoes().size(), 2);
        assertEquals(usuarioSalvo.getSenha(), "encodedPassword");
        assertTrue(usuarioSalvo.getPermissoes().contains(permissaoProfissional));
        assertTrue(usuarioSalvo.getPermissoes().contains(permissaoCliente));
        assertNull(usuarioSalvo.getCliente());

        verify(usuarioRepository).existsByEmail(usuarioVazio.getEmail());
        verify(profissionalService).buscarPorId(1L);
        verify(permissaoService).buscarPorId(3L);
        verify(permissaoService).buscarPorId(4L);
        verify(usuarioRepository).save(usuarioVazio);
        verifyNoMoreInteractions(usuarioRepository, profissionalService, permissaoService);
    }

    @Test
    public void deveAlterarSenha(){
        when(usuarioRepository.findByEmail(usuarioGerente.getEmail())).thenReturn(Optional.of(usuarioGerente));
        when(passwordEncoder.matches(usuarioNovaSenhaInput.getSenhaAtual(), usuarioGerente.getSenha())).thenReturn(true);
        when(usuarioRepository.save(usuarioGerente)).thenReturn(usuarioGerente);
        when(passwordEncoder.encode(usuarioNovaSenhaInput.getNovaSenha())).thenReturn("encodedPassword");

        usuarioService.alterarSenha(usuarioNovaSenhaInput);

        assertEquals(usuarioGerente.getSenha(), "encodedPassword");

        verify(usuarioRepository).findByEmail(usuarioGerente.getEmail());
        verify(usuarioRepository).save(usuarioGerente);
        verifyNoMoreInteractions(usuarioRepository);

    }

    @Test
    public void deveLancarSenhaAtualIncorretaExceptionAoPassarSenhaIncorretaEmAlterarSenha(){
        when(usuarioRepository.findByEmail(usuarioGerente.getEmail())).thenReturn(Optional.of(usuarioGerente));
        when(passwordEncoder.matches(usuarioNovaSenhaInput.getSenhaAtual(), usuarioGerente.getSenha())).thenReturn(false);

        SenhaAtualIncorretaException exception = Assertions.assertThrows(SenhaAtualIncorretaException.class,
                () -> {usuarioService.alterarSenha(usuarioNovaSenhaInput);
                });

        assertEquals("Senha atual não esta correta", exception.getMessage());
    }

    @Test
    public void deveLancarConferenciaSenhaExceptionCasoNovaSenhaEConfirmarSenhaSejamDiferentes(){
        usuarioNovaSenhaInput.setConfirmarSenha("159753");
        when(usuarioRepository.findByEmail(usuarioGerente.getEmail())).thenReturn(Optional.of(usuarioGerente));
        when(passwordEncoder.matches(usuarioNovaSenhaInput.getSenhaAtual(), usuarioGerente.getSenha())).thenReturn(true);

        ConferenciaSenhaException exception = Assertions.assertThrows(ConferenciaSenhaException.class,
                () -> {usuarioService.alterarSenha(usuarioNovaSenhaInput);
                });

        assertEquals("Confirme sua senha corretamente", exception.getMessage());
    }

    @Test
    public void deveAumentarPermissaoDeUmUsuario(){
        permissoes.add(permissaoCliente);
        permissoes.add(permissaoProfissional);
        usuarioProfissional.setPermissoes(permissoes);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuarioProfissional));
        when(usuarioRepository.save(usuarioProfissional)).thenReturn(usuarioProfissional);
        when(permissaoService.buscarPorId(2L)).thenReturn(permissaoRecepcao);
        when(permissaoService.buscarPorId(3L)).thenReturn(permissaoProfissional);
        when(permissaoService.buscarPorId(4L)).thenReturn(permissaoCliente);

        usuarioService.alterarPermissao(2L, 2L);

        assertEquals(usuarioProfissional.getPermissoes().size(), 3);
        assertTrue(usuarioProfissional.getPermissoes().contains(permissaoRecepcao));
        assertTrue(usuarioProfissional.getPermissoes().contains(permissaoProfissional));
        assertTrue(usuarioProfissional.getPermissoes().contains(permissaoCliente));
        assertEquals(usuarioProfissional.getMaiorPermissao(), "RECEPCAO");


    }

    @Test
    public void deveDiminuirPermissaoDeUmUsuario(){
        permissoes.add(permissaoCliente);
        permissoes.add(permissaoProfissional);
        permissoes.add(permissaoRecepcao);
        usuarioRecepcao.setPermissoes(permissoes);
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuarioRecepcao));
        when(usuarioRepository.save(usuarioRecepcao)).thenReturn(usuarioRecepcao);
        when(permissaoService.buscarPorId(4L)).thenReturn(permissaoCliente);

        usuarioService.alterarPermissao(3L, 4L);

        assertEquals(usuarioRecepcao.getPermissoes().size(), 1);
        assertEquals(usuarioRecepcao.getMaiorPermissao(), "CLIENTE");
        assertTrue(usuarioRecepcao.getPermissoes().contains(permissaoCliente));


    }

}