package aplication;

import java.util.InputMismatchException;
import java.util.Scanner;

import jogo.TabuleiroException;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.XadrezPosicao;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		PartidaXadrez partida = new PartidaXadrez();

		while (true) {
			try {
				UI.limpaTela();
				UI.mostrarTabuleiro(partida.getPecas());
				System.out.println();
				System.out.print("Origem: ");
				XadrezPosicao origem = UI.lerXadrezPosicao(sc);
				
				boolean[][] possiveisMovimentos = partida.possiveisMovimentos(origem);
				UI.limpaTela();
				UI.mostrarTabuleiro(partida.getPecas(), possiveisMovimentos);
				
				System.out.println();
				System.out.print("Destino: ");
				
				XadrezPosicao destino = UI.lerXadrezPosicao(sc);
				
				PecaXadrez pecaCapturada  = partida.moverPeca(origem, destino);
			}
			catch (TabuleiroException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}

	}

}
