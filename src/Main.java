import java.util.Arrays;


public class Main {
    
    public static void main(String[] args) {
        int[][] starts = new int[][]{{2,0},{3,0},{7,0}};
        
        
        LPuzzle puzzle = new TwelvePieceLPuzzle(starts);
        
        puzzle.addTetrinomo(LPuzzle.Tetromino.LONG_TIP, LPuzzle.Rotation.Ninety, 2, 0);
        
        puzzle.print();
    }

}
