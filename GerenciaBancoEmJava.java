import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Ponto flutuante e sua precisão....
 */
public class GerenciaBancoEmJava {

	public static Scanner INPUT;

	/**
	 * Erro genérico para as operações financeiras.
	 */
	public static class BancoException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public BancoException(String msg) {
			super(msg);
		}
	}

	/**
	 * Representa um cliente do banco.
	 */
	public static class Cliente {

		private String nome, sobrenome, cpf;

		private final Conta conta = new Conta();

		private Cliente(String nome, String sobrenome, String cpf) {
			this.nome = nome;
			this.sobrenome = sobrenome;
			this.cpf = cpf;
		}

		public Conta getConta() {
			return this.conta;
		}

		@Override
		public String toString() {
			return "CLIENTE: [cpf = %s | nome = %s | sobrenome = %s | saldo = R$ %s]".formatted(this.cpf, this.nome,
					this.sobrenome, this.conta.getSaldo().toPlainString());
		}

		/**
		 * Conta implicita do cliente
		 */
		public static class Conta {

			private BigDecimal saldo = BigDecimal.ZERO;

			/**
			 * Realiza um saque na conta.
			 * 
			 * @param quantia Um valor positivo;
			 */
			public void sacar(BigDecimal quantia) {
				if (quantia.compareTo(BigDecimal.ZERO) <= 0) {
					throw new BancoException("Quantia deve ser maior que zero");
				} else if (this.saldo.compareTo(quantia) < 0) {
					throw new BancoException("Saldo Insuficiente para saque");
				}
				this.saldo = this.saldo.subtract(quantia);
				System.out.println("Sucesso !");
			}

			/**
			 * Realiza um depósito na conta.
			 * 
			 * @param quantia Um valor positivo;
			 */
			public void depositar(BigDecimal quantia) {
				if (quantia.compareTo(BigDecimal.ZERO) <= 0) {
					throw new BancoException("Quantia deve ser maior que zero");
				}
				this.saldo = this.saldo.add(quantia);
				System.out.println("Sucesso !");
			}

			/**
			 * Retorna o saldo da conta.
			 * 
			 * @return O saldo
			 */
			public BigDecimal getSaldo() {
				return BigDecimal.ZERO.add(saldo);
			}
		}
	}

	/**
	 * Representa um banco para gerenciar os clientes.
	 */
	public static class CaixaEletronico {

		private Cliente cliente;

		/**
		 * Inicia um novo cliente;
		 */
		public void novoCliente() {
			System.out.print("\nDigite o nome do cliente: ");
			String nome = INPUT.nextLine();

			System.out.print("\nDigite o sobrenome do cliente: ");
			String sobrenome = INPUT.nextLine();

			System.out.print("\nDigite o cpf do cliente (sem validação): ");
			String cpf = INPUT.nextLine();

			this.cliente = new Cliente(nome, sobrenome, cpf);

		}

		/**
		 * Entrada padão para o caixa eletronico.
		 * 
		 * @return O valor para operação posterior
		 */
		protected BigDecimal receberQuantiaOperacao() {
			System.out.print("Digite a quantia: ");
			String quantia = INPUT.nextLine();

			return new BigDecimal(quantia);
		}

		protected void imprimirOpcoes() {

			System.out.println("\n\n* Selecione uma das opções abaixo *");

			System.out.println("1 - Imprimir dados");
			System.out.println("2 - Saque");
			System.out.println("3 - Depósito");
			System.out.println("4 - Encerrar\n");
		}

		/**
		 * Inicia as operações do caixa eletronico
		 */
		public void operarContaAtual() {

			while (true) {
				try {

					imprimirOpcoes();

					System.out.print("Opção: ");
					int operacao = INPUT.nextInt();
					INPUT.nextLine();

					switch (operacao) {
					case 1:
						System.out.println(this.cliente);
						break;
					case 2:
						this.cliente.getConta().sacar(receberQuantiaOperacao());
						break;
					case 3:
						this.cliente.getConta().depositar(receberQuantiaOperacao());
						break;
					case 4:
						System.out.println("Finalizando conta...");
						INPUT.close();
						System.exit(0);
						break;
					default:
						System.out.println("Opção não reconhecida, digite uma operação válida");
						break;
					}

				} catch (Exception e) {
					if (e instanceof BancoException) {
						System.out.println(e.getLocalizedMessage());
					} else if (e instanceof InputMismatchException) {
						System.out.println("Digite uma opção válida (apenas números)");
					} else {
						e.printStackTrace();
						System.out.println("Um erro desconhecido ocorreu, prosseguindo...");
					}
				}
			}

		}

	}

	public static void main(String[] args) {

		System.out.println("Ao digitar valores, favor não separar milhares e utilizar '.' para decimais.");
		System.out.println("Lucas Rafael de Quadros, RA: 3490640404");

		INPUT = new Scanner(System.in);

		CaixaEletronico caixa = new CaixaEletronico();

		caixa.novoCliente();
		caixa.operarContaAtual();

	}

}
