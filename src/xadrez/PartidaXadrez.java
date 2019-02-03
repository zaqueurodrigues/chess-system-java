package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean cheque;
	
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
	
	public boolean getCheque() {
		return cheque;
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
		if(testeCheque(jogadorAtual)) {
			desfazerMovimento(or, des, pecaCapturada);
			throw new XadrezException("Voce nao pode se colocar em cheque");
		}
		
		cheque = (testeCheque(oponente(jogadorAtual))) ? true : false;
				
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
	
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca peca) {
		Peca p = tabuleiro.removePeca(destino);
		tabuleiro.colocaPeca(p, origem);
		if(peca != null) {
			pecasCapturadas.remove(peca);
			pecasNoTabuleiro.add((PecaXadrez) peca);
		}
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
	
	private Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	private PecaXadrez rei(Cor cor) {
		List<Peca> pecas = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p: pecas) {
			if(p instanceof Rei) {
				return (PecaXadrez)p;
			}
		}
		throw new IllegalStateException("Nao existe o rei da cor " + cor + " no tabuleiro");
	}
	
	private boolean testeCheque(Cor cor) {
		Posicao posicaoRei = rei(cor).getXadrezPosicao().paraPosicao();
		List<Peca> pecasOponente =  pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for (Peca p : pecasOponente) {
			boolean[][] mat = p.possiveisMovimentos();
			if(mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
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
