import java.util.ArrayList;
import java.util.Scanner;

public class simulador {
	static Scanner entrada = new Scanner(System.in);
	static GeradorNumerosAleatorios ger = new GeradorNumerosAleatorios();
	static Fila fila1;
	static Fila fila2;
	static Fila fila3;
	static ArrayList<Double> estadoFila = new ArrayList<>();
	static ArrayList<Evento> listaEvento = new ArrayList<>();
	static ArrayList<Double> numerosAleatorios = new ArrayList<>();
	static double tempoTotal = 0; // diferenca entre tempos
	static double tempoDecorrido = 0; // tempo real
	static int perda = 0; // qnts chegadas cairam

	public static void main(String[] args) {

		System.out.println("Digite a quantidade de servidores da 1ª fila: ");
		int serv1 = entrada.nextInt();
		System.out.println("Digite a capacidade maxima da 1ª fila: (0 para infinito)");
		int capa1 = entrada.nextInt();
		System.out.println("Digite o tempo de chegada min da 1ª fila: ");
		int cheMin1 = entrada.nextInt();
		System.out.println("Digite o tempo de chegada max da 1ª fila: ");
		int cheMax1 = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento min da 1ª fila: ");
		int ateMin1 = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento max da 1ª fila: ");
		int ateMax1 = entrada.nextInt();
		
		System.out.println("Digite a probabilidade de sair da 1ª fila e ir embora: ");
		int probEmbora1 = entrada.nextInt();
		
		System.out.println("Digite a quantidade de servidores da 2ª fila: ");
		int serv2 = entrada.nextInt();
		System.out.println("Digite a capacidade maxima da 2ª fila: (0 para infinito)");
		int capa2 = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento min da 1ª fila: ");
		int ateMin2 = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento max da 1ª fila: ");
		int ateMax2 = entrada.nextInt();

		System.out.println("Digite a probabilidade de sair da 1ª fila e ir embora: ");
		int probEmbora2 = entrada.nextInt();
		
		System.out.println("Digite a quantidade de servidores da 3ª fila: ");
		int serv3 = entrada.nextInt();
		System.out.println("Digite a capacidade maxima da 3ª fila: (0 para infinito)");
		int capa3 = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento min da 3ª fila: ");
		int ateMin3 = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento max da 3ª fila: ");
		int ateMax3 = entrada.nextInt();

		
		fila1 = new Fila(serv1, capa1, cheMin1, cheMax1, ateMin1, ateMax1, "Fila 1");
		
		fila2 = new Fila(serv2, capa2, ateMin1, ateMax1, ateMin2, ateMax2, "Fila 2");
	
		fila3 = new Fila(serv3, capa3, ateMin2, ateMax2, ateMin3, ateMax3, "Fila 3");

		System.out.println(fila1.toString());
		System.out.println(fila2.toString());
		System.out.println(fila3.toString());

		// setup das demais configs
		System.out.println("Digite o estado inicial de chegada: ");
		double estInic = entrada.nextDouble();

		for (int i = 0; i < 100000; i++) {
			numerosAleatorios.add(ger.recebeRandomEntre(0, 1));
		}

		for (int i = 0; i < capa + 1; i++) {
			estadoFila.add(0.0);
		}

		filaSimples(fila, estInic);

		for (int i = 0; i <= fila.getCap(); i++) {

			System.out.println("Tempo total que " + i + " ficaram na fila: " + estadoFila.get(i));
		}
		System.out.println("perdas: " + perda);
		System.out.println("tempo Total: " + tempoDecorrido);
	}

	private static void filaSimples(Fila fi, double inicio) {
		tempoDecorrido = 0;
		chegada(inicio);
		double menorTempo = 0;
		int posMenor = 0;

		// enquanto existirem numeros aleatorios na lista
		while (!numerosAleatorios.isEmpty()) {

			if (fila.getAgora() == fila.getCap()) {

				menorTempo = listaEvento.get(0).getTempo();
				posMenor = 0;
				for (int i = 0; i < listaEvento.size(); i++) {
					if (listaEvento.get(posMenor).getTempo() > listaEvento.get(i).getTempo()) {
						menorTempo = listaEvento.get(i).getTempo();
						posMenor = i;
					}
				}

				// se evento n for saida
				if (listaEvento.get(posMenor).getTipo() == 1) {
					perda++;

					listaEvento.remove(posMenor);
					chegada(menorTempo);
				} else {
					saida(menorTempo);
					listaEvento.remove(posMenor);
				}

			} else {
				// caso fila nao cheia
				menorTempo = listaEvento.get(0).getTempo();
				posMenor = 0;
				for (int i = 0; i < listaEvento.size(); i++) {
					if (listaEvento.get(posMenor).getTempo() > listaEvento.get(i).getTempo()) {
						menorTempo = listaEvento.get(i).getTempo();
						posMenor = i;
					}

				}

				// chegada
				if (listaEvento.get(posMenor).getTipo() == 1) {

					chegada(menorTempo);
					listaEvento.remove(posMenor);

				}
				// saida
				else if (listaEvento.get(posMenor).getTipo() == 0) {
					saida(menorTempo);
					listaEvento.remove(posMenor);
				}
				menorTempo = 0;
				posMenor = 0;
			}
		}
	}

	private static void ch1(double tempo) {
		int posFila = fila.getAgora();
		contabilizaTempo(tempo);

		if (fila.getAgora() < fila.getCap()) {
			fila.setAgora(posFila + 1);
			if (fila.getAgora() <= fila.getServidores()) {
				agendaSaida();
			}
		}
		agendaChegada();
	}

	private static void p12(double tempo) {
		contabilizaTempo(tempo);
		int posFila = fila.getAgora();
		fila.setAgora(posFila - 1);
		if (fila.getAgora() >= fila.getServidores()) {
			agendaSaida();
		}
	}
	
	private static void p23(double tempo) {
		contabilizaTempo(tempo);
		int posFila = fila.getAgora();
		fila.setAgora(posFila - 1);
		if (fila.getAgora() >= fila.getServidores()) {
			agendaSaida();
		}
	}
	
	private static void sa1(double tempo) {
		contabilizaTempo(tempo);
		int posFila = fila.getAgora();
		fila.setAgora(posFila - 1);
		if (fila.getAgora() >= fila.getServidores()) {
			agendaSaida();
		}
	}
	
	private static void sa2(double tempo) {
		contabilizaTempo(tempo);
		int posFila = fila.getAgora();
		fila.setAgora(posFila - 1);
		if (fila.getAgora() >= fila.getServidores()) {
			agendaSaida();
		}
	}

	private static void agendaChegada() {
		double aux = numerosAleatorios.remove(0);
		double result = tempoDecorrido
				+ (((fila.getTempoChegadaMax() - fila.getTempoChegadaMin()) * aux) + fila.getTempoChegadaMin());
		Evento e = new Evento(1, result);
		listaEvento.add(e);
	}

	private static void agendaSaida() {
		double aux = numerosAleatorios.remove(0);
		double result = tempoDecorrido + (((fila.getTempoAtendimentoMax() - fila.getTempoAtendimentoMin()) * aux)
				+ fila.getTempoAtendimentoMin());

		Evento e = new Evento(0, result);
		listaEvento.add(e);

	}

	private static void contabilizaTempo(double tempo) {

		int aux = fila1.getAgora();

		double tempoAnterior = tempoDecorrido;
		tempoDecorrido = tempo;
		double posTemAux = tempoDecorrido - tempoAnterior;
		double tempoAux = estadoFila.get(aux) + posTemAux;
		estadoFila.set(aux, tempoAux);
	}
}
