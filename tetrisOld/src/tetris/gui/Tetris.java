package tetris.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.bind.SchemaOutputResolver;

import tetris.input.KeyboardInput;
import tetris.model.Board;
import tetris.model.BoardCell;
import tetris.model.Game;
import tetris.model.PieceType;
import tetris.model.Score;

public class Tetris extends Canvas {

	private Game game = new Game();
	private final BufferStrategy strategy;

	private final int BOARD_CORNER_X = 200;
	private final int BOARD_CORNER_Y = 50;

	private final KeyboardInput keyboard = new KeyboardInput();
	private long lastIteration = System.currentTimeMillis();

	
	private static final int PIECE_WIDTH = 20;

	Color MYYELLLOW = new Color(255, 255, 102);
	Color MYBLUE = new Color(102, 217, 255);
	Color MYGREEN = new Color(102, 255, 140);
	Color MYRED = new Color(255, 102, 102);
	Color MYORANGE = new Color(255, 179, 102);
	Color MYDARKBLUE = new Color(102, 102, 255);
	Color MYPURPLE = new Color(217, 102, 255);

	public Tetris() {
		JFrame container = new JFrame("Tetris");
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(600, 500));
		panel.setLayout(null);

		setBounds(0, 0, 600, 500);
		panel.add(this);
		setIgnoreRepaint(true);

		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		addKeyListener(keyboard);
		requestFocus();

