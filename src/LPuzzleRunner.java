import java.util.Date;



public class LPuzzleRunner {
    
    public static void main(String[] args) {
        
        TwelvePieceLPuzzle puzzle = new TwelvePieceLPuzzle("[  o o   o         o    oo     o  o   o    o  oo ]");
        puzzle.print();

        System.out.println(new Date());
        TwelvePieceLPuzzle.random(100, 3);
    }

    /*
     * http://www.wolframalpha.com/input/?i=(48+chose+12)+*+100%2F7400000
     * There are around 1_000_000 puzzles with solutions
     * 
     * 
     * Tried 1366566 puzzles to generate 20
     * Tried 7385106 puzzles to generate 100 (in about 75 minutes, 100k puzzles/ min with triple thread)
     * Tried 7267251 puzzles to generate 100 (similar to above)
     */
    
//     Some puzzles
//Difficulty 1.39:  [ o    o    o    o    o  o     oo   o      o  o o]
//Difficulty 2.08:  [  o o   o         o    oo     o  o   o    o  oo ]
//Difficulty 2.08:  [ o   o o     o         ooo  o         o o o    o]
//Difficulty 2.20:  [  o o o   o o    oo    o  o     o           o  o]
//Difficulty 2.48:  [o o   o o     o    o oo     o         o o  o    ]
//Difficulty 2.77:  [    oo  o o     o   o  o     o   ooo         o  ]
//Difficulty 2.77:  [   ooo  o           o       o o   ooo   o     o ]
//Difficulty 2.77:  [  o        ooo o         o  o     o o  oo      o]
//Difficulty 2.77:  [  o oo     o  o  o      o    oo   o     o    o  ]
//Difficulty 2.77:  [  ooo         o     ooo o      o    o   o o     ]
//Difficulty 2.77:  [ o o   oo         o  oo       o   o   ooo       ]
//Difficulty 2.89:  [ oo  o          o    o   o o   o        oo   o o]
//Difficulty 3.18:  [ o   o     oo  o        o  o   o    o    o o o  ]
//Difficulty 3.18:  [o        o oo  oo     oo  o           o  o  o   ]
//Difficulty 3.18:  [oo  o         o o        oo  oo     o  o      o ]
//Difficulty 3.47:  [         o   o oo    o o o o o     o  o   o     ]
//Difficulty 3.47:  [  o o o   o o    o     o o o       o   o     o  ]
//Difficulty 3.47:  [ o  o  o o    o         o o o    o  o o o       ]
//Difficulty 3.47:  [o      o  o o         o   o  oo  o o   o     o  ]
//Difficulty 3.47:  [o   o     o  o  o        o o  o    o     o   oo ]
//Difficulty 3.47:  [o  o  o   o   ooo           o           o  o  oo]
//Difficulty 3.58:  [     o   o oo    o           o oo  oo      o o  ]
//Difficulty 3.58:  [   o oo        oo   o   o   o      o     o  o  o]
//Difficulty 3.58:  [  oo   o    oo   o     oo  o               oo o ]
//Difficulty 3.87:  [      o oo  o           oo oo     o   o    o o  ]
//Difficulty 3.87:  [    o  oo  o   o    o    oo       o o  o       o]
//Difficulty 3.87:  [ o  o  o  o   o           o  o o o o o       o  ]
//Difficulty 3.87:  [ oo      o   o o   o o   o       o    o o   o   ]
//Difficulty 3.87:  [o  o  o  o o         o    o  o          o o   oo]
//Difficulty 4.16:  [      ooo o     o o  o     o  o  o o        o   ]
//Difficulty 4.16:  [    o    o  o  o       o o  o  o  o  o   o   o  ]
//Difficulty 4.16:  [    o o    o  ooo           o   oo  o  o o      ]
//Difficulty 4.16:  [  o   o     o   o  oo o        oo    o o o      ]
//Difficulty 4.16:  [  o o   oo           o o  o   o o o o       o   ]
//Difficulty 4.16:  [ o   o o  o     o  o   o   o  o           o  o o]
//Difficulty 4.16:  [o      o  oo    o   o o      o  o  o   o       o]
//Difficulty 4.28:  [     o   o o  o o o    o  o          oo  o  o   ]
//Difficulty 4.28:  [   o   ooo     o   o      o        o o   o o  o ]
//Difficulty 4.28:  [  o    o      o   o  o o o o     o    o  o    o ]
//Difficulty 4.28:  [  o o    o o o o                 oo o   o   o o ]
//Difficulty 4.28:  [  oo o o    o        o  o          o o  o o    o]
//Difficulty 4.28:  [ o     o      o     o o  oo      o       o o  oo]
//Difficulty 4.28:  [ o  o o        o o   o  o        o  oo     o o  ]
//Difficulty 4.28:  [ o o o      o      o      o    oo       o  o oo ]
//Difficulty 4.28:  [o   o o         o  o o o   o    o       o  o   o]
//Difficulty 4.56:  [       ooo       o o       o oooo         o  o  ]
//Difficulty 4.56:  [   o o        o o o  o  o      o o      o  o  o ]
//Difficulty 4.56:  [ o     o o    o o o        o   o  o o        oo ]
//Difficulty 4.56:  [ o     oo  o o  o   o          o   o  o   o   o ]
//Difficulty 4.56:  [o       o o  o o   o      oo  o o    o       o  ]
//Difficulty 4.56:  [o     o   o o  o     o   o        o o  o   o   o]
//Difficulty 4.56:  [o o  o      o    o  o       o  oo o   o      o  ]
//Difficulty 4.56:  [oo  o        oo   o        o  o  o    o o   o   ]
//Difficulty 4.68:  [   o   o o    o  o      o  o       oo o  o     o]
//Difficulty 4.68:  [  o       o   ooo      o  o o       oo  o o     ]
//Difficulty 4.68:  [  oo  o        o   o    o      oo   o   o   o o ]
//Difficulty 4.68:  [ o       o     o oo o        o oo    o      o  o]
//Difficulty 4.68:  [ o    o    oo        o      o o oo        o oo  ]
//Difficulty 4.68:  [ o   o   o    oo o         o   o    o  o o  o   ]
//Difficulty 4.79:  [   o     o   o   o oo  oo          o   o o    o ]
//Difficulty 4.85:  [      o o o    oo       o  o oo   o       o   o ]
//Difficulty 4.85:  [ o o   o       o    oo    oo    o   o    o    o ]
//Difficulty 4.85:  [o     o       o     o    oo  o        o o o o o ]
//Difficulty 4.85:  [o   o o       o  o    o   o o    o o   o  o     ]
//Difficulty 4.97:  [     o   o oo     o ooo  o              o   o o ]
//Difficulty 4.97:  [   oo        o o o o o            o     o o  o o]
//Difficulty 4.97:  [   ooo    o         o o o   o     o  o o  o     ]
//Difficulty 4.97:  [  o    o  o          o  oo  o o   o   o o     o ]
//Difficulty 4.97:  [  o  o         o  o o   oo o  o   o        o  o ]
//Difficulty 4.97:  [ o       o  o  oo        oo  o o          o o o ]
//Difficulty 4.97:  [ o   oo           o    oo    oo o        o o  o ]
//Difficulty 4.97:  [ o oo  o     o    o    o o o        o      o o  ]
//Difficulty 4.97:  [oo    o       o  o  o   oo o          o     o o ]
//Difficulty 5.08:  [  o    o   o  o o  o       oo  oo         o   o ]
//Difficulty 5.26:  [       oo   ooo         o  o      o  o    oo  o ]
//Difficulty 5.26:  [      o   o     oo oo  o  o o o           o  o  ]
//Difficulty 5.26:  [      o o o     o   o    o   o o     o  o  o   o]
//Difficulty 5.26:  [   o   o    o o  ooo           o o  o   o      o]
//Difficulty 5.26:  [ o   o oo  o       o o     o         o   o oo   ]
//Difficulty 5.26:  [o          o o  o o  o o          o   o o  oo   ]
//Difficulty 5.26:  [o          oo        ooo ooo           o oo     ]
//Difficulty 5.26:  [o   o     o  oo  o o   o         oo       o   o ]
//Difficulty 5.38:  [         o  o o   o o  oo      o o  o  o o      ]
//Difficulty 5.48:  [o   o     o         ooo    o   ooo   oo         ]
//Difficulty 5.48:  [o  o  o           o    o oo   o o         o oo  ]
//Difficulty 5.55:  [   o   o       oo oo         oo    o   o o o    ]
//Difficulty 5.55:  [   o oo         ooo         o o      o    ooo   ]
//Difficulty 5.55:  [  oo  o     o   o     oo oo         o     o   o ]
//Difficulty 5.55:  [ o       o     o   oo oo         oo      o o o  ]
//Difficulty 5.55:  [ o o  o  o  o         o   o  o    o oo  o       ]
//Difficulty 5.66:  [  o o     o   o o          o o  o  o  o      oo ]
//Difficulty 5.66:  [  o o     o o       o o    o   oo    o o o      ]
//Difficulty 5.66:  [ o    o       o    o oo  o   o  o oo          o ]
//Difficulty 5.66:  [o      o  o o     o      o  o o   o   o   o  o  ]
//Difficulty 5.66:  [o  o        o   oo   o o    o     o  o      o  o]
//Difficulty 5.66:  [o o   o       o   o     o  o    o   o o  o     o]
//Difficulty 5.66:  [oo   o         o o  oo  o         o o      o  o ]
//Difficulty 5.77:  [ o o  o  o          o  o    o o  o o      o  o  ]
//Difficulty 5.78:  [  oo           oooo         o  o       oo  o  o ]
//Difficulty 5.95:  [       oo  oo o           o  o   o    o  o  o o ]
//Difficulty 5.95:  [      o o  o  o   o   o  o  o       o o o      o]
//Difficulty 5.95:  [    o      o o oo  o o   o      oo  o         o ]
//Difficulty 5.95:  [    o o    o     o       o o   o   oooo   o     ]
//Difficulty 5.95:  [  o        oo  ooo           o         oo o o  o]
//Difficulty 5.95:  [  o  o o          o o    oo  oo   o       o   o ]
//Difficulty 5.95:  [  o  oo         oo  o       o o     o   o o  o  ]
//Difficulty 5.95:  [  o o      o o oo          o  o    o     o o   o]
//Difficulty 5.95:  [ o         oo    o     o  ooo o          oo    o]
//Difficulty 5.95:  [ o     o      o  o  o   o o  o o          o o  o]
//Difficulty 5.95:  [ o    o  o      o  oo  o o  o o   o  o          ]
//Difficulty 5.95:  [ o    o  oo    o     o     o  oo        o o  o  ]
//Difficulty 5.95:  [ o  o  o    o o  oo                   o o o o  o]
//Difficulty 5.95:  [ o o o   o  o             o o  o o     oo     o ]
//Difficulty 5.95:  [o      o  o o     oo  o           o   o   o oo  ]
//Difficulty 5.95:  [o     o   o   o   ooo            o    oo o  o   ]
//Difficulty 5.95:  [o    o   o   o o        o oo  o            oo o ]
//Difficulty 5.95:  [o o         oo    o    oo     o   o  o o   o    ]
//Difficulty 6.07:  [     o  o o o        o      o o  oo     o  o  o ]
//Difficulty 6.07:  [  o       o o  o     o  o o    o  o   o     o  o]
//Difficulty 6.07:  [  o   o oo  o     o    o       o    oo   o o    ]
//Difficulty 6.07:  [ o   oo     o o     o    o       o  o  o o   o  ]
//Difficulty 6.07:  [ oo   o   o   o        oo o o      o o      o   ]
//Difficulty 6.07:  [o   o o   o   o         o o  o     oo o       o ]
//Difficulty 6.07:  [o o  o      o        o   o   o  o  oo o      o  ]
//Difficulty 6.17:  [  o   o o           o   o o o o  o  o o      o  ]
//Difficulty 6.17:  [  oo   o o  o        o   o  o  o        o   o o ]
//Difficulty 6.19:  [o  o  o     o    o   o oo   o  o     o     o    ]
//Difficulty 6.24:  [o            o oo  oo  o  o   o  o  o   o       ]
//Difficulty 6.24:  [o  o      o           ooo o  o    o       o o o ]
//Difficulty 6.36:  [         o o   oo   oo        o   o   ooo  o    ]
//Difficulty 6.36:  [   o       o  ooo o     o   o  o        o   oo  ]
//Difficulty 6.36:  [   ooo  oo          o o    o  o o         o   o ]
//Difficulty 6.36:  [ o    o  o     o   oo  oo o         o     o  o  ]
//Difficulty 6.36:  [ o   o          o o  ooooo o         o     o    ]
//Difficulty 6.36:  [ o  oo          o  o o     o   o o  o   o      o]
//Difficulty 6.36:  [o   o        o o   o    o  o o    o        oo o ]
//Difficulty 6.36:  [o   o o   o     o o    o   o  o          o  oo  ]
//Difficulty 6.36:  [o   o o  o          o   o o o      o  oo    o   ]
//Difficulty 6.46:  [  o o    o    o     o o  o  o o      o    o o   ]
//Difficulty 6.46:  [ o o  o        o   o       o  oooo        o  o  ]
//Difficulty 6.47:  [    o   o o  o  o oo   o         o    o     o  o]
//Difficulty 6.47:  [   o o  o  o o      o  o o    o          o oo   ]
//Difficulty 6.47:  [o   o o       o  o    o  o o     o     oo   o   ]
//Difficulty 6.47:  [o o       o  o      o    o   ooo  o o          o]
//Difficulty 6.47:  [oo     o           oo  o     o  oo       o  o  o]
//Difficulty 6.58:  [     o    ooo o   o   o oo         o       o   o]
//Difficulty 6.58:  [     o  o    o     o    o o o  o  o   o  o   o  ]
//Difficulty 6.58:  [   o  o o o         oo   o  o  o   o o    o     ]
//Difficulty 6.58:  [ o         oo      o   o   o oo oo    o      o  ]
//Difficulty 6.64:  [     o    o   oo   o    o      o oo o      o o  ]
//Difficulty 6.64:  [     oo   o o     o     o o   o o         o o  o]
//Difficulty 6.64:  [    oo    oo     o     o   o    oo  oo         o]
//Difficulty 6.64:  [  oo o o          o     o o  o  o  oo o         ]
//Difficulty 6.64:  [ o   o   o  o       o o oo  o            o o  o ]
//Difficulty 6.64:  [ o oo  o        o o   o   o             o  o  oo]
//Difficulty 6.64:  [o    o o         oo o      o      oooo o        ]
//Difficulty 6.76:  [     o  oo   o  o      o   o o       oo o   o   ]
//Difficulty 6.76:  [  o     oo          o  o oo  oo          o  o o ]
//Difficulty 6.76:  [  oo o       o  oo  o o              ooo  o     ]
//Difficulty 6.76:  [ o    o  o  o          o  o o o  o  o   o    o  ]
//Difficulty 6.76:  [ o  oo  o          o  o   o o    o       o o  o ]
//Difficulty 6.76:  [o o   o   o o         o   o     oo  oo        o ]
//Difficulty 6.76:  [oo           o    o    oo  o oo       o o   o   ]
//Difficulty 6.87:  [ o o o      o   o     oo  o         o  o  oo    ]
//Difficulty 6.88:  [   o  o  oo oo        o  o        o       o  o o]
//Difficulty 6.93:  [       ooo  o o   o o         o o    o    o o   ]
//Difficulty 6.93:  [     o   o  o  oo o         o o    ooo   o      ]
//Difficulty 6.93:  [  o   o        ooo        o ooo         oo    o ]
//Difficulty 6.98:  [o   o        o o   oo   o         o  ooo o      ]
//Difficulty 7.05:  [       o  oo  o o        o    oo o   o   o o    ]
//Difficulty 7.05:  [   o   o o    o o   o    o o o           o  oo  ]
//Difficulty 7.05:  [ o      o     oo   o  o     oo oo  o     o      ]
//Difficulty 7.05:  [ o o           o o  oo    o   o   o   ooo       ]
//Difficulty 7.05:  [o    o o o        o  oo o   o      o   o    o   ]
//Difficulty 7.09:  [      o   o o   oo  o       o o     o   o  o o  ]
//Difficulty 7.15:  [o    o     oo o    o     o o   oo    o      o   ]
//Difficulty 7.15:  [o  o  o             ooo    o    oo        o  o o]
//Difficulty 7.15:  [oo   o         o  o o      o   ooo  o  o        ]
//Difficulty 7.17:  [     o     o o  o oo  o               o oo  oo  ]
//Difficulty 7.17:  [   oo        o   oo    oo    o  oo    o       o ]
//Difficulty 7.17:  [  o  o o   o           ooo o  o         o   o o ]
//Difficulty 7.17:  [ o        o o  oo  o  o o          o  o    o o  ]
//Difficulty 7.27:  [ o o    o   o  o   o       o     o  o  o o    o ]
//Difficulty 7.28:  [o  oo o              o   o     o  o   o  oo   o ]
//Difficulty 7.34:  [      o   o    o o    o  o oo o  o o         o  ]
//Difficulty 7.38:  [  o  oo     o    o    o o o       o o     o  o  ]
//Difficulty 7.39:  [ o  o   o    o    o    oo  o   o o   o       o  ]
//Difficulty 7.45:  [       oo   o  o    o    oo o   o o    o       o]
//Difficulty 7.45:  [    o  oo   o o   o     o  o     o    o o     o ]
//Difficulty 7.45:  [    o o o          o o   o oo     o   o    o   o]
//Difficulty 7.45:  [  o o  ooo         o o        oo        oo   o  ]
//Difficulty 7.45:  [ o o   o      o   o         o  o oo o   o     o ]
//Difficulty 7.45:  [o          o    o   ooo        oo         ooo o ]
//Difficulty 7.45:  [o   o o  o    o         o oo   o     o oo       ]
//Difficulty 7.45:  [oo  o  o         o   o    oo    o     oo     o  ]
//Difficulty 7.56:  [  o        oo o o    oo o     o    oo    o      ]
//Difficulty 7.56:  [ o  o      o   o   o o        o oo     o  o   o ]
//Difficulty 7.74:  [ o   o      o oo   o     o  o         o   oo  o ]
//Difficulty 7.74:  [o      o   o       o  o o o oo          o  o o  ]
//Difficulty 7.86:  [  o  o      o  o   o     o o  o  o    oo o      ]
//Difficulty 7.86:  [o    oo     o o  o  o       o    o        oo  o ]
//Difficulty 7.97:  [        oo   o    ooo oo        o  o       o o  ]
//Difficulty 7.97:  [   oo o   o     o     o   oo   o    o o o       ]
//Difficulty 8.08:  [ o    o o   o    o       o   o  o o   o     o o ]
//Difficulty 8.15:  [        o  o o o o  o         oo o  o     o o   ]
//Difficulty 8.15:  [     o   oo o o     o o o    o    o       o    o]
//Difficulty 8.15:  [   o  o          o  o o o  o     o  o o  o     o]
//Difficulty 8.15:  [   o  o        o o oo   o              o  ooo o ]
//Difficulty 8.15:  [   oo o         o   o  o   o      o     o   oo o]
//Difficulty 8.15:  [o     o  o o       o o o        ooo    o      o ]
//Difficulty 8.15:  [o   o  o        o      o  o  o      o o o  o   o]
//Difficulty 8.15:  [o  o     o     o   o     o o  oo o  o          o]
//Difficulty 8.15:  [o  oo              oo o   o     o    o   o   o o]
//Difficulty 8.25:  [        o     o    ooo oo o       oo   o      o ]
//Difficulty 8.25:  [ o     o  oo  o      o     o    ooo   o        o]
//Difficulty 8.27:  [ o      o oo  o        o        o o oo o      o ]
//Difficulty 8.27:  [o   o o  o        o     oo  o         o   o  oo ]
//Difficulty 8.27:  [o o   o       o        o oo o o          o oo   ]
//Difficulty 8.55:  [          ooo   o      o oo  oo  o o          o ]
//Difficulty 8.55:  [    o    o o o    o  o  o    o    o o o      o  ]
//Difficulty 8.55:  [o     o   o      o ooo   o     o  o   o      o  ]
//Difficulty 8.59:  [        o o oo     o  o  o  o  oo     o     o   ]
//Difficulty 8.67:  [       ooo   oo o         ooo       o o o       ]
//Difficulty 8.78:  [ o   o   o          o o    o o o o o o    o     ]
//Difficulty 8.84:  [  o o    o  o         o  o     oo  o  o     o o ]
//Difficulty 9.01:  [   o       o   oo o o  o     o    o  oo   o     ]
//Difficulty 9.25:  [o   o o           oo   oo     o   oo  o     o   ]
//Difficulty 9.29:  [     o      o    oo  oo  o         o o o o  o   ]
//Difficulty 9.36:  [          o   oo   o    o  o o o o o  o  o      ]
//Difficulty 9.46:  [o  o o       o   o   o o o   o  o  o  o         ]
//Difficulty 9.57:  [  o    o    o   oooo           oo    o o o      ]
//Difficulty 9.76:  [  o  o          o o   oo   o o    o       o o  o]
//Difficulty 9.82:  [  o         oo  o o   o    o    o   o o   o    o]
//Difficulty 10.06:  [  o o o    o           oo o   o    oo o   o     ]
//Difficulty 10.16:  [o    o o          oo    o o o     o o  o      o ]
//Difficulty 10.35:  [       o ooo  o       o o         o o o    o   o]
//Difficulty 10.35:  [   o   o  o oo  o        oo    o    o      o  o ]
//Difficulty 11.09:  [   o   oo o   o    o     oo        o  oo  o     ]


     
}
