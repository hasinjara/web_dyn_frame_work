package mapping;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Vector;
import annotation.*;
import fonction.Fonction;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.lang.ClassLoader;

public class Mapping {
    String className;
    String method;
    Class<?>[] parameters;
     
    public Mapping() {
    }
    
    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    public Mapping(String className, String method, Class<?>[] parameters) {
        this.className = className;
        this.method = method;
        this.parameters = parameters;
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

    public void setParameters(Class<?>[] parameters) {
        this.parameters = parameters;
    }

    public Class<?>[] getParameters() {
        return this.parameters;
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
                    tmp = new Mapping(n_class, n_method[j].getName(), n_method[j].getParameterTypes());
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
                        tmp = new Mapping(n_class, n_method[j].getName(), n_method[j].getParameterTypes());
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

    public Mapping getMapppingBykey(HashMap <String, Mapping> all ,String key) {
        Mapping val = null;
        for(Map.Entry mapEntry : all.entrySet()) {
            // out.println("All result");
            // out.println("cle "+ mapEntry.getKey());
            // out.println("valeur "+ ((Mapping)mapEntry.getValue()).getClassName() + " " +((Mapping)mapEntry.getValue()).getMethod());
            if(mapEntry.getKey().toString().compareToIgnoreCase(key) == 0) {
                //return new Mapping(((Mapping)mapEntry.getValue()).getClassName(), ((Mapping)mapEntry.getValue()).getMethod());
                return (Mapping)mapEntry.getValue();
            }
        }
    return val;
    }


    
}
