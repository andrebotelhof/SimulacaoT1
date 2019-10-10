public class Evento {
	private Tipo tipo;
	private double tempo;
	private boolean exec = false;
	
	public Evento(Tipo tipo, double tempo) {
		super();
		this.setTipo(tipo);
		this.tempo = tempo;
	}



	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}



	public boolean isExec() {
		return exec;
	}



	public void setExec(boolean exec) {
		this.exec = exec;
	}



	public Tipo getTipo() {
		return tipo;
	}



	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
}
