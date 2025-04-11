import javax.swing.UIManager;

public final class Program {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception $) {
        }
        new Frame();
    }
}