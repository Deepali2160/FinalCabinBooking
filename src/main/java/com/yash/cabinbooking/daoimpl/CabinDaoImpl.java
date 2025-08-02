package com.yash.cabinbooking.daoimpl;

import com.yash.cabinbooking.dao.CabinDao;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CabinDaoImpl implements CabinDao {

    private static final Logger logger = Logger.getLogger(CabinDaoImpl.class.getName());

    @Override
    public boolean addCabin(Cabin cabin) {
        String sql = "INSERT INTO cabins (name, description, location, price_per_night, max_guests, "
                + "bedrooms, bathrooms, amenities, image_url, is_available, is_featured, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set current timestamp for creation
            cabin.setCreatedAt(LocalDateTime.now());

            ps.setString(1, cabin.getName());
            ps.setString(2, cabin.getDescription());
            ps.setString(3, cabin.getLocation());
            ps.setDouble(4, cabin.getPricePerNight());
            ps.setInt(5, cabin.getMaxGuests());
            ps.setInt(6, cabin.getBedrooms());
            ps.setInt(7, cabin.getBathrooms());
            ps.setString(8, cabin.getAmenities());
            ps.setString(9, cabin.getImageUrl());
            ps.setBoolean(10, cabin.isAvailable());
            ps.setBoolean(11, cabin.isFeatured());
            ps.setTimestamp(12, Timestamp.valueOf(cabin.getCreatedAt()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                logger.warning("No rows affected when adding cabin");
                conn.rollback();
                return false;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cabin.setId(generatedKeys.getInt(1));
                    logger.info("Added new cabin with ID: " + cabin.getId());
                } else {
                    logger.warning("Failed to get generated cabin ID");
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding cabin", e);
            return false;
        }
    }

    @Override
    public boolean updateCabin(Cabin cabin) {
        String sql = "UPDATE cabins SET name=?, description=?, location=?, price_per_night=?, "
                + "max_guests=?, bedrooms=?, bathrooms=?, amenities=?, image_url=?, "
                + "is_available=?, is_featured=?, updated_at=NOW() WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cabin.getName());
            ps.setString(2, cabin.getDescription());
            ps.setString(3, cabin.getLocation());
            ps.setDouble(4, cabin.getPricePerNight());
            ps.setInt(5, cabin.getMaxGuests());
            ps.setInt(6, cabin.getBedrooms());
            ps.setInt(7, cabin.getBathrooms());
            ps.setString(8, cabin.getAmenities());
            ps.setString(9, cabin.getImageUrl());
            ps.setBoolean(10, cabin.isAvailable());
            ps.setBoolean(11, cabin.isFeatured());
            ps.setInt(12, cabin.getId());

            int affectedRows = ps.executeUpdate();
            conn.commit();

            logger.info("Updated cabin ID " + cabin.getId() + ". Rows affected: " + affectedRows);
            return affectedRows == 1;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating cabin ID " + cabin.getId(), e);
            return false;
        }
    }

    @Override
    public boolean deleteCabin(int id) {
        String sql = "DELETE FROM cabins WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            conn.commit();

            logger.info("Deleted cabin ID " + id + ". Rows affected: " + affectedRows);
            return affectedRows == 1;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting cabin ID " + id, e);
            return false;
        }
    }

    @Override
    public Cabin getCabinById(int id) {
        String sql = "SELECT * FROM cabins WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cabin cabin = mapCabin(rs);
                    logger.fine("Retrieved cabin by ID: " + id);
                    return cabin;
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting cabin by ID: " + id, e);
        }

        logger.warning("No cabin found with ID: " + id);
        return null;
    }

    @Override
    public List<Cabin> getAllCabins() {
        List<Cabin> cabins = new ArrayList<>();
        String sql = "SELECT * FROM cabins ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            logger.info("Executing query: " + sql);

            while (rs.next()) {
                try {
                    Cabin cabin = mapCabin(rs);
                    cabins.add(cabin);
                    logger.fine("Mapped cabin: " + cabin.getName()
                            + " | Created: " + cabin.getCreatedAt());
                } catch (SQLException e) {
                    logger.log(Level.WARNING, "Error mapping cabin", e);
                }
            }

            logger.info("Retrieved " + cabins.size() + " cabins from database");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all cabins", e);
        }

        return cabins;
    }

    @Override
    public boolean toggleAvailability(int id, boolean available) {
        String sql = "UPDATE cabins SET is_available=?, updated_at=NOW() WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, id);

            int affectedRows = ps.executeUpdate();
            conn.commit();

            logger.info("Toggled availability for cabin ID " + id + " to " + available
                    + ". Rows affected: " + affectedRows);
            return affectedRows == 1;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error toggling availability for cabin ID " + id, e);
            return false;
        }
    }

    @Override
    public boolean toggleFeatured(int id, boolean featured) {
        String sql = "UPDATE cabins SET is_featured=?, updated_at=NOW() WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, featured);
            ps.setInt(2, id);

            int affectedRows = ps.executeUpdate();
            conn.commit();

            logger.info("Toggled featured status for cabin ID " + id + " to " + featured
                    + ". Rows affected: " + affectedRows);
            return affectedRows == 1;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error toggling featured status for cabin ID " + id, e);
            return false;
        }
    }

    private Cabin mapCabin(ResultSet rs) throws SQLException {
        Cabin cabin = new Cabin();
        cabin.setId(rs.getInt("id"));
        cabin.setName(rs.getString("name"));
        cabin.setDescription(rs.getString("description"));
        cabin.setLocation(rs.getString("location"));
        cabin.setPricePerNight(rs.getDouble("price_per_night"));
        cabin.setMaxGuests(rs.getInt("max_guests"));
        cabin.setBedrooms(rs.getInt("bedrooms"));
        cabin.setBathrooms(rs.getInt("bathrooms"));
        cabin.setAmenities(rs.getString("amenities"));
        cabin.setImageUrl(rs.getString("image_url"));
        cabin.setAvailable(rs.getBoolean("is_available"));
        cabin.setFeatured(rs.getBoolean("is_featured"));

        // Handle null timestamps safely
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            cabin.setCreatedAt(createdAt.toLocalDateTime());
        } else {
            logger.warning("Null created_at for cabin ID: " + cabin.getId());
            cabin.setCreatedAt(LocalDateTime.now());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            cabin.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return cabin;
    }
}