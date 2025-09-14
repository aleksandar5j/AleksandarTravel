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
@WebServlet(name = "Aranzmani", urlPatterns = {"/api/aranzmani"})
public class Aranzmani extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        JSONObject obj = new JSONObject();

        try {
            String drz_id = request.getParameter("drz_id");
            String des_id = request.getParameter("des_id");
            String ara_id = request.getParameter("ara_id");
            String ara_od = request.getParameter("ara_od");
            

            String query = "SELECT "
                    + "a.ara_id, a.pre_id, a.obj_id, "
                    + "a.ara_od, a.ara_do, a.ara_cena, a.ara_opis, "
                    + "o.obj_naziv, o.obj_opis, "
                    + "d.des_naziv, "
                    + "d.des_id, "
                    + "z.drz_naziv "
                    + "FROM aranzmani a "
                    + "JOIN objekti o ON a.obj_id = o.obj_id "
                    + "JOIN destinacije d ON o.des_id = d.des_id "
                    + "JOIN drzave z ON d.drz_id = z.drz_id "
                    + "WHERE 1=1 ";

            if (drz_id != null && !drz_id.isEmpty()) {
                query += " AND z.drz_id = " + drz_id;
            }
            if (des_id != null && !des_id.isEmpty()) {
                query += " AND d.des_id = " + des_id;
            }
            if (ara_id != null && !ara_id.isEmpty()) {
                query += " AND a.ara_id = " + ara_id;
            }
            if (ara_od != null && !ara_od.isEmpty()) {
                query += " AND a.ara_od = " + ara_od;
            }

            var res = Main.db.query(query);
            obj.put("data", res);
        } catch (Exception ex) {
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
