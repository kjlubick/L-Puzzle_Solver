
public class TwelvePieceLPuzzle extends LPuzzle {
    
    private PuzzleElement[][] puzzle = new PuzzleElement[6][8];
    
    private Tetromino[][] tetrominos = new Tetromino[6][8];

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
    public void addTetrinomo(Tetromino t, Rotation rotation, int pegX, int pegY) {
        tetrominos[pegY][pegX] = t;
        
        int[][] calc = calculateRotation(rotation, t.yOffsets, t.xOffsets);
        
        for(int i =0;i<calc.length; i++) {
            tetrominos[pegY + calc[i][1]][pegX + calc[i][0]] = t;
        }
        
    }

    private int[][] calculateRotation(Rotation r, int[] yOffsets, int[] xOffsets) {
        int[][] retVal = new int[xOffsets.length][2];
        for (int i = 0; i < xOffsets.length; i++) {
            int xOff = xOffsets[i];
            int yOff = yOffsets[i];
            int rotX = r.rotMatrix[0][0] * xOff + r.rotMatrix[1][0] * xOff;
            int rotY = r.rotMatrix[0][1] * yOff + r.rotMatrix[1][1] * yOff;

            retVal[i][0] = rotX;
            retVal[i][1] = rotY;
        }
        return retVal;
    }

}
