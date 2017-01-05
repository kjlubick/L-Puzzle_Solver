import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SixByEightLPuzzle extends AbstractArrayLPuzzle {

	private static final int WIDTH = 8;

	private static final int HEIGHT = 6;

	public SixByEightLPuzzle(Collection<Point> initialPegs) {
		super(initialPegs);
	}

	public SixByEightLPuzzle(String exportedString) {
		super(exportedString);
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	protected Map<Tetromino, Integer> getInitialPieces() {
		Map<Tetromino, Integer> piecesToUse = new HashMap<Tetromino, Integer>(4);
		for (Tetromino tetromino : Tetromino.values()) {
			piecesToUse.put(tetromino, 3); // can use 3 of each piece
		}
		return piecesToUse;
	}

	// Try building a puzzle by placing all the pegs one at a time. ~180 times
	// faster than generating puzzles randomly and hoping for the best.
	public static void makeRandomPuzzles(final int numPuzzles, int numThreads) {

		Runnable runnable = new RandomPuzzleBuilder() {

			@Override
			public List<Tetromino> generatePieces() {
				List<Tetromino> pieces = new ArrayList<Tetromino>();
				pieces.add(Tetromino.CORNER);
				pieces.add(Tetromino.CORNER);
				pieces.add(Tetromino.CORNER);
				pieces.add(Tetromino.LONG_TIP);
				pieces.add(Tetromino.LONG_TIP);
				pieces.add(Tetromino.LONG_TIP);
				pieces.add(Tetromino.MID_PIECE);
				pieces.add(Tetromino.MID_PIECE);
				pieces.add(Tetromino.MID_PIECE);
				pieces.add(Tetromino.SHORT_TIP);
				pieces.add(Tetromino.SHORT_TIP);
				pieces.add(Tetromino.SHORT_TIP);
				return pieces;
			}

			@Override
			public int getNumPuzzlesToSolve() {
				return numPuzzles;
			}

			@Override
			public AbstractLPuzzle generateEmptyPuzzle() {
				return new SixByEightLPuzzle(Collections.<Point>emptyList());
			}
		};

		for (int i = 0; i < numThreads; i++) {
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}

}
