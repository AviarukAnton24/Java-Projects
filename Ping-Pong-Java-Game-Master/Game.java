package PongGame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1000; // Ширина окна игры
	public static final int HEIGHT = WIDTH * 9/16; // Высота окна игры

	Ball ball; // Мяч
	Paddle paddle1; // Ракетка игрока 1
	Paddle paddle2; // Ракетка игрока 2

	public static boolean running = false; // Флаг, определяющий, запущена ли игра
	private Thread gameThread; // Поток для запуска игры

	// Главный метод программы
	public static void main(String[] args) {
		new Game(); // Создание нового объекта игры и запуск конструктора
	}

	// Конструктор игры
	public Game() {
		canvasSetup(); // Настройка размеров канвы
		initialize(); // Инициализация объектов игры
		new Window("Ping Pong", this); // Создание окна игры
		this.addKeyListener(new KeyInput(paddle1, paddle2)); // Добавление слушателя клавиатуры
		this.setFocusable(true); // Установка фокуса на канву
	}

	// Метод для инициализации объектов игры
	private void initialize() {
		ball = new Ball(); // Создание мяча
		paddle1 = new Paddle(Color.CYAN, true); // Создание ракетки игрока 1
		paddle2 = new Paddle(Color.ORANGE, false); // Создание ракетки игрока 2
	}

	// Метод для настройки размеров канвы
	private void canvasSetup() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Установка предпочтительного размера
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT)); // Установка максимального размера
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT)); // Установка минимального размера
	}

	// Метод, запускаемый в отдельном потоке
	@Override
	public void run() {
		this.requestFocus(); // Установка фокуса на канву

		// Таймер игры
		long lastTime = System.nanoTime();
		double maxFPS = 60.0;
		double frameTime = 1000000000 / maxFPS;
		double delta = 0;

		// Основной игровой цикл
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / frameTime;
			lastTime = now;

			if (delta >= 1) {
				update(); // Обновление состояния игры
				delta--;
				draw(); // Отрисовка игры
			}
		}
		stop(); // Остановка игры после выхода из цикла
	}

	// Метод для отрисовки игры
	private void draw() {
		BufferStrategy buffer = this.getBufferStrategy(); // Получение стратегии буферизации
		if (buffer == null) {
			this.createBufferStrategy(3); // Создание стратегии буферизации
			return;
		}
		Graphics g = buffer.getDrawGraphics(); // Получение объекта для рисования

		// Отрисовка фона
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Отрисовка мяча
		ball.draw(g);

		// Отрисовка ракеток
		paddle1.draw(g);
		paddle2.draw(g);

		g.dispose(); // Очистка ресурсов
		buffer.show(); // Отображение кадра
	}

	// Метод для обновления состояния игры
	private void update() {
		ball.update(paddle1, paddle2); // Обновление состояния мяча
		paddle1.update(ball); // Обновление состояния ракетки игрока 1
		paddle2.update(ball); // Обновление состояния ракетки игрока 2
	}

	// Метод для запуска игры
	public void start() {
		gameThread = new Thread(this); // Создание нового потока для игры
		gameThread.start(); // Запуск игрового потока
		running = true; // Установка флага, что игра запущена
	}

	// Метод для остановки игры
	public void stop() {
		try {
			gameThread.join(); // Ожидание завершения игрового потока
			running = false; // Установка флага, что игра остановлена
		} catch (InterruptedException e) {
			e.printStackTrace(); // Вывод ошибки, если что-то пошло не так
		}
	}

	// Метод для определения знака числа
	public static int sign(double d) {
		if (d >= 0) {
			return 1; // Возвращает 1, если число положительное
		} else {
			return -1; // Возвращает -1, если число отрицательное
		}
	}
}