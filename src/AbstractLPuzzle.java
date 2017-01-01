import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

public abstract class AbstractLPuzzle {

	private double difficulty = 1;

	private SolvingVerbosity solveVerbosity;

	private Stack<TetriPlacement> solution;

	public void printToStdOut() {
		System.out.println();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Tetromino t = getTetromino(x, y);
				String s = (t == null ? " " : t.symbol);
				System.out.printf(" %s%5s%s ", s, getElement(x, y), s);
			}
			System.out.println();
		}
	}

	public String export() { // meant for outputting a computer readable version
								// of the map
		StringBuilder builder = new StringBuilder(getWidth() * getHeight());

		builder.append('[');
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				builder.append(getElement(x, y).toChar);
			}
		}
		builder.append(']');

		return builder.toString();
	}

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract PuzzleElement getElement(int x, int y);

	public abstract void setElement(int x, int y, PuzzleElement peg);

	public abstract Tetromino getTetromino(int x, int y);
	
	public abstract List<TetriPlacement> getTetrinomos();

	/**
	 * Tries to add the tetrinomo at the given location, with the given rotation
	 * 
	 * @param placement
	 * @return true if placement was legal, false otherwise
	 */
	public abstract boolean addTetrinomo(TetriPlacement placement);

	protected abstract boolean canFullyPlaceTetromino(TetriPlacement placement);

	public abstract void clearSolution();

	public abstract void removeTetrinomo(TetriPlacement placement);

	public abstract List<Point> getPegLocations();

	public abstract List<Point> getEmptySpaces();

	public abstract void clearTetrinomos();

	protected List<Point> getRotatedTetrominoOffsets(Rotation r, Tetromino t) {
		List<Point> retVal = new ArrayList<Point>(t.xOffsets.length);
		for (int i = 0; i < t.xOffsets.length; i++) {
			int xOff = t.xOffsets[i];
			int yOff = t.yOffsets[i];
			int rotX = r.rotMatrix[0][0] * xOff + r.rotMatrix[1][0] * yOff;
			int rotY = r.rotMatrix[0][1] * xOff + r.rotMatrix[1][1] * yOff;

			retVal.add(new Point(rotX, rotY));
		}
		return retVal;
	}

	public boolean solveShowingWork() {
		return solve(SolvingVerbosity.SHOW_WORK);
	}

	public boolean solve() {
		return solve(SolvingVerbosity.SHOW_FINAL);
	}

	protected boolean solve(SolvingVerbosity verbosity) {
		clearTetrinomos();
		difficulty = 1;

		Map<Tetromino, Integer> piecesToUse = getInitialPieces();

		solveVerbosity = verbosity;
		this.solution = new Stack<TetriPlacement>();
		if (solve(new ArrayList<Point>(getPegLocations()), piecesToUse)) {
			if (solveVerbosity != SolvingVerbosity.SILENT) {
				System.out.println("Solved");
				this.printToStdOut();
			}
			if (solveVerbosity == SolvingVerbosity.SHOW_WORK) {
				int i = 1;
				while (!solution.isEmpty()) {
					System.out.printf("Step %d: %s", i, solution.pop());
					i++;
				}
			}
			return true;

		} else {
			if (solveVerbosity != SolvingVerbosity.SILENT) {
				System.out.println("No Solution");
			}
			clearTetrinomos();
			difficulty = Double.POSITIVE_INFINITY;
			return false;
		}
	}

	protected abstract Map<Tetromino, Integer> getInitialPieces();

	protected Map<Point, List<TetriRotation>> findPossibilitiesForPegs(List<Point> pegsToTest) {
		Map<Point, List<TetriRotation>> originalRotations = new HashMap<Point, List<TetriRotation>>();

		for (Point peg : pegsToTest) {
			List<TetriRotation> tetriRotations = new ArrayList<TetriRotation>();
			for (Tetromino t : Tetromino.values()) {
				for (Rotation r : Rotation.values()) {
					TetriPlacement placement = new TetriPlacement(peg, t, r);
					if (canFullyPlaceTetromino(placement)) {
						tetriRotations.add(placement.getTetriRotation());
					}
				}
			}
			originalRotations.put(peg, tetriRotations);
		}
		return originalRotations;
	}

	public double getDifficulty() {
		return difficulty;
	}

	private boolean solve(List<Point> pegsLeftToLocate, Map<Tetromino, Integer> numberOfAvailablePieces) {
		if (pegsLeftToLocate.isEmpty()) { // no more pegs to play, we can only
											// have solved the puzzle
			return true;
		}

		Map<Point, List<TetriRotation>> rotations = findPossibilitiesForPegs(pegsLeftToLocate);

		Point pegToTry = null;
		List<TetriRotation> trListToTry = null;
		int smallestRotations = Integer.MAX_VALUE;
		for (Entry<Point, List<TetriRotation>> entry : rotations.entrySet()) {
			List<TetriRotation> list = entry.getValue();
			if (smallestRotations > list.size()) {
				smallestRotations = list.size();
				trListToTry = list;
				pegToTry = entry.getKey();
			}
		}
		if (smallestRotations == 0) { // there is a peg that can't be fit
			return false;
		}

		// debugPrintRotations(rotations);

		pegsLeftToLocate.remove(pegToTry);

		for (int i = 0; i < trListToTry.size(); i++) {
			TetriRotation tr = trListToTry.get(i);

			if (numberOfAvailablePieces.get(tr.tetromino) > 0) {
				Map<Tetromino, Integer> revisedAvailablePieces = new HashMap<Tetromino, Integer>(
						numberOfAvailablePieces);
				revisedAvailablePieces.put(tr.tetromino, revisedAvailablePieces.get(tr.tetromino) - 1);

				TetriPlacement placement = new TetriPlacement(pegToTry, tr);

				if (addTetrinomo(placement)) {
					// copy the pegs, so they aren't interfered with
					if (solve(new ArrayList<Point>(pegsLeftToLocate), revisedAvailablePieces)) {
						// multiply here to make sure it's only done once
						difficulty *= smallestRotations;

						placement.extraDisplayString = String.format("(%d other options)", trListToTry.size() - 1);
						solution.push(placement);
						return true;
					}
					removeTetrinomo(placement);
				}
			}

		}

		// I've tried this peg in all configurations and gotten nothing, no
		// solution down this line

		return false;
	}

	@SuppressWarnings("unused")
	private void debugPrintRotations(Map<Point, List<TetriRotation>> rotations) {
		for (Entry<Point, List<TetriRotation>> entry : rotations.entrySet()) {
			int n = entry.getValue().size();
			System.out.printf("%s = %d %s%n", entry.getKey(), n, entry.getValue());
		}
	}

	public void print(Graphics2D g, int xOffset, int yOffset, int puzzleNumber, PrintingOptions options) {
		final int gridSize = 36; // half an inch
		
		List<TetriPlacement> tetrinomosToShow = Collections.emptyList();
		if (options == PrintingOptions.WITH_HINT) {
			tetrinomosToShow = new ArrayList<TetriPlacement>();
			List<TetriPlacement> allTetrinomos = getTetrinomos();
			// Give the two "easiest" ones to place and the last one, which is likely the hardest.
			tetrinomosToShow.add(allTetrinomos.get(0));
			tetrinomosToShow.add(allTetrinomos.get(1));
			tetrinomosToShow.add(allTetrinomos.get(allTetrinomos.size()-1));
		} else if (options == PrintingOptions.WITH_SOLUTION) {
			tetrinomosToShow = getTetrinomos();
		}
		for (TetriPlacement t :tetrinomosToShow) {
			List<Point> points = getRotatedTetrominoOffsets(t.rotation, t.tetromino);
			g.setColor(t.tetromino.printColor);
			for (Point offset : points) {
				g.fillRect(xOffset + (t.peg.x + offset.x) * gridSize, 
						   yOffset + (t.peg.y + offset.y) * gridSize, 
						   gridSize, gridSize);
			}
			g.fillRect(xOffset + (t.peg.x * gridSize), 
					   yOffset + (t.peg.y * gridSize), 
					   gridSize, gridSize);
		}
		
		// draws a grid
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(2.0f));
		int widthInPts = getWidth() * gridSize;
		for (int i = 0; i <= getHeight(); i++) {
			g.drawLine(0 + xOffset, i * gridSize + yOffset, widthInPts + xOffset, i * gridSize + yOffset);
		}

		int heightInPts = getHeight() * gridSize;
		for (int i = 0; i <= getWidth(); i++) {
			g.drawLine(i * gridSize + xOffset, 0 + yOffset, i * gridSize + xOffset, heightInPts + yOffset);
		}

		// draw pegs
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				PuzzleElement pe = getElement(x, y);
				if (pe == PuzzleElement.PEG) {
					g.drawArc(x * gridSize + gridSize / 4 + xOffset, y * gridSize + gridSize / 4 + yOffset,
							gridSize / 2, gridSize / 2, 0, 360);
				}
			}
		}
		
		// Draw puzzle number and difficulty
		g.setColor(Color.black);
		g.setFont(g.getFont().deriveFont(18f));
		g.drawString(String.format("Puzzle %d - Difficulty %1.2f", puzzleNumber, Math.log(getDifficulty())), 
				xOffset + 1*gridSize, yOffset + gridSize * (getHeight() + 1));

	}


}