		createBufferStrategy(2);
		strategy = getBufferStrategy();

	}

	void gameLoop() {
		while (true) {
			if (keyboard.newGame()) {
				game = new Game();
				game.startGame();
			}
			if (keyboard.quitGame()) {
				game.exit();
			}
			if (game.isPlaying()) {

				if (!game.isPaused()) {
					tetrisLoop();
				}
				if (keyboard.pauseGame()) {
					game.pauseGame();
				}
				try {
					Thread.sleep(20);
				} catch (Exception e) {
				}
			}
			draw();
		}
	}

	void tetrisLoop() {
		if (game.isDropping()) {
			game.moveDown();
		} else if (System.currentTimeMillis() - lastIteration >= game.getIterationDelay()) {
			game.moveDown();
			lastIteration = System.currentTimeMillis();
		}
		if (keyboard.rotate()) {
			game.rotate();
		} else if (keyboard.dropSlowly()) {
			game.moveDown();
		} else if (keyboard.left()) {
			game.moveLeft();
		} else if (keyboard.right()) {
			game.moveRight();
		} else if (keyboard.drop()) {
			game.drop();
		} else if (keyboard.save()) {
			game.savePiece();
		}
	}

	public void draw() {
		Graphics2D g = getGameGraphics();
		drawEmptyBoard(g);
		drawHelpBox(g);
		drawTitle(g);
		drawPiecePreviewBox(g);
		drawPieceSavedBox(g);

		if (game.isPlaying()) {
			drawCells(g);
			drawPiecePreview(g, game.getNextPiece().getType());
			if (game.isSaved()) {
				drawPieceSaved(g, game.getSavedPiece().getType());
			}

			if (game.isPaused()) {
				drawGamePaused(g);
			}
		}

		if (game.isGameOver()) {
			drawCells(g);
			drawGameOver(g);
			
		}

		drawStatus(g);
		drawBestScore(g);

		g.dispose();
		strategy.show();
	}

	private Graphics2D getGameGraphics() {
		return (Graphics2D) strategy.getDrawGraphics();
	}

	private void drawCells(Graphics2D g) {
		BoardCell[][] cells = game.getBoardCells();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				drawBlock(g, BOARD_CORNER_X + i * 20, BOARD_CORNER_Y + (19 - j) * 20, getBoardCellColor(cells[i][j]));
			}
		}
	}

	private void drawEmptyBoard(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 500);
		g.setColor(Color.GRAY);
		g.drawRect(BOARD_CORNER_X - 1, BOARD_CORNER_Y - 1, 10 * PIECE_WIDTH + 2, 20 * PIECE_WIDTH + 2);
	}

	private void drawStatus(Graphics2D g) {
		g.setFont(new Font("Dialog", Font.PLAIN, 16));
		g.setColor(Color.WHITE);
		g.drawString(getLevel(), 420, 170);
		g.drawString(getLines(), 420, 190);
		g.drawString(getScore(), 420, 210);
	}

	private void drawBestScore(Graphics2D g) {
		g.setFont(new Font("Dialog", Font.BOLD, 16));
		g.setColor(Color.WHITE);
		g.drawString("BEST SCORES :", 420, 70);
		g.setFont(new Font("Dialog", Font.PLAIN, 16));

		g.drawString("1. " + game.scoreboardValues(2), 420, 90);
		g.drawString("2. " + game.scoreboardValues(1), 420, 110);
		g.drawString("3. " + game.scoreboardValues(0), 420, 130);
	}

	private void drawGameOver(Graphics2D g) {
		Font font = new Font("Dialog", Font.PLAIN, 16);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("GAME OVER", 260, 480);
	}

	private void drawGamePaused(Graphics2D g) {
		Font font = new Font("Dialog", Font.BOLD , 16);
		g.setFont(font);
		g.setColor(Color.YELLOW);
		g.drawString("GAME PAUSED", 245, 300);
	}

	private String getLevel() {
		return String.format("Your level : %1s", game.getLevel());
	}

	private String getLines() {
		return String.format("Full lines : %1s", game.getLines());
	}

	private String getScore() {
		return String.format("Score : %1s", game.getTotalScore());
	}

	private void drawPiecePreviewBox(Graphics2D g) {
		g.setFont(new Font("Dialog", Font.BOLD, 16));
		g.setColor(Color.WHITE);
		g.drawString("Next :", 50, 350);
	}

	private void drawPieceSavedBox(Graphics2D g) {
		g.setFont(new Font("Dialog", Font.BOLD, 16));
		g.setColor(Color.WHITE);
		g.drawString("Saved:", 450, 350);
	}

	private void drawHelpBox(Graphics2D g) {
		g.setFont(new Font("Dialog", Font.BOLD, 16));
		g.setColor(Color.WHITE);
		g.drawString("H E L P", 50, 70);
		g.setFont(new Font("Dialog", Font.PLAIN, 16));
		g.drawString("F1 : Pause Game", 10, 110);
		g.drawString("F2 : New Game", 10, 130);
		g.drawString("F3 : Exit Game", 10, 150);

		g.drawString("UP : Rotate", 10, 190);
		g.drawString("ARROWS : Move left/right", 10, 210);
		g.drawString("DOWN : Drop slowly", 10, 230);
		g.drawString("SPACE : Drop", 10, 250);

		g.drawString("S: Save a piece", 10, 290);
	}
	private void drawTitle(Graphics2D g) {
		g.setFont(new Font("Dialog", Font.BOLD, 16));
		g.setColor(MYGREEN);
		g.drawString("Tetris Java version améliorée", 190, 30);
	}

	private void drawPiecePreview(Graphics2D g, PieceType type) {
		for (Point p : type.getPoints()) {
			drawBlock(g, 70 + p.x * PIECE_WIDTH, 310 + (3 - p.y) * 20, getPieceColor(type));
		}
	}

	private void drawPieceSaved(Graphics2D g, PieceType type) {
		for (Point p : type.getPoints()) {
			drawBlock(g, 470 + p.x * PIECE_WIDTH, 310 + (3 - p.y) * 20, getPieceColor(type));
		}
	}

	private Color getBoardCellColor(BoardCell boardCell) {
		if (boardCell.isEmpty()) {
			return Color.BLACK;
		}
		return getPieceColor(boardCell.getPieceType());
	}

	private Color getPieceColor(PieceType pieceType) {
		switch (pieceType) {
		case I:
			return MYBLUE;
		case J:
			return MYDARKBLUE;
		case L:
			return MYORANGE;
		case O:
			return MYYELLLOW;
		case S:
			return MYGREEN;
		case Z:
			return MYRED;
		default:
			return MYPURPLE;
		}
	}

	private void drawBlock(Graphics g, int x, int y, Color color) {
		g.setColor(color);
		g.fillRect(x, y, PIECE_WIDTH, PIECE_WIDTH);
		g.drawRect(x, y, PIECE_WIDTH, PIECE_WIDTH);
	}

	public static void main(String[] args) {
		new Tetris().gameLoop();
	}

}
