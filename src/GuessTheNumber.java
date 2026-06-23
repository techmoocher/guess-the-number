import java.util.Scanner;

public class GuessTheNumber {
    /* GLOBALS */
    private static final String RESET   = "\u001B[0m";
    private static final String PINK    = "\u001B[35m";
    private static final String RED     = "\u001B[31m";
    private static final String GREEN   = "\u001B[32m";
    private static final String CYAN    = "\u001B[36m";
    private static final String YELLOW  = "\u001B[33m";

    private static int[]    bestAttempt = {-1, -1, -1};
    private static double[] bestTime    = {-1.0, -1.0, -1.0};

    private static int      target;
    private static byte     level       = 1;
    private static byte     chances     = 10;
    private static boolean  isFirstPlay = true;

    private static final String[] messages = 
    {
        "Pretending to read your guess... (ᐢ..ᐢ )", "Consulting my crystal ball... (ᐢ..ᐢ )",
        "Verifying if you're a genius... (ᐢ..ᐢ )", "Analyzing your dumbness... (ᐢ..ᐢ )",
        "Evaluating your life choices... (ᐢ..ᐢ )", "Calculating how wrong you are... (ᐢ..ᐢ )",
        "Finalizing your doom... (ᐢ..ᐢ )", "Processing your lucky guess... (ᐢ..ᐢ )",
        "Determining the comedic outcome... (ᐢ..ᐢ )", "Humoring your request... (ᐢ..ᐢ )"
    };

