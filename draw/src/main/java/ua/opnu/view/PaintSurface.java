package ua.opnu.view;


import ua.opnu.model.DrawShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Поверхня для малювання фігур. Звичайний компонент Swing
 */
public class PaintSurface extends JComponent {

    // Список фігур, які намальовані на поверхні
    private final List<DrawShape> shapes = new ArrayList<>();

    // Тип поточної фігури
    private int shapeType;

    // Тут ми зберігаємо координати миші на початку перетягування і
    // при його закінченні. Клас Point зберігає координати точки (x, y)
    private Point startDrag;
    private Point endDrag;

    // Перелік кольорів
    private final List<Color> colors = Arrays.asList
            (Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.RED, Color.BLUE, Color.PINK);

    public PaintSurface() {

        // Спочатку тип фігури дорівнює 0
        shapeType = 0;

        // Встановлюємо бажані розміри поверхні.
        // Так як ми успадкувались від класу JComponent,
        // ми викликаємо метод цього класу
        super.setPreferredSize(new Dimension(400, 400));

        // Тут ми прописуємо реакцію на дії миші.
        // Ми повинні запрограмувати - що станеться, коли ми натиснемо на кнопку миші
        // і що станеться, коли ми відпустимо кнопку миші
        this.addMouseListener(new MouseAdapter() {

            // Цей метод буде виконано, коли користувач натисне на кнопку миші
            public void mousePressed(MouseEvent e) {

                // Коли користувач натискає на кнопку миші
                // ми запам'ятовуємо координати курсору відносно нашої поверхні
                // Після чого прирівнюємо endDrag і startDrag (тобто якщо ми просто клацнемо
                // і не будемо рухатимемо мишу, то startDrag і endDrag будуть ідентичні.
                // Після цього перемальовуємо поверхню (метод repaint()).

                startDrag = new Point(e.getX(), e.getY());
                endDrag = startDrag;
                repaint();
            }

            // Цей метод буде виконано, якщо користувач відпускає
            // кнопку миші
            public void mouseReleased(MouseEvent e) {

                // Коли ми відпускаємо кнопку миші, до списку shapes додається нова фігура.
                // Після цього ми очищаємо координати та перемальовуємо поверхню

                // Т.к. у нас кілька типів фігур, ми повинні:
                // 1 - отримати значення поля shapeType - воно містить тип фігури,
                // який ми маємо намалювати
                // 2. Залежно від значення shapeType – створити об'єкт того чи іншого класу
                // Якщо поле shapeType містить якесь інше значення або поле порожнє
                // то нічого не додаємо, т.к. це якась помилка

                DrawShape shape = DrawShape.newInstance(shapeType);

                // Записуємо значення стартової та кінцевої точки в об'єкт
                shape.setStartPoint(startDrag);
                shape.setEndPoint(endDrag);
                // Додаємо нову фігуру до списку
                shapes.add(shape);
                // Очищаємо стартові та кінцеві координати
                startDrag = null;
                endDrag = null;
                // Перемальовуємо поверхню
                repaint();
            }
        });

        // Цей метод буде виконано, якщо користувати робить рух drag (перетягування).
        // Тобто користувач затиснув кнопку миші та рухає мишу
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                // Коли ми рухаємо мишкою - ми запам'ятовуємо нові координати курсору та
                // перемальовуємо поверхню
                endDrag = new Point(e.getX(), e.getY());
                repaint();
            }
        });
    }

    // Встановлення типу фігури
    public void setShapeType(int type) {
        this.shapeType = type;
    }

    /*
     * Метод paint викликається автоматично, коли відбувається перемалювання
     * вікна. Ми самі його не викликаємо!
     *
     * Особливість роботи з 2D графікою така, що кожного разу ми маємо заново
     * перемальовувати вміст поверхні, у тому числі фонову сітку і всі фігури,
     * які ми додали раніше. Саме для цього і був створений список shapes.
     * Якби його не було, то постаті фігури зникали б при кожному перемальовуванні.
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // 1. Встановлюємо згладжування
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 2. Рисуємо фонову сітку
        paintBackgroundGrid(g2);
        // 3. Встановлюємо розмір рамки фігури у 2 пікселі
        g2.setStroke(new BasicStroke(2));
        // 4. Робимо напівпрозорим фон
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

        // Беремо кожну фігуру зі списку фігур та малюємо її на поверхні
        shapes.forEach(s -> {
            // Чорний колір обідка фігури
            g2.setPaint(Color.BLACK);
            // Рисуємо контур фігури
            g2.draw(s.getShape());
            // Встановлюємо колір фону фігури
            g2.setPaint(colors.get(shapes.indexOf(s) % 6));
            // Зафарбовуємо фігуру
            g2.fill(s.getShape());
        });

        // Якщо перемальовка відбувається в момент, коли ми малюємо нову фігуру
        // малюємо сірий контур фігури, яку хочемо намалювати
        if (startDrag != null && endDrag != null) {
            g2.setPaint(Color.LIGHT_GRAY);

            // Т.к. у нас кілька типів фігур, ми повинні:
            // 1 - отримати значення поля shapeType - воно містить тип фігури,
            // який ми маємо намалювати
            // 2. Залежно від значення shapeType – створити об'єкт того чи іншого класу
            // Якщо поле shapeType містить якесь інше значення або поле порожнє
            // то нічого не додаємо, т.к. це якась помилка
            DrawShape shape = DrawShape.newInstance(shapeType);

            // Рисуємо фігуру
            g2.draw(shape.getShape(startDrag, endDrag));
        }
    }

    // Даний метод відображає фон поверхні (сітка)
    private void paintBackgroundGrid(Graphics2D g2) {
        // Сітка матиме сірий колір
        g2.setPaint(Color.LIGHT_GRAY);

        // У циклі малюємо вертикальну лінію через кожні 10 пікселів за шириною
        for (int i = 0; i < getSize().width; i += 10) {
            // Створюємо просту лінію (4 параметри - початкова та кінцева точка)
            Shape line = new Line2D.Float(i, 0, i, getSize().height);
            g2.draw(line);
        }

        // Також через кожні 10 пікселів по висоті малюємо горизонтальну лінію
        for (int i = 0; i < getSize().height; i += 10) {
            // Створюємо просту лінію (4 параметри - початкова та кінцева точка)
            Shape line = new Line2D.Float(0, i, getSize().width, i);
            g2.draw(line);
        }
    }

    public void clearShapes() {
        shapes.clear();
        repaint();
    }
}

