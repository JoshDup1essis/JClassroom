/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package JoshuaDuPlessis;

import java.awt.Color;
import javax.swing.*;

public class TabbedPaneExample extends JFrame {

    public TabbedPaneExample() {
        setTitle("Tabbed Pane Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane);

        // Create tabs
        JPanel tab1 = new JPanel();
        JPanel tab2 = new JPanel();
        JPanel tab3 = new JPanel();

        // Add content to tabs
        JButton button = new JButton("Click Me!");
        tab2.add(button);

        // Set tab titles
        tabbedPane.addTab("Tab 1", tab1);
        tabbedPane.addTab("Tab 2", tab2);
        tabbedPane.addTab("Tab 3", tab3);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TabbedPaneExample();
            }
        });
    }
}
