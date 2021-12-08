package com.agungdh.bpkad.sipdtosimdamigrator.services;

import com.agungdh.bpkad.sipdtosimdamigrator.jdbc.MariadbJdbc;
import com.agungdh.bpkad.sipdtosimdamigrator.models.SipdOption;
import com.agungdh.bpkad.sipdtosimdamigrator.models.SipdUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SipdService {
    @Autowired
    private Environment env;

    public ArrayList<SipdUnit> getSipdOptionsBasedOnUnit() {
        log.debug("Get Option Data");

        ArrayList<SipdUnit> sipdUnits = new ArrayList<SipdUnit>();

        String QUERY = "SELECT wo.option_value, du.nama_skpd, du.is_skpd\n" +
                "from wp_options as wo\n" +
                "join data_unit as du\n" +
                "on substring(wo.option_name, 11) = du.id_skpd\n" +
                "where option_name like '_crb_unit_%'\n" +
                "GROUP BY du.id_unit;";

        try(
                Connection conn = DriverManager.getConnection(
                        env.getProperty("agungdh.jdbc.mariadb.url"),
                        env.getProperty("agungdh.jdbc.mariadb.user"),
                        env.getProperty("agungdh.jdbc.mariadb.password")
                );
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY);
        ) {

            while (rs.next()) {
                sipdUnits.add(new SipdUnit(rs.getString("nama_skpd"), rs.getString("option_value"), rs.getInt("is_skpd")));
            }

        } catch (SQLException e) {
            log.error("SQLException Error: " + e.getMessage());

            e.printStackTrace();
        }

        return sipdUnits;
    }

    public ArrayList<SipdUnit> getSipdOptions() {
        log.debug("Get Option Data");

        ArrayList<SipdUnit> sipdUnits = new ArrayList<SipdUnit>();

        String QUERY = "SELECT wo.*, substring(wo.option_name, 11) as id_skpd_option, du.*\n" +
                "from wp_options as wo\n" +
                "join data_unit as du\n" +
                "on substring(wo.option_name, 11) = du.id_skpd\n" +
                "where option_name like '_crb_unit_%'";

        try(
                Connection conn = DriverManager.getConnection(
                    env.getProperty("agungdh.jdbc.mariadb.url"),
                    env.getProperty("agungdh.jdbc.mariadb.user"),
                    env.getProperty("agungdh.jdbc.mariadb.password")
                );
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY);
            ) {

            while (rs.next()) {
                sipdUnits.add(new SipdUnit(rs.getString("nama_skpd"), rs.getString("option_value"), rs.getInt("is_skpd")));
            }

        } catch (SQLException e) {
            log.error("SQLException Error: " + e.getMessage());

            e.printStackTrace();
        }

        return sipdUnits;
    }
}