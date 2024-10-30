package com.assignment.tictactoe.controller;

import com.assignment.tictactoe.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BoardController implements BoardUI {
    private final BoardImpl board;
    private final AiPlayer ai;
    private final HumanPlayer human;

    // 2D array of Buttons for managing the game board UI
    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;

    // 2D array to hold buttons for easier access
    private Button[][] buttons = new Button[3][3];

    // Additional UI elements
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gameGrid;

    // Constructor to initialize board and player objects
    public BoardController() {
        board = new BoardImpl();
        ai = new AiPlayer(board);
        human = new HumanPlayer(board);
    }

    // Method to initialize buttons directly after they are injected by FXML
    @FXML
    public void initialize() {
        buttons[0][0] = btn00;
        buttons[0][1] = btn01;
        buttons[0][2] = btn02;
        buttons[1][0] = btn10;
        buttons[1][1] = btn11;
        buttons[1][2] = btn12;
        buttons[2][0] = btn20;
        buttons[2][1] = btn21;
        buttons[2][2] = btn22;
    }

    // Event handler for button click events
    @FXML
    void buttonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        String id = button.getId();

        int row = -1, col = -1;

        // Determine row and column using a switch statement based on the button ID
        switch (id) {
            case "btn00":
                row = 0; col = 0; break;
            case "btn01":
                row = 0; col = 1; break;
            case "btn02":
                row = 0; col = 2; break;
            case "btn10":
                row = 1; col = 0; break;
            case "btn11":
                row = 1; col = 1; break;
            case "btn12":
                row = 1; col = 2; break;
            case "btn20":
                row = 2; col = 0; break;
            case "btn21":
                row = 2; col = 1; break;
            case "btn22":
                row = 2; col = 2; break;
            default:
                System.err.println("Invalid button ID: " + id);
                return;  // Exit if the ID is not recognized
        }

        // Execute human move and AI move, then update the board state.
        human.move(row, col);
        ai.findBestMove();
        board.printBoard();
        resetBoard();

        // Check if there's a winner or if the board is full for a draw, directly in buttonClicked

        if (board.checkWinner() != null) {
            NotifyWinner(board.checkWinner().getWinningPiece()); // Notify winner if there's one
        } else if (board.isBoardFull()) {
            alert("Match Drawn!!!"); // Notify if the game is drawn
        }
    }

    
    public void resetBoard() {
        for (int i = 0; i < board.getPieces().length; i++) {
            for (int j = 0; j < board.getPieces()[i].length; j++) {
                update(i, j, board.getPieces()[i][j]);
            }
        }
    }


    @Override
    public void update(int row, int col, Piece piece) {
        Button button = buttons[row][col];
        button.setText(piece == Piece.X ? "X" : piece == Piece.O ? "O" : "");
    }

    // Notify the user of the winner
    @FXML
    public void NotifyWinner(Piece winner) {
        if (winner != null) { // Check if the winner is not null
            String message = (winner == Piece.X) ? "X Won!!!" : "O Won!!!"; // Create message based on winner
            alert(message); // Call to display the message
        }
    }

    // Displays an alert message
    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setOnCloseRequest((DialogEvent event) -> {
            board.initializeBoard();
            resetBoard();
        });
        alert.showAndWait();
    }
}
