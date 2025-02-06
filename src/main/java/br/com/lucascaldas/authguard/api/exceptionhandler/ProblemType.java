package br.com.lucascaldas.authguard.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	BAD_REQUEST("/bad-request", "Requisição Inválida"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	ACESSO_NEGADO("/acesso-negado", "Acesso Negado");
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title) {
		this.uri = "https://authguard.com.br" + path;
		this.title = title;
	}
	
}
