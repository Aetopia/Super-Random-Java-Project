import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Explorer {
    private static Path Value = Path.of(System.getProperty("user.dir")).toAbsolutePath();

    public final static List<Item> Enumerate() {
        ArrayList<Item> collection = new ArrayList<Item>();

        for (File item : Value.toFile().listFiles()) {
            collection.add(new Item(item.getName(), item.isDirectory()));
        }

        return Collections.unmodifiableList(collection);
    }

    public final static Boolean Set(String $) {
        if ($ == null)
            return false;

        var value = Value.resolve($).normalize();
        if (Value.equals(value) || !Files.isDirectory(value))
            return false;

        Value = value;
        return true;
    }

    public final static String Get() {
        return Value.toString();
    }
}
