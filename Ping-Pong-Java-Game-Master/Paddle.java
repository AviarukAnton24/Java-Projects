package PongGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Paddle {

	private int x, y; // Координаты ракетки
	private final int speed = 10; // Скорость движения ракетки
	private final int width = 25, height = 100; // Ширина и высота ракетки
	private int score; // Очки игрока
	private final Color color; // Цвет ракетки
	private final boolean left; // Флаг, указывающий, является ли ракетка левой
	private int vel = 0; // Скорость ракетки

	// Конструктор класса Paddle
	public Paddle(Color c, boolean left) {
		color = c; // Установка цвета ракетки
		this.left = left; // Установка флага, указывающего на сторону ракетки
		if (this.left) {
			x = 0; // Если ракетка левая, начальная позиция по оси X равна 0
		} else {
			x = Game.WIDTH - width; // Если ракетка правая, начальная позиция по оси X равна ширине окна минус ширина ракетки
		}
		y = Game.HEIGHT / 2 - height / 2; // Начальная позиция по оси Y находится в центре окна по вертикали
	}

	// Метод для увеличения очков игрока
	public void addPoint() {
		score++;
	}

	// Метод для отрисовки ракетки и счета игрока
	public void draw(Graphics g) {

		// Отрисовка ракетки
		g.setColor(color);
		g.fillRect(x, y, width, height);

		// Отрисовка счета игрока
		String scoreText = Integer.toString(score);
		int sx;
		Font font = new Font("ROBOTO", Font.PLAIN, 50);
		int strWidth = g.getFontMetrics(font).stringWidth(scoreText);
		int padding = 25;

		if (left) {
			sx = Game.WIDTH / 2 - strWidth - padding;
		} else {
			sx = Game.WIDTH / 2 + padding;
		}

		g.setFont(font);
		g.drawString(scoreText, sx, 50);
	}

	// Метод для обновления состояния ракетки
	public void update(Ball ball) {
		// Движение ракетки
		if (y > Game.HEIGHT - height) {
			y = Game.HEIGHT - height; // Ограничение нижней границы
		} else if (y < 0) {
			y = 0; // Ограничение верхней границы
		} else {
			y += vel; // Обновление позиции по оси Y
		}

		int ballX = ball.getX(); // Получение координаты X мяча
		int ballY = ball.getY(); // Получение координаты Y мяча

		// Проверка столкновений с мячом
		if (left) {
			if (ballX <= width && ballY >= y - Ball.SIZE && ballY <= y + height) {
				ball.changeXDir(); // Изменение направления движения мяча
			}
		} else {
			if (ballX >= x - Ball.SIZE && ballY >= y - Ball.SIZE && ballY <= y + height) {
				ball.changeXDir(); // Изменение направления движения мяча
			}
		}

	}

	// Метод для управления движением ракетки
	public void switchDirection(int dir) {
		vel = speed * dir; // Установка скорости ракетки
	}

	// Метод для остановки движения ракетки
	public void stop() {
		vel = 0; // Установка скорости ракетки в 0 для остановки
	}
}
