package cs3500.freecell.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that represents a game of freecell.
 */
public class FreecellModel implements FreecellOperations<Card> {

  private List<ArrayList<Card>> foundationPiles;
  private List<ArrayList<Card>> cascadePiles;
  private List<ArrayList<Card>> openPiles;
  private boolean gameHasStarted;

  /**
   * constructs a FreecellModel object.
   */
  public FreecellModel() {
    this.foundationPiles = new ArrayList<ArrayList<Card>>();
    this.cascadePiles = new ArrayList<ArrayList<Card>>();
    this.openPiles = new ArrayList<ArrayList<Card>>();
    this.gameHasStarted = false;
  }

  @Override
  public List getDeck() {
    List<Integer> in = new ArrayList<Integer>();
    List<Card> out = new ArrayList<Card>();
    for (int i = 0; i < 52; ++i) {
      in.add(i);
    }
    for (int i = 0; i < 52; ++i) {
      out.add(new Card(i));
    }
    return out;
  }

  @Override
  public void startGame(List deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
      throws IllegalArgumentException {
    if (deck.size() != 52 || !deckIsValid(deck)) {
      throw new IllegalArgumentException("Invalid deck!");
    }
    if (shuffle) {
      Collections.shuffle(deck);
    }

    foundationPiles = new ArrayList<ArrayList<Card>>();
    cascadePiles = new ArrayList<ArrayList<Card>>();
    openPiles = new ArrayList<ArrayList<Card>>();

    for (int i = 0; i < 4; ++i) {
      foundationPiles.add(new ArrayList<Card>());
    }
    for (int i = 0; i < numCascadePiles; ++i) {
      cascadePiles.add(new ArrayList<Card>());
    }
    for (int i = 0; i < numOpenPiles; ++i) {
      openPiles.add(new ArrayList<Card>());
    }
    while (deck.size() > 0) {
      for (int i = 0; i < cascadePiles.size() && deck.size() > 0; ++i) {
        cascadePiles.get(i).add((Card)deck.remove(deck.size()));
      }
    }
    this.gameHasStarted = true;
  }

  /**
   * checks if the deck is valid and contains no duplicate cards.
   * @param deck  the deck to be dealt
   * @return      true if the deck is valid, false otherwise
   */
  public boolean deckIsValid(List<Card> deck) {
    ArrayList<Integer> check = new ArrayList<Integer>();
    for (int i = 0; i < 52; ++i) {
      check.add(deck.get(i).getIndex());
    }
    Collections.sort(check);
    for (int i = 0; i < 52; ++i) {
      if (i != check.get(i)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    if (!isValidMove(source, pileNumber, cardIndex, destination, destPileNumber)) {
      throw new IllegalArgumentException("Invalid move");
    } else if (!this.gameHasStarted) {
      throw new IllegalStateException("Game has not yet started");
    }
    List<Card> sourcePile;
    List<Card> destPile;
    switch (source) {
      case OPEN:
        sourcePile = openPiles.get(pileNumber);
        break;
      case FOUNDATION:
        sourcePile = foundationPiles.get(pileNumber);
        break;
      default:
        sourcePile = cascadePiles.get(pileNumber);
    }
    switch (destination) {
      case OPEN:
        destPile = openPiles.get(destPileNumber);
        break;
      case FOUNDATION:
        destPile = foundationPiles.get(destPileNumber);
        break;
      default:
        destPile = cascadePiles.get(destPileNumber);
    }
    while (sourcePile.size() - 1 != cardIndex) {
      destPile.add(sourcePile.remove(cardIndex));
    }
  }

  /**
   * Checks to see if a move is valid given the piles in consideration.
   *
   * @param source         the type of the source pile see @link{PileType}
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param cardIndex      the index of the card to be moved from the source pile, starting at 0
   * @param destination    the type of the destination pile (see
   * @param destPileNumber the pile number of the given type, starting at 0
   * @return true if the move described by the arguments is valid, false otherwise
   */
  public boolean isValidMove(PileType source, int pileNumber, int cardIndex, PileType destination,
      int destPileNumber) {
    List<ArrayList<Card>> sourceList;
    List<ArrayList<Card>> destList;
    List<Card> sourcePile;
    List<Card> destPile;
    switch (source) {
      case OPEN:
        sourceList = openPiles;
        break;
      case FOUNDATION:
        sourceList = foundationPiles;
        break;
      default:
        sourceList = cascadePiles;
    }
    switch (destination) {
      case OPEN:
        destList = openPiles;
        break;
      case FOUNDATION:
        destList = foundationPiles;
        break;
      default:
        destList = cascadePiles;
    }
    if (pileNumber >= sourceList.size()) {
      return false;
    }

    sourcePile = sourceList.get(pileNumber);

    if (destPileNumber >= destList.size()) {
      return false;
    }

    destPile = destList.get(destPileNumber);
    if (cardIndex > sourcePile.size()) {
      return false;
    }
    if (destination == PileType.OPEN) {
      if (destPile.size() > 0) {
        return false;
      }
    }

    if (destination == PileType.CASCADE) {
      int val = sourcePile.get(cardIndex).getValue();
      boolean isBlack = sourcePile.get(cardIndex).isBlack();
      for (int i = cardIndex + 1; i < sourcePile.size(); ++i) {
        if (sourcePile.get(i).isBlack() == isBlack || val >= sourcePile.get(i).getValue()) {
          return false;
        }
        isBlack = !isBlack;
        val = sourcePile.get(i).getValue();
      }
    }

    if (destination == PileType.FOUNDATION) {
      if (destPile.size() == 0) {
        if (sourcePile.get(cardIndex).getValue() != 0) {
          return false;
        }
      }
      String suit = sourcePile.get(cardIndex).getSuit();
      int val = sourcePile.get(cardIndex).getValue();
      if (destPile.get(0).getSuit() != suit) {
        return false;
      }
      if (destPile.get(destPile.size() - 1).getValue() <= val) {
        return false;
      }
    }


    return true;
  }

  @Override
  public boolean isGameOver() {
    boolean allPilesComplete = true;
    for (int i = 0; i < 4; ++i) {
      if (foundationPiles.get(i).size() != 13) {
        allPilesComplete = false;
      }
    }
    return allPilesComplete;
  }

  @Override
  public Card getCard(PileType pile, int pileNumber, int cardIndex) {
    if (!gameHasStarted) {
      throw new IllegalStateException("Game has not yet started");
    }
    List<ArrayList<Card>> pileCollection;
    List<Card> cards;
    switch (pile) {
      case OPEN:
        pileCollection = openPiles;
        break;
      case FOUNDATION:
        pileCollection = foundationPiles;
        break;
      default:
        pileCollection = cascadePiles;
    }
    if (pileNumber >= pileCollection.size()) {
      throw new IllegalArgumentException("Pile number too large");
    }
    cards = pileCollection.get(pileNumber);
    if (cardIndex >= cards.size()) {
      throw new IllegalArgumentException("Card index too great");
    }

    return cards.get(cardIndex);
  }

  @Override
  public String getGameState() {
    if (!gameHasStarted) {
      throw new IllegalStateException("Game has not yet started");
    }
    String out = "";
    for (int i = 0; i < foundationPiles.size(); ++i) {
      if (i != 0) {
        out += "\n";
      }
      out += "F" + (i + 1) + ":";
      if (foundationPiles.get(i).size() != 0) {
        out += " ";
      }
      for (int j = 0; j < foundationPiles.get(i).size() - 1; ++i) {
        out += foundationPiles.get(i).get(j).toString() + ", ";
      }
      out += foundationPiles.get(i).get(foundationPiles.get(i).size() - 1);
    }
    for (int i = 0; i < openPiles.size(); ++i) {
      if (i != 0) {
        out += "\n";
      }
      out += "O" + (i + 1) + ":";
      if (openPiles.get(i).size() != 0) {
        out += " ";
      }
      for (int j = 0; j < openPiles.get(i).size() - 1; ++i) {
        out += openPiles.get(i).get(j).toString() + ", ";
      }
      out += openPiles.get(i).get(openPiles.get(i).size() - 1);
    }
    for (int i = 0; i < cascadePiles.size(); ++i) {
      if (i != 0 && i != cascadePiles.size() - 1) {
        out += "\n";
      }
      out += "C" + (i + 1) + ":";
      if (cascadePiles.get(i).size() != 0) {
        out += " ";
      }
      for (int j = 0; j < cascadePiles.get(i).size() - 1; ++i) {
        out += cascadePiles.get(i).get(j).toString() + ", ";
      }
      out += cascadePiles.get(i).get(cascadePiles.get(i).size() - 1);
    }
    return out;
  }
}
