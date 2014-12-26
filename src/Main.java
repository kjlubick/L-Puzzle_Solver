import java.util.Arrays;


public class Main {
    
    public static void main(String[] args) {
        int[][] starts = new int[][]{{1,2},{2,3},{3,4}};
        
        
        LPuzzle puzzle = new TwelvePieceLPuzzle(starts);
        
        puzzle.addTetrinomo(LPuzzle.Tetromino.SHORT_TIP, LPuzzle.Rotation.Ninety, 1, 2);
        
        puzzle.print();
    }

}
