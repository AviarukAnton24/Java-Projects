package PongGame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	private final Paddle p1; // Ракетка игрока 1
	private boolean up1 = false; // Флаг для клавиши "вверх" игрока 1
	private boolean down1 = false; // Флаг для клавиши "вниз" игрока 1

	private final Paddle p2; // Ракетка игрока 2
	private boolean up2 = false; // Флаг для клавиши "вверх" игрока 2
	private boolean down2 = false; // Флаг для клавиши "вниз" игрока 2

	// Конструктор, принимающий две ракетки
	public KeyInput(Paddle p1, Paddle p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	// Метод, вызываемый при нажатии клавиши
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode(); // Получаем код нажатой клавиши

		// Управление ракеткой игрока 2
		if (key == KeyEvent.VK_UP) {
			p2.switchDirection(-1); // Двигаем ракетку вверх
			up2 = true; // Устанавливаем флаг нажатия клавиши "вверх"
		}
		if (key == KeyEvent.VK_DOWN) {
			p2.switchDirection(1); // Двигаем ракетку вниз
			down2 = true; // Устанавливаем флаг нажатия клавиши "вниз"
		}

		// Управление ракеткой игрока 1
		if (key == KeyEvent.VK_W) {
			p1.switchDirection(-1); // Двигаем ракетку вверх
			up1 = true; // Устанавливаем флаг нажатия клавиши "вверх"
		}
		if (key == KeyEvent.VK_S) {
			p1.switchDirection(1); // Двигаем ракетку вниз
			down1 = true; // Устанавливаем флаг нажатия клавиши "вниз"
		}

		// Этот код можно использовать для начала игры при нажатии Enter
        if (key == KeyEvent.VK_ENTER) {
            Game.running = true;
        }
	}

	// Метод, вызываемый при отпускании клавиши
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode(); // Получаем код отпущенной клавиши

		// Отпускаем клавишу "вверх" у игрока 2
		if (key == KeyEvent.VK_UP) {
			up2 = false; // Сбрасываем флаг нажатия клавиши "вверх"
		}
		// Отпускаем клавишу "вниз" у игрока 2
		if (key == KeyEvent.VK_DOWN) {
			down2 = false; // Сбрасываем флаг нажатия клавиши "вниз"
		}

		// Отпускаем клавишу "вверх" у игрока 1
		if (key == KeyEvent.VK_W) {
			up1 = false; // Сбрасываем флаг нажатия клавиши "вверх"
		}
		// Отпускаем клавишу "вниз" у игрока 1
		if (key == KeyEvent.VK_S) {
			down1 = false; // Сбрасываем флаг нажатия клавиши "вниз"
		}

		// Остановка движения ракетки игрока 1, если не нажата клавиша "вверх" или "вниз"
		if (!up1 && !down1) {
			p1.stop();
		}
		// Остановка движения ракетки игрока 2, если не нажата клавиша "вверх" или "вниз"
		if (!up2 && !down2) {
			p2.stop();
		}
	}
}
