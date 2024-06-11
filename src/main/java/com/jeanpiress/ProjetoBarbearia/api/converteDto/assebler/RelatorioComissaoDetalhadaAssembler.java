package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioComissaoDetalhada;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioComissaoDetalhadaDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelatorioComissaoDetalhadaAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public RelatorioComissaoDetalhadaDto toModel(RelatorioComissaoDetalhada relatorioComissaoDeltalhada){
        return modelMapper.map(relatorioComissaoDeltalhada, RelatorioComissaoDetalhadaDto.class);
    }

    public List<RelatorioComissaoDetalhadaDto> collectionToModel(List<RelatorioComissaoDetalhada> pacotesProntos){
         return pacotesProntos.stream().map(relatorioComissaoDeltalhada -> toModel(relatorioComissaoDeltalhada))
                .collect(Collectors.toList());
    }
}
