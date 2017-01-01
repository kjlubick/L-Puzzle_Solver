import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EightByEightLPuzzle extends AbstractArrayLPuzzle {

	private static final int WIDTH = 8;

	private static final int HEIGHT = 8;

	public EightByEightLPuzzle(Collection<Point> initialPegs) {
		super(initialPegs);
	}

	public EightByEightLPuzzle(String exportedString) {
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
	public double getDifficulty() {
		// inflate difficulty because there are more things to check
		return super.getDifficulty() + 5;
	}

	@Override
	protected Map<Tetromino, Integer> getInitialPieces() {
		Map<Tetromino, Integer> piecesToUse = new HashMap<Tetromino, Integer>(4);
		for (Tetromino tetromino : Tetromino.values()) {
			piecesToUse.put(tetromino, 4); // can use 4 of each pieces
		}
		return piecesToUse;
	}

	// Try building puzzles by placing all the pegs one at a time. ~38 times
	// faster than randomly generating puzzles and hoping for the best.
	public static void makeRandomPuzzles(final int numPuzzles, int numThreads) {

		Runnable runnable = new RandomPuzzleBuilder() {

			@Override
			public List<Tetromino> generatePieces() {
				List<Tetromino> pieces = new ArrayList<Tetromino>();
				pieces.add(Tetromino.CORNER);
				pieces.add(Tetromino.CORNER);
				pieces.add(Tetromino.CORNER);
				pieces.add(Tetromino.CORNER);
				pieces.add(Tetromino.LONG_TIP);
				pieces.add(Tetromino.LONG_TIP);
				pieces.add(Tetromino.LONG_TIP);
				pieces.add(Tetromino.LONG_TIP);
				pieces.add(Tetromino.MID_PIECE);
				pieces.add(Tetromino.MID_PIECE);
				pieces.add(Tetromino.MID_PIECE);
				pieces.add(Tetromino.MID_PIECE);
				pieces.add(Tetromino.SHORT_TIP);
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
				return new EightByEightLPuzzle(Collections.<Point>emptyList());
			}
		};

		for (int i = 0; i < numThreads; i++) {
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}

}
