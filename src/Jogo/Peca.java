package jogo;

public abstract class Peca {
	
	protected Posicao posicao;
	private Tabuleiro tabuleiro;
	
	public Peca(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	protected Tabuleiro getTabuleiro() {
		return tabuleiro;
	}
	
	public abstract boolean[][] possiveisMovimentos();
		
	public boolean posicaoPossivel(Posicao posicao) {
		return possiveisMovimentos()[posicao.getLinha()][posicao.getColuna()];
	}
	
	public boolean existeUmMovimentoPossivel() {
		boolean[][] mat = possiveisMovimentos();
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat.length; j++) {
				if(mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}

}
