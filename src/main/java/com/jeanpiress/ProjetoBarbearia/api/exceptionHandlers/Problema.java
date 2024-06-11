package com.jeanpiress.ProjetoBarbearia.api.exceptionHandlers;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(defaultValue = "Problema")
public class Problema {

	@Schema(example = "Dados inválidos")
	public String title;
	@Schema(example = "Um ou mais campos estão invalidos")
	public String detail;
	@Schema(description = "Lista de objetos ou campos que geraram o erro")
	public List<Field> fields;
	
	
	@Getter
	@Schema(example = "ObjetoProblema")
	@Builder
	public static class Field{

		@Schema(example = "nome")
		private String name;
		@Schema(example = "nome é obrigatório")
		private String userMessage;
	}
}


