import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Main {
	
	// java -jar puzzle.jar generate 6x8 [num] [numthreads]
	// java -jar puzzle.jar solve [puzzle...]
	// java -jar puzzle.jar print [--hints] [--solutions] \"[ o o...]\" [more puzzles...]
	
	private static void printUsage() {
		System.out.println("Usage");
		System.out.println("\tjava -jar puzzle.jar generate [6x8 or 8x8] [num] [numthreads]");
		System.out.println("\tjava -jar puzzle.jar print [--hints] [--solutions] \"[ o o...]\" [more puzzles...]");
		System.out.println("\tjava -jar puzzle.jar solve \"[ o o...]\" [more puzzles...]");

	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		if (args == null || args.length == 0) {
			printUsage();
			return;
		}
		if ("generate".equals(args[0])) {
			generate(args);
			return;
		} else if ("solve".equals(args[0])) {
			solve(args);
			return;
		} else if ("print".equals(args[0])) {
			print(args);
			return;
		}
		printUsage();
		
	}

	private static void generate(String[] args) {
		String puzzleType = null;
		int numberToGenerate = 0;
		int numberThreads = 0;
		try {
			puzzleType = args[1];
			String numberToGenerateString = args[2];
			String numberThreadsString = args[3];
			if (!("6x8".equals(puzzleType) || "8x8".equals(puzzleType))) {
				System.out.println("Type must be 6x8 or 8x8");
				throw new Exception();
			}
			numberToGenerate = Integer.parseInt(numberToGenerateString);
			numberThreads = Integer.parseInt(numberThreadsString);
			if (numberThreads <= 0 || numberToGenerate <= 0) {
				System.out.println("Numbers should be positive.");
				printUsage();
				return;
			}

		} catch (Exception e) {
			System.out.println("Usage java -jar generator.jar puzzleType numberToGenerate threads");
			System.out.println("Sample usage: java -jar generator 6x8 200 4");
			return;
		}

		System.out.println("Start: " + new Date());
		if ("6x8".equals(puzzleType)) {
			SixByEightLPuzzle.makeRandomPuzzles(numberToGenerate, numberThreads);
		} else {
			EightByEightLPuzzle.makeRandomPuzzles(numberToGenerate, numberThreads);
		}
		// Some eight by eight puzzles (see generatedPuzzles for the entire list)
		// 3.31:  [o  o    o    o o    o o         o oo   o          o   oo   o  o ]
		// 3.54:  [ o   o o   o      oo          o  o   o  o   o o    o o     o   o]
		// 4.32:  [      o o o  o ooo             o    o  o   o  o o   o      oo   ]
		// 4.67:  [ o           o o   o o  o o   o   o     o  o      o o o     o  o]
		// 4.69:  [   o o o         ooo          o o         o o  o    o o o o  o  ]
		// 4.97:  [o          o  o        oooo  ooo  o         o      o      o o o ]
		// 5.04:  [o  o       o   o o  o       o oo o  o           o    o   o   o o]
		// 5.24:  [      o o  o o    o o    o        o o  oo   o  o     o   o  o   ]
		// 5.42:  [  o  o     o oo o o            ooo  o o           oo o      o   ]
		// 5.52:  [  o   o  o   o   o o     o       o    o o   o o   ooo         o ]
		// 6.40:  [     o  o  o   o o o   oo     o     o   o  o o          o     oo]
		// 6.54:  [  o          ooo        oo o o    o        o o   o o  ooo       ]
		// 7.67:  [o    o       o    o o   o  o  oo o oo           o        o  o  o]
		
		// Some six by eight puzzles (see generatedPuzzles for full list)
		// 2.46:  [       o o o   oo    o  o  o   oo         o    o]
		// 2.76:  [     oo o    o    o o   o o     o         o   oo]
		// 2.98:  [  o    o o o   o    o   o  o     o       o    oo]
		// 3.01:  [   o   o   o  o  o   o     o o       o  o  o  o ]
		// 3.06:  [o      o  o o  o     o oo         o     o  o  o ]
		// 3.46:  [o   o o     o   o o        o  o     o   o o  o  ]
		// 3.58:  [         o o o   o  o o o    o    oo   o       o]
		// 4.02:  [       o  oo  o o        o    oo o   o   o o    ]
		// 4.32:  [o  o          ooo o       o  oo      o    o  o  ]
		// 4.84:  [       ooooo        o o   o           o o   o  o]
		// 5.79:  [      o   oo     o  ooo o o            oo     o ]
		// 5.89:  [o         o oo         oo  ooo    o       o    o]
		// 6.41:  [o    o       o   o  o     o  ooo  o o    o      ]
		// 6.64:  [   o  o o o         oo  o     o  o  oo     o    ]

	}

	private static void solve(String[] args) {
		List<AbstractLPuzzle> puzzles = new ArrayList<AbstractLPuzzle>();
		for (String puzzle : args) {
			puzzle = puzzle.trim();
			if (!puzzle.startsWith("[")) {
				continue;
			}
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
		System.out.printf("Solving %d puzzles\n", puzzles.size());
		for (AbstractLPuzzle p : puzzles) {
			p.solve(SolvingVerbosity.SHOW_WORK);
		}
		Collections.sort(puzzles, new Comparator<AbstractLPuzzle>() {

			@Override
			public int compare(AbstractLPuzzle o1, AbstractLPuzzle o2) {
				return Double.compare(o1.getDifficulty(), o2.getDifficulty());
			}
		});
		// print out a summary of the puzzles
		for (AbstractLPuzzle p : puzzles) {
			System.out.printf("Difficulty %1.2f:  %s%n", p.getDifficulty(), p.export());
		}
	}

	private static void print(String[] args) {
		boolean printHints = false;
		boolean printSolutions = false;
		
		if (args.length > 1) {
			if ("--hints".equals(args[1])) {
				printHints = true;
			}else if ("--solutions".equals(args[1])) {
				printSolutions = true;
			} else if (args[1].startsWith("-")) {
				System.out.println("Unrecognized flag "+args[1]);
				printUsage();
				return;
			}
		}
		
		if (args.length > 2) {
			if ("--hints".equals(args[2])) {
				printHints = true;
			}else if ("--solutions".equals(args[2])) {
				printSolutions = true;
			} else if (args[2].startsWith("-")) {
				System.out.println("Unrecognized flag "+args[2]);
				printUsage();
				return;
			}
		}

		List<AbstractLPuzzle> puzzles = new ArrayList<AbstractLPuzzle>();
		for (String puzzle : args) {
			puzzle = puzzle.trim();
			if (!puzzle.startsWith("[")) {
				continue;
			}
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
		// Print them least difficult to most difficult.
		for (AbstractLPuzzle p : puzzles) {
			p.solve(SolvingVerbosity.SILENT);
		}
		Collections.sort(puzzles, new Comparator<AbstractLPuzzle>() {

			@Override
			public int compare(AbstractLPuzzle o1, AbstractLPuzzle o2) {
				return Double.compare(o1.getDifficulty(), o2.getDifficulty());
			}
		});
		LPuzzlePrinter.print4Puzzles(puzzles, printHints, printSolutions);
	}

}
