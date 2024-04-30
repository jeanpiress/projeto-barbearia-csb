package com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@ApiModel("Problema")
public class Problem {

	@ApiModelProperty(example = "Dados inválidos", position = 1)
	public String title;
	@ApiModelProperty(example = "Um ou mais campos estão invalidos", position = 5)
	public String detail;
	@ApiModelProperty(value = "Lista de objetos ou campos que geraram o erro", position = 10)
	public List<Field> fields;
	
	
	@Getter
	@Builder
	@ApiModel("ObjetoProblema")
	public static class Field{

		@ApiModelProperty(example = "nome")
		private String name;
		@ApiModelProperty(example = "nome é obrigatório")
		private String userMessage;
	}
}


