package com.yash.cabinbooking.servlet;

import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.CabinService;
import com.yash.cabinbooking.service.impl.CabinServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/cabins")

public class CabinServlet extends HttpServlet {

    private CabinService cabinService;

    @Override
    public void init() {
        cabinService = new CabinServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        if ("view".equalsIgnoreCase(action)) {
            List<Cabin> cabins = cabinService.getAllCabins();
            req.setAttribute("cabins", cabins);
            req.getRequestDispatcher("/admin/cabin.jsp").forward(req, resp);
            return;
        }

        if ("delete".equals(action) && idParam != null) {
            int id = Integer.parseInt(idParam);
            cabinService.deleteCabin(id);
            resp.sendRedirect("cabin?action=view");
            return;
        }

        if ("toggleAvailability".equals(action) && idParam != null) {
            int id = Integer.parseInt(idParam);
            boolean status = Boolean.parseBoolean(req.getParameter("status"));
            cabinService.toggleAvailability(id, status);
            resp.sendRedirect("cabin?action=view");
            return;
        }

        if ("toggleFeatured".equals(action) && idParam != null) {
            int id = Integer.parseInt(idParam);
            boolean status = Boolean.parseBoolean(req.getParameter("status"));
            cabinService.toggleFeatured(id, status);
            resp.sendRedirect("cabin?action=view");
            return;
        }

        // default: list all cabins
        List<Cabin> cabinList = cabinService.getAllCabins();
        req.setAttribute("cabins", cabinList);
        req.getRequestDispatcher("/admin/cabin.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");

        Cabin cabin = new Cabin();
        cabin.setName(req.getParameter("name"));
        cabin.setDescription(req.getParameter("description"));
        cabin.setLocation(req.getParameter("location"));

        // Price
        double pricePerNight = 0.0;
        String pricePerNightParam = req.getParameter("pricePerNight");
        if (pricePerNightParam != null && !pricePerNightParam.trim().isEmpty()) {
            try {
                pricePerNight = Double.parseDouble(pricePerNightParam.trim());
            } catch (NumberFormatException ignored) {}
        }
        cabin.setPricePerNight(pricePerNight);

        // Guests
        int maxGuests = 0;
        String maxGuestsParam = req.getParameter("maxGuests");
        if (maxGuestsParam != null && !maxGuestsParam.trim().isEmpty()) {
            try {
                maxGuests = Integer.parseInt(maxGuestsParam.trim());
            } catch (NumberFormatException ignored) {}
        }
        cabin.setMaxGuests(maxGuests);

        // Bedrooms
        int bedrooms = 0;
        String bedroomsParam = req.getParameter("bedrooms");
        if (bedroomsParam != null && !bedroomsParam.trim().isEmpty()) {
            try {
                bedrooms = Integer.parseInt(bedroomsParam.trim());
            } catch (NumberFormatException ignored) {}
        }
        cabin.setBedrooms(bedrooms);

        // Bathrooms
        int bathrooms = 0;
        String bathroomsParam = req.getParameter("bathrooms");
        if (bathroomsParam != null && !bathroomsParam.trim().isEmpty()) {
            try {
                bathrooms = Integer.parseInt(bathroomsParam.trim());
            } catch (NumberFormatException ignored) {}
        }
        cabin.setBathrooms(bathrooms);

        // Remaining fields
        cabin.setAmenities(req.getParameter("amenities"));
        cabin.setImageUrl(req.getParameter("imageUrl"));
        cabin.setAvailable("on".equals(req.getParameter("isAvailable")));
        cabin.setFeatured("on".equals(req.getParameter("isFeatured")));

        boolean isSaved;
        if (idParam == null || idParam.isEmpty()) {
            // New cabin
            isSaved = cabinService.addCabin(cabin);
        } else {
            // Update existing cabin
            cabin.setId(Integer.parseInt(idParam));
            isSaved = cabinService.updateCabin(cabin);
        }

        if (isSaved) {
            resp.sendRedirect("cabin?action=view");
        } else {
            req.setAttribute("error", "Cabin not saved. Try again.");
            req.getRequestDispatcher("/admin/cabin.jsp").forward(req, resp);
        }
    }

}
