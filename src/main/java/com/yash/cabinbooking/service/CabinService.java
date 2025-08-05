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
    List<Cabin> getAvailableCabins();  // for users
}
