package com.zetcode;

import com.zetcode.Shape.Tetrominoe;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Класс, описывающий игровую доску
public class Board extends JPanel {

    // Константы, определяющие размеры доски и интервал таймера
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;

    // Таймер для обновления игры
    private Timer timer;
    // Флаг, показывающий, что фигура падает
    private boolean isFallingFinished = false;
    // Флаг паузы
    private boolean isPaused = false;
    // Количество удаленных линий
    private int numLinesRemoved = 0;
    // Текущая позиция по оси X
    private int curX = 0;
    // Текущая позиция по оси Y
    private int curY = 0;
    // Статусная строка
    private JLabel statusbar;
    // Текущая фигура
    private Shape curPiece;
    // Массив, представляющий игровое поле
    private Tetrominoe[] board;

    // Конструктор класса Board
    public Board(Tetris parent) {
        // Инициализация игровой доски
        initBoard(parent);
    }

    // Метод для инициализации игровой доски
    private void initBoard(Tetris parent) {
        // Делаем игровую доску фокусируемой для обработки событий клавиатуры
        setFocusable(true);
        // Получаем статусную строку из родительского окна
        statusbar = parent.getStatusBar();
        // Добавляем слушатель клавиатуры для обработки нажатий клавиш
        addKeyListener(new TAdapter());
    }

    // Метод для вычисления ширины квадрата на игровой доске
    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    // Метод для вычисления высоты квадрата на игровой доске
    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    // Метод для получения фигуры в указанной позиции на доске
    private Tetrominoe shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    // Метод для запуска игры
    void start() {
        // Создаем новую фигуру
        curPiece = new Shape();
        // Инициализируем массив игрового поля
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
        // Очищаем игровое поле
        clearBoard();
        // Создаем новую фигуру
        newPiece();
        // Создаем и запускаем таймер для обновления игры
        int PERIOD_INTERVAL = 300;
        timer = new Timer(PERIOD_INTERVAL, new GameCycle());
        timer.start();
    }

    // Метод для паузы
    private void pause() {
        // Инвертируем флаг паузы
        isPaused = !isPaused;
        // Если игра на паузе, выводим сообщение "paused", иначе - количество удаленных линий
        if (isPaused) {
            statusbar.setText("paused");
        } else {
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        // Перерисовываем игровую доску
        repaint();
    }

    // Переопределенный метод для отрисовки компонента
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    // Метод для отрисовки игровой доски и фигур
    private void doDrawing(Graphics g) {
        var size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Tetrominoe.NoShape) {
                    drawSquare(g, j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }
        if (curPiece.getShape() != Tetrominoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        }
    }

    // Метод для опускания текущей фигуры вниз
    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    // Метод для опускания текущей фигуры на одну линию вниз
    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    // Метод для очистки игрового поля
    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetrominoe.NoShape;
        }
    }

    // Метод для обработки падения фигуры
    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }
        removeFullLines();
        if (!isFallingFinished) {
            newPiece();
        }
    }

    // Метод для создания новой фигуры
    private void newPiece() {
        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 +

                1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();
        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(Tetrominoe.NoShape);
            timer.stop();
            var msg = String.format("Game over. Score: %d", numLinesRemoved);
            statusbar.setText(msg);
        }
    }

    // Метод для попытки перемещения фигуры
    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (shapeAt(x, y) != Tetrominoe.NoShape) {
                return false;
            }
        }
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    // Метод для удаления полных линий
    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Tetrominoe.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }
        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            curPiece.setShape(Tetrominoe.NoShape);
        }
    }

    // Метод для отрисовки квадрата
    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {
        Color[] colors = {new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };
        var color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    // Внутренний класс для обработки игрового цикла
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    // Метод для выполнения игрового цикла
    private void doGameCycle() {
        update();
        repaint();
    }

    // Метод для обновления игрового состояния
    private void update() {
        if (isPaused) {
            return;
        }
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    // Внутренний класс для обработки событий клавиатуры
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (curPiece.getShape() == Tetrominoe.NoShape) {
                return;
            }
            int keycode = e.getKeyCode();
            switch (keycode) {
                case KeyEvent.VK_P -> pause();
                case KeyEvent.VK_LEFT -> tryMove(curPiece, curX - 1, curY);
                case KeyEvent.VK_RIGHT -> tryMove(curPiece, curX + 1, curY);
                case KeyEvent.VK_DOWN -> tryMove(curPiece.rotateRight(), curX, curY);
                case KeyEvent.VK_UP -> tryMove(curPiece.rotateLeft(), curX, curY);
                case KeyEvent.VK_SPACE -> dropDown();
                case KeyEvent.VK_D -> oneLineDown();
            }
        }
    }
}