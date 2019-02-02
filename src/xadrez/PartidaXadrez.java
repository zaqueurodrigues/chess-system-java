package xadrez;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	
	private Tabuleiro tabuleiro;
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		setupInicial();
	}
	
	public PecaXadrez[][] getPecas(){
		
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i=0; i<tabuleiro.getLinhas(); i++) {
			for (int j=0; j<tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		
		return mat;
	}
	
	public PecaXadrez moverPeca(XadrezPosicao origem, XadrezPosicao destino) {
		Posicao or  = origem.paraPosicao();
		Posicao des = destino.paraPosicao();
		
		validaPosicaoOrigem(or);
		Peca pecaCapturada = fazerMovimento(or, des);
		return (PecaXadrez) pecaCapturada;
	}
	
	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removePeca(origem);
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocaPeca(p, destino);
		return pecaCapturada;
		
	}
	private void validaPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.existePosicao(posicao)){
			throw new XadrezException("Nao existe peca na posicao de origem");
		}
	}
	
	
	private void colocaNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocaPeca(peca, new XadrezPosicao(coluna, linha).paraPosicao()); 
	}
	
	private void setupInicial() {
		colocaNovaPeca('b', 2, new Rei(tabuleiro, Cor.PRETO));
		colocaNovaPeca('g', 7, new Rei(tabuleiro, Cor.BRANCO));
		tabuleiro.colocaPeca(new Torre(tabuleiro, Cor.BRANCO), new Posicao(2,1));
		tabuleiro.colocaPeca(new Rei(tabuleiro, Cor.PRETO), new Posicao(0,4));
		tabuleiro.colocaPeca(new Rei(tabuleiro, Cor.BRANCO), new Posicao(7,4));

	}

}
