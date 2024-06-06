package doodleJump;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class doodleApp extends JPanel {

	// переменные
	int score; // Очки
	int level; // Уровень
	int life; // Жизни
	Font sbFont; // Шрифт для отображения счета, уровня и жизней
	private boolean intro = true; // Флаг для отображения вступительного экрана
	private final Timer timer; // Таймер для обновления игрового состояния
	private final Rectangle doodleGuy;
    private final Rectangle p1;
    private final Rectangle p2;
    private final Rectangle p3;
    private final Rectangle p4;
    private final Rectangle p5;
    private final Rectangle p6;
    private final Rectangle p7;
    private final Rectangle p8;
    private final Rectangle p9; // Прямоугольники для отображения персонажа и платформ
	private final Random rnd; // Генератор случайных чисел для размещения платформ
    private boolean screen;

    public doodleApp() {
		setBackground(Color.black);
		sbFont = new Font("Comic Sans MS", Font.BOLD, 48);
		score = 0;
		level = 1;
		life = 1;
		rnd = new Random();
		screen = true;
        // Флаги для управления отображением экрана и стирания платформ

        timer = new Timer(10, new DoodleListener());

		doodleGuy = new Rectangle(400, 550, 75, 75, Color.BLUE);

		doodleGuy.setImage(new ImageIcon(ClassLoader.getSystemResource("doodleGuy2.png")));

		p1 = new Rectangle(400, 799, 100, 25, Color.RED);
		p2 = new Rectangle(500, 700, 100, 25, Color.RED);
		p3 = new Rectangle(300, 500, 100, 25, Color.RED);
		p4 = new Rectangle(200, 400, 100, 25, Color.RED);
		p5 = new Rectangle(600, 200, 100, 25, Color.RED);
		p6 = new Rectangle(100, 300, 100, 25, Color.RED);
		p7 = new Rectangle(-300, 400, 100, 25, Color.BLUE);
		p8 = new Rectangle(-300, 200, 100, 25, Color.BLUE);
		p9 = new Rectangle(-300, 300, 100, 25, Color.BLUE);

		timer.start();

		// слушатели
		addKeyListener(new pressKey());
		setFocusable(true);
	}

	// Метод для отрисовки границ игрового поля
	public void drawBorders(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.fillRect(50, 25, 825, 800); // Основное игровое поле
		g.fillRect(925, 25, 300, 800); // Правая боковая панель
	}

	// Метод для отображения счета, уровня и жизней
	public void scoreboard(Graphics g) {
		g.setFont(sbFont);
		g.setColor(Color.white);
		g.drawString("Счет:", 1000, 150);
		g.setColor(Color.orange);
		g.drawString("" + score, 1050, 200);

		g.setColor(Color.white);
		g.drawString("Уровень:", 1000, 350);
		g.setColor(Color.orange);
		g.drawString("" + level, 1050, 400);

		g.setColor(Color.white);
		g.drawString("Жизни:", 1000, 550);
		g.setColor(Color.orange);
		g.drawString("" + life, 1050, 600);
	}

	// Метод для отображения вступительного экрана
	public void introScreen(Graphics g) {
		g.setFont(sbFont);
		g.setColor(Color.white);
		g.drawString("Добро пожаловать в Doodle Jump", 300, 200);
		g.drawString("Достигайте максимальной высоты!", 300, 300);
		g.drawString("Он будет автоматически прыгать, только один раз на синем!", 50, 400);
		g.drawString("Стрелки влево и вправо для перемещения", 250, 500);
		g.drawString("Вы потеряете очки, если ударитесь о ту же платформу", 50, 600);
		g.drawString("Нажмите любую клавишу для начала", 300, 700);
	}

	// Метод для отображения экрана "Игра окончена"
	public void drawPlayAgain(Graphics g) {
		g.setFont(sbFont);
		g.setColor(Color.white);
		g.drawString("Игра окончена!", 350, 400);
		g.drawString("Играть еще? (y/n)", 300, 500);
	}

	// Переопределение метода paintComponent для отрисовки компонентов
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!intro) {
			drawBorders(g);
			scoreboard(g);
			doodleGuy.drawImage(g);
			p1.fill(g);
			p2.fill(g);
			p3.fill(g);
			p4.fill(g);
			p5.fill(g);
			p6.fill(g);
			p7.fill(g);
			p8.fill(g);
			p9.fill(g);
		} else {
			introScreen(g);
		}

		if (life <= 0) {
			drawPlayAgain(g);
		}
	}

	// Внутренний класс для обработки событий таймера
	private class DoodleListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!intro)

			{

				if (doodleGuy.getY() <= 400) {
					if (screen) {
						p1.setDy(doodleGuy.getDy() * -1);
						p2.setDy(doodleGuy.getDy() * -1);
						p3.setDy(doodleGuy.getDy() * -1);
						p4.setDy(doodleGuy.getDy() * -1);
						p5.setDy(doodleGuy.getDy() * -1);
						p6.setDy(doodleGuy.getDy() * -1);

						screen = false;
					}
					if (p1.getDy() >= 0) {
						p7.setDy(p1.getDy());
						p8.setDy(p1.getDy());
						p9.setDy(p1.getDy());
						p1.fallPlatform();
						score += (int) p1.getDy();
						p2.fallPlatform();
						p3.fallPlatform();
						p4.fallPlatform();
						p5.fallPlatform();
						p6.fallPlatform();
						p7.fallPlatform();
						p8.fallPlatform();
						p9.fallPlatform();
					}
					if (p1.getDy() < 0) {
						screen = true;
						doodleGuy.setY(401);
						doodleGuy.setDy(0);
					}
				} else {
					doodleGuy.fall();
					if (doodleGuy.lands(p1) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
					}
					if (doodleGuy.lands(p2) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
					}
					if (doodleGuy.lands(p3) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
					}
					if (doodleGuy.lands(p4) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
					}
					if (doodleGuy.lands(p5) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
					}
					if (doodleGuy.lands(p6) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
					}
					if (doodleGuy.lands(p7) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
						p7.setFall(true);
					}
					if (doodleGuy.lands(p8) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
						p8.setFall(true);
					}
					if (doodleGuy.lands(p9) && doodleGuy.getDy() >= 0) {
						doodleGuy.setDy(-9.25);
						playHit();
						p9.setFall(true);
					}
				}
				// обновление платформ
				if (p1.getY() >= 800) {
					p1.setY(50);
					p1.setX(rnd.nextInt(600) + 50);
				}
				if (p2.getY() >= 800) {
					p2.setY(50);
					p2.setX(rnd.nextInt(600) + 50);
				}
				if (p3.getY() >= 800) {
					p3.setY(50);
					p3.setX(rnd.nextInt(600) + 50);
				}
				if (p4.getY() >= 800 && level == 1) {
					p4.setY(50);
					p4.setX(rnd.nextInt(600) + 50);
				}
				if (p4.getY() >= 800 && level == 2 && p7.isErase()) {
					p4.setX(-100);
					p4.setY(50);
					p7.setDy(0);
					p7.setY(50);
					p7.setX(rnd.nextInt(600) + 50);
					p7.setErase(false);
				}
				if (p5.getY() >= 800 && level == 1) {
					p5.setY(50);
					p5.setX(rnd.nextInt(600) + 50);
				}
				if (p5.getY() >= 800 && level == 2 && p8.isErase()) {
					p5.setX(-100);
					p5.setY(50);
					p8.setDy(0);
					p8.setY(50);
					p8.setX(rnd.nextInt(600) + 50);
					p8.setErase(false);
				}
				if (

						p6.getY() >= 800 && level == 1) {
					p6.setY(50);
					p6.setX(rnd.nextInt(600) + 50);
				}
				if (p6.getY() >= 800 && level == 2 && p9.isErase()) {
					p6.setX(-100);
					p6.setY(50);
					p9.setDy(0);
					p9.setY(50);
					p9.setX(rnd.nextInt(600) + 50);
					p9.setErase(false);
				}
				// сброс синих
				if (p4.getY() >= 800 && level == 2 && !p7.isErase()) {
					p7.setFall(false);
					p7.setY(50);
					p7.setX(rnd.nextInt(600) + 50);
					p7.setDy(0);
					p4.setX(-100);
					p4.setY(50);
				}
				if (p5.getY() >= 800 && level == 2 && !p8.isErase()) {
					p8.setFall(false);
					p8.setY(50);
					p8.setX(rnd.nextInt(600) + 50);
					p8.setDy(0);
					p5.setX(-100);
					p5.setY(50);
				}
				if (p6.getY() >= 800 && level == 2 && !p9.isErase()) {
					p9.setFall(false);
					p9.setY(50);
					p9.setX(rnd.nextInt(600) + 50);
					p9.setDy(0);
					p6.setX(-100);
					p6.setY(50);
				}
				if (p7.getY() >= 800) {
					p7.setY(-500);
					p7.setX(-600);
					p7.setFall(false);
					p7.setDy(0);
				}
				if (p8.getY() >= 800) {
					p8.setY(-500);
					p8.setX(-600);
					p8.setFall(false);
					p8.setDy(0);
				}
				if (p9.getY() >= 800) {
					p9.setY(-500);
					p9.setX(-600);
					p9.setFall(false);
					p9.setDy(0);
				}
			}

			if (p7.isFall()) {
				p7.fall();
			}
			if (p8.isFall()) {
				p8.fall();
			}
			if (p9.isFall()) {
				p9.fall();
			}

			if (doodleGuy.getX() <= 25) doodleGuy.setX(749);
			if (doodleGuy.getX() >= 750) doodleGuy.setX(26);
			if (doodleGuy.getY() >= 900) life = 0;

			if (score >= 1000) level = 2;

			if (life <= 0) {
				timer.stop();
			}
			repaint();
		}
	}

	// Внутренний класс для обработки событий клавиш
	private class pressKey extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (intro) {
				timer.start();
				intro = false;
			}

			if (life <= 0) {
				if (e.getExtendedKeyCode() == KeyEvent.VK_Y) {
					score = 0;
					life = 1;
					level = 1;
					intro = true;

					p1.setX(400);
					p1.setY(799);

					p2.setX(500);
					p2.setY(700);

					p3.setX(300);
					p3.setY(500);

					p4.setX(200);
					p4.setY(400);

					p5.setX(600);
					p5.setY(200);

					p6.setX(100);
					p6.setY(300);

					p7.setX(-100);
					p7.setY(300);

					p8.setX(-100);
					p8.setY(300);

					p9.setX(-100);
					p9.setY(300);

					p7.setErase(true);
					p8.setErase(true);
					p9.setErase(true);

					doodleGuy.setX(400);
					doodleGuy.setY(550);
					doodleGuy.setDy(0);
					p7.setFall(false);
					p8.setFall(false);
					p9.setFall(false);
				}
				if (e.getExtendedKeyCode() == KeyEvent.VK_N) {
					System.exit(0);
				}
			} else {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					doodleGuy.move(20, 0);
				}

				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					doodleGuy.move(-20, 0);
				}
			}
			repaint();
		}
	}

	// Метод для воспроизведения звука при столкновении
	private void playHit() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResource("Liam Waterbury - smb_jump-small.wav"));
            // Аудио клип для звука при столкновении
            Clip hit = AudioSystem.getClip();
			hit.open(audioInputStream);
			hit.start();
		} catch (Exception e) {
			System.out.println("Ошибка: файл не найден");
			e.printStackTrace();
		}
	}

	// Главный метод, запускающий приложение
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(1300, 900);
		f.setTitle("Doodle Jump");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		doodleApp p = new doodleApp();

		Container c = f.getContentPane();
		c.add(p);

		f.setVisible(true);
	}
}