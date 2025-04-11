import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;

public final class Frame extends JFrame {

    private final JMenuBar MenuBar = new JMenuBar();
    private final JTextField TextField = new JTextField();
    private final JButton Button1 = new JButton("🔺");
    private final JButton Button2 = new JButton("🔄");
    private final DefaultListModel<String> ListModel = new DefaultListModel<String>();
    private final JList<String> List = new JList<String>(ListModel);

    final private void Invoke() {
        CompletableFuture.runAsync(() -> {
            MenuBar.setEnabled(false);
            List.setEnabled(false);

            String text = Explorer.Get();
            SwingUtilities.invokeLater(() -> TextField.setText(text));

            SwingUtilities.invokeLater(() -> ListModel.clear());
            for (String item : Explorer.Enumerate())
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

        TextField.setEditable(false);
        TextField.setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics args) {
            }
        });

        List.setBorder(new EmptyBorder(0, 2, 0, 2));

        Add(this, MenuBar, 0, 0, GridBagConstraints.HORIZONTAL, 1, 0);
        Add(MenuBar, Button1, 0, 0, GridBagConstraints.NONE, 0, 0);
        Add(MenuBar, TextField, 1, 0, GridBagConstraints.BOTH, 1, 1);
        Add(MenuBar, Button2, 2, 0, GridBagConstraints.NONE, 0, 0);
        Add(this, new JScrollPane(List), 0, 1, GridBagConstraints.BOTH, 1, 1);

        Button1.addActionListener(_ -> {
            if (Explorer.Set(".."))
                Invoke();
        });

        Button2.addActionListener(_ -> {
            Invoke();
        });

        List.addListSelectionListener($ -> {
            if (!$.getValueIsAdjusting() && Explorer.Set(List.getSelectedValue()))
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

    void Add(Container container, Component component, int gridx, int gridy, int fill, int weightx, int weighty) {
        var constraints = new GridBagConstraints();

        constraints.fill = fill;

        constraints.gridx = gridx;
        constraints.gridy = gridy;

        constraints.weightx = weightx;
        constraints.weighty = weighty;

        container.add(component, constraints);
    }
}
