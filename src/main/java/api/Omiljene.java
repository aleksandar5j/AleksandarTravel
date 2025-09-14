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
import jakarta.servlet.http.HttpSession;
import main.Main;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author user
 */
@WebServlet(name = "Omiljene", urlPatterns = {"/api/omiljene"})
public class Omiljene extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        JSONObject obj = new JSONObject();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kor_id") == null) {
            obj.put("res", "ERR");
            obj.put("msg", "Niste ulogovani!");
            response.setStatus(403); //forbidden
            return;
        }

        try {
            int kor_id = ((Number) session.getAttribute("kor_id")).intValue();

            var res = Main.db.query(
                    "SELECT o.obj_id, o.obj_naziv, d.des_id, d.des_naziv "
                    + "FROM omiljene om "
                    + "JOIN objekti o ON om.obj_id = o.obj_id "
                    + "JOIN destinacije d ON om.des_id = d.des_id "
                    + "WHERE om.kor_id = ?", kor_id);
            obj.put("res", "OK");
            obj.put("data", res);
        } catch (Exception ex) {
            obj.put("res", "ERR");
            obj.put("msg", ex.getMessage());
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
