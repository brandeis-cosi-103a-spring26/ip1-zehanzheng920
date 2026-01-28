package edu.brandeis.cosi103a.ip1;

import java.util.*;

/**
 * Abstract base class representing a card in the game.
 * All cards have a name and cost, and can be copied.
 */
abstract class Card {
    /** The name of the card */
    protected String name;
    /** The cost in coins to purchase this card */
    protected int cost;
    
    /**
     * Constructs a Card with the given name and cost.
     *
     * @param name the name of the card
     * @param cost the cost in coins to purchase this card
     */
    public Card(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }
    
    /**
     * Gets the name of this card.
     *
     * @return the card's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the cost of this card.
     *
     * @return the cost in coins to purchase this card
     */
    public int getCost() {
        return cost;
    }
    
    /**
     * Gets the value of this card (points or coins depending on card type).
     *
     * @return the card's value
     */
    abstract int getValue();
    
    /**
     * Creates a copy of this card.
     *
     * @return a new Card instance with the same properties
     */
    abstract Card copy();
}

/**
 * Represents an Automation card that provides Automation Points (APs) at game end.
 * These cards contribute to a player's final score.
 */
class AutomationCard extends Card {
    /** The number of Automation Points this card is worth */
    private int value; // APs
    
    /**
     * Constructs an AutomationCard with the given properties.
     *
     * @param name the name of the card
     * @param cost the cost in coins to purchase this card
     * @param value the number of Automation Points this card provides
     */
    public AutomationCard(String name, int cost, int value) {
        super(name, cost);
        this.value = value;
    }
    
    /**
     * Gets the Automation Points value of this card.
     *
     * @return the number of Automation Points
     */
    @Override
    int getValue() {
        return value;
    }
    
    /**
     * Creates a copy of this AutomationCard.
     *
     * @return a new AutomationCard instance with the same properties
     */
    @Override
    Card copy() {
        return new AutomationCard(name, cost, value);
    }
}

/**
 * Represents a Cryptocurrency card that provides coins when played in hand.
 * These cards are used to purchase other cards during the buy phase.
 */
class CryptocurrencyCard extends Card {
    /** The number of coins this card provides when played */
    private int coinValue; // coins when played
    
    /**
     * Constructs a CryptocurrencyCard with the given properties.
     *
     * @param name the name of the card
     * @param cost the cost in coins to purchase this card
     * @param coinValue the number of coins this card provides when played
     */
    public CryptocurrencyCard(String name, int cost, int coinValue) {
        super(name, cost);
        this.coinValue = coinValue;
    }
    
    /**
     * Gets the number of coins this card provides.
     *
     * @return the coin value of this card
     */
    int getCoinValue() {
        return coinValue;
    }
    
    /**
     * Gets the Automation Points value of this card (always 0 for cryptocurrency).
     *
     * @return 0, since cryptocurrency cards contribute no APs to score
     */
    @Override
    int getValue() {
        return 0; // No AP value
    }
    
    /**
     * Creates a copy of this CryptocurrencyCard.
     *
     * @return a new CryptocurrencyCard instance with the same properties
     */
    @Override
    Card copy() {
        return new CryptocurrencyCard(name, cost, coinValue);
    }
}

/**
 * Represents the Supply pile containing all available cards that players can purchase.
 * Manages the inventory and availability of cards during the game.
 */
class Supply {
    /** Map of cards to their quantities in the supply */
    private Map<Card, Integer> cards;
    
    /**
     * Constructs a Supply and initializes it with all game cards and quantities.
     * Sets up the initial deck composition with Automation and Cryptocurrency cards.
     */
    public Supply() {
        cards = new HashMap<>();
        // Add Automation cards
        cards.put(new AutomationCard("Method", 2, 1), 14);
        cards.put(new AutomationCard("Module", 5, 3), 8);
        cards.put(new AutomationCard("Framework", 8, 6), 8);
        
        // Add Cryptocurrency cards
        cards.put(new CryptocurrencyCard("Bitcoin", 0, 1), 60);
        cards.put(new CryptocurrencyCard("Ethereum", 3, 2), 40);
        cards.put(new CryptocurrencyCard("Dogecoin", 6, 3), 30);
    }
    
