package ru.ibs;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ibs.db.ConfJDBC;
import ru.ibs.db.ConfParamsForQueries;
import ru.ibs.db.ConfQueries;

import java.sql.*;

/**
 * Практическое задание №4_JDBC
 */
public class ProductWithSQLTest {

    @Test
    public void addNewProductTest(){
        try (Connection conn =
                     DriverManager.getConnection(ConfJDBC.DB_URL, ConfJDBC.DB_USER, ConfJDBC.DB_PASSWORD)) {
            try (PreparedStatement pstmt = conn.prepareStatement(ConfQueries.INSERT)) {
                pstmt.setInt(1, ConfParamsForQueries.PRODUCT_ID);
                pstmt.setString(2, ConfParamsForQueries.PRODUCT_NAME);
                pstmt.setString(3, ConfParamsForQueries.PRODUCT_TYPE);
                pstmt.setInt(4, ConfParamsForQueries.IS_EXOTIC);
                Assertions.assertEquals(1, pstmt.executeUpdate(), "The number of affected rows should be 1.");
            } catch (SQLException e) {
                Assertions.fail("The insertion into the table did not occur: " + e.getMessage());
            }

            try (PreparedStatement pstmt = conn.prepareStatement(ConfQueries.SELECT)) {
               pstmt.setInt(1, ConfParamsForQueries.PRODUCT_ID);
                ResultSet result = pstmt.executeQuery();
                if (result.next()) {
                    Assertions.assertEquals(ConfParamsForQueries.PRODUCT_ID, result.getInt("food_id"),
                            "The product ID does not match the expected one.");
                    Assertions.assertEquals(ConfParamsForQueries.PRODUCT_NAME, result.getString("food_name"),
                            "The product name does not match the expected one.");
                    Assertions.assertEquals(ConfParamsForQueries.PRODUCT_TYPE, result.getString("food_type"),
                            "The product type does not match what is expected.");
                    Assertions.assertEquals(ConfParamsForQueries.IS_EXOTIC, result.getInt("food_exotic"),
                            "The value of the exotic checkbox does not match the expected value.");
                }
            } catch (SQLException e) {
                Assertions.fail("The SELECT operation failed: " + e.getMessage());
            }
        } catch (SQLException e) {
            Assertions.fail("The connection to the database did not occur: " + e.getMessage());
        }
    }

    @AfterAll
    public static void deleteRow(){
        try (Connection conn =
                     DriverManager.getConnection(ConfJDBC.DB_URL, ConfJDBC.DB_USER, ConfJDBC.DB_PASSWORD)) {
            try (PreparedStatement pstmt = conn.prepareStatement(ConfQueries.DELETE)) {
                pstmt.setInt(1, ConfParamsForQueries.PRODUCT_ID);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Assertions.fail("The deletion request could not be executed: " + e.getMessage());
            }
            try (PreparedStatement pstmt = conn.prepareStatement(ConfQueries.SELECT)) {
                pstmt.setInt(1, ConfParamsForQueries.PRODUCT_ID);
                ResultSet result = pstmt.executeQuery();
                if (result.next()) {
                    Assertions.fail("The deletion of the test value did not occur.");
                }
            }
        } catch (SQLException e) {
            Assertions.fail("The connection to the database did not occur: " + e.getMessage());
        }
    }
}
