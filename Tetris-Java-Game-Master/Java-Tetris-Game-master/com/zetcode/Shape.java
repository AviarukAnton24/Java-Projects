package com.zetcode;

import java.util.Random;

public class Shape {

    // Перечисление для представления фигур
    protected enum Tetrominoe { NoShape, ZShape, SShape, LineShape,
        TShape, SquareShape, LShape, MirroredLShape }

    private Tetrominoe pieceShape; // Текущая фигура
    private int[][] coords; // Координаты фигуры
    private int[][][] coordsTable; // Таблица с координатами фигур

    public Shape() {
        initShape();
    }

    // Инициализация форм фигур и их координат
    private void initShape() {
        coords = new int[4][2];

        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } }, // Пустая фигура
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } }, // Фигура З
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } }, // Фигура S
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } }, // Фигура I
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } }, // Фигура T
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } }, // Фигура O
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } }, // Фигура L
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } } // Фигура J
        };

        setShape(Tetrominoe.NoShape);
    }

    // Установка формы и координат фигуры
    protected void setShape(Tetrominoe shape) {
        for (int i = 0; i < 4 ; i++) {
            System.arraycopy(coordsTable[shape.ordinal()][i], 0, coords[i], 0, 2);
        }
        pieceShape = shape;
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int x(int index) {
        return coords[index][0];
    }

    public int y(int index) {
        return coords[index][1];
    }

    public Tetrominoe getShape() {
        return pieceShape;
    }

    // Установка случайной формы фигуры
    public void setRandomShape() {
        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;

        Tetrominoe[] values = Tetrominoe.values();
        setShape(values[x]);
    }

    // Получение минимальной Y-координаты
    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    // Поворот фигуры влево
    public Shape rotateLeft() {
        if (pieceShape == Tetrominoe.SquareShape) {
            return this;
        }

        var result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }

    // Поворот фигуры вправо
    public Shape rotateRight() {
        if (pieceShape == Tetrominoe.SquareShape) {
            return this;
        }

        var result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }
}