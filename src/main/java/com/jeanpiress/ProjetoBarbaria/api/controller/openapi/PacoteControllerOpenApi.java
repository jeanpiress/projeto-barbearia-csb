package com.jeanpiress.ProjetoBarbaria.api.controller.openapi;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.PacoteDto;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PacoteInput;
import com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers.Problem;
import com.jeanpiress.ProjetoBarbaria.domain.corpoRequisicao.RealiazacaoItemPacote;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pacote;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Pacote")
public interface PacoteControllerOpenApi {

    @ApiOperation("Busca todos os pacotes")
    public ResponseEntity<List<PacoteDto>> listar();

    @ApiOperation("Busca pacote por id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id do pacote inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "pacote não encontrado", response = Problem.class)
    })
    public ResponseEntity<PacoteDto> buscarPorId(@ApiParam(value = "ID de um pacote", example = "1")
                                                     Long pacoteId);

    @ApiOperation("Busca pacotes ativos")
    public ResponseEntity<List<PacoteDto>> buscarPacotesAtivos();

    @ApiOperation("Busca pacotes expirados")
    public ResponseEntity<List<PacoteDto>> buscarPacotesExpirados();

    @ApiOperation("Busca pacotes por cliente")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Id do cliente inválido", response = Problem.class)
    })
    public ResponseEntity<List<PacoteDto>> buscarPorCliente(@ApiParam(value = "ID de um cliente", example = "1")
                                                                Long clienteId);


    @ApiOperation("Faz pagamento por pacote")
    public ResponseEntity<PacoteDto> receberPacote(@ApiParam(name = "Corpo", value = "Representação de um pagamento por pacote")
                                                         RealiazacaoItemPacote realizacaoItemPacote);


}
