package tetris.model;

public class Game {

	private final Board board;
	private Piece nextPiece;
	private Score scoreboard = new Score();

	private Piece savedPiece;

	private boolean alreadySavedThisTurn = false;

	private boolean playing = false;
	private boolean paused = false;
	private boolean dropping = false;
	private boolean gameOver = false;
	private boolean saved = false;

	private int freeFallIterations;
	private int totalScore;

	public Game() {
		board = new Board();
	}

	public BoardCell[][] getBoardCells() {
		return board.getBoardWithPiece();
	}

	public Piece getNextPiece() {
		return nextPiece;
	}

	public int scoreboardValues(int rank) {
		return scoreboard.getAllscore(rank);
	}

	public Piece getSavedPiece() {
		return board.getSavedPiece();
	}

	public long getIterationDelay() {
		return (long) (((11 - getLevel()) * 0.05) * 1000);
	}

	public int getScore() {
		return ((21 + (3 * getLevel())) - freeFallIterations);
	}

	public int getTotalScore() {
		return totalScore;
	}

	public int getLines() {
		return board.getFullLines();
	}

	public int getLevel() {
		if ((board.getFullLines() >= 1) && (board.getFullLines() <= 90)) {
			return 1 + ((board.getFullLines() - 1) / 10);
		} else if (board.getFullLines() >= 91) {
			return 10;
		} else {
			return 1;
		}
	}

	public void startGame() {
		paused = false;
		dropping = false;
		nextPiece = Piece.getRandomPiece();
		board.setCurrentPiece(Piece.getRandomPiece());
		playing = true;
		scoreboard.testingNewScore(totalScore);

	}

	public boolean isPlaying() {
		return playing;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isSaved() {
		return saved;
	}

	public void pauseGame() {
		paused = !paused;
	}

	public void moveLeft() {
		board.moveLeft();
	}

	public void moveRight() {
		board.moveRight();
	}

	public void exit() {
		System.exit(0);
	}

	public void moveDown() {
		if (!board.canCurrentPieceMoveDown()) {

			if (freeFallIterations == 0) {
				playing = false;
				gameOver = true;
				scoreboard.testingNewScore(totalScore);
			} else {
				dropping = false;
				board.setCurrentPiece(nextPiece);
				nextPiece = Piece.getRandomPiece();
				totalScore += getScore();
				freeFallIterations = 0;
				alreadySavedThisTurn = false;
			}
		} else {
			board.moveDown();
			freeFallIterations++;
		}
	}

	public void savePiece() {
		if (alreadySavedThisTurn == false) {
			alreadySavedThisTurn = true;
			saved = true;

			if (board.getSavedPiece() == null) {
				board.removeCurrentPiece();
				nextPiece = Piece.getRandomPiece();
				board.setCurrentPiece(nextPiece);
			} else {
				board.removeCurrentPiece();
				board.setCurrentPiece(board.getSwappingPiece());
			}
		}
	}

	public void rotate() {
		board.rotate();
	}

	public void drop() {
		dropping = true;
	}

	public boolean isDropping() {
		return dropping;
	}

}