import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameCompleteScreen extends JPanel {
    private JFrame frame;
    private Main main;

    public GameCompleteScreen(JFrame frame, Main main) {
        this.frame = frame;
        this.main = main;

        setLayout(new BorderLayout());
        setBackground(new Color(0, 100, 0)); // Dark green background

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 100, 0));
        JLabel titleLabel = new JLabel("CONGRATULATIONS!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        // Message panel
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(new Color(0, 100, 0));

        JLabel completeLabel = new JLabel("You have completed all 10 levels!");
        completeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        completeLabel.setForeground(Color.WHITE);
        completeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Calculate total time
        long totalTime = System.currentTimeMillis() - main.getGameStartTime();
        long seconds = totalTime / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;

        JLabel timeLabel = new JLabel(String.format("Total Time: %02d:%02d", minutes, seconds));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setForeground(Color.CYAN);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heroLabel = new JLabel("You are a true Dungeon Master!");
        heroLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        heroLabel.setForeground(new Color(255, 215, 0)); // Gold color (RGB)
        heroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messagePanel.add(Box.createVerticalGlue());
        messagePanel.add(completeLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        messagePanel.add(timeLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        messagePanel.add(heroLabel);
        messagePanel.add(Box.createVerticalGlue());

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 100, 0));
        buttonPanel.setLayout(new FlowLayout());

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 16));
        playAgainButton.setPreferredSize(new Dimension(150, 40));
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    main.restartGame();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(playAgainButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(exitButton);

        add(titlePanel, BorderLayout.NORTH);
        add(messagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}