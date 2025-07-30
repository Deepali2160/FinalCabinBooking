package com.yash.cabinbooking.service.impl;

import com.yash.cabinbooking.dao.CabinDao;
import com.yash.cabinbooking.dao.impl.CabinDaoImpl;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.CabinService;

import java.util.List;

public class CabinServiceImpl implements CabinService {

    private CabinDao cabinDao;

    public CabinServiceImpl() {
        cabinDao = new CabinDaoImpl();
    }

    @Override
    public List<Cabin> getAllCabins() {
        return cabinDao.getAllCabins();
    }

    @Override
    public Cabin getCabinById(int id) {
        return cabinDao.getCabinById(id);
    }

    @Override
    public boolean addCabin(Cabin cabin) {
        return cabinDao.addCabin(cabin);
    }

    @Override
    public boolean updateCabin(Cabin cabin) {
        return cabinDao.updateCabin(cabin);
    }

    @Override
    public boolean deleteCabin(int id) {
        return cabinDao.deleteCabin(id);
    }

    @Override
    public boolean toggleAvailability(int id, boolean newStatus) {
        return cabinDao.toggleAvailability(id, newStatus);
    }

    @Override
    public boolean toggleFeatured(int id, boolean newStatus) {
        return cabinDao.toggleFeatured(id, newStatus);
    }
}
