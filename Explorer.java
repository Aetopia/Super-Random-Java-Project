import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Explorer {

    private static Path Value = Path.of(System.getProperty("user.dir")).toAbsolutePath();

    public final static List<String> Enumerate() {
        ArrayList<String> collection = new ArrayList<String>();

        for (File item : Value.toFile().listFiles()) {
            collection.add(item.getName());
        }

        return Collections.unmodifiableList(collection);
    }

    public final static Boolean Set(String $) {
        if ($ != null) {
            var value = Value.resolve($).normalize();
            if (!Value.equals(value) && Files.isDirectory(value)) {
                Value = value;
                return true;
            }
        }
        return false;
    }

    public final static String Get() {
        return Value.toString();
    }
}
