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

@WebServlet(name = "Login", urlPatterns = {"/api/login"})
public class Login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        
        JSONObject params=new JSONObject(new JSONTokener(request.getReader()));
        String kor_kime = params.getString("kor_kime");
        String kor_sifra = params.getString("kor_sifra");
            
        JSONObject res = new JSONObject();
        try {
            var q = Main.db.query("SELECT kor_id, kor_imeiprezime, kor_jeadmin FROM korisnici WHERE kor_kime=? AND kor_sifra=?", kor_kime, kor_sifra);
            if(q.isEmpty()) {
                res.put("res","ERR");
            } else {
                HttpSession session = request.getSession();
                var user = q.get(0);
                var kor_id = user.get("kor_id"); //1
                var kor_imeiprezime = user.get("kor_imeiprezime");
                var kor_jeadmin = user.get("kor_jeadmin");
                session.setAttribute("kor_id", kor_id);
                session.setAttribute("kor_imeiprezime", kor_imeiprezime);
                session.setAttribute("kor_jeadmin", kor_jeadmin);
                res.put("res", "OK");
                res.put("data", user);
            }
        } catch(Exception e) {
            e.printStackTrace();
            res.put("res","ERR");
            response.setStatus(500);
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
