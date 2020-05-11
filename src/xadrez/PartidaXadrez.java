package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean xeque;
	private boolean xequeMate;

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

	public boolean getXeque() {
		return xeque;
	}

	public boolean getXequeMate() {
		return xequeMate;
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
		if(testeXeque(jogadorAtual)) {
			desfazerMovimento(or, des, pecaCapturada);
			throw new XadrezException("Voce nao pode se colocar em cheque");
		}

		xeque = (testeXeque(oponente(jogadorAtual))) ? true : false;
		if(testeXequeMate(oponente(jogadorAtual))){
			xequeMate = true;
		}	else {
			proximoTurno();
		}

		return (PecaXadrez) pecaCapturada;
	}

	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removePeca(origem);
		p.increaseMoveCount();
		Peca pecaCapturada = tabuleiro.removePeca(destino);
		tabuleiro.colocaPeca(p, destino);
		if(pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add((PecaXadrez) pecaCapturada);
		}
		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca peca) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removePeca(destino);
		p.decreaseMoveCount();
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

	private boolean testeXeque(Cor cor) {
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

	private boolean testeXequeMate(Cor cor) {
		if(!testeXeque(cor)) {
			return false;
		}

		List <Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : lista) {
			boolean[][] mat = p.possiveisMovimentos();
			for (int i = 0; i<tabuleiro.getLinhas(); i++) {
				for (int j= 0; j<tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez)p).getXadrezPosicao().paraPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = fazerMovimento(origem, destino);
						boolean testeXeque = testeXeque(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if(!testeXeque) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}


	private void colocaNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.colocaPeca(peca, new XadrezPosicao(coluna, linha).paraPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void setupInicial() {
		colocaNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO));
		colocaNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO));
		
		colocaNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocaNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO));
		colocaNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocaNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		colocaNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO));
		colocaNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO));
		
	}

}