enum SolvingVerbosity {
	SILENT, SHOW_FINAL, SHOW_WORK
}

enum PrintingOptions {
	JUST_PUZZLE, WITH_HINT, WITH_SOLUTION
}


enum PuzzleElement {
	BLANK(' '), PEG('o');

	final char toChar;

	private PuzzleElement(char c) {
		this.toChar = c;
	}

	public static PuzzleElement fromChar(char c) {
		switch (c) {
		case ' ':
			return BLANK;
		case 'o':
			return PEG;
		default:
			throw new RuntimeException("Invalid char for Puzzle Element '" + c + '\'');
		}
	}
}

enum Tetromino {
	// Colors from http://www.somersault1824.com/tips-for-designing-scientific-figures-for-color-blind-readers/
    LONG_TIP("*",new int[]{0, 0, -1}, new int[]{-1, -2, -2}, new Color(73, 0, 146)),
    SHORT_TIP("&",new int[]{0, 1, 2}, new int[]{-1, -1, -1}, new Color(146, 73, 0)),
    CORNER("^",new int[]{0, 0, 1}, new int[]{-1, -2, 0},     new Color(109, 182, 255)),
    MID_PIECE("%",new int[]{0, 0, 1}, new int[]{-1, 1, 1},   new Color(255, 255, 109));
    
