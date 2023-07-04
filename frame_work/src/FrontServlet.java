package servlet;

import mapping.*;
import modelview.ModelView;
import utilitaire.Utilitaire;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.sql.Date;
import java.text.Annotation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.MultipartConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.security.auth.login.Configuration.Parameters;
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
 * @param request  servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException      if an I/O error occurs
 */

// @MultipartConfig(
//     fileSizeThreshold = 1024 * 1024,  // Taille maximale d'un fichier avant d'être stocké sur le disque (1 Mo)
//     maxFileSize = 1024 * 1024 * 5,   // Taille maximale d'un fichier (5 Mo)
//     maxRequestSize = 1024 * 1024 * 10 // Taille maximale totale des données multipartes (10 Mo)
// )
public class FrontServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */

    HashMap<String, Mapping> MappingUrls;
    HashMap<Class, Object> ObjectClass;
    Fonction f = new Fonction();

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

    
    void generateMappings() {
        Mapping mapping = new Mapping();
        ServletContext context = this.getServletContext();
        try {
            // Avoir l'url
            ClassLoader loader = context.getClassLoader();
            URI uri = Objects.requireNonNull(loader.getResource("")).toURI();
            File file = new File(uri);
            String classPath = file.getPath();

            // inserer dans mapping
            Vector values = mapping.getAllValuesAnnoted(file, MethodUrl.class);
            Vector correspondance = null;
            HashMap<String, Mapping> tmp = new HashMap<String, Mapping>();
            for (int i = 0; i < values.size(); i++) {
                correspondance = mapping.getCorrespondanceInPackage(file, MethodUrl.class, values.get(i).toString());
                for (int j = 0; j < correspondance.size(); j++) {
                    tmp.put(values.get(i).toString(), (Mapping) (correspondance.get(j)));
                }
            }

            // singleton
            HashMap object_class_tmp = new HashMap<Class, Object>();
            // System.out.println("eoaaa a a a a " + classPath);
            File[] directories = file.listFiles((current, name) -> new File(current, name).isDirectory());
            Class[] all_Classes = null;
            Object obj = null;
            for (File dir : directories) {
                all_Classes = f.getClassInRepository(dir.getAbsolutePath().toString());
                for (Class one_class : all_Classes) {
                    Scope sc = (Scope)one_class.getAnnotation(Scope.class);
                    if(sc != null) {
                        if(sc.value().compareToIgnoreCase("singleton") == 0) {
                            obj = one_class.getConstructor().newInstance();
                            object_class_tmp.put(one_class, obj);
                            // System.out.println("  huhuhuhu  "+ obj);
                        }
                    }
                    
                }
            }
            
            this.MappingUrls = tmp;
            this.ObjectClass = object_class_tmp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void dispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url_load = getSplit(request.getRequestURL().toString());
        Fonction ma_fonction = new Fonction();
        PrintWriter out = response.getWriter();
        try {
            for (Map.Entry mapEntry : this.MappingUrls.entrySet()) {
                out.println("All results");
                out.println("cle " + mapEntry.getKey());
                out.println("valeur " + ((Mapping) mapEntry.getValue()).getClassName() + " "
                        + ((Mapping) mapEntry.getValue()).getMethod());
                if (mapEntry.getKey().toString().compareToIgnoreCase(url_load) == 0) {
                    System.out.println("On peut loader le view");

                    // construire la classe
                    Class class_mapping = Class.forName(((Mapping) mapEntry.getValue()).getClassName());

                    Object object;
                    if(ObjectClass.containsKey(class_mapping) == true) {
                        object = ObjectClass.get(class_mapping);
                        f.setDefaultValue(object);
                        // System.out.println("tafatfatfayatfaytf");
                    }
                    else {
                        object = class_mapping.getConstructor().newInstance();
                    }

                    System.out.println("Object " + object);

                    // avoir les attributs du classe
                    Field[] attributs = class_mapping.getDeclaredFields();
                    f.setObjectAttributeByRequest(object, attributs, request, response);
                    

                    // invocation du methode
                    f.invokeMethodByRequestParameters(object, request);

                    // si l'objet n'est pas null
                    // System.out.println(object + " obj");
                    if (object != null) {
                        request.setAttribute(object.getClass().getSimpleName(), object);
                    }

                    ModelView view = ma_fonction.getViewByMapping((Mapping) mapEntry.getValue(), request);
                    HashMap<String, Object> data_to_send = view.getData();
                    if (data_to_send != null) {
                        for (Map.Entry data : data_to_send.entrySet()) {
                            request.setAttribute(data.getKey().toString(), data.getValue());
                        }
                    }
                    // out.println(view.getView());
                    request.getRequestDispatcher("/" + view.getView()).forward(request, response);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        }
    }

    public void init() throws ServletException {
        generateMappings();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        // out.println();
        try {
            dispatch(request, response);
        } catch (Exception e) {
            // TODO: handle exception
            out.println(e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
