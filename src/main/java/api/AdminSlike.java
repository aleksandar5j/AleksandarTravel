package api;

import static api.Slike.NO_DATA;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.OutputStream;
import java.sql.SQLException;
import main.Main;
import org.json.JSONObject;

@WebServlet(name = "AdminSlike", urlPatterns = {"/api/adminslike"})
public class AdminSlike extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        JSONObject obj = new JSONObject();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kor_id") == null) {
            obj.put("res", "ERR");
            response.setStatus(403);
            try (PrintWriter out = response.getWriter()) {
                obj.write(out);
            }
            return;
        }

        boolean jeAdmin = (boolean) session.getAttribute("kor_jeadmin");
        if (!jeAdmin) {
            obj.put("res", "ERR");
            obj.put("msg", "Nemate pravo pristupa ovoj stranici!");
            response.setStatus(401);
            try (PrintWriter out = response.getWriter()) {
                obj.write(out);
            }
            return;
        }

        try {
            // Iz baze uzmi sve slike, ali ne šalji sliku u bajtovima (ili ako želiš, šalji Base64)
            var q = Main.db.query("SELECT sli_id, obj_id, des_id FROM slike");

            obj.put("res", "OK");
            obj.put("slike", q);
        } catch (SQLException ex) {
            obj.put("res", "ERR");
            obj.put("msg", ex.getMessage());
            ex.printStackTrace();
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
