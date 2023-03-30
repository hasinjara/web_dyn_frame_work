package fonction;
import java.sql.*;
import java.util.*;
import java.util.Date;

import annotation.MethodUrl;
import filtre.Filtre;

import java.io.File;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.*;
public class Fonction {
    public String[] getAnnotation(Object e) {
        Annotation[] allAnnoted = e.getClass().getAnnotations();
        if(allAnnoted.length == 0) {
            System.out.println("No");
            return null;
        }
        String[] val = new String[allAnnoted.length];
        for (int i = 0; i < allAnnoted.length; i++) {
            val[i] = allAnnoted[i].annotationType().getSimpleName();
            System.out.println(val[i]);
        }
    return val;
    }

    public boolean isAnnoted(Class class_type, Class annotation) {
        Annotation[] allAnnoted = class_type.getAnnotations();
        if(allAnnoted.length == 0) {
            return false;
        }
        for (int i = 0; i < allAnnoted.length; i++) {
            //System.out.println( allAnnoted[i].annotationType() + " ---- "+ annotation );
            if(allAnnoted[i].annotationType().equals(annotation) ==  true) {
                return true;
            }
        }
    return false;
    }

    public boolean isAnnoted(Field field, Class annotation) {
        return field.isAnnotationPresent(annotation);
    }

    String getNameClass(String name_file) {
        int len = name_file.length();
        int ind = len - 6;
    return name_file.substring(0, ind);
    }

    public Class[] getClassInRepository(String repertoire) throws Exception {
        File file = new File(repertoire);
        Filtre filtre = new Filtre(file, "class");
        String[] listNameClass = file.list(filtre);
        Class temp = null;
        Class[] val = new Class[listNameClass.length];
        String name_class = "";
        try {
            for(int i = 0; i<listNameClass.length;i++) {
                name_class = getNameClass(listNameClass[i]);
                // System.out.println("jijiji "+ file.getName()+"."+ name_class);
                val[i] = temp.forName(file.getName()+"."+ name_class);
                // System.out.println(val[i]);
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }  
    return val;
    }

    int countAnnotation(String repertoire, Class annotation) throws Exception {
        int val = 0;
        try {
            Class[] allInRepository = getClassInRepository(repertoire);
            for (int i = 0; i < allInRepository.length; i++) {
                if(isAnnoted(allInRepository[i], annotation) == true) {
                    val ++;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public Class[] getClassAnnoted(String repertoire, Class annotation) throws Exception {
        Class[] val = null;
        try {
            Class[] allInRepository = getClassInRepository(repertoire);
            int alloc = countAnnotation(repertoire, annotation);
            val = new Class[alloc];
            int k = 0;
            for (int i = 0; i < allInRepository.length; i++) {
                if(isAnnoted(allInRepository[i], annotation) == true) {
                    val[k] = allInRepository[i];
                    k++;
                    // System.out.println(val[i]);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }
    
    int countAnnotation(Class class_type , Class annotation) throws Exception {
        int val = 0;
        try {
            Field[] allField = class_type.getDeclaredFields();
            //System.out.println(allField.length + "huhuhu");
            for (int i = 0; i < allField.length; i++) {
                if(isAnnoted(allField[i], annotation) == true) {
                    val ++;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public Field[] getFieldAnnoted(Class class_type, Class anotation) throws  Exception {
        Field[] val = null;
        try {
            Field[] allField = class_type.getDeclaredFields();
            int alloc = countAnnotation(class_type, anotation);
            val = new Field[alloc];
            int k = 0;
            for (int i = 0; i < allField.length; i++) {
                if(isAnnoted(allField[i], anotation) == true){
                    val[k] = allField[i];
                    k++;
                    //System.out.println("tafa " + val[i]);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public boolean isAnnoted(Method method, Class annotation) {
        return method.isAnnotationPresent(annotation);
    }

    int countMethodsAnnoted(Class class_type , Class annotation) throws Exception {
        int val = 0;
        try {
            Method[] allMethod = class_type.getDeclaredMethods();
            //System.out.println(allMethod.length + "huhuhu");
            for (int i = 0; i < allMethod.length; i++) {
                if(isAnnoted(allMethod[i], annotation) == true) {
                    // System.out.println("tafa " + allMethod[i].getName());
                    val ++;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public Method[] getMethodsAnnoted(Class class_type, Class anotation) throws  Exception {
        Method[] val = null;
        try {
            Method[] allMethod = class_type.getDeclaredMethods();
            int alloc = countMethodsAnnoted(class_type, anotation);
            // System.out.println(alloc);
            val = new Method[alloc];
            int k = 0;
            for (int i = 0; i < allMethod.length; i++) {
                if(isAnnoted(allMethod[i], anotation) == true){
                    // System.out.println("tafa " + allMethod[i].getName());
                    val[k] = allMethod[i];
                    k++;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    // public String getValuesUrl(Class class_type) {
    //     String val = "";
    //     MethodUrl method = class_type.getAnnotation(MethodUrl.class);
    // return method.url();
    // }



}