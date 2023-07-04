package fonction;
import java.sql.*;
import java.time.*;
import java.util.*;


import annotation.MethodUrl;
import filtre.Filtre;
import mapping.Mapping;
import modelview.ModelView;

import java.io.File;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.*;

import annotation.*;

import java.util.Objects;
import java.util.HashMap;
import java.lang.ClassLoader;



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

    public boolean isAnnoted(Parameter parameter, Class annotation) {
        return parameter.isAnnotationPresent(annotation);
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

    String getNameClass(String name_file) {
        int len = name_file.length();
        int ind = len - 6;
    return name_file.substring(0, ind);
    }

    public ModelView getViewByMapping(Mapping entre) throws Exception {
        ModelView val = new ModelView();
        try {
            Class to_instance = Class.forName(entre.getClassName());
            Object objet = to_instance.getConstructor().newInstance();
            Method[] allMethod = objet.getClass().getDeclaredMethods();
            Method to_invoke = null;
            for (int i = 0; i < allMethod.length; i++) {
                if(allMethod[i].isAnnotationPresent(MethodUrl.class) == true 
                && allMethod[i].getName().compareToIgnoreCase(entre.getMethod()) == 0 ) {
                    to_invoke = allMethod[i];
                }
            }
            val = (ModelView)to_invoke.invoke(objet);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    // public Class getClassByHisPathName(String pathName) {

    // }

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

    int countParametersAnnotation(Parameter[] parameters, Class annotation) throws Exception {
        int val = 0;
        try {
            for (int i = 0; i < parameters.length; i++) {
                if(isAnnoted(parameters[i], annotation) == true) {
                    val ++;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public boolean verifyParamAnnotaion(Parameter[] parameters, Class annotation) throws Exception {
        try {
            int count_annotation = countParametersAnnotation(parameters, annotation);
            int count_param = parameters.length;
            if(count_annotation == count_param) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw new Exception("Touts les arguments doivent etre annote");
        }
        return false;
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


    public <T> T adequatObjectForParameter(HttpServletRequest request, Parameter parameter, Method method)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (request.getParameter(parameter.getAnnotation(ParamName.class).value()) == null) {
            if (parameter.getType().getSimpleName().equals("int")
                    || parameter.getType().getSimpleName().equals("Integer")
                    || parameter.getType().getSimpleName().equals("Double")
                    || parameter.getType().getSimpleName().equals("double")
                    || parameter.getType().getSimpleName().equals("long")
                    || parameter.getType().getSimpleName().equals("Long")
                    || parameter.getType().getSimpleName().equals("float")
                    || parameter.getType().getSimpleName().equals("Float")) {
                return (T) (Number) 0;
            }
            return null;
        }
        T obj = null;
        if (parameter.getType().getSimpleName().equals("int")
                || parameter.getType().getSimpleName().equals("Integer")) {
            obj = (T) (Integer) Integer
                    .parseInt(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim());
        } else if (parameter.getType().getSimpleName().equals("float")
                || parameter.getType().getSimpleName().equals("Float")) {
            obj = (T) (Float) Float
                    .parseFloat(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim());
        } else if (parameter.getType().getSimpleName().equals("Long")
                || parameter.getType().getSimpleName().equals("long")) {
            obj = (T) (Long) Long
                    .parseLong(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim());
        } else if (parameter.getType().getSimpleName().equals("double")
                || parameter.getType().getSimpleName().equals("Double")) {
            obj = (T) (Double) Double
                    .parseDouble(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim());
        } else if (parameter.getType().getSimpleName().equals("String")) {
            obj = (T) (String) request.getParameter(parameter.getAnnotation(ParamName.class).value())
                    .trim();
        } else if (parameter.getType().getSimpleName().equals("Date")) {
            obj = (T) (java.sql.Date) java.sql.Date
                    .valueOf(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim());
        } else if (parameter.getType().getSimpleName().equals("Time")) {
            obj = (T) (Time) Time
                    .valueOf(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim());
        } else if (parameter.getType().getSimpleName().equals("Timestamp")) {
            obj = (T) (Timestamp) Timestamp
                    .valueOf(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim());
        } else if (parameter.getType().getSimpleName().equals("LocalDate")) {
            obj = (T) (java.time.LocalDate) java.sql.Date
                    .valueOf(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim())
                    .toLocalDate();
        } else if (parameter.getType().getSimpleName().equals("LocalTime")) {
            obj = (T) (LocalTime) Time
                    .valueOf(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim())
                    .toLocalTime();
        } else if (parameter.getType().getSimpleName().equals("LocalDateTime")) {
            obj = (T) (LocalDateTime) Timestamp
                    .valueOf(request.getParameter(parameter.getAnnotation(ParamName.class).value())
                            .trim())
                    .toLocalDateTime();
        }
        return obj;
    }

    

    // public String getValuesUrl(Class class_type) {
    //     String val = "";
    //     MethodUrl method = class_type.getAnnotation(MethodUrl.class);
    // return method.url();
    // }



}