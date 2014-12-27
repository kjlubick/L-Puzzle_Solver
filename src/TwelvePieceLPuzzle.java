import java.awt.Point;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class TwelvePieceLPuzzle extends LPuzzle {
    
    private static final int WIDTH = 8;

    private static final int HEIGHT = 6;

    private PuzzleElement[][] puzzle = new PuzzleElement[6][8];
    
    private Tetromino[][] tetrominos = new Tetromino[6][8];
    
    private List<Point> pegs = new ArrayList<>();

    private double difficulty = 1;

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
    
    public TwelvePieceLPuzzle(Collection<Point> initialPegs) 
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
    
    public TwelvePieceLPuzzle(String exportedString) {
        exportedString = exportedString.trim();
        if (exportedString.length() != 50 || exportedString.charAt(0) != '[' || exportedString.charAt(49) != ']') {
            throw new RuntimeException(new InvalidObjectException("Invalid input, should be 48 chars of board, between [] brackets"));
        }
        for(int y = 0;y<getHeight();y++) {
            for(int x = 0; x< getWidth();x++) {
                PuzzleElement newPiece = PuzzleElement.fromChar(exportedString.charAt(1 + x + y * getWidth()));
                puzzle[y][x] = newPiece;
                if (newPiece == PuzzleElement.PEG) {
                    pegs.add(new Point(x, y));
                }
            }
        }
    }

    public static LPuzzle random() {
        TwelvePieceLPuzzle random = null;
        System.out.println("Generating random puzzle");
        Random rand = new Random();
        Set<Point> pegs = new HashSet<Point>(12);
        long i = 0;
        do {
            if (i % 100 == 0)
                System.out.print(".");
            pegs.clear();
            while (pegs.size() < 12) {
                pegs.add(new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)));
            }

            random = new TwelvePieceLPuzzle(pegs);
            i++;
        } while (!random.solve(false));
        random.clearTetrinomos();
        System.out.println("Tried " + i + " bad puzzles");
        return random;
    }
    
    public static void random(int numPuzzles) {
        random(numPuzzles, 1);
    }
    
    public static void random(final int numPuzzles, int numThreads) {
        final AtomicInteger puzzleCount = new AtomicInteger();
        final AtomicLong puzzlesTried = new AtomicLong();  
        final Object syncObject = new Object();     //used to sync System.out
        
        Runnable runnable = new Runnable() {     
            @Override
            public void run() {

                System.out.println("Generating random puzzles");
                while(puzzleCount.get() < numPuzzles) {
                    TwelvePieceLPuzzle random = null;
                    
                    Random rand = new Random();
                    Set<Point> pegs = new HashSet<Point>(12);
                    do {
                        pegs.clear();
                        while (pegs.size() < 12) {
                            pegs.add(new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)));
                        }

                        random = new TwelvePieceLPuzzle(pegs);
                        puzzlesTried.incrementAndGet();
                    } while (!random.solve(false));
                    puzzleCount.incrementAndGet();
                    synchronized (syncObject) {
                        System.out.printf("Difficulty %1.2f:  %s%n", Math.log(random.getDifficulty()), random.export());
                    }
                }
                System.out.println("Tried "+puzzlesTried.get()+" puzzles to generate "+numPuzzles);

                System.out.println(new Date());
            }
        };
        
        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }
        

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
        return difficulty;
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

    public boolean addTetrinomo(Tetromino t, Rotation r, Point peg) {
        return addTetrinomo(t, r, peg.x, peg.y);
    }

    @Override
    public boolean addTetrinomo(Tetromino t, Rotation rotation, int pegX, int pegY) {
        // Phase 1: test if it fits
        if (puzzle[pegY][pegX] != PuzzleElement.PEG || !isNoTouchingPieces(pegX, pegY, t)) {
            return false;
        }
        int[][] calc = calculateRotation(rotation, t.yOffsets, t.xOffsets);
        
        for(int i =0;i<calc.length; i++) {
            int x = pegX + calc[i][0];
            int y = pegY + calc[i][1];
            
            if (isInBoard(x, y) && isBoardClear(x, y) && isNoTouchingPieces(x, y, t)) {
                continue; 
            } else {
                return false;
            }
        }
        
        // Phase 2: set pegs
        tetrominos[pegY][pegX] = t;
        for(int i =0;i<calc.length; i++) {
            int x = pegX + calc[i][0];
            int y = pegY + calc[i][1];
            
            tetrominos[y][x] = t;
        }
        
        return true;
        
    }

    private boolean isNoTouchingPieces(int x, int y, Tetromino t) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1;j<=1; j++) {     //check everything in a surrounding square
                int adjX = x + i;
                int adjY = y+ j;
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

    public void removeTetrinomo(Tetromino t, Rotation r, Point peg) {
        removeTetrinomo(t, r, peg.x, peg.y);
    }

    public void removeTetrinomo(Tetromino t, Rotation r, int pegX, int pegY) {
        if (puzzle[pegY][pegX] != PuzzleElement.PEG) {
            return;
        }
        tetrominos[pegY][pegX] = null;
        
        int[][] calc = calculateRotation(r, t.yOffsets, t.xOffsets);
        
        for(int i =0;i<calc.length; i++) {
            int x = pegX + calc[i][0];
            int y = pegY + calc[i][1];
            
            if (isInBoard(x, y)) {
                tetrominos[y][x] = null;
            } 
        }        
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
    
    @Override
    public void clearSolution() {
        clearTetrinomos();
    }
    
    private void clearTetrinomos() {
        for(int x = 0;x < tetrominos.length; x++) {
            for(int y = 0; y< tetrominos[x].length; y++) {
                tetrominos[x][y] = null;
            }
        }
    }
    
    @Override
    public boolean solve() {
        return solve(true);
    }

    private boolean solve(boolean printStuffHuh) {
        clearTetrinomos();
        difficulty = 1;
        
        Map<Tetromino, Integer> piecesToUse = new HashMap<LPuzzle.Tetromino, Integer>(4);
        for(Tetromino tetromino : Tetromino.values()) {
            piecesToUse.put(tetromino, 3);      //can use 3 of each pieces
        }

        if (solve(new ArrayList<Point>(pegs), piecesToUse)) {
            if (printStuffHuh) {
           System.out.println("Solved");
            this.print();
            }
            // todo, interpret solutions by iterating through all pegs
            return true;

        } else {
            if (printStuffHuh) {
                System.out.println("No Solution");
            }
            clearTetrinomos();
            difficulty = Double.POSITIVE_INFINITY;
            return false;
        }
    }

    private boolean solve(List<Point> pegsLeftToLocate, Map<Tetromino, Integer> piecesToUse) {
        if (pegsLeftToLocate.size() == 0) { //no more pegs to play, we can only have solved the puzzle
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
        
        
        pegsLeftToLocate.remove(pegToTry);
        
        for (int i = 0; i < trListToTry.size(); i++) {
            TetriRotation tr = trListToTry.get(i);
            
            if (piecesToUse.get(tr.tetromino) > 0) {
                Map<Tetromino, Integer> newPiecesToUse = new HashMap<>(piecesToUse);
                newPiecesToUse.put(tr.tetromino, newPiecesToUse.get(tr.tetromino) - 1);
                if (addTetrinomo(tr.tetromino, tr.rotation, pegToTry)) {
                    if (solve(new ArrayList<Point>(pegsLeftToLocate), newPiecesToUse)) {  //copy the pegs, so they aren't interfered with
                        difficulty *= smallestRotations;        //multiply here to make sure it's only done once
                        return true;
                    }
                    removeTetrinomo(tr.tetromino, tr.rotation, pegToTry);
                }
            }
            
            
        }
        
        // I've tried this peg in all configurations and gotten nothing, no solution down this line
        
        return false;
    }

    private Map<Point, List<TetriRotation>> findPossibilitiesForPegs(List<Point> pegsToTest) {
        Map<Point, List<TetriRotation>> originalRotations = new HashMap<>();
        
        for(Point peg: pegsToTest) {
            List<TetriRotation> tetriRotations = new ArrayList<>();
            for(Tetromino t: Tetromino.values()) {
                for(Rotation r: Rotation.values()) {
                    if (addTetrinomo(t, r, peg)) {
                        removeTetrinomo(t, r, peg);
                        tetriRotations.add(new TetriRotation(r, t));
                    }
                }
            }
            originalRotations.put(peg, tetriRotations);
        }
        return originalRotations;
    }

    @SuppressWarnings("unused")
    private void debugPrintRotations(Map<Point, List<TetriRotation>> rotations) {
        for(Entry<Point, List<TetriRotation>> entry: rotations.entrySet()) {
            int n = entry.getValue().size();
            System.out.printf("%s = %d %s%n", entry.getKey(), n, entry.getValue());
        }
    }

    private static class TetriRotation {
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
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((rotation == null) ? 0 : rotation.hashCode());
            result = prime * result + ((tetromino == null) ? 0 : tetromino.hashCode());
            return result;
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
    

}
