import java.awt.Graphics2D;
import java.awt.Point;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


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
        Map<Tetromino, Integer> piecesToUse = new HashMap<AbstractLPuzzle.Tetromino, Integer>(4);
        for (Tetromino tetromino : Tetromino.values()) {
            piecesToUse.put(tetromino, 3); // can use 3 of each pieces
        }
        return piecesToUse;
    }

    public static AbstractLPuzzle random() {
        AbstractArrayLPuzzle random = null;
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

            random = new SixByEightLPuzzle(pegs);
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
                    AbstractArrayLPuzzle random = null;
                    do {
                        pegs.clear();
                        while (pegs.size() < 12) {
                            pegs.add(new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)));
                        }

                        random = new SixByEightLPuzzle(pegs);
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

}
