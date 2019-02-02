package xadrez.pecas;

import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Torre extends PecaXadrez {

	public Torre(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "T";
	}
	
	@Override
	public boolean[][] possiveisMovimentos() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0, 0);
		
		// Acima
		p.setValores(posicao.getLinha() - 1, posicao.getColuna());
		
		while(getTabuleiro().existePosicao(p) && !getTabuleiro().existeUmaPeca(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getLinha() - 1);
		}
		
		if(getTabuleiro().existePosicao(p) && existePecaOponente(p)) { 
			mat[p.getLinha()][p.getColuna()] = true;
		}
		
		// Esquerda
				p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
				
				while(getTabuleiro().existePosicao(p) && !getTabuleiro().existeUmaPeca(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
					p.setColuna(p.getColuna() - 1);
				}
				
				if(getTabuleiro().existePosicao(p) && existePecaOponente(p)) { 
					mat[p.getLinha()][p.getColuna()] = true;
				}
				
		// Direita
				p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
				
				while(getTabuleiro().existePosicao(p) && !getTabuleiro().existeUmaPeca(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
					p.setColuna(p.getColuna() + 1);
				}
				
				if(getTabuleiro().existePosicao(p) && existePecaOponente(p)) { 
					mat[p.getLinha()][p.getColuna()] = true;
				}
				
		// Abaixo
				p.setValores(posicao.getLinha() + 1, posicao.getColuna());
				
				while(getTabuleiro().existePosicao(p) && !getTabuleiro().existeUmaPeca(p)) {
					mat[p.getLinha()][p.getColuna()] = true;
					p.setLinha(p.getLinha() + 1);
				}
				
				if(getTabuleiro().existePosicao(p) && existePecaOponente(p)) { 
					mat[p.getLinha()][p.getColuna()] = true;
				}
				
		return mat;
		
	}


}
