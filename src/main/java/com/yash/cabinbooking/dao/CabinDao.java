package com.yash.cabinbooking.dao;

import com.yash.cabinbooking.model.Cabin;
import java.util.List;

public interface CabinDao {
    List<Cabin> getAllCabins();
    Cabin getCabinById(int id);
    boolean addCabin(Cabin cabin);
    boolean updateCabin(Cabin cabin);
    boolean deleteCabin(int id);
    boolean toggleAvailability(int id, boolean newStatus);
    boolean toggleFeatured(int id, boolean newStatus);
}
