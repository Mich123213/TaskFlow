import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import javax.swing.border.EmptyBorder;
public class TaskFlow {
    static DefaultListModel<String> model = new DefaultListModel<>();
    static JLabel status;
    static final String FILE_NAME = "tasks.txt";
    public static void main(String[] args) {
        JFrame frame = new JFrame("TaskFlow");
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(new EmptyBorder(15,15,15,15));
        panel.setBackground(Color.WHITE);
        JLabel title = new JLabel("TASKFLOW",SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI",Font.BOLD,24));
        JLabel date = new JLabel(
                "Date: " + LocalDate.now(),
                SwingConstants.CENTER);
        date.setForeground(Color.GRAY);
        JPanel header = new JPanel(new GridLayout(2,1));
        header.setBackground(Color.WHITE);
        header.add(title);
        header.add(date);
        JTextField taskField = new JTextField();
        taskField.setFont(new Font("Segoe UI",Font.PLAIN,15));
        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");
        Color btn = new Color(235,235,235);
        add.setBackground(btn);
        delete.setBackground(btn);
        clear.setBackground(btn);
        JPanel input = new JPanel(new BorderLayout(8,8));
        input.setBackground(Color.WHITE);
        input.add(taskField,BorderLayout.CENTER);
        input.add(add,BorderLayout.EAST);
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Segoe UI",Font.PLAIN,15));
        JScrollPane scroll = new JScrollPane(list);
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);
        buttons.add(delete);
        buttons.add(clear);
        status = new JLabel("Total Tasks: 0");
        JPanel center = new JPanel(new BorderLayout(10,10));
        center.setBackground(Color.WHITE);
        center.add(input,BorderLayout.NORTH);
        center.add(scroll,BorderLayout.CENTER);
        center.add(buttons,BorderLayout.SOUTH);
        panel.add(header,BorderLayout.NORTH);
        panel.add(center,BorderLayout.CENTER);
        panel.add(status,BorderLayout.SOUTH);
        frame.setContentPane(panel);
        loadTasks();
        updateStatus();
        add.addActionListener(e -> {
            String task = taskField.getText().trim();
            if(task.isEmpty()){
                JOptionPane.showMessageDialog(frame,
                        "Please enter a task.");
            }else{
                model.addElement(task);
                taskField.setText("");
                updateStatus();
                saveTasks();
            }
        });
        taskField.addActionListener(e -> add.doClick());
        delete.addActionListener(e ->{
            int index = list.getSelectedIndex();
            if(index==-1){
                JOptionPane.showMessageDialog(frame,
                        "Select a task first.");
            }else{
                model.remove(index);
                updateStatus();
                saveTasks();
            }
        });
        clear.addActionListener(e->{
            if(model.isEmpty()){
                JOptionPane.showMessageDialog(frame,
                        "No tasks to clear.");
            }else{
                int option = JOptionPane.showConfirmDialog(
                        frame,
                        "Clear all tasks?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if(option==JOptionPane.YES_OPTION){
                    model.clear();
                    updateStatus();
                    saveTasks();
                }
            }
        });
        frame.setVisible(true);
        }
    static void updateStatus() {
        status.setText("Total Tasks: " + model.getSize());
    }
    static void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < model.size(); i++) {
                writer.println(model.get(i));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to save tasks."
            );
        }
    }
    static void loadTasks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    model.addElement(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to load tasks."
            );
        }
    }
}