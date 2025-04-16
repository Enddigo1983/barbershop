import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showOptionDialog(null, "Выберите опцию:", "Система управления барбершопом",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"Клиент", "Администратор"}, "Клиент");

            switch (choice) {
                case 0:
                    new UserInterface();
                    break;
                case 1:
                    new AdministratorInterface();
                    break;
                default:
                    System.exit(0);
            }
        });
    }
}