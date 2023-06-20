

package JoshuaDuPlessis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomWindowControls extends JFrame {
    private JPanel titleBar;
    private JLabel titleLabel;
    private JButton minimizeButton;
    private JButton maximizeButton;
    private JButton closeButton;

    public CustomWindowControls() {
        setTitle("Custom Window Controls");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Set the JFrame to be undecorated
        setUndecorated(true);

        // Create the title bar panel
        titleBar = new JPanel();
        titleBar.setBackground(Color.GRAY);
        titleBar.setLayout(new BorderLayout());

        // Create the title label and center it horizontally
        titleLabel = new JLabel("Custom Window");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleBar.add(titleLabel, BorderLayout.CENTER);

        // Create the minimize button
        minimizeButton = new JButton("-");
        minimizeButton.setPreferredSize(new Dimension(40, 20));
        minimizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(Frame.ICONIFIED);
            }
        });
        titleBar.add(minimizeButton, BorderLayout.LINE_END);

        // Create the maximize button
        maximizeButton = new JButton("+");
        maximizeButton.setPreferredSize(new Dimension(40, 20));
        maximizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getExtendedState() == Frame.MAXIMIZED_BOTH) {
                    setExtendedState(Frame.NORMAL);
                } else {
                    setExtendedState(Frame.MAXIMIZED_BOTH);
                }
            }
        });
        titleBar.add(maximizeButton, BorderLayout.LINE_END);

        // Create the close button
        closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(40, 20));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        titleBar.add(closeButton, BorderLayout.LINE_END);

        // Add the title bar to the JFrame
        getContentPane().add(titleBar, BorderLayout.NORTH);

        // Set the JFrame visible
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomWindowControls::new);
    }
}
