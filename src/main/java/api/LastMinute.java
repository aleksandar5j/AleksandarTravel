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

@WebServlet(name = "LastMinute", urlPatterns = {"/api/lastminute"})
public class LastMinute extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        JSONObject obj = new JSONObject();

        try {
            var res = Main.db.query(
                    "SELECT "
                    + " a.ara_id, "
                    + " a.obj_id, "
                    + " a.ara_popust, "
                    + " a.ara_od, "
                    + " a.ara_do, "
                    + " a.ara_cena, "
                    + " ROUND(a.ara_cena - (a.ara_cena * a.ara_popust / 100), 0) AS cena_sa_popustom, "
                    + " a.ara_opis, "
                    + " d.drz_naziv, "
                    + " de.des_naziv, "
                    + " de.des_id "
                    + "FROM aranzmani a "
                    + "JOIN objekti o ON a.obj_id = o.obj_id "
                    + "JOIN destinacije de ON o.des_id = de.des_id "
                    + "JOIN drzave d ON de.drz_id = d.drz_id "
                    + "WHERE a.ara_od BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) "
                    + "ORDER BY a.ara_od ASC"
            );

            obj.put("res", "OK");
            obj.put("data", res);

        } catch (SQLException ex) {
            obj.put("msg", "SQL greska!");
            obj.put("res", "ERR");
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
