package com.warriors.statistiques;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class StatistiquesApplication {

    public static void main(String[] args) {
        creerBaseSiInexistante();
        SpringApplication.run(StatistiquesApplication.class, args);
    }

    /**
     * Cree automatiquement la base de donnees PostgreSQL "statistiques_db"
     * si elle n'existe pas encore, sans avoir besoin d'ouvrir pgAdmin.
     */
    private static void creerBaseSiInexistante() {
        String dbName = "statistiques_db";
        String adminUrl = "jdbc:postgresql://localhost:5433/postgres";
        String user = "postgres";
        String password = "postgres";

        try (Connection conn = DriverManager.getConnection(adminUrl, user, password);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'");
            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("========================================================");
                System.out.println("Base de donnees '" + dbName + "' creee automatiquement !");
                System.out.println("========================================================");
            }
        } catch (Exception e) {
            System.out.println("Info base de donnees : " + e.getMessage());
        }
    }
}
