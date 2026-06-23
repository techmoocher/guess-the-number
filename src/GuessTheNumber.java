import java.util.Scanner;

public class GuessTheNumber {
    private static final String RESET  = "\u001B[0m";
    private static final String PINK   = "\u001B[35m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";

    private static int      target;
    private static byte     level = 1;
    private static byte     chances = 10;

    private static int[]    bestAttempt = {-1, -1, -1};
    private static double[] bestTime = {-1.0, -1.0, -1.0};

    private static final String[] messages = 
    {
        "Loading your guess... (ᐢ..ᐢ )", "Checking the answer... (ᐢ..ᐢ )",
        "Verifying your guess... (ᐢ..ᐢ )", "Analyzing your guess... (ᐢ..ᐢ )",
        "Assessing your guess... (ᐢ..ᐢ )", "Evaluating your guess... (ᐢ..ᐢ )",
        "Finalizing the result... (ᐢ..ᐢ )", "Calculating the result... (ᐢ..ᐢ )",
        "Determining the outcome... (ᐢ..ᐢ )", "Processing your request... (ᐢ..ᐢ )"
    };

    private static boolean isFirstPlay = true;

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public static void foxSpeak(String text, int delay, int downtime, boolean lineBreak) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            switch (c) {
                case ','    -> sleep(delay + 300);
                case '.'    -> sleep(delay + 500);
                case ('?')  -> sleep(delay + 500);
                default     -> sleep(delay);
            }
        }

        if (lineBreak) { System.out.println(); }

        sleep(downtime);
    }

    public static void levelSelection(Scanner sc) {
        if (!isFirstPlay) {
            sc.nextLine();

            foxSpeak("So now you know the game, do you wish to play again with the same level?\n", 50, 1000, false);
            System.out.print(">>> Enter your choice [Y/n]: ");

            while (true) {
                String inp = sc.nextLine().trim();

                if (inp.isEmpty() || inp.equalsIgnoreCase("y")) {
                    switch (level) {
                        case 1 -> chances = 10;
                        case 2 -> chances = 5;
                        case 3 -> chances = 3;
                    }
                    return;
                }
                else if (inp.equalsIgnoreCase("n")) {
                    break;
                }
                else {
                    System.out.println(RED + "INVALID INPUT!" + RESET +
                                        "\n\nPlease select a valid option.");
                    System.out.print(">>> Enter your choice [Y/n]: ");
                }
            }
        }

        System.out.println("Select a level to continue:");
        System.out.println("[1] Easy    (10 chances)");
        System.out.println("[2] Medium  (5 chances)");
        System.out.println("[3] Hard    (3 chances)");
        System.out.print(">>> Enter your choice: ");

        while (true) {
            while (!sc.hasNextInt()) {
                System.out.println(RED + "INVALID INPUT!" + RESET +
                                    "\n\nPlease select a valid option (1-3).");
                System.out.print(">>> Enter your choice: ");
                sc.next();
            }
            int inp = sc.nextInt();
            if (inp >= 1 && inp <= 3) {
                level = (byte) inp;
                break;
            }
            else {
                System.out.println(RED + "OUT OF BOUNDS!" + RESET +
                                    "\n\nPlease select a valid option (1-3).");
                System.out.print(">>> Enter your choice: ");
            }
        }

        switch (level) {
            case 1 -> {
                foxSpeak(PINK + "Scared!? Aren't you ꒰ᐢ. . ᐢ꒱" + RESET, 50, 1000, true);
                chances = 10;
            }
            case 2 -> {
                foxSpeak(PINK + "Ohhh...Bold, yet not too close ( ˙꒳​˙ )" + RESET, 60, 1000, true);
                chances = 5;
            }
            case 3 -> {
                foxSpeak(PINK + "W-what!? You can't beat me anyways ( ｡•̀ᴗ-)✧" + RESET, 70, 1000, true);
                chances = 3;
            }
        }
    }

    public static void game(Scanner sc) {
        int attempts = 0;
        long startTime, endTime;

        startTime = System.nanoTime();
        while (chances > 0) {
            System.out.print(">>> Enter your guess (" + chances + " left): ");
            boolean ok = false;
            int guess = -1;
            
            while (!ok) {
                while (!sc.hasNextInt()) {
                    System.out.println(RED + "INVALID INPUT!!!" + RESET +
                                        "\n\nEnter a valid non-negative integer (1-100).");
                    System.out.print(">>> Enter your guess: ");
                    sc.next();
                }
                int inp = sc.nextInt();
                if (inp >= 1 && inp <= 100) {
                    guess = inp;
                    ok = true;
                }
            }
            
            attempts++;
            chances--;

            int downtime = (int)(Math.random() * (2000 - 800 + 1)) + 800;   
            int rnd = (int)(Math.random() * 10);
            System.out.println(CYAN + messages[rnd] + RESET);
            sleep(downtime);

            if (guess < target) {
                System.out.println(RED + "INCORRECT!" + RESET +
                                    " Don't underestimate me ;)\n");
            }
            else if (guess > target) {
                System.out.println(RED + "INCORRECT!" + RESET +
                                    " You're overcomplicating things >:(\n");
            }
            else {
                endTime = System.nanoTime();
                double timeTaken = (endTime - startTime) / 1_000_000_000.0;
                
                System.out.println(GREEN + "CORRECT! You nailed it in " + attempts + " attempt(s)." + RESET);
                System.out.printf(GREEN + "It took you %.2f seconds!!!%n" + RESET, timeTaken);

                int idx = level - 1;
                if (bestAttempt[idx] == -1 || attempts < bestAttempt[idx]) {
                    bestAttempt[idx] = attempts;
                }
                if (bestTime[idx] == -1.0 || timeTaken < bestTime[idx]) {
                    bestTime[idx] = timeTaken;
                }
                
                foxSpeak(PINK + "Uhhh...That's pretty impressive, but NOT that close!!! ꒰ ˙꒳​˙ ꒱\n" + RESET, 80, 1000, true);
                break;
            }
        }

        if (isFirstPlay) {
            isFirstPlay = false;
        }

        if (chances < 0) {
            System.out.println(RED + "GAME OVER!!!" + RESET + "\nYou ran out of chances.");
            sleep(500);
            System.out.println("The correct answer was " + YELLOW + target + RESET + ".\n");
        }
    }

    public static void main(String[] args) {
        boolean repeat;

        try (Scanner sc = new Scanner(System.in)) {
            do {
                repeat = false;

                foxSpeak(PINK + "HELLO..." + RESET, 100, 500, true);
                foxSpeak(PINK + "I'm Karu, a naughty fox /\\. ./\\\n" + RESET, 80, 800, true);
                foxSpeak("Welcome to ", 50, 500, false);
                foxSpeak("GUESS THE NUMBER!!! (fox limited edition)\n", 80, 1000, true);

                foxSpeak("The rules are easyyyy ~~~\n", 80, 500, false);
                foxSpeak("I'm thinking of a number between 1 and 100, and you have to get it right.\n", 50, 1000, true);
                
                target = (int)(Math.random() * 100) + 1;

                levelSelection(sc);

                System.out.println("\nYou're all set. Let's start the game!\n");

                game(sc);

                while (true) {
                    System.out.println("Anyways, what do you wanna do now?");
                    System.out.println("1. Play again!!!");
                    System.out.println("2. View time records.");
                    System.out.println("3. View attempt count records.");
                    System.out.println("4. Quit");
                    System.out.print(">>> Enter your choice: ");

                    byte cmd = -1;
                    while (true) {
                        while (!sc.hasNextInt()) {
                            System.out.println(RED + "INVALID INPUT!" + RESET +
                                                "\nPlease select a valid option (1-4).");
                            System.out.print(">>> Enter your choice: ");
                            sc.next();
                        }
                        int inp = sc.nextInt();
                        if (inp >= 1 && inp <= 4) {
                            cmd = (byte) inp;
                            break;
                        }
                        else {
                            System.out.println(RED + "OUT OF BOUNDS!" + RESET +
                                                "\n\nPlease select a valid option (1-3).");
                            System.out.print(">>> Enter your choice: ");
                        }
                    }

                    switch (cmd) {
                        case 1 -> repeat = true;
                        case 2 -> {
                            System.out.println(YELLOW + "\n--- Your Personal Time Records ---" + RESET);
                            System.out.printf("1. [EASY]   %s%n", bestTime[0] == -1.0 ? "No record yet" : bestTime[0] + "s");
                            System.out.printf("2. [MEDIUM] %s%n", bestTime[1] == -1.0 ? "No record yet" : bestTime[1] + "s");
                            System.out.printf("3. [HARD]   %s%n\n", bestTime[2] == -1.0 ? "No record yet" : bestTime[2] + "s");
                            sleep(3500);
                        }
                        case 3 -> {
                            System.out.println(YELLOW + "\n--- Your Personal Attempt Records ---" + RESET);
                            System.out.printf("1. [EASY]   %s%n", bestAttempt[0] == -1 ? "No record yet" : bestAttempt[0] + " attempts");
                            System.out.printf("2. [MEDIUM] %s%n", bestAttempt[1] == -1 ? "No record yet" : bestAttempt[1] + " attempts");
                            System.out.printf("3. [HARD]   %s%n\n", bestAttempt[2] == -1 ? "No record yet" : bestAttempt[2] + " attempts");
                            sleep(3500);
                        }
                        default -> foxSpeak(PINK + "S-sorry to see you go... (╥﹏╥)" + RESET, 80, 0, false);
                    }
                }
            } while (repeat);
        }
    }
}