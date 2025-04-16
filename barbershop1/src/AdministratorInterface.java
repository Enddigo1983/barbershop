import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministratorInterface extends JFrame {

    private JTextField textField;
    private JButton addServiceButton;
    private JButton addMasterButton;
    private JButton viewAppointmentsButton;
    private JLabel label;

    public AdministratorInterface() {
        // Set up the main frame
        setTitle("Barber Shop Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null); // Center the window

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title at the top
        JLabel titleLabel = new JLabel("ДОБРО ПОЖАЛОВАТЬ, АДМИНИСТРАТОР!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for inputs and buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Welcome message
        label = new JLabel("Выберите действие:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(label);
        centerPanel.add(Box.createVerticalStrut(20));

        // Text field for input
        textField = new JTextField(20);
        textField.setMaximumSize(new Dimension(300, 30));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(textField);
        centerPanel.add(Box.createVerticalStrut(10));

        // Add a new service button
        addServiceButton = new JButton("Добавить новую услугу");
        addServiceButton.setBackground(new Color(0, 120, 215)); // Blue color
        addServiceButton.setForeground(Color.WHITE);
        addServiceButton.setFont(new Font("Arial", Font.BOLD, 14));
        addServiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addServiceButton.setMaximumSize(new Dimension(300, 40));
        addServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serviceName = textField.getText();
                if (serviceName != null && !serviceName.isEmpty()) {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:barbershop.db");
                        PreparedStatement statement = connection.prepareStatement("INSERT INTO services (name) VALUES (?)");
                        statement.setString(1, serviceName);
                        statement.executeUpdate();
                        connection.close();

                        JOptionPane.showMessageDialog(AdministratorInterface.this, "Услуга добавлена!");
                        textField.setText("");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(AdministratorInterface.this, "Ошибка при добавлении услуги!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdministratorInterface.this, "Пожалуйста, введите название услуги!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        centerPanel.add(addServiceButton);
        centerPanel.add(Box.createVerticalStrut(10));

        // Add a new master button
        addMasterButton = new JButton("Добавить нового мастера");
        addMasterButton.setBackground(new Color(0, 120, 215)); // Blue color
        addMasterButton.setForeground(Color.WHITE);
        addMasterButton.setFont(new Font("Arial", Font.BOLD, 14));
        addMasterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addMasterButton.setMaximumSize(new Dimension(300, 40));
        addMasterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String masterName = textField.getText();
                if (masterName != null && !masterName.isEmpty()) {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:barbershop.db");
                        PreparedStatement statement = connection.prepareStatement("INSERT INTO masters (name) VALUES (?)");
                        statement.setString(1, masterName);
                        statement.executeUpdate();
                        connection.close();

                        JOptionPane.showMessageDialog(AdministratorInterface.this, "Мастер добавлен!");
                        textField.setText("");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(AdministratorInterface.this, "Ошибка при добавлении мастера!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdministratorInterface.this, "Пожалуйста, введите имя мастера!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        centerPanel.add(addMasterButton);
        centerPanel.add(Box.createVerticalStrut(10));

        // View all appointments button
        viewAppointmentsButton = new JButton("Список записей");
        viewAppointmentsButton.setBackground(new Color(0, 120, 215)); // Blue color
        viewAppointmentsButton.setForeground(Color.WHITE);
        viewAppointmentsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewAppointmentsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewAppointmentsButton.setMaximumSize(new Dimension(300, 40));
        viewAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = DriverManager.getConnection("jdbc:sqlite:barbershop.db");
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT a.name AS client, s.name AS service, m.name AS master " +
                                    "FROM appointments a " +
                                    "JOIN services s ON a.service_id = s.id " +
                                    "JOIN masters m ON a.master_id = m.id");
                    ResultSet resultSet = statement.executeQuery();

                    StringBuilder appointments = new StringBuilder("<html><b>Список записей:</b><br>");
                    while (resultSet.next()) {
                        appointments.append("Клиент: ").append(resultSet.getString("client"))
                                .append(", Услуга: ").append(resultSet.getString("service"))
                                .append(", Мастер: ").append(resultSet.getString("master"))
                                .append("<br>");
                    }
                    appointments.append("</html>");

                    JOptionPane.showMessageDialog(AdministratorInterface.this, appointments.toString());
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AdministratorInterface.this, "Ошибка при загрузке записей!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        centerPanel.add(viewAppointmentsButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("<html><center>ИНФОРМАЦИЯ ДЛЯ АДМИНИСТРАТОРА<br>Добавляйте услуги и мастеров.<br>Просматривайте записи клиентов.</center></html>", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}