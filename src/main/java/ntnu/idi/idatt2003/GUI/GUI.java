package ntnu.idi.idatt2003.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ntnu.idi.idatt2003.Model.Card;
import ntnu.idi.idatt2003.Model.DeckOfCards;
import ntnu.idi.idatt2003.Model.Hand;

import java.util.Map;
import java.util.stream.Collectors;

public class GUI extends Application {
  // Spill-tilstand og UI-komponenter
  private DeckOfCards deck;
  private Hand hand;

  private FlowPane handDisplay;
  private Label statusHand;
  private Label statusDeck;
  private TextField dealInput;

  // Nytt for analyse av hånd
  private Button checkHandButton;
  private Label sumOfFacesLabel;
  private Label heartsLabel;
  private Label spadeQueenLabel;
  private Label flushLabel;

  @Override
  public void start(Stage primaryStage) {
    // Initialiser deck og hand
    deck = new DeckOfCards();
    hand = new Hand();

    HBox topPanel = new HBox(10);
    Label dealLabel = new Label("Number of cards to deal:");
    dealInput = new TextField();
    Button dealButton = new Button("Deal");
    Button resetButton = new Button("Reset");
    topPanel.getChildren().addAll(dealLabel, dealInput, dealButton, resetButton);


    handDisplay = new FlowPane(10, 10);


    HBox bottomPanel = new HBox(10);
    Button clearButton = new Button("Clear Hand");
    statusHand = new Label("Cards in hand: 0");
    statusDeck = new Label("Cards in deck: 52");
    bottomPanel.getChildren().addAll(clearButton, statusHand, statusDeck);

    HBox analysisPanel = new HBox(10);
    checkHandButton = new Button("Check Hand");
    sumOfFacesLabel = new Label("Sum of faces: 0");
    heartsLabel = new Label("Hearts: ?");
    spadeQueenLabel = new Label("Has spade queen? ");
    flushLabel = new Label("Has 5-flush? ");
    analysisPanel.getChildren().addAll(checkHandButton, sumOfFacesLabel, heartsLabel, spadeQueenLabel, flushLabel);

    // Oppsett av hovedlayout
    VBox root = new VBox(10);
    root.getChildren().addAll(topPanel, handDisplay, bottomPanel, analysisPanel);

    // Event handlers
    dealButton.setOnAction(event -> {
      try {
        int n = Integer.parseInt(dealInput.getText());
        Map<String, Card> dealtCards = deck.dealHand(n);
        for (Card card : dealtCards.values()) {
          hand.addCard(card);
        }
        updateDisplay();
        dealInput.clear(); // Tømmer input-feltet etter at vi har delt kort
      } catch (NumberFormatException e) {
        statusHand.setText("Invalid number");
      } catch (IllegalArgumentException e) {
        statusHand.setText("Not enough cards in deck");
      }
    });

    resetButton.setOnAction(event -> {
      deck = new DeckOfCards();
      hand.clear();
      updateDisplay();
    });

    clearButton.setOnAction(event -> {
      hand.clear();
      updateDisplay();
    });

    // Ny knapp for å checke hand
    checkHandButton.setOnAction(event -> analyzeHand());

    // Startvisning
    updateDisplay();

    // Scene + Stage
    Scene scene = new Scene(root, 700, 400);
    primaryStage.setTitle("Card Game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Metode som analyserer kortene på hånd:
   * 1) Sum av verdier (ess=1, 2=2, ..., J=11, Q=12, K=13)
   * 2) Henter bare Hjerter (H), viser i label
   * 3) Sjekker om "Spar dame" (QS) finnes på hånd
   * 4) Sjekker om det er 5-flush (5 kort av samme suit)
   */
  private void analyzeHand() {
    // 1) Summer verdiene med streams
    int sumOfFaces = hand.getCards().values().stream()
        .mapToInt(card -> rankValue(card.getRank()))
        .sum();
    sumOfFacesLabel.setText("Sum of faces: " + sumOfFaces);

    // 2) Filtrer Hjerter og join i en streng
    String hearts = hand.getCards().values().stream()
        .filter(card -> card.getSuit() == 'H')
        .map(Card::toString)
        .collect(Collectors.joining(" "));
    if (hearts.isEmpty()) {
      heartsLabel.setText("Hearts: No Hearts");
    } else {
      heartsLabel.setText("Hearts: " + hearts);
    }

    // 3) Sjekk om "Spar dame" (Queen of Spades, "QS") finnes
    boolean hasSpadeQueen = hand.getCards().values().stream()
        .anyMatch(card -> card.toString().equals("QS"));
    spadeQueenLabel.setText("Has spade queen? " + (hasSpadeQueen ? "Yes" : "No"));

    // 4) Sjekk 5-flush
    // Gruppér på suit, tell antall, se om >= 5
    boolean hasFlush = hand.getCards().values().stream()
        .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()))
        .values()
        .stream()
        .anyMatch(count -> count >= 5);

    flushLabel.setText("Has 5-flush? " + (hasFlush ? "Yes" : "No"));
  }

  /**
   * Oppdaterer GUI for hånd og deck
   */
  private void updateDisplay() {
    handDisplay.getChildren().clear();
    for (Card card : hand.getCards().values()) {
      Label cardLabel = new Label(card.toString());
      cardLabel.setStyle("-fx-border-color: black; -fx-padding: 5px;");
      cardLabel.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2) {
          hand.removeCard(card.toString());
          updateDisplay();
        }
      });
      handDisplay.getChildren().add(cardLabel);
    }
    statusHand.setText("Cards in hand: " + hand.getCards().size());
    statusDeck.setText("Cards in deck: " + deck.getCards().size());
  }

  /**
   * Metode for å mappe rank-char til tallverdi (ess=1, J=11, Q=12, K=13).
   */
  private int rankValue(char rank) {
    return switch (rank) {
      case 'A' -> 1;
      case '2' -> 2;
      case '3' -> 3;
      case '4' -> 4;
      case '5' -> 5;
      case '6' -> 6;
      case '7' -> 7;
      case '8' -> 8;
      case '9' -> 9;
      case 'T' -> 10;
      case 'J' -> 11;
      case 'Q' -> 12;
      case 'K' -> 13;
      default -> 0;
    };
  }

  public static void main(String[] args) {
    launch(args);
  }
}
