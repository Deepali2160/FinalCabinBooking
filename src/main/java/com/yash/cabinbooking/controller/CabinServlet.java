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
import java.util.ArrayList;

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
                case "toggleFeatured":
                    toggleFeatured(request, response);
                    break;
                // Add to doGet method
                case "viewDetails":
                    showCabinDetails(request, response);
                    break;
                default:
                    listCabins(request, response);
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, "Error processing request: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        // Handle delete and toggle operations separately (no file upload)
        if ("delete".equals(action) || "toggleAvailability".equals(action) || "toggleFeatured".equals(action)) {
            handleSimpleRequest(request, response, action);
            return;
        }

        // Handle add/edit operations with file upload
        if (!ServletFileUpload.isMultipartContent(request)) {
            request.setAttribute("error", "Form must have enctype=multipart/form-data");
            listCabins(request, response);
            return;
        }

        handleMultipartRequest(request, response);
    }

    private void handleSimpleRequest(HttpServletRequest request, HttpServletResponse response, String action)
            throws ServletException, IOException {
        switch (action) {
            case "delete":
                deleteCabin(request, response);
                break;
            case "toggleAvailability":
                toggleAvailability(request, response);
                break;
            case "toggleFeatured":
                toggleFeatured(request, response);
                break;
            default:
                listCabins(request, response);
        }
    }

    private void handleMultipartRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        Cabin cabin = new Cabin();
        boolean isEdit = false;
        List<FileItem> additionalImages = new ArrayList<>();

        try {
            String uploadPath = getServletContext().getRealPath("/images");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            List<FileItem> items = upload.parseRequest(request);
            String fileName = "";

            for (FileItem item : items) {
                if (item.isFormField()) {
                    processFormField(item, cabin);
                    if ("id".equals(item.getFieldName()) && !item.getString().isEmpty()) {
                        isEdit = true;
                    }
                } else {
                    if ("additionalImages".equals(item.getFieldName())) {
                        // Store additional images for processing later
                        if (!item.getName().isEmpty()) {
                            additionalImages.add(item);
                        }
                    } else if (!item.getName().isEmpty()) {
                        // Process primary image
                        fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                                + "_" + new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;

                        if (isEdit) {
                            Cabin existingCabin = cabinService.getCabinById(cabin.getId());
                            if (existingCabin != null && existingCabin.getImageUrl() != null) {
                                File oldFile = new File(uploadPath + File.separator +
                                        existingCabin.getImageUrl().replace("images/", ""));
                                if (oldFile.exists()) oldFile.delete();
                            }
                        }

                        item.write(new File(filePath));
                        cabin.setImageUrl("images/" + fileName);
                    } else if (isEdit) {
                        Cabin existingCabin = cabinService.getCabinById(cabin.getId());
                        if (existingCabin != null) {
                            cabin.setImageUrl(existingCabin.getImageUrl());
                        }
                    }
                }
            }

            // Validate and save cabin first
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

            if (isEdit) {
                cabinService.updateCabin(cabin);
                request.setAttribute("message", "Cabin updated successfully!");
            } else {
                cabin.setCreatedAt(LocalDateTime.now());
                cabinService.addCabin(cabin);
                request.setAttribute("message", "Cabin added successfully!");
            }

            // Process additional images after cabin is saved
            if (!additionalImages.isEmpty()) {
                for (FileItem imageItem : additionalImages) {
                    String imageName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                            + "_" + new File(imageItem.getName()).getName();
                    String imagePath = uploadPath + File.separator + imageName;
                    imageItem.write(new File(imagePath));
                    cabinService.addCabinImage(cabin.getId(), "images/" + imageName);
                }
            }

        } catch (Exception e) {
            handleError(request, response, "Error processing request: " + e.getMessage());
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
            case "pricePerNight":
                cabin.setPricePerNight(Double.parseDouble(value));
                break;
            case "maxGuests":
                cabin.setMaxGuests(Integer.parseInt(value));
                break;
            case "bedrooms":
                cabin.setBedrooms(Integer.parseInt(value));
                break;
            case "bathrooms":
                cabin.setBathrooms(Integer.parseInt(value));
                break;
            case "amenities":
                cabin.setAmenities(value);
                break;
            case "isAvailable":
                cabin.setAvailable("true".equals(value));
                break;
            case "isFeatured":
                cabin.setFeatured("true".equals(value));
                break;
        }
    }

    private String validateCabin(Cabin cabin) {
        if (cabin.getName() == null || cabin.getName().trim().isEmpty()) {
            return "Name is required";
        }
        if (cabin.getDescription() == null || cabin.getDescription().trim().isEmpty()) {
            return "Description is required";
        }
        if (cabin.getLocation() == null || cabin.getLocation().trim().isEmpty()) {
            return "Location is required";
        }
        if (cabin.getPricePerNight() < 100) {
            return "Price must be at least 100";
        }
        if (cabin.getMaxGuests() < 1) {
            return "Max guests must be at least 1";
        }
        if (cabin.getBedrooms() < 1) {
            return "Bedrooms must be at least 1";
        }
        if (cabin.getBathrooms() < 1) {
            return "Bathrooms must be at least 1";
        }
        if (cabin.getAmenities() == null || cabin.getAmenities().trim().isEmpty()) {
            return "Amenities are required";
        }
        return null;
    }

    private void listCabins(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Cabin> cabins = cabinService.getAllCabins();
            request.setAttribute("cabinsList", cabins);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/cabin.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            handleError(request, response, "Error loading cabins: " + e.getMessage());
        }
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
            if (cabin != null) {
                request.setAttribute("cabin", cabin);
            } else {
                request.setAttribute("error", "Cabin not found");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cabin ID");
        } catch (Exception e) {
            request.setAttribute("error", "Error loading cabin: " + e.getMessage());
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/cabin.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteCabin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean deleted = cabinService.deleteCabin(id);

            if (deleted) {
                request.setAttribute("message", "Cabin deleted successfully!");
            } else {
                request.setAttribute("error", "Failed to delete cabin");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cabin ID");
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
                request.setAttribute("message", "Availability updated successfully!");
            } else {
                request.setAttribute("error", "Cabin not found");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cabin ID");
        } catch (Exception e) {
            request.setAttribute("error", "Error updating availability: " + e.getMessage());
        }
        listCabins(request, response);
    }

    private void toggleFeatured(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean featured = Boolean.parseBoolean(request.getParameter("featured"));
            Cabin cabin = cabinService.getCabinById(id);
            if (cabin != null) {
                cabin.setFeatured(featured);
                cabinService.updateCabin(cabin);
                request.setAttribute("message", "Featured status updated successfully!");
            } else {
                request.setAttribute("error", "Cabin not found");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cabin ID");
        } catch (Exception e) {
            request.setAttribute("error", "Error updating featured status: " + e.getMessage());
        }
        listCabins(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        listCabins(request, response);
    }
    // Add new method
    private void showCabinDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Cabin cabin = cabinService.getCabinById(id);
            if (cabin != null) {
                // Load additional images
                List<String> images = cabinService.getCabinImages(id);
                if (!images.isEmpty()) {
                    cabin.setImageUrls(images);
                }
                request.setAttribute("cabin", cabin);
                request.getRequestDispatcher("/admin/cabin-details.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Cabin not found");
                listCabins(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cabin ID");
            listCabins(request, response);
        } catch (Exception e) {
            handleError(request, response, "Error loading cabin details: " + e.getMessage());
        }
    }
}