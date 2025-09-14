package api;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Base64;
import main.Main;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "ImagesListForObjects", urlPatterns = {"/api/imglistforobj"})
@MultipartConfig(maxRequestSize = 25_000_000)
public class ImagesListForObjects extends HttpServlet {

    public static byte[] NO_DATA = new byte[0];

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json"); // vraćamo JSON

        JSONObject obj = new JSONObject();
        JSONArray imagesArray = new JSONArray();

        int obj_id = Integer.parseInt(request.getParameter("obj_id"));

        try {
            var results = Main.db.query(
                    "SELECT s.sli_data, o.obj_naziv, d.des_naziv FROM slike s "
                    + "LEFT JOIN objekti o ON s.obj_id = o.obj_id "
                    + "LEFT JOIN destinacije d ON s.des_id = d.des_id "
                    + "WHERE s.obj_id = ?", obj_id);

            if (!results.isEmpty()) {
                // povuci naziv objekta iz prvog reda
                String obj_naziv = (String) results.get(0).get("obj_naziv");
                obj.put("obj_naziv", obj_naziv); // stavi u JSON
                String des_naziv = (String) results.get(0).get("des_naziv");
                obj.put("des_naziv", des_naziv); // stavi u JSON

                for (var row : results) {
                    byte[] imageData = (byte[]) row.get("sli_data");
                    if (imageData != null) {
                        String base64Image = Base64.getEncoder().encodeToString(imageData);
                        imagesArray.put(base64Image);
                    }
                }
                obj.put("res", "OK");
                obj.put("data", imagesArray);
            } else {
                obj.put("res", "ERR");
                obj.put("data", new JSONArray());
            }
        } catch (SQLException ex) {
            System.out.println("greska: " + ex.getMessage());
            obj.put("res", "ERR");
            obj.put("msg", ex.getMessage());
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(obj.toString());
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