    /**
     * Retrieves a card from the supply by name.
     *
     * @param name the name of the card to retrieve
     * @return the Card object if found, or null if not found
     */
    Card getCard(String name) {
        for (Card card : cards.keySet()) {
            if (card.getName().equals(name)) {
                return card;
            }
        }
        return null;
    }
    
    /**
     * Checks if a card is available for purchase.
     *
     * @param name the name of the card to check
     * @return true if the card exists in supply and has quantity > 0, false otherwise
     */
    boolean canBuyCard(String name) {
        Card card = getCard(name);
        return card != null && cards.get(card) > 0;
    }
    
    /**
     * Purchases a card from the supply and decrements its quantity.
     *
     * @param name the name of the card to purchase
     * @return a copy of the purchased card, or null if unavailable
     */
    Card buyCard(String name) {
        Card card = getCard(name);
        if (card != null && cards.get(card) > 0) {
            cards.put(card, cards.get(card) - 1);
            return card.copy();
        }
        return null;
    }
    
    /**
     * Checks if the game has ended.
     * The game ends when all Framework cards have been purchased from the supply.
     *
     * @return true if all Framework cards have been sold, false otherwise
     */
    boolean gameEnded() {
        // Game ends when all Framework cards have been purchased
        Card framework = getCard("Framework");
        return framework != null && cards.get(framework) == 0;
    }
    
    /**
     * Gets all currently available cards in the supply.
     *
     * @return a map of available cards to their quantities
     */
    Map<Card, Integer> getAvailableCards() {
        Map<Card, Integer> available = new HashMap<>();
        for (Card card : cards.keySet()) {
            if (cards.get(card) > 0) {
                available.put(card, cards.get(card));
            }
        }
        return available;
    }
}

/**
 * Represents a player's collection of cards (deck).
 * Tracks all cards owned by a player and calculates final scoring.
 */
class Deck {
    /** List of all cards in this deck */
    private List<Card> cards;
    
    /**
     * Constructs an empty Deck.
     */
    public Deck() {
        cards = new ArrayList<>();
    }
    
    /**
     * Adds a single card to this deck.
     *
     * @param card the card to add
     */
    void addCard(Card card) {
        cards.add(card);
    }
    
    /**
     * Adds multiple cards to this deck.
     *
     * @param newCards the list of cards to add
     */
    void addCards(List<Card> newCards) {
        cards.addAll(newCards);
    }
    
    /**
     * Calculates the total Automation Points from all Automation cards in the deck.
     *
     * @return the sum of AP values from all AutomationCards
     */
    int getAutomationPoints() {
        int ap = 0;
        for (Card card : cards) {
            if (card instanceof AutomationCard) {
                ap += card.getValue();
            }
        }
        return ap;
    }
    
    /**
     * Gets a copy of all cards in this deck.
     *
     * @return a list containing all cards in the deck
     */
    List<Card> getCards() {
        return new ArrayList<>(cards);
    }
}

/**
 * Represents a player in the game.
 * Manages the player's deck, hand, draw pile, discard pile, and purchasing power.
 */
class Player {
    /** The name of this player */
    private String name;
    /** The complete deck of cards owned by this player */
    private Deck deck;
    /** Cards available to draw during the turn */
    private List<Card> drawPile;
    /** Cards that have been played and are waiting to be reshuffled */
    private List<Card> discardPile;
    /** Cards currently in the player's hand */
    private List<Card> hand;
    
    /**
     * Constructs a Player with the given name and initializes empty piles and hand.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        deck = new Deck();
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        hand = new ArrayList<>();
    }
    
    /**
     * Gets the name of this player.
     *
     * @return the player's name
     */
    String getName() {
        return name;
    }
    
    /**
     * Adds a card to the player's deck and puts it in the discard pile.
     *
     * @param card the card to add
     */
    void addCardToDeck(Card card) {
        deck.addCard(card);
        discardPile.add(card);
    }
    
    /**
     * Adds multiple cards to the discard pile.
     *
     * @param cards the list of cards to add
     */
    void addCardsToDiscard(List<Card> cards) {
        discardPile.addAll(cards);
    }
    
