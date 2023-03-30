package mapping;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Vector;
import annotation.*;
import fonction.Fonction;

public class Mapping {
    String className;
    String method;
     
    public Mapping() {
    }
    
    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }

    public Vector getAllInPackage(File repertoire, Class url_annotaion) throws Exception {
        Vector val = new Vector();
        try {
            Fonction fonction = new Fonction();
            Class[] allInRepository = fonction.getClassInRepository(repertoire.getPath());
            String n_class = "";
            Method[] n_method = null;
            Mapping tmp = new Mapping();
            for (int i = 0; i < allInRepository.length; i++) {
                n_class = allInRepository[i].getName();
                n_method = fonction.getMethodsAnnoted(allInRepository[i], url_annotaion);
                for (int j = 0; j < n_method.length; j++) {
                    tmp = new Mapping(n_class, n_method[j].getName());
                    val.add(tmp);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public Vector getValuesAnnoted(File repertoire, Class url_annotation) throws Exception{ 
        Vector val = new Vector<>();
        try {
            Fonction fonction = new Fonction();
            Class[] allInRepository = fonction.getClassInRepository(repertoire.getPath());
            String n_class = "";
            Method[] n_method = null;
            for (int i = 0; i < allInRepository.length; i++) {
                n_class = allInRepository[i].getName();
                n_method = fonction.getMethodsAnnoted(allInRepository[i], url_annotation);
                for (int j = 0; j < n_method.length; j++) {
                    MethodUrl method = n_method[j].getAnnotation(MethodUrl.class);
                    val.add(method.url());
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public Vector getAllValuesAnnoted(File repertoire, Class url_annotation) throws Exception{
        Vector val = new Vector<String>();
        try {
            // Spécifiez le chemin du dossier à partir duquel vous souhaitez obtenir les noms des dossiers
            File directory = new File(repertoire.getPath());

            // Récupérez tous les noms des dossiers dans le dossier spécifié
            File[] directories = directory.listFiles((current, name) -> new File(current, name).isDirectory());
            Vector tmp = null;
            File tmp_file = null;
            for (int i = 0; i < directories.length; i++) {
                // System.out.println(directories[i]);
                tmp = getValuesAnnoted(directories[i], url_annotation);
                for (int j = 0; j < tmp.size(); j++) {
                    val.add(tmp.get(j).toString());
                }
            }

        } catch (Exception e) {
        // TODO: handle exception
        throw e;
        }
       return val;
    }

    public Vector getCorrespondance(File repertoire, Class url_annotation, String value_url) throws Exception{
        Vector val = new Vector<>();
        try {
            Fonction fonction = new Fonction();
            Class[] allInRepository = fonction.getClassInRepository(repertoire.getPath());
            String n_class = "";
            Method[] n_method = null;
            Mapping tmp = new Mapping();
            for (int i = 0; i < allInRepository.length; i++) {
                n_class = allInRepository[i].getName();
                n_method = fonction.getMethodsAnnoted(allInRepository[i], url_annotation);
                for (int j = 0; j < n_method.length; j++) {
                    MethodUrl method = n_method[j].getAnnotation(MethodUrl.class);
                    if( method.url().compareToIgnoreCase(value_url)  == 0) {
                        tmp = new Mapping(n_class, n_method[j].getName());
                        val.add(tmp);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public Vector getCorrespondanceInPackage(File repertoire, Class url_annotation, String value_url)  throws Exception{
        Vector val = new Vector<>();
        try {
            // Spécifiez le chemin du dossier à partir duquel vous souhaitez obtenir les noms des dossiers
            File directory = new File(repertoire.getPath());

            // Récupérez tous les noms des dossiers dans le dossier spécifié
            File[] directories = directory.listFiles((current, name) -> new File(current, name).isDirectory());

            // Parcourez la liste des noms des dossiers et affichez-les
            Vector tmp = new Vector<>();
            for (File dir : directories) {
                tmp = getCorrespondance(dir, url_annotation, value_url);
                for (int i = 0; i < tmp.size(); i++) {
                    val.add( (Mapping)tmp.get(i));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }


    
}
