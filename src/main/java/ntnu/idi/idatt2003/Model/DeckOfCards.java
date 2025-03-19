package ntnu.idi.idatt2003.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DeckOfCards {
  private final Map<String, Card> cards = new HashMap<>();
  private final char[] suits = {'S', 'H', 'D', 'C'};
  private final char[] ranks = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};
  private final Random random = new Random();

  public DeckOfCards() {
    for (char suit : suits) {
      for (char rank : ranks) {
        Card card = new Card(suit, rank);
        cards.put(card.toString(), card);
      }
    }
  }

  public Map<String, Card> dealHand(int n) {
    if (n < 1 || n > cards.size()) {
      throw new IllegalArgumentException("Not enough cards in deck");
    }
    Map<String, Card> hand = new HashMap<>();
    for (int i = 0; i < n; i++) {
      String[] keys = cards.keySet().toArray(new String[0]);
      String key = keys[random.nextInt(keys.length)];
      hand.put(key, cards.remove(key));
    }
    return hand;
  }

  public Map<String, Card> getCards() {
    return cards;
  }
}
