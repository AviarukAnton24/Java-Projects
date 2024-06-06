package doodleJump;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import java.awt.Color;

public class Rectangle {
	private int x; // координата x
	private int y; // координата y
	private final int h; // высота
	private final int b;
    // скорость вертикального и временная скорость
    private final Color c; // цвет
	private boolean fire, fall, erase; // флаги для огня, падения, стирания
	private double dy; // скорость падения
	private ImageIcon img; // изображение

	// Конструктор
	public Rectangle(int x, int y, int b, int h, Color c) {
		this.x = x;
		this.y = y;
		this.b = b;
		this.h = h;
		this.c = c;
        // ширина, переменные для гравитации
        this.fire = false;
		this.dy = 1;
		this.fall = false;
		erase = true;
	}

	// Метод для получения значения флага стирания
	public boolean isErase() {
		return erase;
	}

	// Метод для установки значения флага стирания
	public void setErase(boolean erase) {
		this.erase = erase;
	}

	// Метод для отрисовки прямоугольника
	public void draw(Graphics g) {
		Color old = g.getColor();
		g.setColor(c);
		g.drawRect(x, y, b, h);
		g.setColor(old);
	}

	// Метод для заполнения прямоугольника цветом
	public void fill(Graphics g) {
		Color old = g.getColor();
		g.setColor(c);
		g.fillRect(x, y, b, h);
		g.setColor(old);
	}

	// Метод для проверки принадлежности точки прямоугольнику
	public boolean containsPoint(int mx, int my) {
        return mx > x && mx < x + b && my > y && my < y + h;
	}

	// Метод для установки изображения
	public void setImage(ImageIcon i) {
		img = i;
	}

	// Метод для получения ширины изображения
	public int getBase(ImageIcon i) {
		return i.getIconWidth();
	}

	// Метод для отрисовки изображения
	public void drawImage(Graphics g) {
		img.paintIcon(null, g, x, y);
	}

	// Метод для перемещения прямоугольника
	public void move(int xAmount, int yAmount) {
		x += xAmount;
		y += yAmount;
	}

	// Метод для падения прямоугольника
	public void fall() {
		dy = dy + .2;
		y = y + (int) dy;
	}

	// Метод для падения платформы
	public void fallPlatform() {
		dy = dy - .2;
		y = y + (int) dy;
	}

	// Метод для получения координаты x
	public int getX() {
		return x;
	}

	// Метод для установки координаты x
	public void setX(int x) {
		this.x = x;
	}

	// Метод для получения координаты y
	public int getY() {
		return y;
	}

	// Метод для установки координаты y
	public void setY(int y) {
		this.y = y;
	}

	// Метод для получения скорости падения
	public double getDy() {
		return dy;
	}

	// Метод для установки скорости падения
	public void setDy(double dy) {
		this.dy = dy;
	}

	// Метод для определения статуса огня
	public boolean isFire() {
		return fire;
	}

	// Метод для установки статуса огня
	public void setFire(boolean fire) {
		this.fire = fire;
	}

	// Метод для получения ширины
	public int getB() {
		return b;
	}

	// Метод для получения высоты
	public int getH() {
		return h;
	}

	// Метод для проверки статуса падения
	public boolean isFall() {
		return fall;
	}

	// Метод для установки статуса падения
	public void setFall(boolean fall) {
		this.fall = fall;
	}

	// Метод для определения столкновения персонажа с платформой
	public boolean lands(Rectangle p) {
        return x + 20 < p.getX() + p.getB() && x + b + 20 > p.getX() && y + 100 >= p.getY() - p.getH() * 1.5
                && y <= p.getY() - p.getH() * 3;
    }
}