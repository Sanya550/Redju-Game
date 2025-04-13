package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class RenjuGame extends Application {
    private static final int SIZE = 19;
    private static final int CELL_SIZE = 30;
    private static final int BOARD_SIZE = SIZE * CELL_SIZE;
    private int[][] board = new int[SIZE][SIZE];
    private boolean isBlackTurn = true;
    private static int WIN_COUNT = 5;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(BOARD_SIZE, BOARD_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBoard(gc);

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> handleMouseClick(e, gc));

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, BOARD_SIZE, BOARD_SIZE);

        primaryStage.setTitle("Renju Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawBoard(GraphicsContext gc) {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, BOARD_SIZE, BOARD_SIZE);
        gc.setStroke(Color.BLACK);

        for (int i = 0; i < SIZE; i++) {
            double pos = i * CELL_SIZE + CELL_SIZE / 2.0;
            gc.strokeLine(pos, CELL_SIZE / 2.0, pos, BOARD_SIZE - CELL_SIZE / 2.0);// Vertical
            gc.strokeLine(CELL_SIZE / 2.0, pos, BOARD_SIZE - CELL_SIZE / 2.0, pos);//Horizontal
        }
    }

    private void handleMouseClick(MouseEvent e, GraphicsContext gc) {
        int col = (int) (e.getX() / CELL_SIZE);
        int row = (int) (e.getY() / CELL_SIZE);

        if (board[row][col] == 0) {
            board[row][col] = isBlackTurn ? 1 : 2;
            drawStone(gc, row, col, isBlackTurn);
            if (checkWinCondition(row, col)) {
                JOptionPane.showMessageDialog(null, (isBlackTurn ? "Black" : "White") + " wins!");
                resetBoard(gc);
            }
            isBlackTurn = !isBlackTurn;
        }
    }

    public void drawStone(GraphicsContext gc, int row, int col, boolean isBlack) {
        double offset = 5.0;
        double diameter = CELL_SIZE - 2 * offset;

        gc.setFill(isBlack ? Color.BLACK : Color.WHITE);
        gc.fillOval(col * CELL_SIZE + offset, row * CELL_SIZE + offset, diameter, diameter);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(col * CELL_SIZE + offset, row * CELL_SIZE + offset, diameter, diameter);
    }

    private boolean checkWinCondition(int row, int col) {
        int player = board[row][col];
        return checkDirection(row, col, player, 1, 0) || // Horizontal
                checkDirection(row, col, player, 0, 1) || // Vertical
                checkDirection(row, col, player, 1, 1) || // Diagonal /
                checkDirection(row, col, player, 1, -1);  // Diagonal \
    }

    private boolean checkDirection(int row, int col, int player, int dRow, int dCol) {
        int count = 1;
        count += countStones(row, col, player, dRow, dCol);
        count += countStones(row, col, player, -dRow, -dCol);
        return count == WIN_COUNT;
    }

    private int countStones(int row, int col, int player, int dRow, int dCol) {
        int count = 0;
        for (int i = 1; i < WIN_COUNT; i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;
            if (newRow < 0 || newRow >= SIZE || newCol < 0 || newCol >= SIZE || board[newRow][newCol] != player) {
                break;
            }
            count++;
        }
        return count;
    }

    private void resetBoard(GraphicsContext gc) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0;
            }
        }
        isBlackTurn = true;
        drawBoard(gc);
    }
}

