package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.UsuarioDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.UsuarioInput;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.UsuarioNovaSenhaInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Usuarios")
public interface UsuarioControllerOpenApi {

    @ApiOperation("Lista os usuarios")
    public ResponseEntity<List<UsuarioDto>> buscarUsuarios();

    @ApiOperation("Busca um usuario por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id do usuario inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Usuario não encontrado", response = Problem.class)
    })
    public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@ApiParam(value = "ID de um usuario", example = "1")
                                                             Long usuarioId);

    @ApiOperation("Busca um usuario por email")
    @ApiResponses({
            @ApiResponse(code = 400, message = "email do usuario inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Usuario não encontrado", response = Problem.class)
    })
    public ResponseEntity<UsuarioDto> buscarUsuarioPorEmail(@ApiParam(value = "Email de um usuario", example = "joao@csb.com.br")
                                                                String email);


    @ApiOperation("Cria um novo usuario com cliente existente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuario cadastrado"),
            @ApiResponse(code = 400, message = "Dados invalidos", response = Problem.class),
            @ApiResponse(code = 400, message = "Mensagem incompreensivel", response = Problem.class),
            @ApiResponse(code = 400, message = "Email já cadastrado"),
            @ApiResponse(code = 404, message = "Recurso não encontrado", response = Problem.class)
    })
    public ResponseEntity<UsuarioDto> criarUsuarioClienteExistente(@ApiParam(name = "Corpo", value = "Representação de um novo usuario")
                                                                       UsuarioInput usuarioInput,
                                                                   @ApiParam(value = "Id de um cliente", example = "1")
                                                                       Long clienteId);


    @ApiOperation("Cria um novo usuario")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuario cadastrado"),
            @ApiResponse(code = 400, message = "Dados invalidos", response = Problem.class),
            @ApiResponse(code = 400, message = "Mensagem incompreensivel", response = Problem.class),
            @ApiResponse(code = 400, message = "Email já cadastrado")
    })
    public ResponseEntity<UsuarioDto> criarUsuarioNovo(@ApiParam(name = "Corpo", value = "Representação de um novo usuario")
                                                           UsuarioInput usuarioInput);

    @ApiOperation("Cria um novo usuario com profissional existente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuario cadastrado"),
            @ApiResponse(code = 400, message = "Dados invalidos", response = Problem.class),
            @ApiResponse(code = 400, message = "Mensagem incompreensivel", response = Problem.class),
            @ApiResponse(code = 400, message = "Email já cadastrado"),
            @ApiResponse(code = 404, message = "Recurso não encontrado", response = Problem.class)
    })
    public ResponseEntity<UsuarioDto> criarUsuarioProfissionalExistente(@ApiParam(name = "Corpo", value = "Representação de um novo usuario")
                                                                            UsuarioInput usuarioInput,
                                                                        @ApiParam(value = "Id de um profissional", example = "1")
                                                                            Long profissionalId);


    @ApiOperation("Altera senha de um usuario")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuario atualizado"),
            @ApiResponse(code = 404, message = "Usuario não encontrado", response = Problem.class)
    })
    public void alterarSenha(@RequestBody @Valid UsuarioNovaSenhaInput novaSenha);


    @ApiOperation("Altera permissao de um usuario")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuario atualizado"),
            @ApiResponse(code = 404, message = "Usuario não encontrado", response = Problem.class)
    })
    public void alterarPermissaoUsuario(@ApiParam(value = "Id de um usuario", example = "1")
                                            Long usuarioId,
                                        @ApiParam(value = "Id de uma permissao", example = "1")
                                            Long permissaoId);
}
