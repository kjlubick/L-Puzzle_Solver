import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LPuzzlePrinter {
	
	public static void print4Puzzles(List<AbstractLPuzzle> puzzles) {
		AbstractLPuzzle[] arr = new AbstractLPuzzle[puzzles.size()];
		arr = puzzles.toArray(arr);
		print4Puzzles(arr);
	}

    //print 4 puzzles to a page
    public static void print4Puzzles(AbstractLPuzzle... puzzles) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = new PageFormat();
        Paper defaultPaper = pf.getPaper();
        defaultPaper.setImageableArea(72/5, 72/5, defaultPaper.getWidth()-72*2/5, defaultPaper.getHeight()-72*2/5);
        pf.setPaper(defaultPaper);
        job.setPrintable(new FourPuzzlePrintable(puzzles), pf);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static void printUsage() {
    	System.out.println("Usage java -jar printer.jar [puzzle] [puzzle...]");
    }
    
    public static void main(String[] args) {
    	System.out.println(Arrays.toString(args));
    	if (args == null) {
    		printUsage();
    		return;
    	}
		
		List<AbstractLPuzzle> puzzles = new ArrayList<AbstractLPuzzle>();
		for (String puzzle:args) {
			puzzle = puzzle.trim();
			if (puzzle.length() == 50) {
				puzzles.add(new SixByEightLPuzzle(puzzle));
			} else if (puzzle.length() == 66) {
				puzzles.add(new EightByEightLPuzzle(puzzle));
			} else {
				System.out.printf("Malformed puzzle: %s\n", puzzle);
			}
		}
		if (puzzles.isEmpty()) {
			printUsage();
			return;
		}
		System.out.printf("Printing %d puzzles\n", puzzles.size());
		print4Puzzles(puzzles);
	}
    
    
    private static class FourPuzzlePrintable implements Printable {
        
        AbstractLPuzzle[] puzzles;

        public FourPuzzlePrintable(AbstractLPuzzle[] puzzles) {
            this.puzzles = puzzles;
        }        

        @Override
        public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > (puzzles.length - 1)/4) {
                return NO_SUCH_PAGE;
            }

            /* User (0,0) is typically outside the imageable area, so we must
             * translate by the X and Y values in the PageFormat to avoid clipping
             */
            Graphics2D g2d = (Graphics2D)g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            int arrayOffset = pageIndex*4;
  
            for(int i = 0; i + arrayOffset <puzzles.length && i< 4;i++) {
                AbstractLPuzzle p = puzzles[arrayOffset + i];
                p.print(g2d, i % 2 * (int) Math.round(4.1 * 72), i / 2 * (int) Math.round(5.5 * 72));
            }

            /* tell the caller that this page is part of the printed document */
            return PAGE_EXISTS;
        }
        
    }

}
