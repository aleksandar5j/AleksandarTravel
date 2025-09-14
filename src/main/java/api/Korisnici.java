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

/**
 *
 * @author user
 */
@WebServlet(name = "Korisnici", urlPatterns = {"/api/korisnici"})
public class Korisnici extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
          
        JSONObject res=new JSONObject();
        
        HttpSession session=request.getSession(false);
        if(session==null || session.getAttribute("kor_id")==null) {
            res.put("res", "ERR");
            response.setStatus(403); //forbidden
            return;
        }
        
        boolean jeAdmin = (boolean) session.getAttribute("kor_jeadmin");
        if(!jeAdmin) {
            res.put("res", "ERR");
            res.put("msg", "Nemate pravo pristupa ovoj stranici!");
            response.setStatus(401);
            return;
        }
        
        try {
            var q = Main.db.query("SELECT kor_id, kor_imeiprezime, kor_telefon, kor_email, kor_sifra, kor_kime, kor_jeadmin, age_id FROM korisnici");
            res.put("res", "OK");
            res.put("data", q);
        } catch(Exception e) {
            res.put("res", "ERR");
            e.printStackTrace();
        }
        
        try (PrintWriter out = response.getWriter()) {
            res.write(out);
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
