package xyz.hellothomas.jedi.client.util;

import lombok.ToString;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DBUtil {

    private DBUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static DBType getDBType(DataSource dataSource) {
        String productName;
        try {
            productName = getDatabaseProductName(dataSource);
        } catch (SQLException e) {
            throw new RuntimeException("get productName error", e);
        }

        for (DBType dbType : DBType.values()) {
            if (productName.contains(dbType.getProductName())) {
                return dbType;
            }
        }
        // no match, return null
        throw new RuntimeException("productName: " + productName + ", not supported.");
    }

    public static String getDatabaseProductName(DataSource dataSource) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();
            return metaData.getDatabaseProductName();
        }
    }

    @ToString
    public enum DBType {
        MYSQL("MySQL"),
        ORACLE("Oracle"),
        ;

        private String productName;

        DBType(String productName) {
            this.productName = productName;
        }

        public String getProductName() {
            return productName;
        }
    }
}
