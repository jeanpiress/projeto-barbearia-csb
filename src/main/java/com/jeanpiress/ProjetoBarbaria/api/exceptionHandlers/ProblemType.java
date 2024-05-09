package com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers;

import lombok.Getter;

@Getter
public enum ProblemType {

	DADOS_INVALIDOS("dados-invalidos", "Dados invalidos"),
	MENSAGEM_INCOMPEENSIVEL("/menssagem-incompreensivel", "Menssagem incompreensivel"),
	PARAMETRO_INVALIDO("/paramentro-invalido", "O parametro esta incorreto"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-contrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro interno inesperado"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negocio"),
	ACESSO_NEGADO("/acesso-negado", "Acesso negado"),
	EMAIL_EXISTENTE("/email-existente", "Email já cadastrado"),
	SENHA_INCORRETA("/senha-incorreta", "Senha atual esta incorreta"),
	CONFERENCIA_SENHA_INCORRETA("/conf-incorreta", "Confirme corretamente sua senha");
	
	
	private String title;

	
	ProblemType(String path, String title){
		this.title = title;
	}
}
