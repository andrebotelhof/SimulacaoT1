import java.util.ArrayList;
import java.util.Scanner;

public class simulador {
	static Scanner entrada = new Scanner(System.in);
	static GeradorNumerosAleatorios ger = new GeradorNumerosAleatorios();
	static Fila fila1;
	static Fila fila2;
	static Fila fila3;
	static ArrayList<Evento> listaEvento = new ArrayList<>();
	static ArrayList<Double> numerosAleatorios = new ArrayList<>();
	static int contAleatUsado = 0;
	static double tempoTotal = 0; // diferenca entre tempos
	static double tempoDecorrido = 0; // tempo real
	static int perda1 = 0; // qnts chegadas cairam
	static int perda2 = 0;
	static int perda3 = 0;
	static double probEmbora1 = 0;
	static double probEmbora2 = 0;

	public static void main(String[] args) {

		// System.out.println("Digite a quantidade de servidores da 1� fila: ");
		int serv1 = 1;// entrada.nextInt();
		// System.out.println("Digite a capacidade maxima da 1� fila: (0 para
		// infinito)");
		int capa1 = 4;// entrada.nextInt();
		boolean finito1 = true;
		if (capa1 == 0)
			finito1 = false;
		// System.out.println("Digite o tempo de chegada min da 1� fila: ");
		int cheMin1 = 4;// entrada.nextInt();
		// System.out.println("Digite o tempo de chegada max da 1� fila: ");
		int cheMax1 = 6;// entrada.nextInt();
		// System.out.println("Digite o tempo de atendimento min da 1� fila: ");
		int ateMin1 = 5;// entrada.nextInt();
		// System.out.println("Digite o tempo de atendimento max da 1� fila: ");
		int ateMax1 = 7;// entrada.nextInt();

		// System.out.println("Digite a probabilidade de sair da 1� fila e ir embora:
		// ");
		probEmbora1 = 0.3;// entrada.nextInt();

		// System.out.println("Digite a quantidade de servidores da 2� fila: ");
		int serv2 = 2;// entrada.nextInt();
		// System.out.println("Digite a capacidade maxima da 2� fila: (0 para
		// infinito)");
		int capa2 = 5;// entrada.nextInt();
		boolean finito2 = true;
		if (capa2 == 0)
			finito2 = false;
		// System.out.println("Digite o tempo de atendimento min da 2� fila: ");
		int ateMin2 = 8;// entrada.nextInt();
		// System.out.println("Digite o tempo de atendimento max da 2� fila: ");
		int ateMax2 = 16;// entrada.nextInt();

		// System.out.println("Digite a probabilidade de sair da 2� fila e ir embora:
		// ");
		probEmbora2 = 0.2;// entrada.nextInt();

		// System.out.println("Digite a quantidade de servidores da 3� fila: ");
		int serv3 = 3;// entrada.nextInt();
		// System.out.println("Digite a capacidade maxima da 3� fila: (0 para
		// infinito)");
		int capa3 = 4;// entrada.nextInt();
		boolean finito3 = true;
		if (capa3 == 0)
			finito3 = false;
		// System.out.println("Digite o tempo de atendimento min da 3� fila: ");
		int ateMin3 = 10;// entrada.nextInt();
		// System.out.println("Digite o tempo de atendimento max da 3� fila: ");
		int ateMax3 = 18;// entrada.nextInt();

		fila1 = new Fila(serv1, capa1, cheMin1, cheMax1, ateMin1, ateMax1, "Fila 1", finito1);

		fila2 = new Fila(serv2, capa2, ateMin1, ateMax1, ateMin2, ateMax2, "Fila 2", finito2);

		fila3 = new Fila(serv3, capa3, ateMin2, ateMax2, ateMin3, ateMax3, "Fila 3", finito3);

		System.out.println(fila1.toString());
		System.out.println(fila2.toString());
		System.out.println(fila3.toString());

		// setup das demais configs
		// System.out.println("Digite o estado inicial de chegada: ");
		double estInic = 2;// entrada.nextDouble();

		// gera os aleatorios
		for (int i = 0; i < 100000; i++) {
			numerosAleatorios.add(ger.recebeRandomEntre(0, 1));
		}

		// preenche os tempos de acordo com a capacidade das tres filas
		for (int i = 0; i < capa1 + 1; i++) {
			fila1.addEstadoFila(0.0);
		}
		for (int i = 0; i < capa2 + 1; i++) {
			fila2.addEstadoFila(0.0);
		}
		for (int i = 0; i < capa3 + 1; i++) {
			fila3.addEstadoFila(0.0);
		}

		listaEvento.add(new Evento(Tipo.CH1, estInic));

		while (contAleatUsado < numerosAleatorios.size()-2) {
			ArrayList<Evento> temp = new ArrayList<Evento>();
			for (Evento e : listaEvento) {
				if (e.isExec() == false) {
					temp.add(e);
				}
			}
			Evento vaiRoda = temp.get(0);
			for (Evento e : temp) {
				if (e.getTempo() <= vaiRoda.getTempo()) {
					vaiRoda = e;
				}
			}
			if (vaiRoda.getTipo().equals(Tipo.CH1)) {
				ch1(vaiRoda.getTempo());
			} else if (vaiRoda.getTipo().equals(Tipo.SA1)) {
				sa1(vaiRoda.getTempo());
			} else if (vaiRoda.getTipo().equals(Tipo.P12)) {
				p12(vaiRoda.getTempo());
			} else if (vaiRoda.getTipo().equals(Tipo.P23)) {
				p23(vaiRoda.getTempo());
			} else if (vaiRoda.getTipo().equals(Tipo.SA2)) {
				sa2(vaiRoda.getTempo());
			} else if (vaiRoda.getTipo().equals(Tipo.SA3)) {
				sa3(vaiRoda.getTempo());
			}

			int index = listaEvento.indexOf(vaiRoda);
			vaiRoda.setExec(true);
			listaEvento.add(index, vaiRoda);

		}

		int i = 0;
		System.out.println("----- FILA 1: -----");
		ArrayList<Double> array = fila1.getEstadoFila();
		for (i = 0; i < array.size(); i++) {
			System.out.println("Tempo total que ficou com " + i + " pessoas: " + array.get(i));
			System.out.println("Probabilidade de estar com " + i + " pessoas: " + calculaTempoFila(array.get(i)) + "%");
		}
		System.out.println("Quantidade de perdas da fila 1: " + fila1.getQtdPerda());
		int i2 = 0;
		System.out.println("----- FILA 2: -----");
		ArrayList<Double> array2 = fila2.getEstadoFila();
		for (i2 = 0; i2 < array2.size(); i2++) {
			System.out.println("Tempo total que ficou com " + i2 + " pessoas: " + array2.get(i2));
			System.out.println("Probabilidade de estar com " + i2 + " pessoas: " + calculaTempoFila(array2.get(i2)) + "%");
		}
		System.out.println("Quantidade de perdas da fila 2: " + fila2.getQtdPerda());
		int i3 = 0;
		System.out.println("----- FILA 3: -----");
		ArrayList<Double> array3 = fila3.getEstadoFila();
		for (i3 = 0; i3 < array3.size(); i3++) {
			System.out.println("Tempo total que ficou com " + i3 + " pessoas: " + array3.get(i3));
			System.out.println("Probabilidade de estar com " + i3+ " pessoas: "+ calculaTempoFila(array3.get(i3)) + "%");
		}
		System.out.println("Quantidade de perdas da fila 3: " + fila3.getQtdPerda());

	}
	
