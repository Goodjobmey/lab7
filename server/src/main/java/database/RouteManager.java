package database;

import data.Coordinates;
import data.Location;
import data.Route;

import java.sql.SQLException;
import java.util.ArrayList;

public class RouteManager {

    public Route getRoute(int id) {
        var select_route_sql = "SELECT * FROM routes WHERE route_id = ?";
        try (var conn = Database.connect()) {
            assert conn != null;
            try (var pstmt = conn.prepareStatement(select_route_sql)) {
                pstmt.setInt(1, id);
                var rs = pstmt.executeQuery();
                if (rs.next()) {
                    var name = rs.getString(2);
                    var xC = rs.getLong(3);
                    var yC = rs.getDouble(4);
                    var creation_date = rs.getTimestamp(5);
                    var xLF = rs.getDouble(6);
                    var yLF = rs.getInt(7);
                    var zLF = rs.getInt(8);
                    var xLT = rs.getDouble(9);
                    var yLT = rs.getInt(10);
                    var zLT = rs.getInt(11);
                    var distance = rs.getDouble(12);
                    var owner_user_id = rs.getInt(13);
                    return new Route(id, name, new Coordinates(xC, yC), creation_date.toLocalDateTime(), new Location(xLF, yLF, zLF), new Location(xLT, yLT, zLT), (float) distance, owner_user_id);
                }
                throw new RuntimeException("Cannot find route");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Integer addRoute(Route route) {
        var insert_route_sql = "INSERT INTO routes(name, x_coordinate, y_coordinate, x_location_from, y_location_from, z_location_from," +
                "x_location_to, y_location_to, z_location_to, distance, owner_user_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING route_id";
        try (var conn = Database.connect()) {
            assert conn != null;
            try (var pstmt = conn.prepareStatement(insert_route_sql)) {
                pstmt.setString(1, route.getName());
                pstmt.setLong(2, route.getCoordinates().getX());
                pstmt.setDouble(3, route.getCoordinates().getY());
                pstmt.setDouble(4, route.getFrom().getX());
                pstmt.setInt(5, route.getFrom().getY());
                pstmt.setInt(6, route.getFrom().getZ());
                pstmt.setDouble(7, route.getTo().getX());
                pstmt.setInt(8, route.getTo().getY());
                pstmt.setInt(9, route.getTo().getZ());
                pstmt.setDouble(10, route.getDistance());
                pstmt.setInt(11, route.getOwnerUserId());
                var result = pstmt.executeQuery();
                if (!result.next()) {
                    return null;
                }
                var t = result.getInt(1);
                System.out.println(t);
                return t;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRoute(int id, Route route) {
        var update_route_sql = "UPDATE routes SET name = ?, x_coordinate = ?, y_coordinate = ?, x_location_from = ?, y_location_from = ?, z_location_from = ?" +
                "x_location_to = ?, y_location_to = ?, z_location_to = ?, distance = ?,  owner_user_id = ? WHERE route_id = ?";
        try (var conn = Database.connect();
             var pstmt = conn.prepareStatement(update_route_sql)) {
                pstmt.setString(1, route.getName());
                pstmt.setLong(2, route.getCoordinates().getX());
                pstmt.setDouble(3, route.getCoordinates().getY());
                pstmt.setDouble(4, route.getFrom().getX());
                pstmt.setInt(5, route.getFrom().getY());
                pstmt.setInt(6, route.getFrom().getZ());
                pstmt.setDouble(7, route.getTo().getX());
                pstmt.setInt(8, route.getTo().getY());
                pstmt.setInt(9, route.getTo().getZ());
                pstmt.setDouble(10, route.getDistance());
                pstmt.setInt(11, route.getOwnerUserId());
                pstmt.setInt(12, id);
                pstmt.executeUpdate();
            }
         catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteRoute(int id) {
        var delete_route_sql = "DELETE FROM routes WHERE route_id = ?";
        try (var conn = Database.connect()) {
            assert conn != null;
            try (var pstmt = conn.prepareStatement(delete_route_sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Route> getAllRoutes() {
        var result = new ArrayList<Route>();
        try (var conn = Database.connect(); var stmt = conn.createStatement()) {
                var rs = stmt.executeQuery("SELECT route_id, name, x_coordinate, y_coordinate, creation_date, x_location_from, y_location_from, z_location_from," +
                        " x_location_to, y_location_to, z_location_to, distance, owner_user_id FROM routes");
                while (rs.next()) {

                    var id = rs.getInt(1);
                    var name = rs.getString(2);
                    var xC = rs.getLong(3);
                    var yC = rs.getDouble(4);
                    var creation_date = rs.getTimestamp(5);
                    var xLF = rs.getDouble(6);
                    var yLF = rs.getInt(7);
                    var zLF = rs.getInt(8);
                    var xLT = rs.getDouble(9);
                    var yLT = rs.getInt(10);
                    var zLT = rs.getInt(11);
                    var distance = rs.getDouble(12);
                    var owner_user_id = rs.getInt(13);

                    result.add(new Route(id, name, new Coordinates(xC, yC), creation_date.toLocalDateTime(), new Location(xLF, yLF, zLF), new Location(xLT, yLT, zLT), (float) distance, owner_user_id));
                }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }



    public void deleteAllUserRoutes(int ownerUserId) {
        var query = "DELETE FROM routes WHERE owner_user_id = ?";
        try (var conn = Database.connect(); var pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ownerUserId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}