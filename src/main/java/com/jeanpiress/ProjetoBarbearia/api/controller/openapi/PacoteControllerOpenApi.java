package com.jeanpiress.ProjetoBarbearia.api.controller.openapi;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PacoteDto;
import com.jeanpiress.ProjetoBarbearia.domain.corpoRequisicao.RealiazacaoItemPacote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Pacote")
public interface PacoteControllerOpenApi {

    @Operation(summary ="Busca todos os pacotes")
    public ResponseEntity<List<PacoteDto>> listar();

    @Operation(summary ="Busca pacote por id", responses ={
            @ApiResponse(responseCode = "400", description = "Id do pacote inválido", content = @Content(schema = @Schema)),
            @ApiResponse(responseCode = "404", description = "pacote não encontrado", content = @Content(schema = @Schema))
    })
    public ResponseEntity<PacoteDto> buscarPorId(@Parameter(description = "ID de um pacote", example = "1")
                                                     Long pacoteId);

    @Operation(summary ="Busca pacotes ativos")
    public ResponseEntity<List<PacoteDto>> buscarPacotesAtivos();

    @Operation(summary ="Busca pacotes expirados")
    public ResponseEntity<List<PacoteDto>> buscarPacotesExpirados();

    @Operation(summary ="Busca pacotes por cliente", responses ={
            @ApiResponse(responseCode = "400", description = "Id do cliente inválido", content = @Content(schema = @Schema))
    })
    public ResponseEntity<List<PacoteDto>> buscarPorCliente(@Parameter(description = "ID de um cliente", example = "1")
                                                                Long clienteId);


    @Operation(summary ="Faz pagamento por pacote")
    public ResponseEntity<PacoteDto> receberPacote(@Parameter(description = "Representação de um pagamento por pacote")
                                                         RealiazacaoItemPacote realizacaoItemPacote);


}
