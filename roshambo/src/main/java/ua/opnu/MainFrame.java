package ua.opnu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MainFrame extends JFrame implements ActionListener {

    public MainFrame(String title) throws HeadlessException {
        super(title);

        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.WHITE));

        JButton rockButton = new JButton("Камінь");
        rockButton.addActionListener(this);
        rockButton.setActionCommand("rock");
        JButton paperButton = new JButton("Папір");
        paperButton.addActionListener(this);
        paperButton.setActionCommand("paper");
        JButton scissorsButton = new JButton("Ножиці");
        scissorsButton.addActionListener(this);
        scissorsButton.setActionCommand("scissors");

        this.add(rockButton);
        this.add(paperButton);
        this.add(scissorsButton);

        this.pack();
        this.setVisible(true);
    }

    private GameShape generateShape() {
        // Метод повертає об'єкт ігрової фігури випадковим чином
        int random = new Random().nextInt(3);
        switch (random) {
            case 0:
                return new Rock();
            case 1:
                return new Paper();
            case 2:
            default:
                return new Scissors();
        }
    }

    private int checkWinner(GameShape player, GameShape computer) {
        // first check tie
        if (player.getClass() == computer.getClass()) {
            return 0;
        }

        // player wins cases
        if (player instanceof Rock) {
            if (computer instanceof Scissors) return 1; // камінь б'є ножиці
            if (computer instanceof Paper) return -1;   // папір б'є камінь
        } else if (player instanceof Paper) {
            if (computer instanceof Rock) return 1;     // папір б'є камінь
            if (computer instanceof Scissors) return -1;// ножиці б'ють папір
        } else if (player instanceof Scissors) {
            if (computer instanceof Paper) return 1;    // ножиці б'ють папір
            if (computer instanceof Rock) return -1;    // камінь б'є ножиці
        }

        // На всякий випадок (має не відбуватись) — нічия
        return 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Генерується хід комп'ютера
        GameShape computerShape = generateShape();

        GameShape playerShape = null;
        // Визначаємо, на яку кнопку натиснув гравець
        switch (e.getActionCommand()) {
            case "rock":
                playerShape = new Rock();
                break;
            case "paper":
                playerShape = new Paper();
                break;
            case "scissors":
                playerShape = new Scissors();
                break;
            default:
                playerShape = new Rock(); // захисне присвоєння (не має статись)
        }

        // Визначити результат гри (параметри: гравець, комп'ютер)
        int gameResult = checkWinner(playerShape, computerShape);

        // Сформувати повідомлення
        String message = "Player shape: " + playerShape + ". Computer shape: " + computerShape + ". ";
        switch (gameResult) {
            case -1:
                message += "Computer has won!";
                break;
            case 0:
                message += "It's a tie!";
                break;
            case 1:
                message += "Player has won!";
                break;
        }

        // Вивести діалогове вікно з повідомленням
        JOptionPane.showMessageDialog(null, message);
    }
}
