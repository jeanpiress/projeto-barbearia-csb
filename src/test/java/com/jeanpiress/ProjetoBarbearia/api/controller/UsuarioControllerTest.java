package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler.UsuarioAssembler;
import com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler.UsuarioInputDissembler;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.UsuarioDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioNovaSenhaInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.Permissao;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import com.jeanpiress.ProjetoBarbearia.domain.model.Usuario;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.UsuarioRepository;
import com.jeanpiress.ProjetoBarbearia.domain.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class UsuarioControllerTest {

    @InjectMocks
    UsuarioController usuarioController;

    @Mock
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    UsuarioAssembler usuarioAssembler;

    @Mock
    UsuarioInputDissembler usuarioInputDissembler;

    MockMvc mockMvc;
    Usuario usuario;
    UsuarioDto usuarioDto;
    UsuarioInput usuarioInput;
    UsuarioNovaSenhaInput usuarioNovaSenhaInput;
    Profissional profissional;
    Cliente cliente;
    Usuario usuarioNovo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).alwaysDo(print()).build();

        Permissao permissao = new Permissao(1L, "GERENTE", null, null);
        profissional = new Profissional(1L, "João Silva", "João", "34999999999",
                null, LocalDate.parse("1991-11-13"), BigDecimal.ZERO, null, true, null);

        cliente = new Cliente(1L, "João", "34999999999", LocalDate.parse("1991-11-13"),
                null, BigDecimal.ZERO, null, null, 30, true, profissional, null);

        usuario = new Usuario(1L, "joao@csb.com", "123456", "joao", "GERENTE", null,
                null, Set.of(permissao));

        usuarioDto = new UsuarioDto();

        usuarioInput = new UsuarioInput("joao@csb.com", "123456", "joao", "GERENTE");

        usuarioNovaSenhaInput = new UsuarioNovaSenhaInput("joao@csb.com", "123456", "789123", "789123");

        usuarioNovo = new Usuario(2L, "joao@csb.com", "123456", "João", "GERENTE", null, null, null);
    }

    @Test
    void deveListarTodosUsuarios() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuario);

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(usuarioAssembler.collectionToModel(usuarios)).thenReturn(Arrays.asList(usuarioDto));

        mockMvc.perform(get("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(usuarioRepository).findAll();
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        when(usuarioService.buscarPorId(anyLong())).thenReturn(usuario);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDto);

        mockMvc.perform(get("/usuarios/id/{usuarioId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(usuarioService).buscarPorId(1L);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveBuscarUsuarioPorEmail() throws Exception {
        when(usuarioService.buscarPorEmail(anyString())).thenReturn(usuario);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDto);

        mockMvc.perform(get("/usuarios/email/{email}", "joao@csb.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(usuarioService).buscarPorEmail("joao@csb.com");
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveCriarUsuarioClienteExistente() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(usuarioService.criarUsuarioClienteExistente(any(Usuario.class), anyLong())).thenReturn(usuario);
        when(usuarioInputDissembler.toDomainObject(any(UsuarioInput.class))).thenReturn(usuario);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDto);

        mockMvc.perform(post("/usuarios/criar/cliente/{clienteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInput)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(usuarioService).criarUsuarioClienteExistente(any(Usuario.class), anyLong());
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveCriarUsuarioNovo() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(usuarioService.criarUsuario(any(Usuario.class))).thenReturn(usuario);
        when(usuarioInputDissembler.toDomainObject(any(UsuarioInput.class))).thenReturn(usuario);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDto);

        mockMvc.perform(post("/usuarios/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInput)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(usuarioService).criarUsuario(any(Usuario.class));
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveAlterarSenha() throws Exception {
        doNothing().when(usuarioService).alterarSenha(any(UsuarioNovaSenhaInput.class));

        mockMvc.perform(put("/usuarios/alterar-senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(usuarioNovaSenhaInput)))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(usuarioService).alterarSenha(any(UsuarioNovaSenhaInput.class));
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveAlterarPermissaoUsuario() throws Exception {
        doNothing().when(usuarioService).alterarPermissao(anyLong(), anyLong());

        mockMvc.perform(put("/usuarios/{usuarioId}/alterar-permissao/{permissaoId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioService).alterarPermissao(1L, 1L);
        verifyNoMoreInteractions(usuarioService);
    }

    @Test
    void deveCriarUsuarioProfissionalExistente() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(usuarioInputDissembler.toDomainObject(any(UsuarioInput.class))).thenReturn(usuario);
        when(usuarioService.criarUsuarioProfissionalExistente(any(Usuario.class), anyLong())).thenReturn(usuarioNovo);
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(usuarioDto);

        mockMvc.perform(post("/usuarios/criar/profissional/{profissionalId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInput)))
                .andExpect(status().isCreated())
                .andReturn();

        verify(usuarioInputDissembler).toDomainObject(any(UsuarioInput.class));
        verify(usuarioService).criarUsuarioProfissionalExistente(any(Usuario.class), eq(1L));
        verify(usuarioAssembler).toModel(any(Usuario.class));
        verifyNoMoreInteractions(usuarioInputDissembler, usuarioService, usuarioAssembler);
    }
}
