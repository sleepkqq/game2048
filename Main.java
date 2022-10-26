package com.javarush.games.game2048;

import com.javarush.engine.cell.Color;

import javax.swing.*;


public class Main extends Game2048 {
    public Main(int side) {
        super(side);
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        String getMessage = JOptionPane.showInputDialog(jFrame, "Введите длину стороны игрового поля");
        Game2048 main = new Game2048(Integer.parseInt(getMessage));
        main.showMessageDialog(Color.GRAY, getMessage, Color.ORANGE, 40);

    }
}
