package br.inatel.dm112.conta;

public class SaldoInsuficienteException extends Exception {

	private static final long serialVersionUID = 1L; //default

	public SaldoInsuficienteException(String message) {
		super(message);
	}

}
