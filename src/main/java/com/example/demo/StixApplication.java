package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.Properties;

@SpringBootApplication
public class StixApplication {

    public static void main(String[] args) {
        String account = "ist-gbi-tst"; // e.g., myorg-myaccount
        String warehouse = "GBI_SALES_BAP_VWH";
        String database = "GBI_SALES_DATA_ENG_DB";
        String schema = "sales_data_eng";
        String user = "GBI_SALES_STIX_BIZ_USER";

        String privateKeyFilePath = "/Users/hemantkumar.ghaydar/Documents/work/trino_snowflake/privateKey.p8"; // Path to your .p8 private key file
        String privateKeyFilePassword = "BHE2L3@px!1P"; // Leave empty string if no password

        String jdbcUrl = "jdbc:snowflake://" + account + ".snowflakecomputing.com:443/";

        Properties props = new Properties();
        props.put("user", user);
        props.put("warehouse", warehouse);
        props.put("db", database);
        props.put("schema", schema);
        props.put("private_key_file", privateKeyFilePath);

        // Only include private_key_file_pwd if your private key is encrypted
        if (!privateKeyFilePassword.isEmpty()) {
            props.put("private_key_file_pwd", privateKeyFilePassword);
        }

        Connection connection = null;
        try {
            // Load the Snowflake JDBC driver
            Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;


            // Establish the connection
            connection = DriverManager.getConnection(jdbcUrl, props);

            if (connection != null) {
                System.out.println("Successfully connected to Snowflake!");
                preparedStatement = connection.prepareStatement("select * From rdc_recon_stix_grid_transmission_dataset limit 10");

                // Execute the query
                resultSet = preparedStatement.executeQuery();

                // Process the ResultSet
                while (resultSet.next()) {
                    System.out.println(resultSet.getObject(1));
                    System.out.println(resultSet.getObject(2));
                    System.out.println(resultSet.getObject(3));
                }
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Snowflake JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

}
