import java.awt.Point;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A 2d-array based implementation of the LPuzzle
 * 
 * @author KevinLubick
 *
 */
public abstract class AbstractArrayLPuzzle extends AbstractLPuzzle {

	// puzzle holds the pegs/blanks.  This is kept separate from the tetrominoes to make for easier
	// solving code.
	private PuzzleElement[][] puzzle = new PuzzleElement[getHeight()][getWidth()];
	
	// The tetrominoes that have been added to this board, in the order they were applied.
	private List<TetriPlacement> placedTetrominoes = new ArrayList<TetriPlacement>();

	// The tetrominoes locations on the board.  This has all the x,y coordinates that are filled.
	private Tetromino[][] tetrominoes = new Tetromino[getHeight()][getWidth()];
	
	// A list of pegs in this board.  This can be dynamically changed (e.g. when building a random
	// board), so getPegLocations will use pegsDirty to see if it needs to be dynamically recalculated.
	private List<Point> pegs = new ArrayList<Point>();

	private boolean pegsDirty;

	public AbstractArrayLPuzzle(Collection<Point> initialPegs) {
		for (int y = 0; y < puzzle.length; y++) {
			for (int x = 0; x < puzzle[y].length; x++) {
				puzzle[y][x] = PuzzleElement.BLANK;
			}
		}
		for (Point p : initialPegs) {
			int x = p.x;
			int y = p.y;
			puzzle[y][x] = PuzzleElement.PEG;
			pegs.add(new Point(x, y));
		}
	}

	public AbstractArrayLPuzzle(String exportedString) {
		exportedString = exportedString.trim();
		int expectedLength = 2 + getWidth() * getHeight();
		if (exportedString.length() != expectedLength || exportedString.charAt(0) != '['
				|| exportedString.charAt(expectedLength - 1) != ']') {
			throw new RuntimeException(new InvalidObjectException(
					"Invalid input, should be " + expectedLength + " chars of board, between [] brackets"));
		}
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				PuzzleElement newPiece = PuzzleElement.fromChar(exportedString.charAt(1 + x + y * getWidth()));
				puzzle[y][x] = newPiece;
				if (newPiece == PuzzleElement.PEG) {
					pegs.add(new Point(x, y));
				}
			}
		}
	}

	@Override
	public PuzzleElement getElement(int x, int y) {
		y = y % getHeight();
		x = x % getWidth();
		return puzzle[y][x];
	}

	@Override
	public void setElement(int x, int y, PuzzleElement peg) {
		y = y % getHeight();
		x = x % getWidth();
		puzzle[y][x] = peg;
		pegsDirty = true;
	}

	@Override
	public Tetromino getTetromino(int x, int y) {
		y = y % getHeight();
		x = x % getWidth();
		return tetrominoes[y][x];
	}
	
	@Override
	public List<TetriPlacement> getTetrinomos() {
		return placedTetrominoes;
	}

	@Override
	public boolean addTetrinomo(TetriPlacement p) {
		Tetromino t = p.tetromino;

		if (puzzle[p.peg.y][p.peg.x] != PuzzleElement.PEG || !canFullyPlaceTetromino(p)) {
			return false;
		}
		
		placedTetrominoes.add(p);

		// set pegs
		List<Point> offsets = getRotatedTetrominoOffsets(p.rotation, t);
		tetrominoes[p.peg.y][p.peg.x] = t;
		for (Point offset : offsets) {
			int x = p.peg.x + offset.x;
			int y = p.peg.y + offset.y;

			tetrominoes[y][x] = t;
		}

		return true;
	}

	@Override
	protected boolean canFullyPlaceTetromino(TetriPlacement p) {
		Tetromino t = p.tetromino;
		// check the peg location
		if (tetrominoes[p.peg.y][p.peg.x] != null) {
			return false;
		}
		List<Point> offsets = getRotatedTetrominoOffsets(p.rotation, t);
		// go through all the offsets
		for (Point offset : offsets) {
			int x = p.peg.x + offset.x;
			int y = p.peg.y + offset.y;

			if (isInBoard(x, y) && isBoardClear(x, y)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean isBoardClear(int x, int y) {
		return tetrominoes[y][x] == null && puzzle[y][x] == PuzzleElement.BLANK;
	}

	private boolean isInBoard(int x, int y) {
		return x < getWidth() && x >= 0 && y < getHeight() && y >= 0;
	}

	@Override
	public void removeTetrinomo(TetriPlacement p) {
		if (puzzle[p.peg.y][p.peg.x] != PuzzleElement.PEG) {
			return;
		}
		placedTetrominoes.remove(p);
		// blanket reset. It is only guaranteed to work if a tetrinomo was
		// previously placed here.
		tetrominoes[p.peg.y][p.peg.x] = null;

		List<Point> offsets = getRotatedTetrominoOffsets(p.rotation, p.tetromino);

		for (Point offset : offsets) {
			int x = p.peg.x + offset.x;
			int y = p.peg.y + offset.y;

			if (isInBoard(x, y)) {
				tetrominoes[y][x] = null;
			}
		}
	}

	@Override
	public void clearSolution() {
		clearTetrinomos();
	}

	@Override
	public void clearTetrinomos() {
		for (int x = 0; x < tetrominoes.length; x++) {
			for (int y = 0; y < tetrominoes[x].length; y++) {
				tetrominoes[x][y] = null;
			}
		}
		placedTetrominoes.clear();
	}

	@Override
	public List<Point> getPegLocations() {
		if (pegsDirty) {
			pegs.clear();
			for (int y = 0; y < getHeight(); y++) {
				for (int x = 0; x < getWidth(); x++) {
					if (puzzle[y][x] == PuzzleElement.PEG) {
						pegs.add(new Point(x, y));
					}
				}
			}
		}
		return pegs;
	}

	@Override
	public List<Point> getEmptySpaces() {
		List<Point> points = new ArrayList<Point>();
		for (int y = 0; y < puzzle.length; y++) {
			for (int x = 0; x < puzzle[y].length; x++) {
				if (puzzle[y][x] == PuzzleElement.BLANK && tetrominoes[y][x] == null) {
					points.add(new Point(x, y));
				}
			}
		}
		return points;
	}

}
