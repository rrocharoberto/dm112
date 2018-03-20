package br.inatel.dm112.conta;

import static org.junit.Assert.*;

import org.junit.Test;

public class ContaTest {

	@Test
	public void testContaCorrente() {

		ContaCorrente cc = new ContaCorrente(1, "cliente 1", 15);// 15 reais de manutenção
		assertEquals(0, cc.getSaldo(), 0.001);// conta zerada
		assertEquals(1, cc.getNumero());

		cc.depositar(100);
		assertEquals(100, cc.getSaldo(), 0.001);

		try {
			cc.sacar(30);
			assertEquals(70, cc.getSaldo(), 0.001);
		} catch (SaldoInsuficienteException e) {
			fail("O saldo deve ser 70: " + e.getMessage());
		}

		try {
			cc.sacar(80);
			fail("O saldo deve ser 70");
		} catch (SaldoInsuficienteException e) {
			// ok. Realmente deveria dar exceção e manter os 70 reais
			assertEquals(70, cc.getSaldo(), 0.001);
		}

		try {
			cc.atualizarSaldo();// desconta 15 reais
			assertEquals(55, cc.getSaldo(), 0.001);
		} catch (SaldoInsuficienteException e) {
			fail("O saldo deve ser 55: " + e.getMessage());
		}

		try {
			cc.sacar(50);
			assertEquals(5, cc.getSaldo(), 0.001);
		} catch (SaldoInsuficienteException e) {
			fail("O saldo deve ser 5: " + e.getMessage());
		}

		try {
			cc.atualizarSaldo();// desconta 15 reais
			fail("Não deveria ter atualizado, pois não tem saldo.");
		} catch (SaldoInsuficienteException e) {
			assertEquals(5, cc.getSaldo(), 0.001);
		}
	}

	@Test
	public void testContaPoupanca() {

		ContaPoupanca cp = new ContaPoupanca(1, "cliente 1", 10);// 10%, para ficar fácil de validar ;)

		assertEquals(0, cp.getSaldo(), 0.001);// conta zerada

		cp.depositar(100);
		assertEquals(100, cp.getSaldo(), 0.001);

		try {
			cp.atualizarSaldo();// aumenta 10 reais
			assertEquals(110, cp.getSaldo(), 0.001);
		} catch (SaldoInsuficienteException e) {
			fail("O saldo deve ser 110: " + e.getMessage());
		}

		try {
			cp.atualizarSaldo();// aumenta 11 reais
			assertEquals(121, cp.getSaldo(), 0.001);
		} catch (SaldoInsuficienteException e) {
			fail("O saldo deve ser 121: " + e.getMessage());
		}
	}

	@Test
	public void testTransferir() {

		ContaCorrente cc1 = new ContaCorrente(1, "cliente 1", 15);// 15 reais de manutenção
		assertEquals(0, cc1.getSaldo(), 0.001);// conta zerada

		cc1.depositar(100);
		assertEquals(100, cc1.getSaldo(), 0.001);

		ContaCorrente cc2 = new ContaCorrente(2, "cliente 2", 15);// 15 reais de manutenção
		assertEquals(0, cc2.getSaldo(), 0.001);// conta zerada

		cc2.depositar(100);
		assertEquals(100, cc2.getSaldo(), 0.001);

		try {
			cc1.transferirPara(cc2, 30);// transfere 30 reais
			assertEquals(70, cc1.getSaldo(), 0.001);
			assertEquals(130, cc2.getSaldo(), 0.001);
		} catch (SaldoInsuficienteException e) {
			fail("Problema na transferência: " + e.getMessage());
		}
	}

	@Test
	public void testGerenteContas() {

		ContaCorrente cc1 = new ContaCorrente(11, "cliente 1", 15);// 15 reais de manutenção
		ContaCorrente cc2 = new ContaCorrente(12, "cliente 2", 10);// 10 reais de manutenção
		ContaPoupanca cp1 = new ContaPoupanca(21, "cliente 1", 5);// 5%
		ContaPoupanca cp2 = new ContaPoupanca(22, "cliente 2", 10);// 10%
		cc1.depositar(100);
		cc2.depositar(1000);
		cp1.depositar(1000);
		cp2.depositar(3000);

		GerenteContas ger = new GerenteContas();
		ger.adicionar(cc1);
		ger.adicionar(cc2);
		ger.adicionar(cp1);
		ger.adicionar(cp2);

		ger.atualizarTodasContas();

		assertEquals(85, cc1.getSaldo(), 0.001);
		assertEquals(990, cc2.getSaldo(), 0.001);
		assertEquals(1050, cp1.getSaldo(), 0.001);
		assertEquals(3300, cp2.getSaldo(), 0.001);

		ger.removerConta(21);// removeu a cp1

		ger.atualizarTodasContas();

		assertEquals(70, cc1.getSaldo(), 0.001);
		assertEquals(980, cc2.getSaldo(), 0.001);
		assertEquals(3630, cp2.getSaldo(), 0.001);

		assertEquals(1050, cp1.getSaldo(), 0.001);// o valor da cp1 deve ficar inalterado, pois removeu ela

		// teste com conta com saldo insuficiente
		ContaCorrente cc3 = new ContaCorrente(11, "cliente 1", 15);// 15 reais de manutenção
		cc3.depositar(5);

		ger.adicionar(cc3);
		ger.atualizarTodasContas();

		assertEquals(5, cc3.getSaldo(), 0.001);// não muda o valor do saldo

	}
}
