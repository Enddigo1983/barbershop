import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserInterface extends JFrame {

    private JTextField textField;
    private JComboBox<String> serviceComboBox;
    private JComboBox<String> masterComboBox;
    private JButton button;
    private JLabel label;

    public UserInterface() {

        setTitle("Barber Shop Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null); // Center the window


        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);


        JLabel titleLabel = new JLabel("ДОБРО ПОЖАЛОВАТЬ, КЛИЕНТ!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);


        label = new JLabel("Выберите действие:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(label);
        centerPanel.add(Box.createVerticalStrut(20));


        textField = new JTextField(20);
        textField.setMaximumSize(new Dimension(300, 30));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(new JLabel("Ваше имя:"));
        centerPanel.add(textField);
        centerPanel.add(Box.createVerticalStrut(10));


        serviceComboBox = new JComboBox<>(loadServices());
        serviceComboBox.setMaximumSize(new Dimension(300, 30));
        serviceComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(new JLabel("Выберите услугу:"));
        centerPanel.add(serviceComboBox);
        centerPanel.add(Box.createVerticalStrut(10));


        masterComboBox = new JComboBox<>(loadMasters());
        masterComboBox.setMaximumSize(new Dimension(300, 30));
        masterComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(new JLabel("Выберите мастера:"));
        centerPanel.add(masterComboBox);
        centerPanel.add(Box.createVerticalStrut(20));


        button = new JButton("Записаться на прием");
        button.setBackground(new Color(0, 120, 215)); // Blue color
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 40));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField.getText();
                String service = (String) serviceComboBox.getSelectedItem();
                String master = (String) masterComboBox.getSelectedItem();

                if (name != null && !name.isEmpty() && service != null && master != null) {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:sqlite:barbershop.db");
                        // Get service_id and master_id
                        int serviceId = getServiceId(service);
                        int masterId = getMasterId(master);

                        PreparedStatement statement = connection.prepareStatement(
                                "INSERT INTO appointments (name, service_id, master_id) VALUES (?, ?, ?)");
                        statement.setString(1, name);
                        statement.setInt(2, serviceId);
                        statement.setInt(3, masterId);
                        statement.executeUpdate();
                        connection.close();

                        JOptionPane.showMessageDialog(UserInterface.this, "Запись успешно создана!");
                        textField.setText("");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(UserInterface.this, "Ошибка при создании записи!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(UserInterface.this, "Пожалуйста, заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        centerPanel.add(button);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("<html><center>ИНФОРМАЦИЯ ДЛЯ КЛИЕНТА<br>Записаться можно только на ближайшую неделю.<br>Получите удовольствие здесь и сейчас.</center></html>", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private String[] loadServices() {
        ArrayList<String> services = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:barbershop.db");
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM services");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                services.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return services.toArray(new String[0]);
    }

    private String[] loadMasters() {
        ArrayList<String> masters = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:barbershop.db");
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM masters");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                masters.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return masters.toArray(new String[0]);
    }

    private int getServiceId(String serviceName) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:barbershop.db");
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM services WHERE name = ?");
        statement.setString(1, serviceName);
        ResultSet resultSet = statement.executeQuery();
        int id = resultSet.next() ? resultSet.getInt("id") : -1;
        connection.close();
        return id;
    }

    private int getMasterId(String masterName) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:barbershop.db");
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM masters WHERE name = ?");
        statement.setString(1, masterName);
        ResultSet resultSet = statement.executeQuery();
        int id = resultSet.next() ? resultSet.getInt("id") : -1;
        connection.close();
        return id;
    }
}