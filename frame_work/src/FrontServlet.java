package servlet;
import mapping.*;
import modelview.ModelView;
import utilitaire.Utilitaire;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import annotation.*;
import fonction.Fonction;

import java.util.Vector;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.lang.ClassLoader;
import java.util.*;


 /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
public class FrontServlet extends HttpServlet {
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    HashMap <String, Mapping> MappingUrls; 

    String getSplit(String url) {
        Utilitaire u = new Utilitaire();
    return u.getUrl(url);
    }

    String getClassPath() {
    String classPath = null;
        try {
            ServletContext context = this.getServletContext();
            ClassLoader loader = context.getClassLoader();
            URI uri = Objects.requireNonNull(loader.getResource("")).toURI();
            File f = new File(uri);
            classPath = f.getPath();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    return classPath;
    }

    String toUpperCaseAt(String s, int ind) {
        char[] tab = s.toCharArray();
        String maj = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String min = "abcdefghijklmnopqrstuvwxyz";
        char[] maj_char = maj.toCharArray();
        char[] min_char = min.toCharArray();
        for(int i = 0 ; i<maj_char.length ; i++) {
            if(tab[ind] == min_char[i]) {
                tab[ind] = maj_char[i];
            }
        }
        String res = new String(tab);
        return res;
    }

    void generateMappings() {
        Mapping mapping = new Mapping();
        ServletContext context = this.getServletContext();
        try {
            // Avoir l'url
            ClassLoader loader = context.getClassLoader();
            URI uri = Objects.requireNonNull(loader.getResource("")).toURI();
            File f = new File(uri);
            String classPath = f.getPath();
           
            //inserer dans mapping
            Vector values = mapping.getAllValuesAnnoted(f, MethodUrl.class);
            Vector correspondance = null;
            HashMap <String, Mapping> tmp = new HashMap<String, Mapping>();
            for (int i = 0; i < values.size(); i++) {
                correspondance = mapping.getCorrespondanceInPackage(f, MethodUrl.class, values.get(i).toString());
                for (int j = 0; j < correspondance.size(); j++) {
                    tmp.put(values.get(i).toString(), (Mapping)(correspondance.get(j)));
                }
            }
            this.MappingUrls = tmp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void dispatch(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String url_load = getSplit(request.getRequestURL().toString());
        Fonction ma_fonction = new Fonction();
        PrintWriter out=response.getWriter();
        try {
            for(Map.Entry mapEntry : this.MappingUrls.entrySet()) {
                out.println("All results");
                out.println("cle "+ mapEntry.getKey());
                out.println("valeur "+ ((Mapping)mapEntry.getValue()).getClassName() + " " +((Mapping)mapEntry.getValue()).getMethod());
                if(mapEntry.getKey().toString().compareToIgnoreCase(url_load) == 0) {
                    // out.println("On peut loader le view");
                    
                    // construire la classe
                    Class class_mapping = Class.forName(((Mapping)mapEntry.getValue()).getClassName());
                    Object object = class_mapping.getConstructor().newInstance();
                    System.out.println("Object " + object);

                    // avoir les attributs du classe
                    Field[] attributs = class_mapping.getDeclaredFields();
                    Class default_class = String.class; 
                    for (Field field : attributs) {
                        out.println( "Atributs :"+ field.getName() );
                        out.println( "Valeur :"+ request.getParameter(field.getName()));
                        if(request.getParameter(field.getName()) != null ) {
                            field.setAccessible(true);
                            default_class = (Class)field.getType();
                            System.out.println("Type "+ default_class + " type "+ (Class)field.getType());
                            String set = "set"+ toUpperCaseAt(field.getName(), 0);
                            if(default_class.equals(String.class)) {
                                object.getClass().getDeclaredMethod(set ,default_class).invoke(object, request.getParameter(field.getName()));
                            }
                            if(default_class.equals(int.class)) {
                                int a = 0;
                                if(request.getParameter(field.getName()) != null) {
                                    a = Integer.valueOf(request.getParameter(field.getName()));
                                }
                                object.getClass().getDeclaredMethod(set ,default_class).invoke(object, a);
                            }
                            if(default_class.equals(double.class)){
                                double a = 0;
                                if(request.getParameter(field.getName()) != null) {
                                    a = Double.valueOf(request.getParameter(field.getName()));
                                }
                                object.getClass().getDeclaredMethod(set,default_class).invoke(object, a);
                            }
                            if(default_class.equals(float.class)){
                                float a = 0;
                                if(request.getParameter(field.getName()) != null) {
                                    a = Float.valueOf(request.getParameter(field.getName()));
                                }
                                object.getClass().getDeclaredMethod(set,default_class).invoke(object, a);
                            }
                            if(default_class.equals(java.util.Date.class)) {
                                java.util.Date a = new java.util.Date();
                                object.getClass().getDeclaredMethod(set,default_class).invoke(object, a);
                            }
                            
                            
                        }
                    }

                    // si l'objet n'est pas null
                    System.out.println(object + " obj");
                    if(object != null ) {
                        request.setAttribute(object.getClass().getSimpleName(), object);
                    }

                    ModelView view = ma_fonction.getViewByMapping((Mapping)mapEntry.getValue());
                    HashMap<String, Object> data_to_send = view.getData();
                    if(data_to_send != null) {
                        for(Map.Entry data : data_to_send.entrySet()){
                            request.setAttribute(data.getKey().toString(), data.getValue());
                        }
                    }
                    // out.println(view.getView());
                    request.getRequestDispatcher("/"+view.getView()).forward(request, response);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    
    public void init() throws ServletException {
        generateMappings();
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            PrintWriter out=response.getWriter();
            // out.println();
            try {
                dispatch(request, response);
            } catch (Exception e) {
                // TODO: handle exception
                out.println(e.getMessage());
            }
    }



    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
     @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
