package api;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import main.Main;

@WebServlet(name = "Slika", urlPatterns = {"/api/slika"})
public class Slika extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("image/jpeg");
         String id = request.getParameter("id");
        if (id == null) {
            response.setStatus(400);
            return;
        }
        try {
            var q = Main.db.query("SELECT sli_data FROM slike WHERE sli_id = ?", Integer.parseInt(id));
            if (q.size() == 0) {
                response.setStatus(404);
                return;
            }
            byte[] data = (byte[]) q.get(0).get("sli_data");
            response.setContentType("image/jpeg"); // ili drugi content-type u zavisnosti od slike
            try (OutputStream out = response.getOutputStream()) {
                out.write(data);
            }
        } catch (Exception e) {
            response.setStatus(500);
            e.printStackTrace();
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
