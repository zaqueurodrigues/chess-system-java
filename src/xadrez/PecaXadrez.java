package xadrez;


import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;

public abstract class PecaXadrez extends Peca {
	private Cor cor;
	private int moveCount;

	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	public void increaseMoveCount() {
		moveCount++;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public void decreaseMoveCount() {
		moveCount--;
	}
	
	public XadrezPosicao getXadrezPosicao() {
		return XadrezPosicao.dePosicao(posicao);
	}
	
	protected boolean existePecaOponente(Posicao posicao) {
		
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		
		return p != null && p.getCor() != cor;
	}

		
	
	
	
}
