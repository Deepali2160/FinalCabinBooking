package com.yash.cabinbooking.controller;


import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.CabinService;
import com.yash.cabinbooking.serviceimpl.CabinServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/available-cabins")

public class AvailableCabinsServlet extends HttpServlet {
    private CabinService cabinService = new CabinServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cabin> cabins = cabinService.getAllCabins();
        request.setAttribute("cabins", cabins);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/available-cabins.jsp");
        dispatcher.forward(request, response);
    }
}
