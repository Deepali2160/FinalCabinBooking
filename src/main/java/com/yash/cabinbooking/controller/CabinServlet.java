package com.yash.cabinbooking.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.CabinService;
import com.yash.cabinbooking.serviceimpl.CabinServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/admin/cabin")
public class CabinServlet extends HttpServlet {

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

            try {
                switch (action) {
                    case "add":
                        showCabinForm(request, response);
                        break;
                    case "edit":
                        showEditForm(request, response);
                        break;
                    case "delete":
                        deleteCabin(request, response);
                        break;
                    case "toggleAvailability":
                        toggleAvailability(request, response);
                        break;
                    case "viewDetails":
                        showCabinDetails(request, response);
                        break;
                    default:
                        listCabins(request, response);
                        break;
                }
            } catch (Exception e) {
                handleError(request, response, "Error: " + e.getMessage());
            }
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            String action = request.getParameter("action");

            if ("delete".equals(action) || "toggleAvailability".equals(action)) {
                handleSimpleRequest(request, response, action);
                return;
            }

            if (!ServletFileUpload.isMultipartContent(request)) {
                request.setAttribute("error", "Form must have enctype=multipart/form-data");
                listCabins(request, response);
                return;
            }

            handleMultipartRequest(request, response);
        }

        private void handleSimpleRequest(HttpServletRequest request, HttpServletResponse response, String action)
                throws ServletException, IOException {
            if ("delete".equals(action)) {
                deleteCabin(request, response);
            } else if ("toggleAvailability".equals(action)) {
                toggleAvailability(request, response);
            } else {
                listCabins(request, response);
            }
        }

        private void handleMultipartRequest(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            Cabin cabin = new Cabin();
            boolean isEdit = false;

            try {
                // Get the real path to the webapp's images directory
                String uploadPath = getServletContext().getRealPath("/images");
                File uploadDir = new File(uploadPath);

                // Create directory if it doesn't exist
                if (!uploadDir.exists()) {
                    boolean created = uploadDir.mkdirs();
                    if (!created) {
                        throw new IOException("Failed to create images directory");
                    }
                }

                // Parse the request to get file items
                List<FileItem> items = upload.parseRequest(request);
                String fileName = "";

                for (FileItem item : items) {
                    if (item.isFormField()) {
                        processFormField(item, cabin);
                        if ("id".equals(item.getFieldName()) && !item.getString().isEmpty()) {
                            isEdit = true;
                        }
                    } else {
                        if (!item.getName().isEmpty()) {
                            // Generate unique filename with timestamp
                            fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                                    + "_" + new File(item.getName()).getName();
                            String filePath = uploadPath + File.separator + fileName;

                            // Handle existing file for edits
                            if (isEdit) {
                                Cabin existingCabin = cabinService.getCabinById(cabin.getId());
                                if (existingCabin != null && existingCabin.getImageUrl() != null) {
                                    // Extract just the filename from the URL
                                    String oldFileName = existingCabin.getImageUrl().substring(
                                            existingCabin.getImageUrl().lastIndexOf('/') + 1);
                                    File oldFile = new File(uploadPath + File.separator + oldFileName);
                                    if (oldFile.exists()) {
                                        oldFile.delete();
                                    }
                                }
                            }

                            // Save the new file
                            item.write(new File(filePath));

                            // Store relative path in database (without context path)
                            cabin.setImageUrl("images/" + fileName);
                        } else if (isEdit) {
                            // Keep existing image if no new file was uploaded
                            Cabin existingCabin = cabinService.getCabinById(cabin.getId());
                            if (existingCabin != null) {
                                cabin.setImageUrl(existingCabin.getImageUrl());
                            }
                        }
                    }
                }

                // Validate cabin data
                String error = validateCabin(cabin);
                if (error != null) {
                    request.setAttribute("error", error);
                    if (isEdit) {
                        showEditForm(request, response);
                    } else {
                        showCabinForm(request, response);
                    }
                    return;
                }

                // Save or update cabin
                if (isEdit) {
                    cabinService.updateCabin(cabin);
                    request.setAttribute("message", "Cabin updated successfully!");
                } else {
                    cabin.setCreatedAt(LocalDateTime.now());
                    cabinService.addCabin(cabin);
                    request.setAttribute("message", "Cabin added successfully!");
                }

            } catch (Exception e) {
                handleError(request, response, "Error: " + e.getMessage());
                return;
            }

            listCabins(request, response);
        }

    private void processFormField(FileItem item, Cabin cabin) throws Exception {
        String fieldName = item.getFieldName();
        String value = item.getString();

        switch (fieldName) {
            case "id":
                if (!value.isEmpty()) cabin.setId(Integer.parseInt(value));
                break;
            case "name":
                cabin.setName(value);
                break;
            case "description":
                cabin.setDescription(value);
                break;
            case "location":
                cabin.setLocation(value);
                break;
            case "hourlyRate":
                cabin.setHourlyRate(Double.parseDouble(value));
                break;
            case "capacity":
                cabin.setCapacity(Integer.parseInt(value));
                break;
            case "amenities":
                cabin.setAmenities(value);
                break;
            case "isAvailable":
                cabin.setAvailable("true".equals(value));
                break;
        }
    }

    private String validateCabin(Cabin cabin) {
        if (cabin.getName() == null || cabin.getName().trim().isEmpty()) {
            return "Name is required";
        }
        if (cabin.getLocation() == null || cabin.getLocation().trim().isEmpty()) {
            return "Location is required";
        }
        if (cabin.getHourlyRate() < 50) {
            return "Hourly rate must be at least 50";
        }
        if (cabin.getCapacity() < 1) {
            return "Capacity must be at least 1";
        }
        return null;
    }

    private void listCabins(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cabin> cabins = cabinService.getAllCabins();
        request.setAttribute("cabinsList", cabins);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/cabin.jsp");
        dispatcher.forward(request, response);
    }

    private void showCabinForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("cabin", new Cabin());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/cabin.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Cabin cabin = cabinService.getCabinById(id);
            request.setAttribute("cabin", cabin != null ? cabin : new Cabin());
        } catch (Exception e) {
            request.setAttribute("error", "Error loading cabin: " + e.getMessage());
        }
        request.getRequestDispatcher("/admin/cabin.jsp").forward(request, response);
    }

    private void deleteCabin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean deleted = cabinService.deleteCabin(id);
            request.setAttribute(deleted ? "message" : "error",
                    deleted ? "Cabin deleted successfully!" : "Failed to delete cabin");
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting cabin: " + e.getMessage());
        }
        listCabins(request, response);
    }

    private void toggleAvailability(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean available = Boolean.parseBoolean(request.getParameter("available"));
            Cabin cabin = cabinService.getCabinById(id);
            if (cabin != null) {
                cabin.setAvailable(available);
                cabinService.updateCabin(cabin);
                request.setAttribute("message", "Availability updated!");
            } else {
                request.setAttribute("error", "Cabin not found");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error updating availability: " + e.getMessage());
        }
        listCabins(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        listCabins(request, response);
    }

    private void showCabinDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Cabin cabin = cabinService.getCabinById(id);
            if (cabin != null) {
                request.setAttribute("cabin", cabin);
                request.getRequestDispatcher("/admin/cabin-details.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Cabin not found");
                listCabins(request, response);
            }
        } catch (Exception e) {
            handleError(request, response, "Error loading cabin details: " + e.getMessage());
        }
    }
}
