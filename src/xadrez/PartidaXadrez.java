package xadrez;

import java.util.ArrayList;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	
	private ArrayList<PecaXadrez> pecasNoTabuleiro;
	private ArrayList<PecaXadrez> pecasCapturadas = new ArrayList<PecaXadrez>();
	

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.BRANCO;
		pecasNoTabuleiro = new ArrayList<PecaXadrez>();
		setupInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}

		return mat;
	}

	public boolean[][] possiveisMovimentos(XadrezPosicao origem) {
		Posicao posicao = origem.paraPosicao();
		validaPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).possiveisMovimentos();
	}

	public PecaXadrez moverPeca(XadrezPosicao origem, XadrezPosicao destino) {
		Posicao or = origem.paraPosicao();
		Posicao des = destino.paraPosicao();
		validaPosicaoOrigem(or);
		validaPosicaoDestino(or, des);
		Peca pecaCapturada = fazerMovimento(or, des);
		proximoTurno();
		return (PecaXadrez) pecaCapturada;
	}

	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removePeca(origem);
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocaPeca(p, destino);
		if(pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add((PecaXadrez) pecaCapturada);
		}
		return pecaCapturada;

	}

	private void validaPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.existeUmaPeca(posicao)) {
			throw new XadrezException("Nao existe peca na posicao de origem");
		}
		if(jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()){
			throw new XadrezException("A peca escolhida nao e sua");
		}
		if (!tabuleiro.peca(posicao).existeUmMovimentoPossivel()) {
			throw new XadrezException("Nao existem movimentos possiveis para peca escolhida");
		}
	}

	private void validaPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).posicaoPossivel(destino)) {
			throw new XadrezException("A peca nao pode realizar esse movimento");
		}
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private void colocaNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocaPeca(peca, new XadrezPosicao(coluna, linha).paraPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void setupInicial() {
		colocaNovaPeca('b', 2, new Rei(tabuleiro, Cor.PRETO));
		colocaNovaPeca('g', 7, new Rei(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('d', 3, new Torre(tabuleiro, Cor.PRETO));
		
	}

}
