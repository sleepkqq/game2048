package com.javarush.games.game2048;
import com.javarush.engine.cell.*;

import javax.swing.*;


public class Game2048 extends Game {


    public static int SIDE = 0;

    public Game2048 (int side) {
        SIDE = side;
    }


    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;

    private int score = 0;



    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    
    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private void createNewNumber() {
        if (getMaxTileValue() == 2048)
            win();
        boolean isNotCorrectNumber = false;
        while (!isNotCorrectNumber) {
            int x = getRandomNumber(SIDE), y = getRandomNumber(SIDE);
            if (gameField[y][x] == 0) {
                gameField[y][x] = getRandomNumber(10) == 9 ? 4 : 2;
                isNotCorrectNumber = true;
            }
        }
    }


    private void setCellColoredNumber(int x, int y, int value) {
        setCellValueEx(x, y, getColorByValue(value), value > 0 ? "" + value : "");
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 2: return Color.LIGHTBLUE;
            case 4: return Color.LIGHTCORAL;
            case 8: return Color.CORAL;
            case 16: return Color.BLUEVIOLET;
            case 32: return Color.VIOLET;
            case 64: return Color.DARKVIOLET;
            case 128: return Color.LIGHTPINK;
            case 256: return Color.PINK;
            case 512: return Color.ORANGE;
            case 1028: return Color.RED;
            case 2048: return Color.YELLOW;
            default: return Color.WHITE;
        }
    }

    private void drawScene() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }

    private boolean compressRow(int[] row) {
        boolean answer = false;
        int positionNumZero = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {
                if (positionNumZero != i) {
                    row[positionNumZero] = row[i];
                    row[i] = 0;
                    answer = true;
                }
                positionNumZero++;
            }
        }
        return answer;
    }

    private boolean mergeRow(int[] row) {
        boolean answer = false;
        for (int i = 1; i < row.length; i++) {
            if (row[i - 1] == row[i] && row[i - 1] != 0) {
                row[i - 1] += row[i];
                score += row[i - 1];
                setScore(score);
                row[i] = 0;
                answer = true;
            }
        }
        return answer;
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            } else {
                return;
            }
        }
        if (!canUserMove()) {
            gameOver();
            return;
        }
        switch (key) {
            case DOWN: moveDown(); drawScene(); break;
            case UP: moveUp(); drawScene(); break;
            case LEFT: moveLeft(); drawScene(); break;
            case RIGHT: moveRight(); drawScene(); break;
        }
    }

    private void moveLeft() {
        boolean isNewNumberNeeded = false;
        for (int[] row : gameField) {
            boolean wasCompressed = compressRow(row);
            boolean wasMerged = mergeRow(row);
            if (wasMerged) {
                compressRow(row);
            }
            if (wasCompressed || wasMerged) {
                isNewNumberNeeded = true;
            }
        }
        if (isNewNumberNeeded) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }


    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private int[][] rotateCW(int[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;
        int[][] ret = new int[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M-1-r] = mat[r][c];
            }
        }
        return ret;
    }

    private void rotateClockwise() {
        gameField = rotateCW(gameField);
    }


    private int getMaxTileValue() {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                max = Math.max(max, gameField[j][i]);
            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "ты победил ура!!!", Color.LIGHTGREEN, 40);
    }

    private boolean canUserMove() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] == 0) {
                    return true;
                } else if (y < SIDE - 1 && gameField[y][x] == gameField[y + 1][x]) {
                    return true;
                } else if ((x < SIDE - 1) && gameField[y][x] == gameField[y][x + 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "проигрыш;(", Color.RED, 40);
    }
}
