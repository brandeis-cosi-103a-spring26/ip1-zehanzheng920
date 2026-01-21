package edu.brandeis.cosi103a.ip1;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int player1Score = 0;
        int player2Score = 0;
        
        System.out.println("========================================");
        System.out.println("     WELCOME TO THE DICE ROLLING GAME");
        System.out.println("========================================");
        System.out.println("Game Rules:");
        System.out.println("- 2 players take turns rolling a 6-sided die");
        System.out.println("- Each player gets 10 turns");
        System.out.println("- On each turn, you can re-roll up to 2 times");
        System.out.println("- Your final roll is added to your score");
        System.out.println("- The player with the highest score wins!");
        System.out.println("========================================\n");
        
        // Play 10 rounds for each player (20 total turns)
        for (int round = 1; round <= 10; round++) {
            // Player 1's turn
            System.out.println("\n--- ROUND " + round + " ---");
            System.out.println("Player 1's Turn:");
            int player1RollValue = playerTurn(scanner, "Player 1");
            player1Score += player1RollValue;
            System.out.println("Points added: " + player1RollValue);
            System.out.println("Player 1 Total Score: " + player1Score);
            
            // Player 2's turn
            System.out.println("\nPlayer 2's Turn:");
            int player2RollValue = playerTurn(scanner, "Player 2");
            player2Score += player2RollValue;
            System.out.println("Points added: " + player2RollValue);
            System.out.println("Player 2 Total Score: " + player2Score);
        }
        
        // Display final results
        displayGameResults(player1Score, player2Score);
        scanner.close();
    }
    
    /**
     * Handles a single player's turn with re-rolling option
     */
    static int playerTurn(Scanner scanner, String playerName) {
        int currentRoll = rollDie();
        System.out.println("You rolled: " + currentRoll);
        
        int rerollsUsed = 0;
        final int MAX_REROLLS = 2;
        
        // Allow up to 2 re-rolls
        while (rerollsUsed < MAX_REROLLS) {
            System.out.print("Do you want to re-roll? (yes/no): ");
            String choice = scanner.nextLine().toLowerCase().trim();
            
            if (choice.equals("yes") || choice.equals("y")) {
                currentRoll = rollDie();
                System.out.println("You rolled: " + currentRoll);
                rerollsUsed++;
                System.out.println("Re-rolls remaining: " + (MAX_REROLLS - rerollsUsed));
            } else if (choice.equals("no") || choice.equals("n")) {
                System.out.println("You chose to end your turn.");
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
        
        if (rerollsUsed == MAX_REROLLS) {
            System.out.println("You have used all re-rolls. Your turn ends.");
        }
        
        return currentRoll;
    }
    
    /**
     * Rolls a 6-sided die
     */
    static int rollDie() {
        return (int) (Math.random() * 6) + 1;
    }
    
    /**
     * Displays the final game results
     */
    static void displayGameResults(int player1Score, int player2Score) {
        System.out.println("\n========================================");
        System.out.println("            GAME RESULTS");
        System.out.println("========================================");
        System.out.println("Player 1 Final Score: " + player1Score);
        System.out.println("Player 2 Final Score: " + player2Score);
        System.out.println("----------------------------------------");
        
        if (player1Score > player2Score) {
            System.out.println("🎉 PLAYER 1 WINS! 🎉");
            System.out.println("Player 1 wins by " + (player1Score - player2Score) + " points!");
        } else if (player2Score > player1Score) {
            System.out.println("🎉 PLAYER 2 WINS! 🎉");
            System.out.println("Player 2 wins by " + (player2Score - player1Score) + " points!");
        } else {
            System.out.println("🤝 IT'S A TIE! 🤝");
            System.out.println("Both players scored " + player1Score + " points!");
        }
        System.out.println("========================================");
    }
}
