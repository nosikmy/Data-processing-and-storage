import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Saver {
    public static void savePeople(People people){
        FileOutputStream fileOutputStream = null;
        try {
            Path saves = Path.of("saves");
            if (!Files.exists(saves)) {
                Files.createDirectories(saves);
            }
            fileOutputStream = new FileOutputStream("saves/save.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(people);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static People getPeople() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("saves/save.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (People) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }

    }
}
