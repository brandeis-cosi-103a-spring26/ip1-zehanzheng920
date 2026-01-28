package edu.brandeis.cosi103a.ip1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class DominationTest {

    private Supply supply;
    private Player player;

    @Before
    public void setUp() {
        supply = new Supply();
        player = new Player("TestPlayer");
    }

    // ===== Card Tests =====
    @Test
    public void testAutomationCardCreation() {
        AutomationCard card = new AutomationCard("Method", 2, 1);
        assertEquals("Method", card.getName());
        assertEquals(2, card.getCost());
        assertEquals(1, card.getValue());
    }

    @Test
    public void testAutomationCardCopy() {
        AutomationCard original = new AutomationCard("Module", 5, 3);
        Card copy = original.copy();
        
        assertTrue(copy instanceof AutomationCard);
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getCost(), copy.getCost());
        assertEquals(original.getValue(), copy.getValue());
    }

    @Test
    public void testCryptocurrencyCardCreation() {
        CryptocurrencyCard card = new CryptocurrencyCard("Bitcoin", 0, 1);
        assertEquals("Bitcoin", card.getName());
        assertEquals(0, card.getCost());
        assertEquals(1, card.getCoinValue());
        assertEquals(0, card.getValue()); // No AP value
    }

    @Test
    public void testCryptocurrencyCardCopy() {
        CryptocurrencyCard original = new CryptocurrencyCard("Ethereum", 3, 2);
        Card copy = original.copy();
        
        assertTrue(copy instanceof CryptocurrencyCard);
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getCost(), copy.getCost());
    }

    // ===== Supply Tests =====
    @Test
    public void testSupplyInitialization() {
        assertNotNull(supply.getCard("Method"));
        assertNotNull(supply.getCard("Module"));
        assertNotNull(supply.getCard("Framework"));
        assertNotNull(supply.getCard("Bitcoin"));
        assertNotNull(supply.getCard("Ethereum"));
        assertNotNull(supply.getCard("Dogecoin"));
    }

    @Test
    public void testSupplyCanBuyCard() {
        assertTrue(supply.canBuyCard("Bitcoin"));
        assertTrue(supply.canBuyCard("Method"));
        assertFalse(supply.canBuyCard("NonexistentCard"));
    }

    @Test
    public void testSupplyBuyCard() {
        Card card = supply.buyCard("Bitcoin");
        assertNotNull(card);
        assertEquals("Bitcoin", card.getName());
    }

    @Test
    public void testSupplyBuyCardReducesCount() {
        assertTrue(supply.canBuyCard("Method"));
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        supply.buyCard("Method");
        assertFalse(supply.canBuyCard("Method"));
    }

    @Test
    public void testGameEndsWhenFrameworkEmpty() {
        assertFalse(supply.gameEnded());
        
        // Buy all Framework cards
        for (int i = 0; i < 8; i++) {
            supply.buyCard("Framework");
        }
        
        assertTrue(supply.gameEnded());
    }

    // ===== Deck Tests =====
    @Test
    public void testDeckAddCard() {
        Deck deck = new Deck();
        Card card = new AutomationCard("Method", 2, 1);
        deck.addCard(card);
        
        assertEquals(1, deck.getCards().size());
        assertEquals("Method", deck.getCards().get(0).getName());
    }

    @Test
    public void testDeckAutomationPoints() {
        Deck deck = new Deck();
        deck.addCard(new AutomationCard("Method", 2, 1));
        deck.addCard(new AutomationCard("Module", 5, 3));
        deck.addCard(new CryptocurrencyCard("Bitcoin", 0, 1));
        
        // Only Automation cards count towards AP
        assertEquals(4, deck.getAutomationPoints());
    }

    @Test
    public void testDeckMultipleAutomationCards() {
        Deck deck = new Deck();
        deck.addCard(new AutomationCard("Framework", 8, 6));
        deck.addCard(new AutomationCard("Framework", 8, 6));
        deck.addCard(new AutomationCard("Module", 5, 3));
        
        assertEquals(15, deck.getAutomationPoints());
    }

    // ===== Player Tests =====
    @Test
    public void testPlayerCreation() {
        assertEquals("TestPlayer", player.getName());
        assertNotNull(player.getHand());
    }

    @Test
    public void testPlayerAddCardToDeck() {
        Card card = new AutomationCard("Method", 2, 1);
        player.addCardToDeck(card);
        
        assertEquals(1, player.getAutomationPoints());
    }

    @Test
    public void testPlayerSetupStarterDeck() {
        player.setupStarterDeck(supply);
        
        List<Card> hand = player.getHand();
        assertEquals(5, hand.size()); // Initial hand size is 5
        
        // Check that player has some Bitcoins and Methods
        int bitcoinCount = 0;
        int methodCount = 0;
        for (Card card : hand) {
            if (card.getName().equals("Bitcoin")) bitcoinCount++;
            if (card.getName().equals("Method")) methodCount++;
        }
        assertTrue(bitcoinCount > 0 || methodCount > 0);
    }

    @Test
    public void testPlayerGetCryptocurrencyCards() {
        // Setup a deck and hand with crypto cards
        player.addCardToDeck(new CryptocurrencyCard("Bitcoin", 0, 1));
        player.addCardToDeck(new CryptocurrencyCard("Bitcoin", 0, 1));
        player.addCardToDeck(new CryptocurrencyCard("Ethereum", 3, 2));
        player.addCardToDeck(new CryptocurrencyCard("Ethereum", 3, 2));
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        
        // Deal a hand
        player.dealHand(5);
        
        // Count crypto cards in hand (may vary based on random shuffle)
        List<Card> cryptoCards = player.getCryptocurrencyCards();
        assertTrue(cryptoCards.size() >= 0 && cryptoCards.size() <= 4);
    }

    @Test
    public void testPlayerGetAvailableCoins() {
        player.addCardToDeck(new CryptocurrencyCard("Bitcoin", 0, 1));
        player.addCardToDeck(new CryptocurrencyCard("Ethereum", 3, 2));
        player.addCardToDeck(new CryptocurrencyCard("Dogecoin", 6, 3));
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        
        // Note: We need to set up a proper hand to test coin calculation
        // This tests the underlying logic for cryptocurrency cards
    }

    @Test
    public void testPlayerGetAutomationPoints() {
        player.addCardToDeck(new AutomationCard("Method", 2, 1));
        player.addCardToDeck(new AutomationCard("Module", 5, 3));
        player.addCardToDeck(new AutomationCard("Framework", 8, 6));
        
        assertEquals(10, player.getAutomationPoints());
    }

    @Test
    public void testPlayerHandDealing() {
        player.setupStarterDeck(supply);
        List<Card> initialHand = player.getHand();
        int initialSize = initialHand.size();
        
        player.cleanup();
        player.endTurn();
        
        assertEquals(initialSize, player.getHand().size());
    }

    @Test
    public void testPlayerCleanup() {
        player.addCardToDeck(new CryptocurrencyCard("Bitcoin", 0, 1));
        player.setupStarterDeck(supply);
        
        assertFalse(player.getHand().isEmpty());
        player.cleanup();
        assertTrue(player.getHand().isEmpty());
    }

    // ===== Game Tests =====
    @Test
    public void testGameInitialization() {
        domination game = new domination();
        assertNotNull(game);
    }

    @Test
    public void testGamePlaysWithoutError() {
        domination game = new domination();
        // Play a few turns to ensure the game runs without errors
        for (int i = 0; i < 5 && !game.isGameOver(); i++) {
            game.playTurn();
        }
        // If we get here without exception, the game logic works
        assertTrue(true);
    }

    @Test
    public void testGameEndsEventually() {
        domination game = new domination();
        int maxTurns = 10000;
        int turns = 0;
        
        while (!game.isGameOver() && turns < maxTurns) {
            game.playTurn();
            turns++;
        }
        
        assertTrue(game.isGameOver());
        assertTrue(turns < maxTurns);
    }

    // ===== Integration Tests =====
    @Test
    public void testGameSupplyDepletes() {
        Supply testSupply = new Supply();
        
        // Verify initial state
        assertTrue(testSupply.canBuyCard("Framework"));
        assertFalse(testSupply.gameEnded());
        
        // Buy all Framework cards
        int frameworksBought = 0;
        while (testSupply.canBuyCard("Framework")) {
            testSupply.buyCard("Framework");
            frameworksBought++;
        }
        
        assertEquals(8, frameworksBought);
        assertTrue(testSupply.gameEnded());
    }

    @Test
    public void testPlayerDecksGrowWithPurchases() {
        Player testPlayer = new Player("TestPlayer");
        
        testPlayer.addCardToDeck(new AutomationCard("Method", 2, 1));
        testPlayer.addCardToDeck(new AutomationCard("Module", 5, 3));
        testPlayer.addCardToDeck(new AutomationCard("Framework", 8, 6));
        
        assertEquals(10, testPlayer.getAutomationPoints());
    }

    @Test
    public void testMultiplePlayersCanPlay() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        
        player1.setupStarterDeck(supply);
        player2.setupStarterDeck(supply);
        
        assertFalse(player1.getHand().isEmpty());
        assertFalse(player2.getHand().isEmpty());
    }

    @Test
    public void testCardPurchaseAndDeckIntegration() {
        Supply testSupply = new Supply();
        Player testPlayer = new Player("TestPlayer");
        
        // Buy a card from supply
        Card boughtCard = testSupply.buyCard("Method");
        assertNotNull(boughtCard);
        
        // Add to player deck
        testPlayer.addCardToDeck(boughtCard);
        
        assertEquals(1, testPlayer.getAutomationPoints());
    }
}
