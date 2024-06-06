package PongGame;

import javax.swing.JFrame;

public class Window {

	// Конструктор класса Window
	public Window(String title, Game game) {
		// Создание нового фрейма
		JFrame frame = new JFrame(title);

		// Установка операции закрытия по умолчанию
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Запрет изменения размеров окна
		frame.setResizable(false);

		// Добавление игрового объекта во фрейм
		frame.add(game);
		// Автоматическое подгонка размеров окна
		frame.pack();
		// Размещение окна по центру экрана
		frame.setLocationRelativeTo(null);
		// Отображение окна
		frame.setVisible(true);

		// Запуск игры
		game.start();
	}
}