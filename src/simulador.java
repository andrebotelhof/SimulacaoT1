import java.util.ArrayList;
import java.util.Scanner;

public class simulador {
	static Scanner entrada = new Scanner(System.in);
	static GeradorNumerosAleatorios ger = new GeradorNumerosAleatorios();
	static Fila fila;
	static ArrayList<Double> estadoFila = new ArrayList<>();
	static ArrayList<Evento> listaEvento = new ArrayList<>();
	static ArrayList<Double> numerosAleatorios = new ArrayList<>();
	static double tempoTotal = 0; // diferenca entre tempos
	static double tempoDecorrido = 0; // tempo real
	static int perda = 0; // qnts chegadas cairam

	public static void main(String[] args) {

		System.out.println("Digite a quantidade de servidores da fila: ");
		int serv = entrada.nextInt();
		System.out.println("Digite a capacidade maxima da fila: ");
		int capa = entrada.nextInt();
		System.out.println("Digite o tempo de chegada min: ");
		int cheMin = entrada.nextInt();
		System.out.println("Digite o tempo de chegada max: ");
		int cheMax = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento min: ");
		int ateMin = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento max: ");
		int ateMax = entrada.nextInt();

		fila = new Fila(serv, capa, cheMin, cheMax, ateMin, ateMax);

		System.out.println(fila.toString());

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

	private static void chegada(double tempo) {
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

	private static void saida(double tempo) {
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

		int aux = fila.getAgora();

		double tempoAnterior = tempoDecorrido;
		tempoDecorrido = tempo;
		double posTemAux = tempoDecorrido - tempoAnterior;
		double tempoAux = estadoFila.get(aux) + posTemAux;
		estadoFila.set(aux, tempoAux);
	}
}
