package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ClienteDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ClienteInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Clientes")
public interface ClienteControllerOpenApi {

    @Operation(summary ="Lista os clientes")
    public ResponseEntity<List<ClienteDto>> listar(@Parameter(description = "nome de um cliente")
                                                       String nome);


    @Operation(summary ="Busca cliente por id", responses ={
            @ApiResponse(responseCode = "400", description = "Id do cliente inválido", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "404", description = "cliente não encontrado", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ClienteDto> buscarPorId(@Parameter(description = "ID de um cliente", example = "1")
                                                      Long clienteId);


    @Operation(summary ="Cria um novo cliente", responses ={
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "400", description = "Mensagem incompreensivel")
    })
    public ResponseEntity<ClienteDto> adicionar(@Parameter(description = "Representação de um novo cliente")
                                                    ClienteInput clienteInput);


    @Operation(summary ="Altera um cliente", responses ={
            @ApiResponse(responseCode = "200", description = "Cliente atualizado"),
            @ApiResponse(responseCode = "404", description = "cliente não encontrado", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ClienteDto> alterar(@Parameter(description = "Representação de um novo cliente com novos dados")
                                                  ClienteInput clienteInput,
                                              @Parameter(description = "ID de um cliente", example = "1")
                                                  Long clienteId);


    @Operation(summary ="Apaga um cliente", responses ={
            @ApiResponse(responseCode = "204", description = "Cliente excluido"),
            @ApiResponse(responseCode = "404", description = "cliente não encontrado", content = @Content(schema = @Schema))
    })
    public void deletar(@Parameter(description = "ID de um cliente", example = "1")
                            Long clienteId);


}
