/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package api;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import main.Main;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author user
 */
@WebServlet(name = "ImagesAdd", urlPatterns = {"/api/imagesadd"})
@MultipartConfig(maxRequestSize = 25_000_000)
public class ImagesAdd extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        JSONObject res = new JSONObject();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("kor_id") == null) {
            res.put("res", "ERR");
            response.setStatus(401);
            try (PrintWriter out = response.getWriter()) {
                res.write(out);
            }
            return;
        }

        boolean jeAdmin = (boolean) session.getAttribute("kor_jeadmin");
        if (!jeAdmin) {
            res.put("res", "ERR");
            res.put("msg", "Nemate pravo pristupa ovoj stranici!");
            response.setStatus(401);
            try (PrintWriter out = response.getWriter()) {
                res.write(out);
            }
            return;
        }

        Part part = request.getPart("image");
        if (part == null) {
            res.put("res", "ERR");
            res.put("msg", "Nema poslate slike");
            response.setStatus(400);
            try (PrintWriter out = response.getWriter()) {
                res.write(out);
            }
            return;
        }
        long size = part.getSize();
        byte[] data;
        try (InputStream in = part.getInputStream()) {
            data = in.readNBytes((int) size);
        }

        try {
            // Uzmi parametre iz requesta, a ne iz JSON-a
            String objIdStr = request.getParameter("obj_id");
            String desIdStr = request.getParameter("des_id");
            if (objIdStr == null || desIdStr == null) {
                throw new Exception("Nedostaju parametri obj_id ili des_id");
            }
            int obj_id = Integer.parseInt(objIdStr);
            int des_id = Integer.parseInt(desIdStr);

            Main.db.execute("INSERT INTO slike (age_id, obj_id, des_id, sli_data) VALUES (?, ?, ?, ?)", 1, obj_id, des_id, data);

            res.put("res", "OK");
        } catch (Exception e) {
            res.put("res", "ERR");
            res.put("message", e.getMessage());
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
