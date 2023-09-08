package hr.project.utils;

import hr.project.exceptions.SerializationFileEmptyException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serialization<T> {
    private static final String SERIALIZATION_FILE = "dat/serialization.dat";

    public static <T> void Serialize(List<T> changes) {
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(SERIALIZATION_FILE);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(changes);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static<T> List<T> Deserialize() {
        ObjectInputStream objectinputstream = null;
        List<T> changes = new ArrayList<>();
        try {
            FileInputStream streamIn = new FileInputStream(SERIALIZATION_FILE);
            objectinputstream = new ObjectInputStream(streamIn);
            changes = (List<T>) objectinputstream.readObject();

        } catch (EOFException | FileNotFoundException ex) {
            throw new SerializationFileEmptyException("Serialization file is empty or doesn't exist!");
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (objectinputstream != null) {
                try {
                    objectinputstream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return changes;
    }
}
