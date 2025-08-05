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
        String sql = "INSERT INTO cabins (name, location, capacity, hourly_rate, description, amenities, image_url, is_available, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // Set current timestamp for creation
                cabin.setCreatedAt(LocalDateTime.now());

                ps.setString(1, cabin.getName());
                ps.setString(2, cabin.getLocation());
                ps.setInt(3, cabin.getCapacity());
                ps.setDouble(4, cabin.getHourlyRate());
                ps.setString(5, cabin.getDescription());
                ps.setString(6, cabin.getAmenities());
                ps.setString(7, cabin.getImageUrl());
                ps.setBoolean(8, cabin.isAvailable());
                ps.setTimestamp(9, Timestamp.valueOf(cabin.getCreatedAt()));

                int affectedRows = ps.executeUpdate();

                if (affectedRows == 0) {
                    logger.warning("No rows affected when adding cabin");
                    DBConnection.rollbackAndClose(conn);
                    return false;
                }

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cabin.setId(generatedKeys.getInt(1));
                        logger.info("Added new cabin with ID: " + cabin.getId());
                    } else {
                        logger.warning("Failed to get generated cabin ID");
                        DBConnection.rollbackAndClose(conn);
                        return false;
                    }
                }

                DBConnection.commitAndClose(conn);
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding cabin", e);
            DBConnection.rollbackAndClose(conn);
            return false;
        }
    }

    @Override
    public boolean updateCabin(Cabin cabin) {
        String sql = "UPDATE cabins SET name=?, location=?, capacity=?, hourly_rate=?, "
                + "description=?, amenities=?, image_url=?, is_available=?, "
                + "updated_at=NOW() WHERE id=?";


        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, cabin.getName());
                ps.setString(2, cabin.getLocation());
                ps.setInt(3, cabin.getCapacity());
                ps.setDouble(4, cabin.getHourlyRate());
                ps.setString(5, cabin.getDescription());
                ps.setString(6, cabin.getAmenities());
                ps.setString(7, cabin.getImageUrl());
                ps.setBoolean(8, cabin.isAvailable());
                ps.setInt(9, cabin.getId());


                int affectedRows = ps.executeUpdate();
                DBConnection.commitAndClose(conn);

                logger.info("Updated cabin ID " + cabin.getId() + ". Rows affected: " + affectedRows);
                return affectedRows == 1;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating cabin ID " + cabin.getId(), e);
            DBConnection.rollbackAndClose(conn);
            return false;
        }
    }

    @Override
    public boolean deleteCabin(int id) {
        String sql = "DELETE FROM cabins WHERE id=?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int affectedRows = ps.executeUpdate();
                DBConnection.commitAndClose(conn);

                logger.info("Deleted cabin ID " + id + ". Rows affected: " + affectedRows);
                return affectedRows == 1;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting cabin ID " + id, e);
            DBConnection.rollbackAndClose(conn);
            return false;
        }
    }

    @Override
    public Cabin getCabinById(int id) {
        String sql = "SELECT * FROM cabins WHERE id=?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Cabin cabin = mapCabin(rs);
                        logger.fine("Retrieved cabin by ID: " + id);
                        DBConnection.commitAndClose(conn);
                        return cabin;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting cabin by ID: " + id, e);
            DBConnection.rollbackAndClose(conn);
        } finally {
            DBConnection.closeQuietly(conn);
        }

        logger.warning("No cabin found with ID: " + id);
        return null;
    }

    @Override
    public List<Cabin> getAllCabins() {
        List<Cabin> cabins = new ArrayList<>();
        String sql = "SELECT * FROM cabins ORDER BY created_at DESC";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

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
                DBConnection.commitAndClose(conn);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all cabins", e);
            DBConnection.rollbackAndClose(conn);
        }

        return cabins;
    }

    @Override
    public boolean toggleAvailability(int id, boolean available) {
        String sql = "UPDATE cabins SET is_available=?, updated_at=NOW() WHERE id=?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBoolean(1, available);
                ps.setInt(2, id);

                int affectedRows = ps.executeUpdate();
                DBConnection.commitAndClose(conn);

                logger.info("Toggled availability for cabin ID " + id + " to " + available
                        + ". Rows affected: " + affectedRows);
                return affectedRows == 1;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error toggling availability for cabin ID " + id, e);
            DBConnection.rollbackAndClose(conn);
            return false;
        }
    }



    private Cabin mapCabin(ResultSet rs) throws SQLException {
        Cabin cabin = new Cabin();
        cabin.setId(rs.getInt("id"));
        cabin.setName(rs.getString("name"));
        cabin.setDescription(rs.getString("description"));
        cabin.setLocation(rs.getString("location"));
        cabin.setHourlyRate(rs.getDouble("hourly_rate"));
        cabin.setCapacity(rs.getInt("capacity"));
        cabin.setAmenities(rs.getString("amenities"));
        cabin.setImageUrl(rs.getString("image_url"));
        cabin.setAvailable(rs.getBoolean("is_available"));

        // Handle created_at safely
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            cabin.setCreatedAt(createdAt.toLocalDateTime());
        } else {
            logger.warning("Null created_at for cabin ID: " + cabin.getId());
            cabin.setCreatedAt(LocalDateTime.now());
        }

        // Handle updated_at safely
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            cabin.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return cabin;
    }




    @Override
    public List<Cabin> getAvailableCabins() {
        List<Cabin> cabins = new ArrayList<>();
        String sql = "SELECT * FROM cabins WHERE is_available = true ORDER BY created_at DESC";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Cabin cabin = mapCabin(rs);
                    cabins.add(cabin);
                }

                DBConnection.commitAndClose(conn);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting available cabins", e);
            DBConnection.rollbackAndClose(conn);
        }

        return cabins;
    }

}