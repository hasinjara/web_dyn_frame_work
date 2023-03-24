package main;
import table.*;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Vector;
import java.io.File;
import utilitaire.Utilitaire;
import mapping.*;
import fonction.*;
import annotation.*;
import java.util.Map;
import java.util.HashMap;
public class Main {
    public static void main(String[] args) {
        Fonction fonction = new Fonction();
        Emp emp = new Emp();
        Simple simple = new Simple();
        Utilitaire u = new Utilitaire();
        Mapping mapping = new Mapping();
        // System.out.println(u.getUrl("http://localhost:8082/test_frame_work/FrontServlet"));
        //String[] annotation = fonction.getAnnotation(emp);
        try {
            //Class[] allClass = fonction.getClassAnnoted("table",TableName.class);
            //  Field[] allField = fonction.getFieldAnnoted(Dept.class, FieldName.class);
            // TableName a = Dept.class.getAnnotation(TableName.class);
            // System.out.println(a.nom_table());
            // Vector test = mapping.getCorrespondance("table", MethodUrl.class, "Dept-getId");
            // for (int i = 0; i < test.size(); i++) {
            //     System.out.println( ((Mapping)test.get(i)).getClassName() + "   "+  ((Mapping)test.get(i)).getMethod() );
            // }

            // Spécifiez le chemin du dossier à partir duquel vous souhaitez obtenir les noms des dossiers
            File directory = new File("../");

            // Récupérez tous les noms des dossiers dans le dossier spécifié
            String[] directories = directory.list((current, name) -> new File(current, name).isDirectory());

            // Parcourez la liste des noms des dossiers et affichez-les
            // for (String dir : directories) {
            //     System.out.println(dir);
            // }

           
            File d = new File("D:\\etude\\Doc_S4\\Web_dyn_Mr_naina\\test_frame_work");
            Vector values = mapping.getAllValuesAnnoted(d, MethodUrl.class);
            Vector correspondance = null;
                HashMap <String, Mapping> tmp = new HashMap<String, Mapping>();
                for (int i = 0; i < values.size(); i++) {
                    correspondance = mapping.getCorrespondanceInPackage(d, MethodUrl.class, values.get(i).toString());
                    for (int j = 0; j < correspondance.size(); j++) {
                        tmp.put(values.get(i).toString(), (Mapping)(correspondance.get(j)));
                    }
                }
                for(Map.Entry mapEntry : tmp.entrySet()) {
                    System.out.println("cle "+ mapEntry.getKey());
                    System.out.println("valeur "+ ((Mapping)mapEntry.getValue()).getClassName() + " " +((Mapping)mapEntry.getValue()).getMethod() );
                }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            // e.printStackTrace();
        }
        
    }
}
