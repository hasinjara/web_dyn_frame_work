package fonction;
import java.sql.*;
import java.time.*;
import java.util.*;


import filtre.Filtre;
import mapping.Mapping;
import modelview.ModelView;
import fileUpload.*;
import annotation.*;

import java.io.File;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.*;
import java.lang.annotation.*;



import java.util.Objects;
import java.util.HashMap;

import javax.servlet.*; 
import javax.servlet.http.*;



public class Fonction {
    String toUpperCaseAt(String s, int ind) {
        char[] tab = s.toCharArray();
        String maj = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String min = "abcdefghijklmnopqrstuvwxyz";
        char[] maj_char = maj.toCharArray();
        char[] min_char = min.toCharArray();
        for (int i = 0; i < maj_char.length; i++) {
            if (tab[ind] == min_char[i]) {
                tab[ind] = maj_char[i];
            }
        }
        String res = new String(tab);
        return res;
    }

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

    public ModelView getViewByMapping(Mapping entre, HttpServletRequest request) throws Exception {
        ModelView val = null;
        try {
            Class to_instance = Class.forName(entre.getClassName());
            Object objet = to_instance.getConstructor().newInstance();
            Method[] allMethod = objet.getClass().getDeclaredMethods();
            Method to_invoke = null;
            for (int i = 0; i < allMethod.length; i++) {
                if(allMethod[i].isAnnotationPresent(MethodUrl.class) == true 
                && allMethod[i].getName().compareToIgnoreCase(entre.getMethod()) == 0 ) {
                    if(verifyParamAnnotaion(allMethod[i].getParameters(), ParamName.class) == true) {
                        val = invokeMethodByRequestParameters(objet, request);
                    }
                    else {
                        val = (ModelView)allMethod[i].invoke(objet);
                    }
                    // to_invoke = allMethod[i];
                }
            }
            // val = (ModelView)to_invoke.invoke(objet);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return val;
    }

    public ModelView getViewByMapping(Mapping entre) throws Exception {
        ModelView val = null;
        try {
            Class to_instance = Class.forName(entre.getClassName());
            Object objet = to_instance.getConstructor().newInstance();
            Method[] allMethod = objet.getClass().getDeclaredMethods();
            Method to_invoke = null;
            for (int i = 0; i < allMethod.length; i++) {
                if(allMethod[i].isAnnotationPresent(MethodUrl.class) == true 
                && allMethod[i].getName().compareToIgnoreCase(entre.getMethod()) == 0 ) {
                    // if(verifyParamAnnotaion(allMethod[i].getParameters(), ParamName.class) == false) {
                    //     to_invoke = allMethod[i];
                    // }
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

            if(count_param == 0) {
                return false;
            }

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

    public void setObjectAttributeByRequest(Object object, Field field, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            field.setAccessible(true);
            Class default_class = (Class) field.getType();
            String set = "set" + toUpperCaseAt(field.getName(), 0);
            if (request.getParameter(field.getName()) != null) {
                // System.out.println("Type " + default_class + " type " + (Class) field.getType());
                if (default_class.equals(String.class)) {
                    object.getClass().getDeclaredMethod(set, default_class).invoke(object,
                            request.getParameter(field.getName()));
                }
                if (default_class.equals(int.class)) {
                    int a = 0;
                    if (request.getParameter(field.getName()) != null) {
                        a = Integer.valueOf(request.getParameter(field.getName()));
                    }
                    object.getClass().getDeclaredMethod(set, default_class).invoke(object, a);
                }
                if (default_class.equals(double.class)) {
                    double a = 0;
                    if (request.getParameter(field.getName()) != null) {
                        a = Double.valueOf(request.getParameter(field.getName()));
                    }
                    object.getClass().getDeclaredMethod(set, default_class).invoke(object, a);
                }
                if (default_class.equals(float.class)) {
                    float a = 0;
                    if (request.getParameter(field.getName()) != null) {
                        a = Float.valueOf(request.getParameter(field.getName()));
                    }
                    object.getClass().getDeclaredMethod(set, default_class).invoke(object, a);
                }
                if (default_class.equals(java.util.Date.class)) {
                    java.util.Date a = new java.util.Date();
                    object.getClass().getDeclaredMethod(set, default_class).invoke(object, a);
                }

            }
            
            if(default_class.equals(FileUpload.class) == true) {
                // System.out.println("Tafa file upload "+ field.getName());
                try {
                    if(request.getPart(field.getName()) != null) {
                        Part filepart  = request.getPart(field.getName()); 
                        FileUpload tmp = new FileUpload();
                        tmp.setNom(filepart.getSubmittedFileName());
                        tmp.setBytes(filepart.getInputStream().readAllBytes());
                        System.out.println(tmp.getNom()+" "+ tmp.getBytes());
                        String fileName = filepart.getSubmittedFileName();
                        String path = "E:\\" + fileName; // Spécifiez le chemin souhaité sur le serveur
                        filepart.write(path);
                        object.getClass().getDeclaredMethod(set, default_class).invoke(object, tmp);
                        // System.out.println("tafa hatram farany");
                    }
                } catch (ServletException e) {
                    // TODO: handle exception
                    System.out.println(e.getMessage());
                }
                
             
                // Enumeration<String> parameterNames = request.getParameterNames();
                // String paramName = "";
                // while (parameterNames.hasMoreElements()) {
                //     paramName = parameterNames.nextElement();
                //     // System.out.println("Nom du champ : " + paramName);
                //     if(paramName.compareToIgnoreCase(field.getName()) == 0) {
                //         System.out.println("Nom du champ : " + paramName);
                        
                //     }
                // }
            }
            
            
        }
        catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public void setObjectAttributeByRequest(Object object, Field[] field, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            for (Field f : field) {
                setObjectAttributeByRequest(object, f, request, response);
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public ModelView invokeMethodByRequestParameters(Object object, HttpServletRequest request)  throws Exception {
        ModelView val = null;
        try {
            Method[] all_methods = this.getMethodsAnnoted(object.getClass(), MethodUrl.class);
                    // Annotation annotation = null;
                    Parameter[] parameters = null;
                    Vector object_attribut = null;
                    for (int i = 0; i < all_methods.length; i++) {
                        parameters = all_methods[i].getParameters();
                        if( verifyParamAnnotaion(parameters, ParamName.class)  == true) {
                            object_attribut = new Vector<>();
                            for (int j = 0; j < parameters.length; j++) {
                                object_attribut.add(this.adequatObjectForParameter(request, parameters[j], all_methods[i] ) )  ;
                                // System.out.println(object_attribut.get(j));
                            }
                            // System.out.println(parameters.length + "  " + object_attribut.toArray().length + " len");
                            val =  (ModelView)all_methods[i].invoke(object, object_attribut.toArray());
                            // System.out.println(all_methods[i].getName() +  "  jaoi OI F");
                        }
                        // System.out.println("huhuhu");
                        // if(parameters.length != 0) {
                        //     System.out.println("huhuhu " + parameters[0]);
                        // }
                        // for (int j = 0; j < parameters.length; j++) {
                        //     System.out.println("Parameter name " +parameters[i].getName());
                        // }
                        
                    }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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

    public void setDefaultValue(Object obj) throws Exception {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            Class type;
            for (Field field : fields) {
                field.setAccessible(true);
                type = field.getType(); 
                if(type.isPrimitive()) {
                    // boolean, char, boolean, int, double, float, byte, short, long
                    if(type == boolean.class) {
                        field.set(obj, false);
                    }
                    else if(type == char.class) {
                        field.set(obj, "");
                    }
                    else {
                        field.set(obj, 0);
                    }
                }
                else {
                    field.set(obj, null);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    public Mapping getTarget(String url, HashMap<String, Mapping> dictionary) throws Exception {
        Mapping target = null;
        for (Map.Entry mapEntry : dictionary.entrySet()) {
            if(mapEntry.getKey().toString().compareToIgnoreCase(url) == 0) {
                return (Mapping) mapEntry.getValue();
            }
        }
        if(target == null) {
            throw new Exception("URL introuvable");
        }
        return target;
    }

    public boolean verifyAuth(Class class_mapping, Mapping mapping,String init_param_profil, String init_param_connect ,HttpSession session) throws Exception {
        boolean val = false;
        try {
            Method action = class_mapping.getDeclaredMethod(mapping.getMethod(), mapping.getParameters());
            Auth authState = action.getAnnotation(Auth.class) ;
            if(authState == null) {
                return true;
            }
            else {
                String profil = authState.profil(); 
                System.out.println("mv    "+ session.getAttribute(init_param_profil));  
                if(session.getAttribute(init_param_profil) != null || session.getAttribute(init_param_connect) != null) {
                    boolean isConnect = (boolean)session.getAttribute(init_param_connect);
                    if(profil.equals("") && isConnect == true ) {
                        val = true;
                    }
                    else if(session.getAttribute(init_param_profil).equals(profil) && isConnect == true){
                        // if(session.getAttribute(getInitParameter("profil")).equals(profil))
                        val = true;
                    }
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