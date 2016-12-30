import java.util.Arrays;
import java.util.Date;

public class LPuzzleGenerator {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		String puzzleType = null;
		int numberToGenerate = 0;
		int numberThreads = 0;
		try {
			puzzleType = args[0];
			String numberToGenerateString = args[1];
			String numberThreadsString = args[2];
			if (!("6x8".equals(puzzleType) || "8x8".equals(puzzleType))) {
				System.out.println("Type must be 6x8 or 8x8");
				throw new Exception();
			}
			numberToGenerate = Integer.parseInt(numberToGenerateString);
			numberThreads = Integer.parseInt(numberThreadsString);
			if (numberThreads <= 0 || numberToGenerate <= 0) {
				return;
			}

		} catch (Exception e) {
			System.out.println("Usage java -jar generator.jar puzzleType numberToGenerate threads");
			System.out.println("Sample usage: java -jar generator 6x8 200 4");
			return;
		}

		System.out.println("Start: " + new Date());
		if ("6x8".equals(puzzleType)) {
			SixByEightLPuzzle.random2(numberToGenerate, numberThreads);
		} else {
			EightByEightLPuzzle.random(numberToGenerate, numberThreads);
		}
	}

	/*
	 * Assuming the density of these puzzles is 1 in 4.7 million, there are
	 * around 100 million 8x8 puzzles
	 * 
	 * Tried 106_027_608 puzzles to generate 21 in 14.5 hours (RTA, using 3
	 * cores on local machine) ( 120k /min) Tried 462_888_716 puzzles to
	 * generate 100 in 14.75 hours (RTA using 16 cores on remote machine) (520k
	 * /min)
	 */

	// Some eight by eight puzzles (see generatedPuzzles for the entire list)
	// 2.77 [ o o o o o o oo o o o o o o oo]
	// 3.04 [ o o o o oo o o o o o o o o o o]
	// 4.23 [o o o o o o o o oo o o oo o o ]
	// 5.00 [ o o o ooo o o o o o o o o o o ]
	// 6.08 [ o o o o o o o o o o o o o o o o]
	// 7.05 [ o o o o ooo o o o o o o o oo ]
	// 8.15 [o o o oooo ooo o o o o o o ]
	// 9.13 [o o o o o o o oo o o o o o o o]
	// 10.35 [ o ooo oo o o o o o o o ooo ]
	// 11.33 [ o o o o o o o o o oo o o o o o ]
	// 12.42 [ o o o oo o o ooo o o oo o o ]
	// 13.92 [ o o o o o o o o o o o o ooo o ]
	// 14.73 [o o o o o o o oo o oo o o o o]

	/*
	 * http://www.wolframalpha.com/input/?i=(48+chose+12)+*+100%2F7400000 There
	 * are around 1_000_000 6x8 puzzles with solutions
	 * 
	 * 
	 * Tried 1366566 puzzles to generate 20 Tried 7385106 puzzles to generate
	 * 100 (in about 75 minutes, 100k puzzles/ min with triple thread) Tried
	 * 7267251 puzzles to generate 100 (similar to above) Tried 14598483 puzzles
	 * to generate 200 (in about 150 minutes, same rate as above) Tried 40690768
	 * puzzles to generate 600 (in about 6:40 [400 min], same rate as above)
	 * Tried 70406134 puzzles to generate 1000 (in about 1:35 [95 min], using 16
	 * cores, 780k / minute)
	 */

	// Some six by eight puzzles (see generatedPuzzles for full list)
	// 0.69 [ oo o o o o o o o o oo]
	// 1.79 [ o o o oo o o o oo o o]
	// 2.08 [o o o o o o oo o o o o ]
	// 3.18 [ o o o o o o o o oo o o]
	// 4.09 [ o o o o o o o o o o oo]
	// 5.08 [o o o o o o o o o o o o ]
	// 6.07 [ o o o o o o o o o o o o ]
	// 7.05 [ o oo o o o oo o o o o ]
	// 8.03 [o o ooo o o oo o o o ]
	// 9.01 [ ooooo o o o o o o o]
	// 10.04 [ o oo o ooo o o oo o ]
	// 11.04 [o o oo oo ooo o o o]
	// 11.84 [o o o o o o ooo o o o ]
	// 14.14 [ o o o o oo o o o oo o ]

}
