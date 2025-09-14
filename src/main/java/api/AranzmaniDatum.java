package api;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import main.Main;
import org.json.JSONObject;

@WebServlet(name = "AranzmaniDatum", urlPatterns = {"/api/aranzmanidatum"})
public class AranzmaniDatum extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        JSONObject obj = new JSONObject();

        String ara_od = request.getParameter("ara_od");

        try {
            var res = Main.db.query(
                    "SELECT d.des_id, d.des_naziv, dr.drz_naziv, a.ara_id, a.obj_id, a.ara_od, a.ara_do, a.ara_cena, a.ara_opis "
                    + "FROM aranzmani a "
                    + "JOIN objekti o ON o.obj_id = a.obj_id "
                    + "JOIN destinacije d ON d.des_id = o.des_id "
                    + "JOIN drzave dr ON dr.drz_id = d.drz_id "
                    + "WHERE DATE(a.ara_od) = ?",
                    ara_od
            );

            obj.put("data", res);
        } catch (SQLException ex) {
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
