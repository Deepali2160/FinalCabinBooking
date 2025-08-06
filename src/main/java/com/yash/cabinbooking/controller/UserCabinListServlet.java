package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.CabinService;
import com.yash.cabinbooking.serviceimpl.CabinServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/cabins")
public class UserCabinListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CabinService cabinService;

    @Override
    public void init() throws ServletException {
        cabinService = new CabinServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listAvailableCabins(request, response);
                break;
            case "view":
                viewCabinDetails(request, response);
                break;
            case "book":
                showBookingForm(request, response);
                break;
            default:
                listAvailableCabins(request, response);
        }
    }

    private void listAvailableCabins(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Cabin> availableCabins = cabinService.getAvailableCabins();
            request.setAttribute("cabins", availableCabins);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cabin-list.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading cabins: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void viewCabinDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int cabinId = Integer.parseInt(request.getParameter("id"));
            Cabin cabin = cabinService.getCabinById(cabinId);

            if (cabin != null && cabin.isAvailable()) {
                request.setAttribute("cabin", cabin);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/cabin-details.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("error", "Cabin not found or not available");
                listAvailableCabins(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cabin ID");
            listAvailableCabins(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading cabin details: " + e.getMessage());
            listAvailableCabins(request, response);
        }
    }

    private void showBookingForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int cabinId = Integer.parseInt(request.getParameter("id"));
            Cabin cabin = cabinService.getCabinById(cabinId);

            if (cabin != null && cabin.isAvailable()) {
                request.setAttribute("cabin", cabin);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/book-cabin.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("error", "Cabin not found or not available for booking");
                listAvailableCabins(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cabin ID");
            listAvailableCabins(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading booking form: " + e.getMessage());
            listAvailableCabins(request, response);
        }
    }
}
