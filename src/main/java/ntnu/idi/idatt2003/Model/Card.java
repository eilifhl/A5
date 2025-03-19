package ntnu.idi.idatt2003.Model;

public class Card {
  private char suit;
  private char rank;

  public Card(char suit, char rank) {
    this.suit = suit;
    this.rank = rank;
  }

  public char getSuit() {
    return suit;
  }

  public char getRank() {
    return rank;
  }

  @Override
  public String toString() {
    return "" + rank + suit;
  }
}
