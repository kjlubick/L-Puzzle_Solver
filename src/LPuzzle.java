import java.awt.Point;



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

    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public abstract PuzzleElement getElement(int x, int y);
    
    public abstract Tetromino getTetromino(int x, int y);
    
    /**
     * Tries to add the tetrinomo at the given location, with the given rotation
     * @param placement
     * @return true if placement was legal, false otherwise
     */
    public abstract boolean addTetrinomo(TetriPlacement placement);

    public abstract boolean solve();

    public abstract double getDifficulty();

    public abstract void clearSolution();

    public abstract boolean solveShowingWork();

    public abstract void removeTetrinomo(TetriPlacement placement);
    
    static class TetriRotation {
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
    
    static class TetriPlacement {
        public final Rotation rotation;
        public final Tetromino tetromino;
        public final Point point;
        public String extraDisplayString;
        
        public TetriPlacement(Point point, Tetromino tetromino,Rotation rotation) {
            this(point, tetromino, rotation, "");
        }
        
        public TetriPlacement(Point point, TetriRotation tr) {
            this(point, tr.tetromino, tr.rotation, "");
        }
        
        public TetriPlacement(Point point, Tetromino tetromino,Rotation rotation, String extraDisplayString) {
            this.rotation = rotation;
            this.tetromino = tetromino;
            this.point = point;
            this.extraDisplayString = extraDisplayString;
        }
        
        public TetriRotation getTetriRotation() {
            return new TetriRotation(rotation, tetromino);
        }
        
        @Override
        public String toString() {
            return String.format("Place a %s at (%d,%d) with rotation %s %s%n", tetromino.name(),
                    point.x, point.y, rotation.name(), extraDisplayString);
        }
    }
}
