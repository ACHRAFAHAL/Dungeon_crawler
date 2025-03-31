import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameOverScreen extends JPanel {
    private JFrame frame;
    private Main main;
    private Image GameOverbackgroundImage;
    private Image GameOvertitleImage;

    public GameOverScreen(JFrame frame, Main main) {
        this.frame = frame;
        this.main = main;
        this.setLayout(new BorderLayout());

        try {
            System.out.println("Game over triggered!");
            GameOverbackgroundImage = ImageIO.read(new File("./img/GameOverbackground.png"));
            GameOvertitleImage = ImageIO.read(new File("./img/GameOvertitle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel titleLabel = new JLabel(new ImageIcon(GameOvertitleImage));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.NORTH);
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        JButton restartButton = new JButton("RESTART");
        JButton quitButton = new JButton("Quit");

        restartButton.setFont(new Font("Serif", Font.BOLD, 30));
        quitButton.setFont(new Font("Serif", Font.BOLD, 30));

        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);
        this.add(buttonPanel, BorderLayout.CENTER);

        // reStart button
        restartButton.addActionListener(e -> {
            SoundManager.stopSound();
            try {
                main.startGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Quit button action to exit the application
        quitButton.addActionListener(e -> System.exit(0));
    }

    // Override paintComponent to draw the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (GameOverbackgroundImage != null) {
            g.drawImage(GameOverbackgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
