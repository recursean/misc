package DealOrNoDeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;

public class DealOrNoDeal {
    Board board;
    Banker banker;
    Player player;

    int round;

    public DealOrNoDeal() {
        board = new Board();
        banker = new Banker();
        player = new Player();

        round = 1;

        gameLoop();
    }

    private void gameLoop() {
        boolean noDeal = true;
        int numChoices = 6;
        int offer;
        boolean goodChoice;

        System.out.println("********************************");
        System.out.println("* Welcome to Deal Or No Deal!! *");
        System.out.println("********************************");

        System.out.println();

        board.printRewards();
        board.printCases();
        
        goodChoice = false;
        while(!goodChoice) {
            goodChoice = board.chooseInitialCase(player.askForInitialCase());
        }

        System.out.println("You have chosen case number " + player.initialCase + ". Good luck.");
        System.out.println();

        round = 1;

        while(noDeal) {
            if(round > 1) {
                numChoices = 5;
            }

            System.out.println("----- ROUND " + round + " -----");
            System.out.println("Choose " + numChoices + " cases");
            System.out.println();

            for(; numChoices > 0; numChoices--) {
                System.out.println(numChoices + " cases left to choose this round.");
                System.out.println();

                try {
                    Thread.sleep(1500);
                }
                catch(InterruptedException e) {
                    System.exit(1);
                }

                board.printRewards();
                board.printCases();

                goodChoice = false;
                while(!goodChoice) {
                    goodChoice = board.chooseCase(player.askForCaseNumber());
                }

                try {
                    Thread.sleep(3000);
                }
                catch(InterruptedException e) {
                    System.exit(1);
                }
            }

            System.out.print("The banker is calling");

            for(int i = 0; i < 3; i++) {
                System.out.print(".");
                try {
                    Thread.sleep(750);
                }
                catch(InterruptedException e) {
                    System.exit(1);
                }
            }

            System.out.println();

            offer = banker.createOffer(board);

            System.out.println("The banker is offering: $" + offer);
            System.out.println();

            noDeal = !player.dealOrNoDeal();

            if(noDeal) {
                System.out.println();
                System.out.println("NO DEAL!");
                System.out.println();

                round++;
            }
            
            else {
                if(board.casesLeft > 1) {
                    System.out.println("DEAL!");
                    System.out.println();
                    System.out.println("You accepted the offer of $" + offer);
                }
                
                System.out.println();
                System.out.print("Let's open your case");

                for(int i = 0; i < 3; i++) {
                    System.out.print(".");
                    try {
                        Thread.sleep(750);
                    }
                    catch(InterruptedException e) {
                        System.exit(1);
                    }
                }

                System.out.println();

                System.out.println("Your case contained: " + board.initialCaseValue);
            }
        }
    }

    class Board {
        private List<Double> cases;
        private HashMap<Double, Boolean> rewardsMap;

        final Double[] rewards = {
            0.01, 1.0, 5.0, 10.0, 25.0, 50.0, 75.0,
            100.0, 200.0, 300.0, 400.0, 500.0, 750.0,
            1000.0, 5000.0, 
            10000.0, 25000.0, 50000.0, 75000.0,
            100000.0, 200000.0, 300000.0, 400000.0, 500000.0, 750000.0,
            1000000.0
        };

        int casesLeft;
        double initialCaseValue;

        public Board() {
            initCases();
            casesLeft = cases.size();

            rewardsMap = new HashMap<Double, Boolean>();
            initRewardsLeft();
        } 

        private void initCases() {
            cases = new ArrayList<Double>();

            for(Double reward: rewards) {
                cases.add(reward);
            }
        
            Collections.shuffle(cases);
        }

        private void initRewardsLeft() {
            for(Double val: rewards) {
                rewardsMap.put(val, true);
            }
        }

        private boolean chooseInitialCase(int num) {
            if(num <= cases.size()) {
                initialCaseValue = cases.get(num - 1);
                cases.set(num - 1, 0.0);
                casesLeft--;

                return true;
            }

            else {
                System.out.println("Invalid case number.");
                System.out.println();

                return false;
            }
        }

        private boolean chooseCase(int num) {
            if(num <= cases.size() && cases.get(num - 1) != 0.0) {
                System.out.println();
                System.out.print("Opening case " + num);
                
                for(int i = 0; i < 3; i++) {
                    System.out.print(".");
                    try {
                        Thread.sleep(750);
                    }
                    catch(InterruptedException e) {
                        System.exit(1);
                    }
                }

                System.out.println();

                System.out.println("$" + cases.get(num - 1));
                System.out.println();

                rewardsMap.replace(cases.get(num - 1), false);

                cases.set(num - 1, 0.0);
                casesLeft--;

                return true;
            }

            else {
                System.out.println("Invalid case number.");
                System.out.println();

                return false;
            }
        }

        private void printRewards() {
            for(int i = 0; i < rewards.length / 2; i++) {
                if(rewardsMap.get(rewards[i])) {
                    System.out.print("$" + rewards[i]);
                }

                else {
                    System.out.print("X");
                }

                if(rewardsMap.get(rewards[i + rewards.length / 2])) {
                    System.out.println("\t\t$" + rewards[i + rewards.length / 2]);
                }

                else {
                    System.out.println("\t\tX");
                }
            }

            System.out.println();
        }

        private void printCases() {
            for(int i = cases.size() - 5; i <= cases.size(); i++) {
                if(cases.get(i - 1) == 0.0) {
                    System.out.print(" X");
                }
                else  {
                    System.out.print(" " + i);
                }
            }

            System.out.println();

            for(int i = cases.size() - 6; i > 0; i = i - 5) {
                for(int j = i - 4; j <= i; j++) {
                    if(cases.get(j) == 0.0) {
                        System.out.print(" X");
                    }
                    else  {
                        System.out.print(" " + j);
                    }
                }

                System.out.println();
            }

            System.out.println();
        }
    }

    class Banker {
        public Banker() {

        }

        private int createOffer(Board board) {
            double offer = 0;

            for(Double value: board.cases) {
                offer += value; 
            }

            return (int)offer / board.casesLeft;
        }
    }

    class Player {
        int initialCase;
        Scanner scanner;

        public Player() {
            scanner = new Scanner(System.in);
        }

        private int askForInitialCase() {
            System.out.println("Pick an initial case number: ");

            initialCase = scanner.nextInt();

            return initialCase;
        }

        private int askForCaseNumber() {
            System.out.println("Pick a case number: ");

            return scanner.nextInt();
        }

        private boolean dealOrNoDeal() {
            System.out.println("Deal or no deal?");

            boolean ret = false;

            while(true) {
                scanner.nextLine();
                String decision = scanner.nextLine();
    
                if(decision.toLowerCase().equals("deal")) {
                    ret = true;

                    break;
                }
    
                else if(decision.toLowerCase().equals("no deal")){
                    ret = false;

                    break;
                }
    
                else {
                    System.out.println("Try again.");
                }
            }

            return ret;
        }
    }
}