package com.zetcode;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tetris extends JFrame {

    private JLabel statusbar;

    public Tetris() {
        initUI();
    }

    // Инициализация пользовательского интерфейса
    private void initUI() {
        statusbar = new JLabel("Score: 0");
        add(statusbar, BorderLayout.SOUTH);

        // Создание игровой доски
        Board board = new Board(this);
        add(board);
        board.start();

        // Настройка параметров окна
        setTitle("Tetris");
        setSize(400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Получение статусной строки
    public JLabel getStatusBar() {
        return statusbar;
    }

    public static void main(String[] args) {
        // Запуск приложения в отдельном потоке
        EventQueue.invokeLater(() -> {
            Tetris game = new Tetris();
            game.setVisible(true);
        });
    }
}