    /**
     * Sets up the player's starter deck with 7 Bitcoins and 3 Methods,
     * shuffles them, and deals the initial hand of 5 cards.
     *
     * @param supply the game supply to purchase cards from
     */
    void setupStarterDeck(Supply supply) {
        // Add 7 Bitcoins and 3 Methods
        for (int i = 0; i < 7; i++) {
            Card bitcoin = supply.buyCard("Bitcoin");
            if (bitcoin != null) {
                addCardToDeck(bitcoin);
            }
        }
        for (int i = 0; i < 3; i++) {
            Card method = supply.buyCard("Method");
            if (method != null) {
                addCardToDeck(method);
            }
        }
        
        // Shuffle and deal initial hand
        drawPile.addAll(discardPile);
        discardPile.clear();
        Collections.shuffle(drawPile);
        dealHand(5);
    }
    
    /**
     * Deals the specified number of cards from the draw pile to the player's hand.
     * If the draw pile runs out, no more cards are dealt.
     *
     * @param count the number of cards to deal
     */
    void dealHand(int count) {
        hand.clear();
        for (int i = 0; i < count && !drawPile.isEmpty(); i++) {
            hand.add(drawPile.remove(drawPile.size() - 1));
        }
    }
    
    /**
     * Cleans up the turn by moving the hand to the discard pile and clearing the hand.
     */
    void cleanup() {
        // Move hand and played cards to discard
        discardPile.addAll(hand);
        hand.clear();
    }
    
    /**
     * Ends the current turn by reshuffling the deck if necessary and dealing a new hand.
     * If the draw pile is empty, the discard pile is shuffled and becomes the new draw pile.
     */
    void endTurn() {
        // Prepare for next turn
        if (drawPile.isEmpty() && !discardPile.isEmpty()) {
            drawPile.addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(drawPile);
        }
        dealHand(5);
    }
    
    /**
     * Gets a copy of the current cards in the player's hand.
     *
     * @return a list of cards currently in hand
     */
    List<Card> getHand() {
        return new ArrayList<>(hand);
    }
    
    /**
     * Gets all Cryptocurrency cards from the player's current hand.
     *
     * @return a list of all CryptocurrencyCards in the hand
     */
    List<Card> getCryptocurrencyCards() {
        List<Card> cryptoCards = new ArrayList<>();
        for (Card card : hand) {
            if (card instanceof CryptocurrencyCard) {
                cryptoCards.add(card);
            }
        }
        return cryptoCards;
    }
    
    /**
     * Calculates the total coin value available from all Cryptocurrency cards in hand.
     *
     * @return the sum of coins provided by all cryptocurrency cards
     */
    int getAvailableCoins() {
        int coins = 0;
        for (Card card : getCryptocurrencyCards()) {
            coins += ((CryptocurrencyCard) card).getCoinValue();
        }
        return coins;
    }
    
    /**
     * Gets the player's total Automation Points from all cards in the deck.
     *
     * @return the total AP value
     */
    int getAutomationPoints() {
        return deck.getAutomationPoints();
    }
}

/**
 * Main game controller for a 2-player Dominion-style card game.
 * Manages game flow, player turns, card purchasing, and win conditions.
 * The game uses an automated AI strategy for card purchasing decisions.
 */
public class domination {
    /** The first player */
    private Player player1;
    /** The second player */
    private Player player2;
    /** The player whose turn it currently is */
    private Player currentPlayer;
    /** The player who is not currently taking a turn */
    private Player otherPlayer;
    /** The supply containing all available cards for purchase */
    private Supply supply;
    /** Random number generator for determining starting player */
    private Random random;
    
    /**
     * Constructs a new Domination game and initializes both players, the supply,
     * and determines the starting player randomly.
     */
    public domination() {
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        supply = new Supply();
        random = new Random();
        
        // Setup starter decks
        player1.setupStarterDeck(supply);
        player2.setupStarterDeck(supply);
        
        // Choose random starting player
        currentPlayer = random.nextBoolean() ? player1 : player2;
        otherPlayer = currentPlayer == player1 ? player2 : player1;
    }
    
