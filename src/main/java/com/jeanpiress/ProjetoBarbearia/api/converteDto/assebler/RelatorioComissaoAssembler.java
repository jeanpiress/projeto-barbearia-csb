package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioComissao;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioComissao;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioComissaoDto;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.RelatorioComissaoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelatorioComissaoAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public RelatorioComissaoDto toModel(RelatorioComissao relatorioComissao){
        return modelMapper.map(relatorioComissao, RelatorioComissaoDto.class);
    }

    public List<RelatorioComissaoDto> collectionToModel(List<RelatorioComissao> pacotesProntos){
         return pacotesProntos.stream().map(relatorioComissao -> toModel(relatorioComissao))
                .collect(Collectors.toList());
    }
}
