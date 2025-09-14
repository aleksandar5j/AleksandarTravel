package api;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.sql.SQLException;
import main.Main;
import org.json.JSONObject;

@WebServlet(name = "Slike", urlPatterns = {"/api/slike"})
@MultipartConfig(maxRequestSize = 25_000_000)
public class Slike extends HttpServlet {

    public static byte[] NO_DATA = new byte[0];
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("image");

        JSONObject obj = new JSONObject();
        
        int des_id = Integer.parseInt(request.getParameter("des_id"));
        
        byte[] res = NO_DATA; 
        
        try {
            var q = Main.db.query("SELECT sli_data FROM slike WHERE des_id = ?", des_id);
            res = (byte[]) q.get(0).get("sli_data");
            obj.put("data", res);
        } catch (SQLException ex) {
            System.out.println("Greska: " + ex.getMessage());
        }

        try (OutputStream out = response.getOutputStream()) {
            out.write(res);
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
