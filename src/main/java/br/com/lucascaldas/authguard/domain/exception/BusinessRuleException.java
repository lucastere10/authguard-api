package br.com.lucascaldas.authguard.domain.exception;

public class BusinessRuleException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessRuleException(String mensagem) {
		super(mensagem);
	}
	
	public BusinessRuleException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
	
}
