import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;

final class GridBagHelper {
    public static void Add(Container container, Component component, int gridx, int gridy, int fill, int weightx,
            int weighty) {
        var constraints = new GridBagConstraints();

        constraints.fill = fill;

        constraints.gridx = gridx;
        constraints.gridy = gridy;

        constraints.weightx = weightx;
        constraints.weighty = weighty;

        container.add(component, constraints);
    }
}

public final class Frame extends JFrame {

    private final JMenuBar MenuBar = new JMenuBar();
    private final JTextField TextField = new JTextField();
    private final JButton Button1 = new JButton("🔺");
    private final JButton Button2 = new JButton("🔄");
    private final DefaultListModel<Item> ListModel = new DefaultListModel<Item>();
    private final JList<Item> List = new JList<Item>(ListModel);

    final private void Invoke() {
        CompletableFuture.runAsync(() -> {
            MenuBar.setEnabled(false);
            List.setEnabled(false);

            String text = Explorer.Get();
            SwingUtilities.invokeLater(() -> TextField.setText(text));

            SwingUtilities.invokeLater(() -> ListModel.clear());
            for (Item item : Explorer.Enumerate())
                SwingUtilities.invokeLater(() -> ListModel.addElement(item));

            MenuBar.setEnabled(true);
            List.setEnabled(true);
        });
    }

    Frame() {
        setTitle("Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        MenuBar.setLayout(new GridBagLayout());

        List.setCellRenderer(new Renderer());

        TextField.setEditable(false);
        TextField.setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics args) {
            }
        });

        List.setBorder(new EmptyBorder(0, 2, 0, 2));

        GridBagHelper.Add(this, MenuBar, 0, 0, GridBagConstraints.HORIZONTAL, 1, 0);
        GridBagHelper.Add(MenuBar, Button1, 0, 0, GridBagConstraints.NONE, 0, 0);
        GridBagHelper.Add(MenuBar, TextField, 1, 0, GridBagConstraints.BOTH, 1, 1);
        GridBagHelper.Add(MenuBar, Button2, 2, 0, GridBagConstraints.NONE, 0, 0);
        GridBagHelper.Add(this, new JScrollPane(List), 0, 1, GridBagConstraints.BOTH, 1, 1);

        Button1.addActionListener(_ -> {
            if (Explorer.Set(".."))
                Invoke();
        });

        Button2.addActionListener(_ -> {
            Invoke();
        });

        List.addListSelectionListener($ -> {
            var item = List.getSelectedValue();
            if (item == null)
                return;

            if (!$.getValueIsAdjusting() && Explorer.Set(item.Name))
                Invoke();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent args) {
                Invoke();
            }
        });

        setVisible(true);
    }
}

final class Renderer extends DefaultListCellRenderer {
    private final String Folder = "📁";

    private final String File = "📄";

    @Override
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        var item = (Item) value;

        var icon = new JLabel(item.Directory ? Folder : File);
        var font = icon.getFont();
        icon.setFont(font.deriveFont(font.getSize() * 2f));
        icon.setBorder(new EmptyBorder(0, 0, 0, 4));

        var label = new JLabel(item.Name);

        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(true);
        panel.add(icon);
        panel.add(label);

        if (isSelected) {
            var color = list.getSelectionForeground();

            panel.setBackground(list.getSelectionBackground());
            panel.setForeground(color);

            icon.setForeground(color);
            label.setForeground(color);
        } else {
            var color = list.getForeground();

            panel.setBackground(list.getBackground());
            panel.setForeground(color);

            icon.setForeground(color);
            label.setForeground(color);
        }

        return panel;
    }
}