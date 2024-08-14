package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.UsuarioDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioInput;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.UsuarioNovaSenhaInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;



import java.util.List;
@Tag(name = "Usuarios")
@SecurityRequirement(name = "security_auth")
public interface UsuarioControllerOpenApi {

    @Operation(summary = "Lista os usuarios")
    public ResponseEntity<List<UsuarioDto>> buscarUsuarios();

    @Operation(summary = "Busca um usuario por id", responses ={

            @ApiResponse(responseCode = "400", description = "Id do usuario inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
})

    public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@Parameter(description = "ID de um usuario", example = "1", required = true)
                                                             Long usuarioId);

    @Operation(summary = "Busca um usuario por email", responses =
    {
            @ApiResponse(responseCode = "400", description = "email do usuario inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<UsuarioDto> buscarUsuarioPorEmail(@Parameter(description = "Email de um usuario", example = "joao@csb.com.br", required = true)
                                                                String email);

    @Operation(summary = "Cria um novo usuario com cliente existente", responses =
    {
            @ApiResponse(responseCode = "201", description = "Usuario cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "400", description = "Mensagem incompreensivel", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "400", description = "Email já cadastrado"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<UsuarioDto> criarUsuarioClienteExistente(@RequestBody(description = "Representação de um novo usuario", required = true)
                                                                       UsuarioInput usuarioInput,
                                                                   @Parameter(description = "Id de um cliente", example = "1", required = true)
                                                                       Long clienteId);


    @Operation(summary = "Cria um novo usuario", responses =
    {
            @ApiResponse(responseCode = "201", description = "Usuario cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "400", description = "Mensagem incompreensivel", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "400", description = "Email já cadastrado")
    })
    public ResponseEntity<UsuarioDto> criarUsuarioNovo(@RequestBody(description = "Representação de um novo usuario", required = true)
                                                           UsuarioInput usuarioInput);

    @Operation(summary = "Cria um novo usuario com profissional existente", responses =
    {
            @ApiResponse(responseCode = "201", description = "Usuario cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "400", description = "Mensagem incompreensivel", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "400", description = "Email já cadastrado"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<UsuarioDto> criarUsuarioProfissionalExistente(@RequestBody(description = "Representação de um novo usuario", required = true)
                                                                            UsuarioInput usuarioInput,
                                                                        @Parameter(description = "Id de um profissional", example = "1", required = true)
                                                                            Long profissionalId);


    @Operation(summary = "Altera senha de um usuario", responses =
    {
            @ApiResponse(responseCode = "200", description = "Usuario atualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void alterarSenha(UsuarioNovaSenhaInput novaSenha);


    @Operation(summary = "Altera permissao de um usuario", responses =
    {
            @ApiResponse(responseCode = "200", description = "Usuario atualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void alterarPermissaoUsuario(@Parameter(description = "Id de um usuario", example = "1", required = true)
                                            Long usuarioId,
                                        @Parameter(description = "Id de uma permissao", example = "1", required = true)
                                            Long permissaoId);
}
