import java.awt.Point;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class RandomPuzzleBuilder implements Runnable {

	private static AtomicInteger puzzleCount = new AtomicInteger();
	private static AtomicLong puzzlesTried = new AtomicLong();
	private static Object syncObject = new Object(); // used to sync System.out

	private boolean puzzleBuilder(AbstractLPuzzle puzzle, List<AbstractLPuzzle.Tetromino> piecesLeft) {
		if (piecesLeft.isEmpty()) {
			return true;
		}
		List<Point> possiblePoints = puzzle.getEmptySpaces();
		if (possiblePoints.isEmpty()) {
			throw new RuntimeException("Something has gone horribly wrong.  Too many pieces to place");
		}
		Collections.shuffle(possiblePoints);
		// Save some cycles by only thinking about up to 8 points, since we bail
		// out after 2 failures.
		int maxPoints = Math.min(8, possiblePoints.size());
		Map<Point, List<AbstractLPuzzle.TetriRotation>> possibilities = puzzle
				.findPossibilitiesForPegs(possiblePoints.subList(0, maxPoints));
		int thingsToTry = 0;
		for (Entry<Point, List<AbstractLPuzzle.TetriRotation>> entry : possibilities.entrySet()) {
			Point p = entry.getKey();
			puzzle.setElement(p.x, p.y, AbstractLPuzzle.PuzzleElement.PEG);
			List<AbstractLPuzzle.TetriRotation> list = entry.getValue();
			Collections.shuffle(list);
			for (AbstractLPuzzle.TetriRotation tr : list) {
				if (piecesLeft.contains(tr.tetromino)) {
					AbstractLPuzzle.TetriPlacement tp = new AbstractLPuzzle.TetriPlacement(p, tr);
					if (!puzzle.addTetrinomo(tp)) {
						throw new RuntimeException(
								"Something has gone horribly wrong.  Failed to add a good possibility.");
					}
					piecesLeft.remove(tr.tetromino);
					puzzlesTried.incrementAndGet();
					// Can we build a puzzle from here?
					if (puzzleBuilder(puzzle, piecesLeft)) {
						// WE DID IT.
						return true;
					}
					// Nope, remove what we tried and try again.
					puzzle.removeTetrinomo(tp);
					piecesLeft.add(tr.tetromino);
					// Try not to get too bogged down with bad things in the
					// leaves.
					thingsToTry++;
					if (thingsToTry >= 2) {
						puzzle.setElement(p.x, p.y, AbstractLPuzzle.PuzzleElement.BLANK);
						return false;
					}
				}
			}
			puzzle.setElement(p.x, p.y, AbstractLPuzzle.PuzzleElement.BLANK);
		}
		return false;
	}

	@Override
	public void run() {

		System.out.println("Generating random puzzles");
		while (puzzleCount.get() < getNumPuzzlesToSolve()) {
			AbstractLPuzzle random = generateEmptyPuzzle();
			List<AbstractLPuzzle.Tetromino> pieces = generatePieces();

			if (puzzleBuilder(random, pieces)) {
				puzzlesTried.incrementAndGet();
				if (random.solve(SolvingVerbosity.SILENT)) {
					puzzleCount.incrementAndGet();
					synchronized (syncObject) {
						System.out.printf("Difficulty %1.2f:  %s%n", Math.log(random.getDifficulty()), random.export());
					}
				}
			}
		}
		System.out.println("Tried " + puzzlesTried.get() + " puzzles to generate " + puzzleCount.get());

		System.out.println(new Date());
	}

	public abstract int getNumPuzzlesToSolve();

	public abstract AbstractLPuzzle generateEmptyPuzzle();

	public abstract List<AbstractLPuzzle.Tetromino> generatePieces();

}
