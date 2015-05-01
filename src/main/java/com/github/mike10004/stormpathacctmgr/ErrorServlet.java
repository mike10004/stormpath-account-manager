/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */
package com.github.mike10004.stormpathacctmgr;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mchaberski
 */
public class ErrorServlet extends HttpServlet {

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
        String codeStr = request.getParameter("code");
        int code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        if (codeStr != null && codeStr.matches("\\d+{1,3}")) {
            code = Integer.parseInt(codeStr);
        }
        response.setStatus(code);
        response.setContentType("text/plain;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(codeStr + " error");
        }
    }

}
