package com.example.inventory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** GET /inventory/items returns the stock list. GET /inventory/health is for Kubernetes probes. */
@WebServlet(urlPatterns = {"/items", "/health"})
public class InventoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if ("/health".equals(req.getServletPath())) {
            resp.getWriter().write("{\"status\":\"ok\"}");
        } else {
            resp.getWriter().write(Inventory.toJson());
        }
    }
}
