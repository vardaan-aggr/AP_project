import javax.swing.*;

class instructorDashboard {

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();

        JButton mySections = new JButton("My Sections");
        mySections.setBounds(0, 100, 220, 50);
        frame.add(mySections);

        JButton gradeBook = new JButton("Grade Book");
        gradeBook.setBounds(300, 100, 220, 50);
        frame.add(gradeBook);
    
        JButton classStats = new JButton("Class Stats");
        classStats.setBounds(0, 500, 220, 50);
        frame.add(classStats);

        frame.setSize(500, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}