package jogo;

public class Tabuleiro {
	
	private int linhas;
	private int colunas;
	private Peca[][] pecas;
	
	public Tabuleiro(int linhas, int colunas) {
		
		if(linhas<1 || colunas<1) {
			throw new TabuleiroException("Erro ao criar tabuleiro: A quantidade de linha e colunas devem ser noinimo 1");
		}
		
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}


	public int getColunas() {
		return colunas;
	}


	public Peca peca(int linha, int coluna) {
		
		if(!existePosicao(linha, coluna)) {
			throw new TabuleiroException("A posicao nao existe");
		}
		return pecas[linha][coluna];
	}
	
	public Peca peca(Posicao posicao) {
		
		if(!existePosicao(posicao)) {
			throw new TabuleiroException("A posicao nao existe");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}
	
	public void colocaPeca(Peca peca, Posicao posicao) {
		if (existeUmaPeca(posicao)) {
			throw new TabuleiroException("Existe uma peca nessa posicao " + posicao);
		}
		pecas [posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	public Peca removePeca(Posicao posicao) {
		if(!existePosicao(posicao)) {
			throw new TabuleiroException("A posicao nao existe");
		}
		if(peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		
		return aux;
	}
	
	
	public boolean existePosicao(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
	}
	
	public boolean existePosicao(Posicao posicao) {
		return existePosicao(posicao.getLinha(), posicao.getColuna());
	}
	
	public boolean existeUmaPeca(Posicao posicao) {
		
		if(!existePosicao(posicao)) {
			throw new TabuleiroException("A posicao nao existe");
		}
				
		return peca(posicao) != null;
	}
	
	
	
	

}
 