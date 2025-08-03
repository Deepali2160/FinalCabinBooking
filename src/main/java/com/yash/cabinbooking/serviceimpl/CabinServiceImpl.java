package com.yash.cabinbooking.serviceimpl;

import com.yash.cabinbooking.dao.CabinDao;
import com.yash.cabinbooking.daoimpl.CabinDaoImpl;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.CabinService;

import java.util.List;

public class CabinServiceImpl implements CabinService {
    private CabinDao cabinDAO = new CabinDaoImpl();

    @Override
    public boolean addCabin(Cabin cabin) {
        return cabinDAO.addCabin(cabin);
    }

    @Override
    public boolean updateCabin(Cabin cabin) {
        return cabinDAO.updateCabin(cabin);
    }

    @Override
    public boolean deleteCabin(int id) {
        return cabinDAO.deleteCabin(id);
    }

    @Override
    public Cabin getCabinById(int id) {
        return cabinDAO.getCabinById(id);
    }

    @Override
    public List<Cabin> getAllCabins() {
        return cabinDAO.getAllCabins();
    }

    @Override
    public boolean toggleAvailability(int id, boolean available) {
        return cabinDAO.toggleAvailability(id, available);
    }

    @Override
    public boolean toggleFeatured(int id, boolean featured) {
        return cabinDAO.toggleFeatured(id, featured);
    }
    @Override
    public boolean addCabinImage(int cabinId, String imageUrl) {
        return cabinDAO.addCabinImage(cabinId, imageUrl);
    }

    @Override
    public List<String> getCabinImages(int cabinId) {
        return cabinDAO.getCabinImages(cabinId);
    }

    @Override
    public boolean deleteCabinImage(int imageId) {
        return cabinDAO.deleteCabinImage(imageId);
    }

    @Override
    public Cabin getCabinWithImages(int id) {
        Cabin cabin = cabinDAO.getCabinById(id);
        if (cabin != null) {
            List<String> images = cabinDAO.getCabinImages(id);
            cabin.setImageUrls(images);
        }
        return cabin;
    }
}