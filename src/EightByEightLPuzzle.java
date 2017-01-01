import java.awt.Point;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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

	public static AbstractLPuzzle random() {
		AbstractArrayLPuzzle random = null;
		System.out.println("Generating random puzzle");
		Random rand = new Random();
		Set<Point> pegs = new HashSet<Point>(16);
		long i = 0;
		do {
			if (i % 100 == 0)
				System.out.print(".");
			pegs.clear();
			while (pegs.size() < 16) {
				pegs.add(new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)));
			}

			random = new EightByEightLPuzzle(pegs);
			i++;
		} while (!random.solve(SolvingVerbosity.SILENT));
		random.clearTetrinomos();
		System.out.println("Tried " + i + " bad puzzles");
		return random;
	}

	public static void random(int numPuzzles) {
		random(numPuzzles, 1);
	}

	/**
	 * 
	 * @deprecated use random2 instead, it's much much faster.
	 */
	@Deprecated
	public static void random(final int numPuzzles, int numThreads) {
		final AtomicInteger puzzleCount = new AtomicInteger();
		final AtomicLong puzzlesTried = new AtomicLong();
		final Object syncObject = new Object(); // used to sync System.out

		Runnable runnable = new Runnable() {
			@Override
			public void run() {

				System.out.println("Generating random puzzles");
				Set<Point> pegs = new HashSet<Point>(16);
				Random rand = new Random(new SecureRandom().nextLong());
				while (puzzleCount.get() < numPuzzles) {
					AbstractArrayLPuzzle random = null;
					do {
						pegs.clear();
						while (pegs.size() < 16) {
							pegs.add(new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)));
						}

						random = new EightByEightLPuzzle(pegs);
						puzzlesTried.incrementAndGet();
					} while (!random.solve(SolvingVerbosity.SILENT));
					puzzleCount.incrementAndGet();
					synchronized (syncObject) {
						System.out.printf("Difficulty %1.2f:  %s%n", Math.log(random.getDifficulty()), random.export());
					}
				}
				System.out.println("Tried " + puzzlesTried.get() + " puzzles to generate " + numPuzzles);

				System.out.println(new Date());
			}
		};

		for (int i = 0; i < numThreads; i++) {
			Thread thread = new Thread(runnable);
			thread.start();
		}
	}

	// Try building a puzzle by placing all the pegs one at a time. ~38 times
	// faster than
	// randomly generating puzzles and hoping for the best.
	public static void random2(final int numPuzzles, int numThreads) {

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
