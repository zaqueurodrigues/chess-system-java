package aplication;

import xadrez.PartidaXadrez;

public class Program {

	public static void main(String[] args) {
		
		PartidaXadrez partida = new PartidaXadrez();
		
		UI.mostrarTabuleiro(partida.getPecas());
		
	}

}
