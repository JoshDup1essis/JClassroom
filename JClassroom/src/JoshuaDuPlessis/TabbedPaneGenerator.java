package JoshuaDuPlessis;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TabbedPaneGenerator {
    private StorageDatabase storage;

    public TabbedPaneGenerator(StorageDatabase storage) {
        this.storage = storage;
    }

    public JTabbedPane generateTabbedPane(List<String> classNames) {
        JTabbedPane tabbedPane = new JTabbedPane();

        for (String className : classNames) {
            JPanel tabPanel = new JPanel();
            tabPanel.setBackground(Color.BLACK);
            tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));

            // Retrieve assignments for the current class
            List<Map<String, Object>> assignments = storage.getAssignmentsByClassName(className);

            for (Map<String, Object> assignment : assignments) {
                // Extract assignment details from the map
                int assignmentID = (int) assignment.get("AssignmentID");
                String title = (String) assignment.get("AssTitle");
                String details = (String) assignment.get("Details");
                Date dueDate = (Date) assignment.get("DueDate");

                // Create a panel for each assignment with an outline border
                JPanel assignmentPanel = createAssignmentPanel(title, details, dueDate);
                tabPanel.add(assignmentPanel);

                // Add vertical space between assignments
                
                tabPanel.add(Box.createVerticalStrut(10));
                
            }

            // Add the tabPanel to the tabbedPane
            tabbedPane.addTab(className, tabPanel);
        }

        return tabbedPane;
    }

    private JPanel createAssignmentPanel(String title, String details, Date dueDate) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Create an outline border for the panel
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        panel.setBorder(border);

        // Create labels for assignment details
        JLabel titleLabel = createHeadingLabel("Title: " + title);
        JLabel detailsLabel = createHeadingLabel("Details: " + details);
        JLabel dueDateLabel = createHeadingLabel("Due Date: " + formatDate(dueDate));

        // Add the labels to the assignment panel
        panel.add(titleLabel);
        panel.add(detailsLabel);
        panel.add(dueDateLabel);

        return panel;
    }

    private JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        return label;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
