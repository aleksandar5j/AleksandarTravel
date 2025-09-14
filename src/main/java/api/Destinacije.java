/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package api;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.Main;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author user
 */
@WebServlet(name = "Destinacije", urlPatterns = {"/api/destinacije"})
public class Destinacije extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        JSONObject obj = new JSONObject();

        try {
            String drz_id = request.getParameter("drz_id");
            String query;
            if (drz_id != null && !drz_id.isEmpty()) {
                query = "SELECT des_id, drz_id, des_naziv FROM destinacije WHERE drz_id = " + drz_id;
            } else {
                query = "SELECT d.des_id, d.drz_id, d.des_naziv, dd.drz_naziv "
                        + "FROM destinacije d "
                        + "LEFT JOIN drzave dd ON dd.drz_id = d.drz_id";
            }

            var res = Main.db.query(query);
            obj.put("data", res);       
        } 
        
        catch (Exception ex) {
            ex.getMessage();
        }

        try (PrintWriter out = response.getWriter()) {
            obj.write(out);
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