	public static String calculaTempoFila(double x) {
		double tempo = 100 * x / tempoDecorrido;
		return format(tempo);
	}

	public static String format(double x) {
		return String.format("%.2f", x);
		}

	private static void ch1(double tempo) {
		int posFila = fila1.getAgora();
		contabilizaTempo(tempo);
		if (fila1.getAgora() < fila1.getCap()) {
			fila1.setAgora(posFila + 1);
			if (fila1.getAgora() <= fila1.getServidores()) {
				if ((contAleatUsado < numerosAleatorios.size())
						&& (numerosAleatorios.get(contAleatUsado) < (1 - probEmbora1))) {
					contAleatUsado++;
					double temp = tempo + rnd(fila1.getTempoAtendimentoMax(), fila1.getTempoAtendimentoMin());
					listaEvento.add(new Evento(Tipo.P12, temp));
				} else {
					contAleatUsado++;
					double temp = tempo + rnd(fila1.getTempoAtendimentoMax(), fila1.getTempoAtendimentoMin());
					listaEvento.add(new Evento(Tipo.SA1, temp));
				}
			}
		} else {
			fila1.maisUmaPerda();
		}
		listaEvento.add(new Evento(Tipo.CH1, tempo + rnd(fila1.getTempoChegadaMax(), fila1.getTempoAtendimentoMin())));
	}

	private static void sa1(double tempo) {
		int posFila1 = fila1.getAgora();
		contabilizaTempo(tempo);
		fila1.setAgora(posFila1 - 1);
		if (fila1.getAgora() >= fila1.getServidores()) {
			if ((contAleatUsado < numerosAleatorios.size())
					&& (numerosAleatorios.get(contAleatUsado) < (1 - probEmbora1))) {
				contAleatUsado++;
				double temp = tempo + rnd(fila1.getTempoAtendimentoMax(), fila1.getTempoAtendimentoMin());
				listaEvento.add(new Evento(Tipo.P12, temp));
			} else {
				contAleatUsado++;
				double temp = tempo + rnd(fila1.getTempoAtendimentoMax(), fila1.getTempoAtendimentoMin());
				listaEvento.add(new Evento(Tipo.SA1, temp));
			}
		}
	}

