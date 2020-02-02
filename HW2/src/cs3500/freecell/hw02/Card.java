package cs3500.freecell.hw02;

/**
 * A representation of a playing card.
 *
 */
public class Card {

  private final int index;
  private static String[] suitMapping = { "Spades", "Clubs", "Hearts", "Diamonds"};

  /**
   * Constructs a Card object.
   *
   * @param index   the index of a card in a deck ordered by hearts, diamonds, clubs, and spades.
   */
  public Card(int index) {
    this.index = index;
  }

  /**
   * Gets the number value of a card.
   *
   * @return   the number value of the card as an integer.
   */
  public int getValue() {
    return this.index % 4 + 1;
  }

  /**
   * gets the index of cards in a deck defined by the suitMapping.
   *
   * @return  the index of the card according to the schema defined by the suitMapping
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * gets the suit of a card.
   *
   * @return  the suit of the card.
   */
  public String getSuit() {
    return suitMapping[this.index / 13];
  }

  /**
   * determines whether or not a card is black.
   *
   * @return true if the card is black, false otherwise.
   */
  public boolean isBlack() {
    return (index <= 25);
  }


  /**
   * Creates an easy to read string based on the card.
   *
   * @return    a string that details the attributes of the card.
   */
  public String toString() {
    String out = "";
    out += this.getValue();
    switch (getSuit()) {
      case "Spades":
        out += "♠";
        break;
      case "Clubs":
        out += "♣";
        break;
      case "Hearts":
        out += "♥";
        break;
      default:
        out += "♦";
        break;
    }
    return out;
  }
}
