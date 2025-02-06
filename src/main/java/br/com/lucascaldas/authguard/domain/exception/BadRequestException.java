package br.com.lucascaldas.authguard.domain.exception;

public abstract class BadRequestException extends BusinessRuleException {

	private static final long serialVersionUID = 1L;

	protected BadRequestException(String mensagem) {
		super(mensagem);
	}
	
}