package servlet;
import mapping.*;
import modelview.ModelView;
import utilitaire.Utilitaire;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import annotation.*;
import fonction.Fonction;

import java.util.Vector;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.lang.ClassLoader;


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
                out.println("All result");
                out.println("cle "+ mapEntry.getKey());
                out.println("valeur "+ ((Mapping)mapEntry.getValue()).getClassName() + " " +((Mapping)mapEntry.getValue()).getMethod());
                if(mapEntry.getKey().toString().compareToIgnoreCase(url_load) == 0) {
                    // out.println("On peut loader le view");
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
