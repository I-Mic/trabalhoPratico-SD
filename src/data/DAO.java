package data;

import java.io.*;

public final class DAO {
    private DAO() {}

    @SuppressWarnings("unchecked")
    public static <T> T load(final String filepath)
            throws ClassNotFoundException, IOException {
        try (
                final ObjectInputStream in = new ObjectInputStream(new FileInputStream(filepath));
        ) {
            return (T) in.readObject();
        } catch (final Exception e) {
            throw e;
        }
    }

    public static <T> void store(
            final T object,
            final String filepath
    ) throws IOException {
        try (
                final ObjectOutputStream out =
                        new ObjectOutputStream(new FileOutputStream(filepath));
        ) {
            out.writeObject(object);
        } catch (final Exception e) {
            throw e;
        }
    }

}
