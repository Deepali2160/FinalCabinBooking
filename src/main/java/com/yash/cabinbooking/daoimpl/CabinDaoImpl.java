package com.yash.cabinbooking.dao.impl;

import com.yash.cabinbooking.dao.CabinDao;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CabinDaoImpl implements CabinDao {

    private Connection conn;

    public CabinDaoImpl() {
        conn = DBConnection.getConnection();
    }

    @Override
    public List<Cabin> getAllCabins() {
        List<Cabin> cabins = new ArrayList<>();
        String sql = "SELECT * FROM cabins";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cabins.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cabins;
    }

    @Override
    public Cabin getCabinById(int id) {
        String sql = "SELECT * FROM cabins WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addCabin(Cabin c) {
        String sql = "INSERT INTO cabins(name, description, location, price_per_night, max_guests, bedrooms, bathrooms, amenities, image_url, is_available, is_featured, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setCabinParams(ps, c);
            ps.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCabin(Cabin c) {
        String sql = "UPDATE cabins SET name=?, description=?, location=?, price_per_night=?, max_guests=?, bedrooms=?, bathrooms=?, amenities=?, image_url=?, is_available=?, is_featured=?, updated_at=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setCabinParams(ps, c);
            ps.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(13, c.getId());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCabin(int id) {
        String sql = "DELETE FROM cabins WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean toggleAvailability(int id, boolean newStatus) {
        return toggleBooleanField(id, newStatus, "isAvailable");
    }

    @Override
    public boolean toggleFeatured(int id, boolean newStatus) {
        return toggleBooleanField(id, newStatus, "isFeatured");
    }

    // --- Helpers ---

    private boolean toggleBooleanField(int id, boolean newValue, String columnName) {
        String sql = "UPDATE cabins SET " + columnName + "=?, updatedAt=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, newValue);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setCabinParams(PreparedStatement ps, Cabin c) throws SQLException {
        ps.setString(1, c.getName());
        ps.setString(2, c.getDescription());
        ps.setString(3, c.getLocation());
        ps.setDouble(4, c.getPricePerNight());
        ps.setInt(5, c.getMaxGuests());
        ps.setInt(6, c.getBedrooms());
        ps.setInt(7, c.getBathrooms());
        ps.setString(8, c.getAmenities());
        ps.setString(9, c.getImageUrl());
        ps.setBoolean(10, c.isAvailable());
        ps.setBoolean(11, c.isFeatured());
    }

    private Cabin mapRow(ResultSet rs) throws SQLException {
        Cabin c = new Cabin();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setLocation(rs.getString("location"));
        c.setPricePerNight(rs.getDouble("price_per_night"));
        c.setMaxGuests(rs.getInt("max_guests"));
        c.setBedrooms(rs.getInt("bedrooms"));
        c.setBathrooms(rs.getInt("bathrooms"));
        c.setAmenities(rs.getString("amenities"));
        c.setImageUrl(rs.getString("image_url"));
        c.setAvailable(rs.getBoolean("is_available"));
        c.setFeatured(rs.getBoolean("is_featured"));
        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        c.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return c;
    }
}
