package data;

import business.Reserva;

import java.io.*;
import java.util.Map;

public class codVooDAO {
    public  static void saveInstanceObj(int codVOO, String filepath){
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(codVOO);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getInstanceObj(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ss = new ObjectInputStream(new FileInputStream(path));
        int codVOO = (int) ss.readObject();
        ss.close();
        return codVOO;
    }
}
