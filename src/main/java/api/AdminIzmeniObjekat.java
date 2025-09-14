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

@WebServlet(name = "AdminIzmeniObjekat", urlPatterns = {"/api/adminizmeniobjekat"})
public class AdminIzmeniObjekat extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        
        JSONObject obj = new JSONObject();
        JSONObject params = new JSONObject(new JSONTokener(request.getReader()));
        
        HttpSession session=request.getSession(false);
        if(session==null || session.getAttribute("kor_id")==null) {
            obj.put("res", "ERR");
            response.setStatus(403); //forbidden
            return;
        }
        
        boolean jeAdmin = (boolean) session.getAttribute("kor_jeadmin");
        if(!jeAdmin) {
            obj.put("res", "ERR");
            obj.put("msg", "Nemate pravo pristupa ovoj stranici!");
            response.setStatus(401);
            return;
        }

        try {
            
            int obj_id = params.getInt("obj_id");
            String obj_naziv = params.getString("obj_naziv");
            String obj_adresa = params.getString("obj_adresa");
            String obj_telefon = params.getString("obj_telefon");
            int obj_brojzvezdica = params.getInt("obj_brojzvezdica");
            String obj_opis = params.getString("obj_opis");
            
            var res = Main.db.execute("UPDATE objekti "
                    + "SET obj_naziv = ?, obj_adresa = ?, obj_telefon = ?, obj_brojzvezdica = ?, obj_opis = ? "
                    + "WHERE obj_id = ?", obj_naziv, obj_adresa, obj_telefon, obj_brojzvezdica, obj_opis, obj_id);
            obj.put("res", "OK");
            obj.put("msg", "Uspešno ste izmenili objekat!");
            obj.put("data", res);       
        }      
        catch (Exception ex) {
            ex.printStackTrace();
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