	private static void sa2(double tempo) {
		int posFila2 = fila2.getAgora();
		contabilizaTempo(tempo);
		fila2.setAgora(posFila2 - 1);
		if (fila2.getAgora() >= fila2.getServidores()) {
			if ((contAleatUsado < numerosAleatorios.size())
					&& (numerosAleatorios.get(contAleatUsado) < (1 - probEmbora2))) {
				contAleatUsado++;
				double temp = tempo + rnd(fila2.getTempoAtendimentoMax(), fila2.getTempoAtendimentoMin());
				listaEvento.add(new Evento(Tipo.P23, temp));
			} else {
				contAleatUsado++;
				double temp = tempo + rnd(fila2.getTempoAtendimentoMax(), fila2.getTempoAtendimentoMin());
				listaEvento.add(new Evento(Tipo.SA2, temp));
			}
		}
	}

	private static void sa3(double tempo) {
		int posFila3 = fila3.getAgora();
		contabilizaTempo(tempo);
		fila3.setAgora(posFila3 - 1);
		if (fila3.getAgora() >= fila3.getServidores()) {
			double temp = tempo + rnd(fila3.getTempoAtendimentoMax(), fila3.getTempoAtendimentoMin());
			listaEvento.add(new Evento(Tipo.SA3, temp));
		}
	}

	private static void p12(double tempo) {
		int posFila1 = fila1.getAgora();
		int posFila2 = fila2.getAgora();
		contabilizaTempo(tempo);
		fila1.setAgora(posFila1 - 1);
		if (fila1.getAgora() >= fila1.getServidores()) {
			if ((contAleatUsado < numerosAleatorios.size())
					&& (numerosAleatorios.get(contAleatUsado) < (1 - probEmbora1))) {
				contAleatUsado++;
				double temp = tempo + rnd(fila1.getTempoAtendimentoMax(), fila1.getTempoAtendimentoMin());
				listaEvento.add(new Evento(Tipo.P12, temp));
			} else {
				contAleatUsado++;
				double temp = tempo + rnd(fila1.getTempoAtendimentoMax(), fila1.getTempoAtendimentoMin());
				listaEvento.add(new Evento(Tipo.SA1, temp));
			}
		} else {
			fila2.maisUmaPerda();
		}
		fila2.setAgora(posFila2 + 1);
		if (fila2.getAgora() <= fila2.getServidores()) {
			double temp2 = tempo + rnd(fila2.getTempoAtendimentoMax(), fila2.getTempoAtendimentoMin());
			listaEvento.add(new Evento(Tipo.SA2, temp2));
		}
	}

	private static void p23(double tempo) {
		int posFila2 = fila2.getAgora();
		int posFila3 = fila3.getAgora();
		contabilizaTempo(tempo);
		fila2.setAgora(posFila2 - 1);
		if (fila2.getAgora() >= fila2.getServidores()) {
			if ((contAleatUsado < numerosAleatorios.size())
					&& (numerosAleatorios.get(contAleatUsado) < (1 - probEmbora2))) {
				contAleatUsado++;
				double temp = tempo + rnd(fila2.getTempoAtendimentoMax(), fila2.getTempoAtendimentoMin());
				listaEvento.add(new Evento(Tipo.P23, temp));
			} else {
				contAleatUsado++;
				double temp = tempo + rnd(fila2.getTempoAtendimentoMax(), fila2.getTempoAtendimentoMin());
				listaEvento.add(new Evento(Tipo.SA2, temp));
			}
		} else {
			fila3.maisUmaPerda();
		}
		fila3.setAgora(posFila3 + 1);
		if (fila3.getAgora() <= fila3.getServidores()) {
			double temp2 = tempo + rnd(fila3.getTempoAtendimentoMax(), fila3.getTempoAtendimentoMin());
			listaEvento.add(new Evento(Tipo.SA3, temp2));
		}
	}

	private static double rnd(double a, double b) {
		contAleatUsado++;
		return ((b - a) * numerosAleatorios.get(contAleatUsado - 1)) + a;
	}

	private static void contabilizaTempo(double tempo) {

		int aux1 = fila1.getAgora();
		int aux2 = fila2.getAgora();
		int aux3 = fila3.getAgora();
		double tempoAnterior = tempoDecorrido;
		tempoDecorrido = tempo;
		double posTemAux = tempoDecorrido - tempoAnterior;
		double tempoAux1 = (fila1.getEstadoFila().get(aux1) + posTemAux);
		double tempoAux2 = (fila2.getEstadoFila().get(aux2) + posTemAux);
		double tempoAux3 = (fila3.getEstadoFila().get(aux3) + posTemAux);

		fila1.setEstado(aux1, tempoAux1);
		fila2.setEstado(aux2, tempoAux2);
		fila3.setEstado(aux3, tempoAux3);
	}
}
