

public abstract class LPuzzle {
    
    public enum PuzzleElement {
        BLANK(' '), PEG('o');
        
        public char toChar;

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
                throw new RuntimeException("Invalid char for Puzzle Element '"+c+'\'');
            }
        }
    }
    
    public enum Tetromino {
        LONG_TIP("*",new int[]{0, 0, -1}, new int[]{-1, -2, -2}),
        SHORT_TIP("&",new int[]{0, 1, 2}, new int[]{-1, -1, -1}),
        CORNER("^",new int[]{0, 0, 1}, new int[]{-1, -2, 0}),
        MID_PIECE("%",new int[]{0, 0, 1}, new int[]{-1, 1, 1});
        
        private Tetromino(String symbol, int[] xOffsets, int[] yOffsets) {
            this.symbol = symbol;
            this.xOffsets = xOffsets;
            this.yOffsets = yOffsets;
        }
        
        String symbol;
        int[] xOffsets;
        int[] yOffsets;
    }
    
    public enum Rotation {
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
        
        int[][] rotMatrix;
    }
    
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public abstract PuzzleElement getElement(int x, int y);
    
    public abstract Tetromino getTetromino(int x, int y);
    
    public void print() {
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

    //return true if it was successful, false otherwise
    public abstract boolean addTetrinomo(Tetromino t, Rotation rotation, int pegX, int pegY);

    public abstract boolean solve();

    public abstract double getDifficulty();

    public String export() {    //meant for outputting a computer readable version of the map
        StringBuilder builder = new StringBuilder(getWidth() * getHeight());
        
        builder.append('[');
        for(int y = 0;y<getHeight();y++) {
            for(int x = 0; x< getWidth();x++) {
                builder.append(getElement(x, y).toChar);
            }
        }
        builder.append(']');
        
        return builder.toString();
    }

    public abstract void clearSolution();
}
