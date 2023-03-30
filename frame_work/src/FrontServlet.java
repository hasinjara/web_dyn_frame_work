package servlet;
import mapping.*;

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
    
    public void init() throws ServletException {
        ServletContext context = this.getServletContext();
        Mapping mapping = new Mapping();
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


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            PrintWriter out=response.getWriter();
            // out.print("Hello world");
            // out.print(request.getRequestURL().toString());
           
            for(Map.Entry mapEntry : this.MappingUrls.entrySet()) {
                out.println("All result");
                out.println("cle "+ mapEntry.getKey());
                out.println("valeur "+ ((Mapping)mapEntry.getValue()).getClassName() + " " +((Mapping)mapEntry.getValue()).getMethod());
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
