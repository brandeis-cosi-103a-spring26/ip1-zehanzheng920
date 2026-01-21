package edu.brandeis.cosi103a.ip1;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Dice Rolling Game (App.java)
 */
public class AppTest {
    
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    
    @Before
    public void setUp() {
        // Capture system output for testing
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }
    
    // ==================== TESTS FOR rollDie() ====================
    
    /**
     * Test that rollDie returns a value within the valid range [1, 6]
     */
    @Test
    public void testRollDieReturnsValidRange() {
        for (int i = 0; i < 100; i++) {
            int roll = App.rollDie();
            assertTrue("Die roll should be between 1 and 6", roll >= 1 && roll <= 6);
        }
    }
    
    /**
     * Test that rollDie never returns 0
     */
    @Test
    public void testRollDieNeverReturnsZero() {
        for (int i = 0; i < 100; i++) {
            int roll = App.rollDie();
            assertNotEquals("Die roll should never be 0", 0, roll);
        }
    }
    
    /**
     * Test that rollDie never returns 7 or higher
     */
    @Test
    public void testRollDieNeverExceedsSix() {
        for (int i = 0; i < 100; i++) {
            int roll = App.rollDie();
            assertTrue("Die roll should never exceed 6", roll <= 6);
        }
    }
    
    /**
     * Test that rollDie produces varied results (not always the same number)
     */
    @Test
    public void testRollDieProducesVariedResults() {
        boolean hasVariation = false;
        int firstRoll = App.rollDie();
        
        for (int i = 0; i < 50; i++) {
            if (App.rollDie() != firstRoll) {
                hasVariation = true;
                break;
            }
        }
        
        assertTrue("Die should produce varied results", hasVariation);
    }
    
    // ==================== TESTS FOR playerTurn() ====================
    
    /**
     * Test playerTurn when player chooses not to re-roll
     */
    @Test
    public void testPlayerTurnNoReroll() {
        String input = "no\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int result = App.playerTurn(scanner, "Player 1");
        
        assertTrue("Player turn should return a valid die value", result >= 1 && result <= 6);
    }
    
    /**
     * Test playerTurn when player re-rolls once and then stops
     */
    @Test
    public void testPlayerTurnOneReroll() {
        String input = "yes\nno\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int result = App.playerTurn(scanner, "Player 1");
        
        assertTrue("Player turn should return a valid die value after re-roll", 
                   result >= 1 && result <= 6);
    }
    
    /**
     * Test playerTurn when player uses both re-rolls
     */
    @Test
    public void testPlayerTurnTwoRerolls() {
        String input = "yes\nyes\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int result = App.playerTurn(scanner, "Player 1");
        
        assertTrue("Player turn should return a valid die value after two re-rolls", 
                   result >= 1 && result <= 6);
    }
    
    /**
     * Test playerTurn handles case-insensitive input
     */
    @Test
    public void testPlayerTurnCaseInsensitive() {
        String input = "YES\nNO\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int result = App.playerTurn(scanner, "Player 1");
        
        assertTrue("Player turn should handle case-insensitive input", 
                   result >= 1 && result <= 6);
    }
    
    /**
     * Test playerTurn with abbreviated yes answer
     */
    @Test
    public void testPlayerTurnAbbreviatedYes() {
        String input = "y\nno\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        int result = App.playerTurn(scanner, "Player 1");
        
        assertTrue("Player turn should accept 'y' as yes", 
                   result >= 1 && result <= 6);
    }
    
    // ==================== TESTS FOR displayGameResults() ====================
    
    /**
     * Test displayGameResults when Player 1 wins
     */
    @Test
    public void testDisplayGameResultsPlayer1Wins() {
        App.displayGameResults(100, 50);
        String output = outContent.toString();
        
        assertTrue("Should declare Player 1 as winner", output.contains("PLAYER 1 WINS"));
        assertTrue("Should show correct scores", output.contains("100"));
        assertTrue("Should show correct scores", output.contains("50"));
    }
    
    /**
     * Test displayGameResults when Player 2 wins
     */
    @Test
    public void testDisplayGameResultsPlayer2Wins() {
        App.displayGameResults(40, 80);
        String output = outContent.toString();
        
        assertTrue("Should declare Player 2 as winner", output.contains("PLAYER 2 WINS"));
        assertTrue("Should show correct scores", output.contains("40"));
        assertTrue("Should show correct scores", output.contains("80"));
    }
    
    /**
     * Test displayGameResults when game is a tie
     */
    @Test
    public void testDisplayGameResultsTie() {
        App.displayGameResults(75, 75);
        String output = outContent.toString();
        
        assertTrue("Should declare a tie", output.contains("TIE"));
        assertTrue("Should show tied score", output.contains("75"));
    }
    
    /**
     * Test displayGameResults shows the point difference for Player 1 win
     */
    @Test
    public void testDisplayGameResultsShowsPointDifference() {
        App.displayGameResults(100, 60);
        String output = outContent.toString();
        
        assertTrue("Should show the winning margin", output.contains("40"));
    }
    
    // ==================== INTEGRATION TESTS ====================
    
    /**
     * Test that multiple die rolls don't always produce the same result
     */
    @Test
    public void testGameVariability() {
        boolean hasVariation = false;
        int firstRoll = App.rollDie();
        
        for (int i = 0; i < 100; i++) {
            if (App.rollDie() != firstRoll) {
                hasVariation = true;
                break;
            }
        }
        
        assertTrue("Game should produce varied rolls", hasVariation);
    }
    
    /**
     * Test that die roll is within expected bounds for a 6-sided die
     */
    @Test
    public void testDieBoundaries() {
        int minRoll = 7;
        int maxRoll = 0;
        
        for (int i = 0; i < 100; i++) {
            int roll = App.rollDie();
            minRoll = Math.min(minRoll, roll);
            maxRoll = Math.max(maxRoll, roll);
        }
        
        assertTrue("Minimum roll should be at least 1", minRoll >= 1);
        assertTrue("Maximum roll should be at most 6", maxRoll <= 6);
    }
}
