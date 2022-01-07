package data;
import java.io.*;
import java.util.Map;
import business.*;

public class UtilizadoresDAO {
    public  static  void saveInstanceObj(Map<String, User> contas,String filepath){
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(contas);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Map <String,User> getInstanceObj(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ss = new ObjectInputStream(new FileInputStream(path));
        Map <String,User>  s = (Map <String,User> ) ss.readObject();
        ss.close();
        return s;

    }

}
