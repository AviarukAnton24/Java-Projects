package game2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game2048 extends JPanel {
  private static final Color BG_COLOR = new Color(0xbbada0); // Цвет фона
  private static final String FONT_NAME = "Arial"; // Название шрифта
  private static final int TILE_SIZE = 64; // Размер плитки
  private static final int TILES_MARGIN = 16; // Отступ между плитками

  private Tile[] myTiles; // Массив плиток
  boolean myWin = false; // Флаг победы
  boolean myLose = false; // Флаг поражения
  int myScore = 0; // Счет игры

  /**
   * Конструктор класса
   */
  public Game2048() {
    setPreferredSize(new Dimension(340, 400)); // Установка предпочтительного размера панели
    setFocusable(true); // Установка фокуса
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // Обработка нажатия клавиши ESC
          resetGame();
        }
        if (canMove()) { // Проверка возможности хода
          myLose = true;
        }

        if (!myWin && !myLose) {
          switch (e.getKeyCode()) { // Обработка клавиш со стрелками
            case KeyEvent.VK_LEFT:
              left();
              break;
            case KeyEvent.VK_RIGHT:
              right();
              break;
            case KeyEvent.VK_DOWN:
              down();
              break;
            case KeyEvent.VK_UP:
              up();
              break;
          }
        }

        if (!myWin && canMove()) { // Проверка на возможность хода
          myLose = true;
        }

        repaint(); // Перерисовка игрового поля
      }
    });
    resetGame(); // Сброс игры
  }

  /**
   * Метод для сброса игры
   */
  public void resetGame() {
    myScore = 0; // Обнуление счета
    myWin = false; // Сброс флага победы
    myLose = false; // Сброс флага поражения
    myTiles = new Tile[4 * 4]; // Создание нового массива плиток
    for (int i = 0; i < myTiles.length; i++) {
      myTiles[i] = new Tile(); // Инициализация каждой плитки
    }
    addTile(); // Добавление двух новых плиток
    addTile();
  }

  /**
   * Метод для обработки движения плиток влево
   */
  public void left() {
    boolean needAddTile = false;
    for (int i = 0; i < 4; i++) {
      Tile[] line = getLine(i);
      Tile[] merged = mergeLine(moveLine(line));
      setLine(i, merged);
      if (!needAddTile && !compare(line, merged)) {
        needAddTile = true;
      }
    }

    if (needAddTile) {
      addTile();
    }
  }

  /**
   * Метод для обработки движения плиток вправо
   */
  public void right() {
    myTiles = rotate(180);
    left();
    myTiles = rotate(180);
  }

  /**
   * Метод для обработки движения плиток вверх
   */
  public void up() {
    myTiles = rotate(270);
    left();
    myTiles = rotate(90);
  }

  /**
   * Метод для обработки движения плиток вниз
   */
  public void down() {
    myTiles = rotate(90);
    left();
    myTiles = rotate(270);
  }

  /**
   * Метод для получения плитки по указанным координатам
   */
  private Tile tileAt(int x, int y) {
    return myTiles[x + y * 4];
  }

  /**
   * Метод для добавления новой плитки
   */
  private void addTile() {
    List<Tile> list = availableSpace();
    if (!availableSpace().isEmpty()) {
      int index = (int) (Math.random() * list.size()) % list.size();
      Tile emptyTime = list.get(index);
      emptyTime.value = Math.random() < 0.9 ? 2 : 4;
    }
  }

  /**
   * Метод для получения списка доступных плиток
   */
  private List<Tile> availableSpace() {
    final List<Tile> list = new ArrayList<>(16);
    for (Tile t : myTiles) {
      if (t.isEmpty()) {
        list.add(t);
      }
    }
    return list;
  }

  /**
   * Метод для проверки заполненности поля
   */
  private boolean isFull() {
    return availableSpace().isEmpty();
  }

  /**
   * Метод для проверки возможности хода
   */
  boolean canMove() {
    if (!isFull()) {
      return false;
    }
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        Tile t = tileAt(x, y);
        if ((x < 3 && t.value == tileAt(x + 1, y).value)
                || ((y < 3) && t.value == tileAt(x, y + 1).value)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Метод для сравнения двух линий плиток
   */
  private boolean compare(Tile[] line1, Tile[] line2) {
    if (line1 == line2) {
      return true;
    } else if (line1.length != line2.length) {
      return false;
    }

    for (int i = 0; i < line1.length; i++) {
      if (line1[i].value != line2[i].value) {
        return false;
      }
    }
    return true;
  }

  /**
   * Метод для поворота плиток на указанный угол
   */
  private Tile[] rotate(int angle) {
    Tile[] newTiles = new Tile[4 * 4];
    int offsetX = 3, offsetY = 3;
    if (angle == 90) {
      offsetY = 0;
    } else if (angle == 270) {
      offsetX = 0;
    }

    double rad = Math.toRadians(angle);
    int cos = (int) Math.cos(rad);
    int sin = (int) Math.sin(rad);
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        int newX = (x * cos) - (y * sin) + offsetX;
        int newY = (x * sin) + (y * cos) + offsetY;
        newTiles[(newX) + (newY) * 4] = tileAt(x, y);
      }
    }
    return newTiles;
  }

  /**
   * Метод для перемещения плиток в указанной линии
   */
  private Tile[] moveLine(Tile[] oldLine) {
    LinkedList<Tile> l = new LinkedList<>();
    for (int i = 0; i < 4; i++) {
      if (!oldLine[i].isEmpty())
        l.addLast(oldLine[i]);
    }
    if (l.isEmpty()) {
      return oldLine;
    } else {
      Tile[] newLine = new Tile[4];
      ensureSize(l);
      for (int i = 0; i < 4; i++) {
        newLine[i] = l.removeFirst();
      }
      return newLine;
    }
  }

  /**
   * Метод для объединения плиток в указанной линии
   */
  private Tile[] mergeLine(Tile[] oldLine) {
    LinkedList<Tile> list = new LinkedList<>();
    for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
      int num = oldLine[i].value;
      if (i < 3 && oldLine[i].value == oldLine[i + 1].value) {
        num *= 2;
        myScore += num;
        int ourTarget = 2048;
        if (num == ourTarget) {
          myWin = true;
        }
        i++;
      }
      list.add(new Tile(num));
    }
    if (list.isEmpty()) {
      return oldLine;
    } else {
      ensureSize(list);
      return list.toArray(new Tile[4]);
    }
  }

  /**
   * Метод для обеспечения указанного размера линии плиток
   */
  private static void ensureSize(List<Tile> l) {
    while (l.size() != 4) {
      l.add(new Tile());
    }
  }

  /**
   * Метод для получения линии плиток по указанному индексу
   */
  private Tile[] getLine(int index) {
    Tile[] result = new Tile[4];
    for (int i = 0; i < 4; i++) {
      result[i] = tileAt(i, index);
    }
    return result;
  }

  /**
   * Метод для установки линии плиток по указанному индексу
   */
  private void setLine(int index, Tile[] re) {
    System.arraycopy(re, 0, myTiles, index * 4, 4);
  }

  /**
   * Метод для отрисовки игрового поля
   */
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.setColor(BG_COLOR);
    g.fillRect(0, 0, this.getSize().width, this.getSize().height);
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        drawTile(g, myTiles[x + y * 4], x, y);
      }
    }
  }

  /**
   * Метод для отрисовки плитки
   */
  private void drawTile(Graphics g2, Tile tile, int x, int y) {
    Graphics2D g = ((Graphics2D) g2);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
    int value = tile.value;
    int xOffset = offsetCoors(x);
    int yOffset = offsetCoors(y);
    g.setColor(tile.getBackground());
    g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
    g.setColor(tile.getForeground());
    final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
    final Font font = new Font(FONT_NAME, Font.BOLD, size);
    g.setFont(font);

    String s = String.valueOf(value);
    final FontMetrics fm = getFontMetrics(font);

    final int w = fm.stringWidth(s);
    final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

    if (value != 0)
      g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);

    if (myWin || myLose) { // Отрисовка сообщений о победе или поражении
      g.setColor(new Color(255, 255, 255, 30));
      g.fillRect(0, 0, getWidth(), getHeight());
      g.setColor(new Color(78, 139, 202));
      g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
      if (myWin) {
        g.drawString("Вы выиграли!", 68, 150);
      }
      if (myLose) {
        g.drawString("Игра окончена!", 50, 130);
        g.drawString("Вы проиграли!", 64, 200);
      }
      if (myWin || myLose) {
        g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
        g.setColor(new Color(128, 128, 128, 128));
        g.drawString("Нажмите ESC для начала заново", 80

                , getHeight() - 40);
      }
    }
    g.setFont(new Font(FONT_NAME, Font.PLAIN, 28));
    g.drawString("Счет: " + myScore, 120, 355);

  }

  /**
   * Метод для вычисления смещения плитки по указанной координате
   */
  private static int offsetCoors(int arg) {
    return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
  }

  /**
   * Вложенный класс для представления плитки
   */
  static class Tile {
    int value; // Значение плитки

    public Tile() {
      this(0);
    }

    public Tile(int num) {
      value = num;
    }

    public boolean isEmpty() {
      return value == 0;
    }

    public Color getForeground() {
      return value < 16 ? new Color(0x776e65) :  new Color(0xf9f6f2);
    }

    public Color getBackground() {
        return switch (value) {
            case 2 -> new Color(0xeee4da);
            case 4 -> new Color(0xede0c8);
            case 8 -> new Color(0xf2b179);
            case 16 -> new Color(0xf59563);
            case 32 -> new Color(0xf67c5f);
            case 64 -> new Color(0xf65e3b);
            case 128 -> new Color(0xedcf72);
            case 256 -> new Color(0xedcc61);
            case 512 -> new Color(0xedc850);
            case 1024 -> new Color(0xedc53f);
            case 2048 -> new Color(0xedc22e);
            default -> new Color(0xcdc1b4);
        };
    }
  }

  /**
   * Метод main для запуска игры
   */
  public static void main(String[] args) {
    JFrame game = new JFrame();
    game.setTitle("Игра 2048");
    game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    game.setSize(340, 400);
    game.setResizable(false);

    game.add(new Game2048());

    game.setLocationRelativeTo(null);
    game.setVisible(true);
  }
}
