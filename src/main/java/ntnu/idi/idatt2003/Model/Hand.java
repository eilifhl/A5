package ntnu.idi.idatt2003.Model;

import java.util.HashMap;
import java.util.Map;

public class Hand {
  private final Map<String, Card> cards = new HashMap<>();

  public void addCard(Card card) {
    cards.put(card.toString(), card);
  }

  public Map<String, Card> getCards() {
    return cards;
  }

  public void removeCard(String key) {
    cards.remove(key);
  }

  public void clear() {
    cards.clear();
  }
}
