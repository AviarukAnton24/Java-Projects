package PongGame;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {

	public static final int SIZE = 16; // Размер мяча

	private int x, y; // Позиция мяча по осям X и Y
	private int xVel, yVel; // Скорость мяча по осям X и Y
	private final int speed = 5; // Скорость мяча

	// Конструктор мяча
	public Ball() {
		reset(); // Сбросить мяч в начальное положение
	}

	// Метод сброса мяча в начальное положение
	private void reset() {
		x = Game.WIDTH / 2 - SIZE / 2; // Установить мяч в центр по X
		y = Game.HEIGHT / 2 - SIZE / 2; // Установить мяч в центр по Y

		xVel = Game.sign(Math.random() * 2 - 1); // Случайное направление по X
		yVel = Game.sign(Math.random() * 2 - 1); // Случайное направление по Y
	}

	// Изменить направление движения по X
	public void changeXDir() {
		xVel *= -1; // Инвертировать направление по X
	}

	// Изменить направление движения по Y
	public void changeYDir() {
		yVel *= -1; // Инвертировать направление по Y
	}

	// Отрисовать мяч
	public void draw(Graphics g) {
		g.setColor(Color.white); // Установить цвет мяча белым
		g.fillOval(x, y, SIZE, SIZE); // Нарисовать мяч
	}

	// Обновить позицию мяча
	public void update(Paddle paddle1, Paddle paddle2) {
		x += xVel * speed; // Изменить позицию по X
		y += yVel * speed; // Изменить позицию по Y

		// Проверка столкновения с верхней и нижней границами
		if (y >= Game.HEIGHT - SIZE || y <= 0) {
			changeYDir(); // Изменить направление по Y
		}

		// Проверка, если мяч ушел за правую границу
		if (x >= Game.WIDTH - SIZE) {
			paddle1.addPoint(); // Добавить очко игроку 1
			reset(); // Сбросить мяч
		}

		// Проверка, если мяч ушел за левую границу
		if (x <= 0) {
			paddle2.addPoint(); // Добавить очко игроку 2
			reset(); // Сбросить мяч
		}
	}

	// Получить текущую позицию по X
	public int getX() {
		return x;
	}

	// Получить текущую позицию по Y
	public int getY() {
		return y;
	}
}
