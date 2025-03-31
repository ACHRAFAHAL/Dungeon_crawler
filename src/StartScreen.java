import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StartScreen extends JPanel {
    private JFrame frame;
    private Main main;
    private Image backgroundImage;
    private Image titleImage;

    // Constructor now accepts a Main instance
    public StartScreen(JFrame frame, Main main) {
        this.frame = frame;
        this.main = main;
        this.setLayout(new BorderLayout());

        try {
            backgroundImage = ImageIO.read(new File("./img/background.jpg"));
            titleImage = ImageIO.read(new File("./img/title.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SoundManager.playSound("./sound/op.wav");
        JLabel titleLabel = new JLabel(new ImageIcon(titleImage));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.CENTER);
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);

        JButton startButton = new JButton("Start Game");
        JButton quitButton = new JButton("Quit");

        startButton.setFont(new Font("Serif", Font.BOLD, 30));
        quitButton.setFont(new Font("Serif", Font.BOLD, 30));

        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);
        this.add(buttonPanel, BorderLayout.CENTER);

        // Start button
        startButton.addActionListener(e -> {
            SoundManager.stopSound();
            try {
                main.startGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Quit button
        quitButton.addActionListener(e -> System.exit(0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
