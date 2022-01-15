package data;

import java.io.*;

public class codReservaDAO {
    public  static void saveInstanceObj(int codRESERVA, String filepath){
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(codRESERVA);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getInstanceObj(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ss = new ObjectInputStream(new FileInputStream(path));
        int codRESERVA  = (int) ss.readObject();
        ss.close();
        return codRESERVA;

    }
}