    /**
     * Executes one complete turn for the current player.
     * This includes:
     * 1. Displaying the player's hand and available coins
     * 2. Using AI strategy to choose and purchase a card
     * 3. Cleanup phase: moving hand to discard
     * 4. End of turn: reshuffling and dealing new hand
     * 5. Switching to the other player
     */
    void playTurn() {
        System.out.println("\n=== " + currentPlayer.getName() + "'s Turn ===");
        System.out.println("Hand: " + handToString(currentPlayer.getHand()));
        
        // Buy phase
        int availableCoins = currentPlayer.getAvailableCoins();
        System.out.println("Available coins: " + availableCoins);
        
        // Choose a card to buy (AI strategy: buy best card affordable)
        Card cardToBuy = chooseBestCard(availableCoins);
        
        if (cardToBuy != null) {
            Card bought = supply.buyCard(cardToBuy.getName());
            if (bought != null) {
                currentPlayer.addCardToDeck(bought);
                System.out.println("Bought: " + bought.getName());
            }
        } else {
            System.out.println("No affordable cards to buy");
        }
        
        // Cleanup phase
        currentPlayer.cleanup();
        currentPlayer.endTurn();
        
        // Swap players
        Player temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
    }
    
    /**
     * AI strategy for selecting the best card to purchase given available coins.
     * Prioritizes Automation cards by their point value, then by cost.
     * Only considers cards the player can afford.
     *
     * @param availableCoins the number of coins available to spend
     * @return the best affordable Card, or null if no cards can be afforded
     */
    Card chooseBestCard(int availableCoins) {
        Map<Card, Integer> available = supply.getAvailableCards();
        Card bestCard = null;
        int bestValue = -1;
        
        // Prefer high-value cards first, then high-cost cards
        for (Card card : available.keySet()) {
            if (card.getCost() <= availableCoins) {
                int cardScore = card instanceof AutomationCard ? 
                    card.getValue() * 100 : card.getCost();
                if (cardScore > bestValue) {
                    bestValue = cardScore;
                    bestCard = card;
                }
            }
        }
        return bestCard;
    }
    
    /**
     * Checks if the game has reached its end condition.
     * The game ends when all Framework cards have been purchased from the supply.
     *
     * @return true if the game has ended, false otherwise
     */
    boolean isGameOver() {
        return supply.gameEnded();
    }
    
    /**
     * Main game loop that plays until the game ends, then displays the winner.
     * Alternates turns between players until the supply runs out of Framework cards.
     * Calculates final Automation Points for both players and determines the winner.
     */
    void playGame() {
        System.out.println("Starting Dominion game...");
        
        while (!isGameOver()) {
            playTurn();
        }
        
        // Game over - determine winner
        int p1Points = player1.getAutomationPoints();
        int p2Points = player2.getAutomationPoints();
        
        System.out.println("\n=== Game Over ===");
        System.out.println("Player 1 Automation Points: " + p1Points);
        System.out.println("Player 2 Automation Points: " + p2Points);
        
        if (p1Points > p2Points) {
            System.out.println("Player 1 wins!");
        } else if (p2Points > p1Points) {
            System.out.println("Player 2 wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }
    
    /**
     * Converts a list of cards to a formatted string showing card names and counts.
     * Used for displaying the player's hand in a readable format.
     *
     * @param hand the list of cards to convert
     * @return a string representation of the hand, e.g., "Bitcoin x2 Method x1"
     */
    String handToString(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        // Count occurrences of each card by name
        Map<String, Integer> cardCounts = new HashMap<>();
        for (Card card : hand) {
            cardCounts.put(card.getName(), cardCounts.getOrDefault(card.getName(), 0) + 1);
        }
        // Build the string
        for (String cardName : cardCounts.keySet()) {
            sb.append(cardName).append("x").append(cardCounts.get(cardName)).append(" ");
        }
        return sb.toString();
    }
    
    /**
     * Entry point for the Domination game application.
     * Creates a new game instance and begins gameplay.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        domination game = new domination();
        game.playGame();
    }
}