    /* HELPERS */
    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private static void foxSpeak(String text, int delay, int downtime, boolean lineBreak) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            switch (c) {
                case ','    -> sleep(delay + 200);
                case '.'    -> sleep(delay + 400);
                case '?'    -> sleep(delay + 400);
                default     -> sleep(delay);
            }
        }

        if (lineBreak) { System.out.println(); }

        sleep(downtime);
    }

    private static void levelSelection(Scanner sc) {
        if (!isFirstPlay) {
            sc.nextLine();

            foxSpeak("You survive!? Wanna stick to the same level?\n(you don't have have to, you know...", 35, 500, true);
            System.out.print(">>> Enter your choice [Y/n]: ");

            boolean ok = false;
            while (!ok) {
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
                    ok = true;
                }
                else {
                    System.out.println(RED + "INVALID INPUT!" + RESET +
                                        "\n\nHuh? It's a Y/N question dumbass.");
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
                                    "\n\nI said A NUMBER from 1 to 3. A NUMBER!!!");
                foxSpeak("Did your mom teach you to tell numbers and letters apart??", 35, 300, true);
                System.out.print(">>> Enter your choice: ");
                sc.next();
            }
            int inp = sc.nextInt();
            if (inp >= 1 && inp <= 3) {
                level = (byte) inp;
                sc.nextLine();
                break;
            }
            else {
                System.out.println(RED + "OUT OF BOUNDS!" + RESET +
                                    "\n\n1, 2, or 3, alright? Counting is hard, I know...");
                foxSpeak("Counting is not easy, I know...", 35, 300, true);
                System.out.print(">>> Enter your choice: ");
            }
        }

        switch (level) {
            case 1 -> {
                foxSpeak(PINK + "Easy? How adorable... >3<" + RESET, 40, 750, true);
                chances = 10;
            }
            case 2 -> {
                foxSpeak(PINK + "Medium? Boring...but nice try. <3" + RESET, 40, 750, true);
                chances = 5;
            }
            case 3 -> {
                foxSpeak(PINK + "Hard!? Ohhh ~~ Promise me you won't cry >_." + RESET, 40, 750, true);
                chances = 3;
            }
        }
    }

    private static void game(Scanner sc) {
        int guess = -1;
        int attempts = 0;
        long startTime, endTime;

        startTime = System.nanoTime();
        while (chances > 0) {
            System.out.print(">>> Enter your guess (" + chances + " left): ");
            
            boolean ok = false;
            while (!ok) {
                while (!sc.hasNextInt()) {
                    System.out.println(RED + "INVALID INPUT!!!" + RESET +
                                        "\n\nThat's not a number dumbass.");
                    foxSpeak("Maybe you should check your keyboard layout.", 35, 300, true);
                    System.out.print(">>> Enter your guess: ");
                    sc.next();
                }
                int inp = sc.nextInt();
                if (inp >= 1 && inp <= 100) {
                    guess = inp;
                    ok = true;
                }
                else {
                    System.out.println(RED + "OUT OF BOUNDS!" + RESET +
                                        "\n\nThe number must be between 1 and 100, alright?");
                    foxSpeak("Teacher, could you please teach them to count?", 35, 300, true);
                    System.out.print(">>> Enter your choice: ");
                }
            }
            
            attempts++;
            chances--;

            int downtime = (int)(Math.random() * (1500 - 500 + 1)) + 500;
            int rnd = (int)(Math.random() * 10);
            System.out.println(CYAN + messages[rnd] + RESET);
            sleep(downtime);

            if (guess != target) {
                if (guess < target) {
                    System.out.println(RED + "INCORRECT!" + RESET +
                                        " Too low buddy! Don't underestimate me. ;)\n");
                }
                else if (guess > target) {
                    System.out.println(RED + "INCORRECT!" + RESET +
                                        " Too high blud! Just double-digit numbers, not rocket science. >:(\n");
                }

                if (chances == 0) {
                    System.out.println(RED + "GAME OVER!!!" + RESET +
                                        "\nOhhh ~~ Looks like someone has no chances left.");
                    sleep(500);
                    System.out.println("The correct answer was " + YELLOW + target + RESET +
                                        ". Better luck next time, hoo-man!\n");
                    break;
                }
            }
            else {
                endTime = System.nanoTime();
                double timeTaken = (endTime - startTime) / 1_000_000_000.0;
                
                System.out.println(GREEN + "CORRECT! You nailed it in " + attempts + " attempt(s)." + RESET);
                System.out.printf(GREEN + "It took you %.2f seconds!" + RESET +
                                    "I suspect it's pure luck tho...%n", timeTaken);

                int idx = level - 1;
                if (bestAttempt[idx] == -1 || attempts < bestAttempt[idx]) {
                    bestAttempt[idx] = attempts;
                }
                if (bestTime[idx] == -1.0 || timeTaken < bestTime[idx]) {
                    bestTime[idx] = timeTaken;
                }
                
                foxSpeak(PINK + "Purr...Bet you're just lucky this time. Wanna tray again, hoo-man? :333\n" + RESET, 40, 800, true);
                break;
            }
        }

        isFirstPlay = false;
    }

    public static void main(String[] args) {
        boolean repeat;

        try (Scanner sc = new Scanner(System.in)) {
            do {
                repeat = false;

                if (isFirstPlay) {
                    foxSpeak(PINK + "HELLO..." + RESET, 100, 500, true);
                    foxSpeak(PINK + "I'm Karu, a naughty fox" + RESET, 80, 0, true);
                    System.out.println("/\\../\\\n");
                    sleep(500);

                    foxSpeak("Welcome to ", 50, 500, false);
                    foxSpeak("GUESS THE NUMBER!!! (fox limited edition)\n", 50, 1000, true);

                    foxSpeak("The rules are simple ~~~", 40, 300, true);
                    foxSpeak("I think of a number between 1 and 100, and you have to get it right.\n", 30, 800, true);
                }
                
                target = (int)(Math.random() * 100) + 1;
                
                levelSelection(sc);

                foxSpeak("\nAlright, you're all set. Don't embarrass yourself ~~~\n", 30, 800, true);

                game(sc);

                boolean ok = false;
                while (!ok) {
                    System.out.println("Anyways, what do you wanna do now?");
                    System.out.println("1. Play again (!!!)");
                    System.out.println("2. View your 'speedy' time records");
                    System.out.println("3. View your attempt count records");
                    System.out.println("4. Quit (that's alright I understand)");
                    System.out.print(">>> Enter your choice: ");

                    byte cmd = -1;
                    while (true) {
                        while (!sc.hasNextInt()) {
                            System.out.println(RED + "INVALID INPUT!" + RESET +
                                                "\n\nA NUMBER from 1 to 4, please ~~~.");
                            System.out.print(">>> Enter your choice: ");
                            sc.next();
                        }
                        int inp = sc.nextInt();
                        if (inp >= 1 && inp <= 4) {
                            cmd = (byte) inp;
                            sc.nextLine();
                            break;
                        }
                        else {
                            System.out.println(RED + "OUT OF BOUNDS!" + RESET +
                                                "\n\nPick a number from 1 to 4, please ~~~");
                            foxSpeak("Counting is not that hard, right?", 35, 300, true);
                            System.out.print(">>> Enter your choice: ");
                        }
                    }

                    switch (cmd) {
                        case 1 -> {
                            repeat = true;
                            ok = true;
                        }
                        case 2 -> {
                            System.out.println(YELLOW + "\n--- Your Time Records ---" + RESET);
                            System.out.printf("1. [EASY]   %s%n", bestTime[0] == -1.0 ? "No record yet" : bestTime[0] + "s");
                            System.out.printf("2. [MEDIUM] %s%n", bestTime[1] == -1.0 ? "No record yet" : bestTime[1] + "s");
                            System.out.printf("3. [HARD]   %s%n\n", bestTime[2] == -1.0 ? "No record yet" : bestTime[2] + "s");
                            System.out.print("\nPress any keys to return to the menu...");
                            sc.nextLine();
                            System.out.println();
                        }
                        case 3 -> {
                            System.out.println(YELLOW + "\n--- Your Attempt Count Records ---" + RESET);
                            System.out.printf("1. [EASY]   %s%n", bestAttempt[0] == -1 ? "No record yet" : bestAttempt[0] + " attempts");
                            System.out.printf("2. [MEDIUM] %s%n", bestAttempt[1] == -1 ? "No record yet" : bestAttempt[1] + " attempts");
                            System.out.printf("3. [HARD]   %s%n\n", bestAttempt[2] == -1 ? "No record yet" : bestAttempt[2] + " attempts");
                            System.out.print("\nPress any keys to return to the menu...");
                            sc.nextLine();
                            System.out.println();
                        }
                        case 4 -> {
                            foxSpeak(PINK + "Awww, why so soon? S-sorry to see you go... (T~T)" + RESET, 80, 0, false);
                            repeat = false;
                            ok = true;
                        }
                    }
                }
            } while (repeat);
        }
    }
}