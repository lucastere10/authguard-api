package br.com.lucascaldas.authguard.domain.exception;

public abstract class NotFoundException extends BusinessRuleException {

	private static final long serialVersionUID = 1L;

	protected NotFoundException(String mensagem) {
		super(mensagem);
	}
	
}
