package filtre;
import java.io.*;

public class Filtre implements FilenameFilter {
    File f;
    String extension;
    public Filtre(File f, String extension) {
        this.f = f;
        this.extension = extension;
    }

    public boolean accept(File dir, String name) {
        int len = name.length();
        int ind = len - 5;
        String s = name.substring(ind, len);
        //System.out.println(s);
        if(s.compareToIgnoreCase(extension) == 0) {
            //System.out.println(dir.getPath());
            return true;
        }
    return false;
    }
}