package br.com.lucascaldas.authguard.domain.exception;

public class AccessDeniedException extends BusinessRuleException {
    
    private static final long serialVersionUID = 1L;

	public AccessDeniedException(String mensagem) {
		super(mensagem);
	}

}
