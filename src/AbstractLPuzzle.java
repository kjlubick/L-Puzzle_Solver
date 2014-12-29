import java.awt.Point;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A 2-darray-based implementation of the LPuzzle
 * @author KevinLubick
 *
 */
public abstract class AbstractLPuzzle extends LPuzzle {

    private PuzzleElement[][] puzzle = new PuzzleElement[getHeight()][getWidth()];
    
    private Tetromino[][] tetrominos = new Tetromino[getHeight()][getWidth()];
    
    private List<Point> pegs = new ArrayList<>();
    
    public AbstractLPuzzle(Collection<Point> initialPegs) 
    {
        for(int x = 0;x < puzzle.length; x++) {
            for(int y = 0; y< puzzle[x].length; y++) {
                puzzle[x][y] = PuzzleElement.BLANK;
            }
        }
        for(Point p: initialPegs) {
            int x = p.x;
            int y = p.y;
            puzzle[y][x] = PuzzleElement.PEG;
            pegs.add(new Point(x, y));
        }  
    }
    
    public AbstractLPuzzle(String exportedString) {
        exportedString = exportedString.trim();
        int expectedLength = 2 + getWidth()*getHeight();
        if (exportedString.length() != expectedLength || exportedString.charAt(0) != '[' || exportedString.charAt(expectedLength - 1) != ']') {
            throw new RuntimeException(new InvalidObjectException(
                    "Invalid input, should be "+expectedLength+ " chars of board, between [] brackets"));
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
    public Tetromino getTetromino(int x, int y) {
        y = y % getHeight();
        x = x % getWidth();
        return tetrominos[y][x];
    }

    @Override
    public boolean addTetrinomo(TetriPlacement p) {
        Tetromino t = p.tetromino;
        
        if (puzzle[p.peg.y][p.peg.x] != PuzzleElement.PEG || !canFullyPlaceTetromino(p)) {
            return false;
        }
        
        // set pegs
        List<Point> offsets = getRotatedTetrominoOffsets(p.rotation, t);
        tetrominos[p.peg.y][p.peg.x] = t;
        for (Point offset: offsets) {
            int x = p.peg.x + offset.x;
            int y = p.peg.y + offset.y;

            tetrominos[y][x] = t;
        }

        return true;
    }

    protected boolean canFullyPlaceTetromino(TetriPlacement p) {
        Tetromino t = p.tetromino;
        // check the peg location
        if (!isNoTouchingPieces(p.peg, t)) {
            return false;
        }
        List<Point> offsets = getRotatedTetrominoOffsets(p.rotation, t);
        // go through all the offsets
        for (Point offset: offsets) {
            int x = p.peg.x + offset.x;
            int y = p.peg.y + offset.y;

            if (isInBoard(x, y) && isBoardClear(x, y) && isNoTouchingPieces(x, y, t)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    private boolean isNoTouchingPieces(Point p, Tetromino t) {
        return isNoTouchingPieces(p.x, p.y, t);
    }

    private boolean isNoTouchingPieces(int x, int y, Tetromino t) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1;j<=1; j++) {     //check everything in a surrounding square
                int adjX = x + i;
                int adjY = y + j;
                if (isInBoard(adjX, adjY)) {
                    if (tetrominos[adjY][adjX] == t) {
                        // if a tetromino of the same type is within one, we can't continue
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isBoardClear(int x, int y) {
        return tetrominos[y][x] == null && puzzle[y][x] == PuzzleElement.BLANK;
    }

    private boolean isInBoard(int x, int y) {
        return x < getWidth() && x >= 0 &&
                y < getHeight() && y >= 0;
    }

    @Override
    public void removeTetrinomo(TetriPlacement p) {
        if (puzzle[p.peg.y][p.peg.x] != PuzzleElement.PEG) {
            return;
        }
        // blanket reset.  It is only guaranteed to work if a tetrinomo was previously placed here.
        tetrominos[p.peg.y][p.peg.x] = null;
        
        List<Point> offsets = getRotatedTetrominoOffsets(p.rotation, p.tetromino);
        
        for (Point offset: offsets) {
            int x = p.peg.x + offset.x;
            int y = p.peg.y + offset.y;
            
            if (isInBoard(x, y)) {
                tetrominos[y][x] = null;
            } 
        }        
    }

    @Override
    public void clearSolution() {
        clearTetrinomos();
    }
    
    @Override
    public void clearTetrinomos() {
        for(int x = 0;x < tetrominos.length; x++) {
            for(int y = 0; y< tetrominos[x].length; y++) {
                tetrominos[x][y] = null;
            }
        }
    }

    @Override
    public List<Point> getPegLocations() {
        return pegs;
    }


    

}
