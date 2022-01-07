package data;
import java.io.*;
import java.util.Map;
import business.*;

public class ReservasDAO {
    public  static  void saveInstanceObj(Map<String, Reserva> reservas,String filepath){
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(reservas);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Map <String,Reserva> getInstanceObj(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ss = new ObjectInputStream(new FileInputStream(path));
        Map <String,Reserva>  s = (Map <String,Reserva> ) ss.readObject();
        ss.close();
        return s;

    }


}
