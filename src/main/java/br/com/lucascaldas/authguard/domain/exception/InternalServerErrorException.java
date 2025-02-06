package br.com.lucascaldas.authguard.domain.exception;

public abstract class InternalServerErrorException extends BusinessRuleException {

	private static final long serialVersionUID = 1L;

	protected InternalServerErrorException(String mensagem) {
		super(mensagem);
	}
	
}
