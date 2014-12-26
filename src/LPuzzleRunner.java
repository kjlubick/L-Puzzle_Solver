import java.util.Arrays;


public class LPuzzleRunner {
    
    public static void main(String[] args) {
        int[][] starts = new int[][] { { 2, 0 }, { 3, 0 }, { 7, 0 }, { 4, 1 }, { 6, 1 },
                { 0, 2 }, { 2, 3 }, { 6, 3 }, { 2, 4 }, { 2, 5 }, { 5, 5 }, { 6, 5 } };

        LPuzzle puzzle = new TwelvePieceLPuzzle(starts);
        
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.LONG_TIP, LPuzzle.Rotation.Ninety, 2, 0));
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.LONG_TIP, LPuzzle.Rotation.MirrorNinety, 2, 3));
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.LONG_TIP, LPuzzle.Rotation.MirrorTwoSeventy, 2, 5));
        
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.SHORT_TIP, LPuzzle.Rotation.OneEighty, 3, 0));
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.SHORT_TIP, LPuzzle.Rotation.MirrorNinety, 6, 5));
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.SHORT_TIP, LPuzzle.Rotation.MirrorTwoSeventy, 6, 1));
        
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.CORNER, LPuzzle.Rotation.MirrorTwoSeventy, 7, 0));
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.CORNER, LPuzzle.Rotation.Ninety, 5, 5));
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.CORNER, LPuzzle.Rotation.TwoSeventy, 0, 2));
        
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.MID_PIECE, LPuzzle.Rotation.MirrorNone, 4, 1));
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.MID_PIECE, LPuzzle.Rotation.MirrorOneEighty, 6, 3));
        System.out.println(puzzle.addTetrinomo(LPuzzle.Tetromino.MID_PIECE, LPuzzle.Rotation.MirrorNinety, 2, 4));
        
        puzzle.print();
    }

}
