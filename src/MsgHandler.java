import java.nio.charset.StandardCharsets;

public class MsgHandler {
    public String fromByteToString(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (i<a.length && a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }



}
