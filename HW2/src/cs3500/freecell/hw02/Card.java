package cs3500.freecell.hw02;

/**
 * A representation of a playing card.
 */
public class Card {
  private final int value;
  private final String suit;

  /**
   * Constructs a Card object
   * @param value   The number value of a card (A = 1, 4 = 4, J = 10).
   * @param suit  The suit of the
   */
  public Card(int value, String suit) {
    this.value = value;
    this.suit = suit;
  }

  /**
   *
   * @return   the number value of the card as an integer.
   */
  public int getValue() {
    return this.value;
  }

  /**
   *
   * @return  the suit of the card.
   */
  public String getSuit() {
    return this.suit;
  }
}
