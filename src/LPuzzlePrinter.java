import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

public class LPuzzlePrinter {

	public static void print4Puzzles(List<AbstractLPuzzle> puzzles, boolean printHints, boolean printSolutions) {
		AbstractLPuzzle[] arr = new AbstractLPuzzle[puzzles.size()];
		arr = puzzles.toArray(arr);
		print4Puzzles(printHints, printSolutions, arr);
	}

	// print 4 puzzles to a page
	public static void print4Puzzles(boolean printHints, boolean printSolutions, AbstractLPuzzle... puzzles) {
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf = new PageFormat();
		Paper defaultPaper = pf.getPaper();
		defaultPaper.setImageableArea(72 / 5, 72 / 5, defaultPaper.getWidth() - 72 * 2 / 5,
				defaultPaper.getHeight() - 72 * 2 / 5);
		pf.setPaper(defaultPaper);
		job.setPrintable(new FourPuzzlePrintable(puzzles, printHints, printSolutions), pf);
		boolean ok = job.printDialog();
		if (ok) {
			try {
				job.print();
			} catch (PrinterException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static class FourPuzzlePrintable implements Printable {

		AbstractLPuzzle[] puzzles;
		boolean printHints, printSolutions;
		private int pagesOfContent, pagesOfHints, pagesOfSolutions;

		public FourPuzzlePrintable(AbstractLPuzzle[] puzzles, boolean printHints, boolean printSolutions) {
			this.puzzles = puzzles;
			this.pagesOfContent = (puzzles.length - 1) / 4;
			this.printHints = printHints;
			if (printHints) {
				this.pagesOfHints = (puzzles.length - 1) / 8 + 2;
			}
			this.printSolutions = printSolutions;
			if (printSolutions) {
				this.pagesOfSolutions = pagesOfContent + 2;
			}
		}

		@Override
		public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

			if (pageIndex > pagesOfContent + pagesOfHints + pagesOfSolutions) {
				return NO_SUCH_PAGE;
			}

			/*
			 * User (0,0) is typically outside the imageable area, so we must
			 * translate by the X and Y values in the PageFormat to avoid
			 * clipping
			 */
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			
			if (printHints && pageIndex == pagesOfContent + 1) {
				printFullPage("Hints for Selected Puzzles", g2d);
				return PAGE_EXISTS;
			}
			
			if (printSolutions && pageIndex == pagesOfContent + pagesOfHints + 1) {
				printFullPage("Puzzle Solutions", g2d);
				return PAGE_EXISTS;
			}
			final int TOP_MARGIN_PTS = 36;
			final int PUZZLE_HEIGHT_PTS = 5 * 72; // 5 inches
			final int PUZZLE_WIDTH_PTS = (int) Math.round(4.1 * 72); // 5 inches

			if (pageIndex <= pagesOfContent || pageIndex > pagesOfContent + pagesOfHints + 1) {
				// Correct pageIndex if printing solutions
				PrintingOptions options = PrintingOptions.JUST_PUZZLE;
				if (pageIndex > pagesOfContent) {
					pageIndex %= (pagesOfContent + pagesOfHints + 2);
					options = PrintingOptions.WITH_SOLUTION;
				}
				
				int arrayOffset = pageIndex * 4;
				
				for (int i = 0; i + arrayOffset < puzzles.length && i < 4; i++) {
					int puzzleNumber = arrayOffset + i;
					AbstractLPuzzle p = puzzles[puzzleNumber];
					p.print(g2d, i % 2 * PUZZLE_WIDTH_PTS, 
							i / 2 * PUZZLE_HEIGHT_PTS + TOP_MARGIN_PTS, 
							1 + puzzleNumber,
							options);
				}
			} else {
				// handle hints
				pageIndex %= (pagesOfContent + 2);
				int arrayOffset = pageIndex * 8;
				
				for (int i = 0; (2 * i) + arrayOffset < puzzles.length && i < 4; i++) {
					int puzzleNumber = arrayOffset + (i * 2);
					AbstractLPuzzle p = puzzles[puzzleNumber];
					p.print(g2d, i % 2 * PUZZLE_WIDTH_PTS, 
							i / 2 * PUZZLE_HEIGHT_PTS + TOP_MARGIN_PTS, 
							1 + puzzleNumber,
							PrintingOptions.WITH_HINT);
				}
			}
			

			/* tell the caller that this page is part of the printed document */
			return PAGE_EXISTS;
		}

		private void printFullPage(String msg, Graphics2D g2d) {
			g2d.setColor(Color.black);
			g2d.setFont(g2d.getFont().deriveFont(28f));
			g2d.drawString(msg, 100, 100);
		}

	}

}
