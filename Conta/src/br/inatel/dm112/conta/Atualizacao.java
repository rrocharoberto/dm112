package br.inatel.dm112.conta;

public interface Atualizacao {

	public void atualizarSaldo() throws SaldoInsuficienteException;

	public int getNumero();
}
