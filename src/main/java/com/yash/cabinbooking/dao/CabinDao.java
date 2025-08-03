package com.yash.cabinbooking.dao;

import com.yash.cabinbooking.model.Cabin;
import java.util.List;

public interface CabinDao {
    boolean addCabin(Cabin cabin);
    boolean updateCabin(Cabin cabin);
    boolean deleteCabin(int id);
    Cabin getCabinById(int id);
    List<Cabin> getAllCabins();
    boolean toggleAvailability(int id, boolean available);
    boolean toggleFeatured(int id, boolean featured);
    List<String> getCabinImages(int cabinId);
    boolean addCabinImage(int cabinId, String imageUrl);
    boolean deleteCabinImage(int imageId);
}