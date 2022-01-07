package data;
import java.io.*;
import java.util.List;
import java.util.Map;
import business.*;


public class VoosDao {
    public  static  void saveInstanceObj(List<Voo> voos, String filepath){
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(voos);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static List <Voo> getInstanceObj(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ss = new ObjectInputStream(new FileInputStream(path));
        List <Voo> s = (List <Voo> )ss.readObject();
        ss.close();
        return s;

    }
}
