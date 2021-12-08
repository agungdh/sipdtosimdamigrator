package com.agungdh.bpkad.sipdtosimdamigrator.services;

import com.agungdh.bpkad.sipdtosimdamigrator.jdbc.MariadbJdbc;
import com.agungdh.bpkad.sipdtosimdamigrator.models.SipdOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class SipdService {
    @Autowired
    private Environment env;

    public void getSipdOptionsBasedOnUnit() {
        System.out.println("Get Unit Data");

        String QUERY = "SELECT wo.option_value, du.nama_skpd, du.is_skpd\n" +
                "from wp_options as wo\n" +
                "join data_unit as du\n" +
                "on substring(wo.option_name, 11) = du.id_skpd\n" +
                "where option_name like '_crb_unit_%'\n" +
                "GROUP BY du.id_unit;";

        try(Connection conn = DriverManager.getConnection(
                env.getProperty("agungdh.jdbc.mariadb.url"),
                env.getProperty("agungdh.jdbc.mariadb.user"),
                env.getProperty("agungdh.jdbc.mariadb.password")
        );
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);) {
            while (rs.next()) {
                System.out.println("Is SKPD: " + rs.getString("is_skpd"));
                System.out.println("SKPD: " + rs.getString("nama_skpd"));
                System.out.println("Option Value: " + rs.getString("option_value"));
                System.out.println("====================================================");
            }
        } catch (SQLException e) {
            System.out.println("SQLException Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getSipdOptions() {
        System.out.println("Get Option Data");

        String QUERY = "SELECT wo.*, substring(wo.option_name, 11) as id_skpd_option, du.*\n" +
                "from wp_options as wo\n" +
                "join data_unit as du\n" +
                "on substring(wo.option_name, 11) = du.id_skpd\n" +
                "where option_name like '_crb_unit_%'";

        try(Connection conn = DriverManager.getConnection(
            env.getProperty("agungdh.jdbc.mariadb.url"),
            env.getProperty("agungdh.jdbc.mariadb.user"),
            env.getProperty("agungdh.jdbc.mariadb.password")
        );
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);) {
            while (rs.next()) {
                System.out.println("ID SKPD: " + rs.getLong("id_skpd"));
                System.out.println("Option Name: " + rs.getString("option_name"));
                System.out.println("Option Value: " + rs.getString("option_value"));
                System.out.println("Nama SKPD: " + rs.getString("nama_skpd"));
                System.out.println("====================================================");
            }
        } catch (SQLException e) {
            System.out.println("SQLException Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
