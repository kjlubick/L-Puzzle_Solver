import java.awt.Point;
import java.io.InvalidObjectException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class TwelvePieceLPuzzle extends LPuzzle {
    
    private static final int WIDTH = 8;

    private static final int HEIGHT = 6;

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
            throw new RuntimeException(new InvalidObjectException(
                    "Invalid input, should be 48 chars of board, between [] brackets"));
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
        } while (!random.solve(SolvingVerbosity.SILENT));
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
                Set<Point> pegs = new HashSet<Point>(12);
                Random rand = new Random(new SecureRandom().nextLong());
                while(puzzleCount.get() < numPuzzles) {
                    TwelvePieceLPuzzle random = null;
                    do {
                        pegs.clear();
                        while (pegs.size() < 12) {
                            pegs.add(new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)));
                        }

                        random = new TwelvePieceLPuzzle(pegs);
                        puzzlesTried.incrementAndGet();
                    } while (!random.solve(SolvingVerbosity.SILENT));
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
    public boolean addTetrinomo(TetriPlacement placement) {
        Tetromino t = placement.tetromino;
        int pegX = placement.point.x;
        int pegY = placement.point.y;
        // Phase 1: test if it fits
        if (puzzle[pegY][pegX] != PuzzleElement.PEG || !isNoTouchingPieces(pegX, pegY, t)) {
            return false;
        }
        int[][] calc = calculateRotation(placement.rotation, t.yOffsets, t.xOffsets);

        for (int i = 0; i < calc.length; i++) {
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
        for (int i = 0; i < calc.length; i++) {
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

    @Override
    public void removeTetrinomo(TetriPlacement p) {
        if (puzzle[p.point.y][p.point.x] != PuzzleElement.PEG) {
            return;
        }
        tetrominos[p.point.y][p.point.x] = null;
        
        int[][] calc = calculateRotation(p.rotation, p.tetromino.yOffsets, p.tetromino.xOffsets);
        
        for(int i =0;i<calc.length; i++) {
            int x = p.point.x + calc[i][0];
            int y = p.point.y + calc[i][1];
            
            if (isInBoard(x, y)) {
                tetrominos[y][x] = null;
            } 
        }        
    }

    @Override
    public void clearSolution() {
        clearTetrinomos();
    }
    
    public void clearTetrinomos() {
        for(int x = 0;x < tetrominos.length; x++) {
            for(int y = 0; y< tetrominos[x].length; y++) {
                tetrominos[x][y] = null;
            }
        }
    }
    


    @SuppressWarnings("unused")
    private void debugPrintRotations(Map<Point, List<TetriRotation>> rotations) {
        for(Entry<Point, List<TetriRotation>> entry: rotations.entrySet()) {
            int n = entry.getValue().size();
            System.out.printf("%s = %d %s%n", entry.getKey(), n, entry.getValue());
        }
    }

    @Override
    public List<Point> getPegLocations() {
        return pegs;
    }


    

}