	private Tetromino(String symbol, int[] xOffsets, int[] yOffsets, Color printColor) {
        this.symbol = symbol;
        this.xOffsets = xOffsets;
        this.yOffsets = yOffsets;
        this.printColor = printColor;
    }
    
    final String symbol;
    final int[] xOffsets;
    final int[] yOffsets;
    final Color printColor;
}

enum Rotation {
    None(new int[][]{{1,0},{0,1}}), 
    Ninety(new int[][]{{0,-1},{1,0}}), 
    OneEighty(new int[][]{{-1,0},{0,-1}}), 
    TwoSeventy(new int[][]{{0,1},{-1,0}}), 
    MirrorNone(new int[][]{{-1,0},{0,1}}), 
    MirrorNinety(new int[][]{{0,-1},{-1,0}}),
    MirrorOneEighty(new int[][]{{1,0},{0,-1}}), 
    MirrorTwoSeventy(new int[][]{{0,1},{1,0}});
    
    private Rotation(int[][] rotMatrix) {
        this.rotMatrix = rotMatrix;
    }
    
    final int[][] rotMatrix;
}


class TetriRotation {
	public final Rotation rotation;
	public final Tetromino tetromino;

	public TetriRotation(Rotation rotation, Tetromino tetromino) {
		this.rotation = rotation;
		this.tetromino = tetromino;
	}

	@Override
	public String toString() {
		return "TetriRotation [rotation=" + rotation + ", tetromino=" + tetromino + ']';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rotation == null) ? 0 : rotation.hashCode());
		return prime * result + ((tetromino == null) ? 0 : tetromino.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TetriRotation other = (TetriRotation) obj;
		if (rotation != other.rotation)
			return false;
		if (tetromino != other.tetromino)
			return false;
		return true;
	}
}

class TetriPlacement {
	public final Rotation rotation;
	public final Tetromino tetromino;
	public final Point peg;
	public String extraDisplayString;

	public TetriPlacement(Point point, Tetromino tetromino, Rotation rotation) {
		this(point, tetromino, rotation, "");
	}

	public TetriPlacement(Point point, TetriRotation tr) {
		this(point, tr.tetromino, tr.rotation, "");
	}

	public TetriPlacement(Point point, Tetromino tetromino, Rotation rotation, String extraDisplayString) {
		this.rotation = rotation;
		this.tetromino = tetromino;
		this.peg = point;
		this.extraDisplayString = extraDisplayString;
	}

	public TetriRotation getTetriRotation() {
		return new TetriRotation(rotation, tetromino);
	}

	@Override
	public String toString() {
		return String.format("Place a %s at (%d,%d) with rotation %s %s%n", tetromino.name(), peg.x, peg.y,
				rotation.name(), extraDisplayString);
	}
}