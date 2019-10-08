public class Evento {
	private Tipo tipo;
	private double tempo;
	
	public Evento(Tipo tipo, double tempo) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
	}



	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}
}
