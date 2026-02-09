package example.calcolatrice.controller;

import example.calcolatrice.expression.Espressione;
import example.calcolatrice.expression.ExpressionException;
import example.calcolatrice.expression.Frazione;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public class Controller {
    @FXML
    public BorderPane rootPane;
    @FXML
    private TextField displayEspressione;

    @FXML
    private TextField displayMessaggi;

    private final StringBuilder espressione = new StringBuilder();

    @FXML
    public void initialize() {
        // Richiede il focus appena la finestra è pronta
        javafx.application.Platform.runLater(() -> rootPane.requestFocus());

        // Event Filter: intercetta i tasti prima che vengano consumati da altri componenti
        rootPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleKeyPressed(event);
            event.consume(); // Impedisce che il tasto faccia altre azioni (es. navigare tra i bottoni)
        });


    }


    //pulsanti con il mouse
    @FXML
    private void handleButton(ActionEvent evento) {
        Button pulsante = (Button) evento.getSource();
        espressione.append(pulsante.getText());
        aggiornaDisplay();
    }

    private void aggiornaDisplay() {
        displayEspressione.setText(espressione.toString());
    }


    @FXML
    private void clearDisplay() {
        espressione.setLength(0);
        displayMessaggi.clear();
        aggiornaDisplay();
    }

    @FXML
    private void backspace() {
        if (!espressione.isEmpty()) {
            espressione.deleteCharAt(espressione.length() - 1);
            aggiornaDisplay();
        }
    }

    //input da tastiera

    @FXML
    private void handleKeyPressed(KeyEvent evento) {
        System.out.println(evento.getCode());

        if (evento.isShiftDown()) {
            String testo = evento.getText();

            if (testo.equals("ì")) {
                espressione.append("^");
                aggiornaDisplay();
                return;
            }

            switch (evento.getCode()) {
                case DIGIT7 -> espressione.append("/");
                case DIGIT8 -> espressione.append("(");
                case DIGIT9 -> espressione.append(")");
                case PLUS -> espressione.append("*");
                case EQUALS -> espressione.append("^");

                default -> {
                    return;
                }
            }
        } else {
            switch (evento.getCode()) {
                // Numeri (tastiera normale + numerico)
                case DIGIT0, DIGIT1, DIGIT2, DIGIT3, DIGIT4,
                     DIGIT5, DIGIT6, DIGIT7, DIGIT8, DIGIT9,
                     NUMPAD0, NUMPAD1, NUMPAD2, NUMPAD3, NUMPAD4,
                     NUMPAD5, NUMPAD6, NUMPAD7, NUMPAD8, NUMPAD9 -> espressione.append(evento.getText());

                // Operazioni
                case PLUS, ADD -> espressione.append("+");
                case MINUS, SUBTRACT -> espressione.append("-");
                case ASTERISK, MULTIPLY -> espressione.append("*");
                case SLASH, DIVIDE -> espressione.append("/");
                case CIRCUMFLEX -> espressione.append("^");
                case C -> backspace();

                // Controlli
                case BACK_SPACE -> backspace();
                case DELETE -> clearDisplay();
                case ENTER -> displayMessaggi.setText("Invio premuto");


                default -> {
                    return;
                }
            }
        }


       aggiornaDisplay();
    }


    public void solveExpression(ActionEvent actionEvent) {
        String testo = displayEspressione.getText();
        testo = testo.replaceAll("^-(.*)","0-$1");
        testo = testo.replaceAll("\\(-(.*)","0-$1");


        try {
            Frazione risultato = Espressione.risolvi(testo);
            displayMessaggi.setText(risultato.toString());

        } catch (ExpressionException e) {
            // Mostra il messaggio di errore specifico
            displayMessaggi.setText(e.getMessage());
            e.printStackTrace(); // Stampa anche lo stack trace nella console

        } catch (ArithmeticException e) {
            displayMessaggi.setText("Errore aritmetico: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
