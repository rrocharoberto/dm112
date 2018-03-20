package br.inatel.dm112.conta;

public abstract class Conta implements Atualizacao {

	private int numero;
	private String cliente;
	private float saldo = 0;

	public Conta(int numero, String cliente) {
		this.numero = numero;
		this.cliente = cliente;
	}

	public void sacar(float valor) throws SaldoInsuficienteException {
		if (this.saldo < valor) {
			throw new SaldoInsuficienteException("Saldo insuficiente: " + this.saldo);
		}
		saldo -= valor;
	}

	public void depositar(float valor) {
		saldo += valor;
	}
	
	public void transferirPara(Conta c, float valor)  throws SaldoInsuficienteException {
		this.sacar(valor);
		c.depositar(valor);
	}
	
	public float getSaldo() {
		return saldo;
	}
	
	@Override
	public int getNumero() {
		return this.numero;
	}
	
}
