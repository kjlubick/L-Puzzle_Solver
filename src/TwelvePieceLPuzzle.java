import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class TwelvePieceLPuzzle extends LPuzzle {
    
    private PuzzleElement[][] puzzle = new PuzzleElement[6][8];
    
    private Tetromino[][] tetrominos = new Tetromino[6][8];
    
    private List<Point> pegs = new ArrayList<>();

    public TwelvePieceLPuzzle(int[][] initialPegs) 
    {
        for(int x = 0;x < puzzle.length; x++) {
            for(int y = 0; y< puzzle[x].length; y++) {
                puzzle[x][y] = PuzzleElement.BLANK;
            }
        }
        for(int i = 0;i<initialPegs.length;i++) {
            int x = initialPegs[i][0];
            int y = initialPegs[i][1];
            puzzle[y][x] = PuzzleElement.PEG;
            pegs.add(new Point(x, y));
        }  
    }

    @Override
    public int getWidth() {
        return 8;
    }

    @Override
    public int getHeight() {
        return 6;
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
    public boolean addTetrinomo(Tetromino t, Rotation rotation, int pegX, int pegY) {
        if (puzzle[pegY][pegX] != PuzzleElement.PEG) {
            return false;
        }
        tetrominos[pegY][pegX] = t;
        
        int[][] calc = calculateRotation(rotation, t.yOffsets, t.xOffsets);
        
        for(int i =0;i<calc.length; i++) {
            int x = pegX + calc[i][0];
            int y = pegY + calc[i][1];
            
            if (x < getWidth() && x >= 0 &&
                    y < getHeight() && y >= 0 && 
                    tetrominos[y][x] == null && puzzle[y][x] == PuzzleElement.BLANK) {
                tetrominos[y][x] = t;
            } else {
                //undo previous tetromino pieces
                for(int j = i-1; j>=0 ;j --) {
                    x = pegX + calc[j][0];
                    y = pegY + calc[j][1];
                    tetrominos[y][x] = null;
                }
                tetrominos[pegY][pegX] = null;
                return false;
            }
            
        }
        return true;
        
    }

    public boolean addTetrinomo(Tetromino t, Rotation r, Point peg) {
        return addTetrinomo(t, r, peg.x, peg.y);
    }

    private int[][] calculateRotation(Rotation r, int[] yOffsets, int[] xOffsets) {
        int[][] retVal = new int[xOffsets.length][2];
        for (int i = 0; i < xOffsets.length; i++) {
            int xOff = xOffsets[i];
            int yOff = yOffsets[i];
            int rotX = r.rotMatrix[0][0] * xOff + r.rotMatrix[1][0] * yOff;
            int rotY = r.rotMatrix[0][1] * xOff + r.rotMatrix[1][1] * yOff;

            retVal[i][0] = rotX;
            retVal[i][1] = rotY;
        }
        return retVal;
    }
    
    private void clearTetrinomos() {
        for(int x = 0;x < tetrominos.length; x++) {
            for(int y = 0; y< tetrominos[x].length; y++) {
                tetrominos[x][y] = null;
            }
        }
    }

    private class TetriRotation {
        public final Rotation rotation;
        public final Tetromino tetromino;
        public TetriRotation(Rotation rotation, Tetromino tetromino) {
            this.rotation = rotation;
            this.tetromino = tetromino;
        }
        @Override
        public String toString() {
            return "TetriRotation [rotation=" + rotation + ", tetromino=" + tetromino + "]";
        } 
        
    }
    
    @Override
    public void solve() {
        clearTetrinomos();
        Map<Point, Set<TetriRotation>> originalRotations = new HashMap<>();
        for(Point peg: pegs) {
            Set<TetriRotation> tetriRotations = new HashSet<>();
            for(Tetromino t: Tetromino.values()) {
                for(Rotation r: Rotation.values()) {
                    if (addTetrinomo(t, r, peg)) {
                        tetriRotations.add(new TetriRotation(r, t));
                    }
                    clearTetrinomos();
                }
            }
            originalRotations.put(peg, tetriRotations);
        }
        
        System.out.println(originalRotations);
        
        long totalCombos = 1;
        for(Entry<Point, Set<TetriRotation>> entry: originalRotations.entrySet()) {
            int n = entry.getValue().size();
            totalCombos *= n;
            System.out.printf("%s = %d %s%n", entry.getKey(), n, entry.getValue());
        }
        System.out.println(totalCombos +" combos");
        
        
        
    }

}
