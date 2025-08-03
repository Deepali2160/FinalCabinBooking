package com.yash.cabinbooking.service;

import com.yash.cabinbooking.model.Cabin;
import java.util.List;

public interface CabinService {
    boolean addCabin(Cabin cabin);
    boolean updateCabin(Cabin cabin);
    boolean deleteCabin(int id);
    Cabin getCabinById(int id);
    List<Cabin> getAllCabins();
    boolean toggleAvailability(int id, boolean available);
    boolean toggleFeatured(int id, boolean featured);
    boolean addCabinImage(int cabinId, String imageUrl);
    List<String> getCabinImages(int cabinId);
    boolean deleteCabinImage(int imageId);

    // Optional: Add method to get cabin with all images
    Cabin getCabinWithImages(int id);
}