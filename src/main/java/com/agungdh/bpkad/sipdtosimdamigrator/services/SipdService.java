package com.agungdh.bpkad.sipdtosimdamigrator.services;

import com.agungdh.bpkad.sipdtosimdamigrator.jdbc.MariadbJdbc;
import com.agungdh.bpkad.sipdtosimdamigrator.models.SipdOption;
import com.agungdh.bpkad.sipdtosimdamigrator.models.SipdSubUnit;
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

    public void updateSubUnitValue()
    {
        ArrayList<SipdUnit> units = this.getSipdOptionsBasedOnUnit();

        ArrayList<SipdSubUnit> sipdSubUnits;

        for (int i = 0; i < units.size(); i++) {
            sipdSubUnits = new ArrayList<SipdSubUnit>();

            String QUERY = "SELECT du.id as id_data_unit, wo.option_value as kode_skpd, du.nama_skpd, wo_unit.option_value as kode_unit, du_unit.nama_skpd as nama_unit\n" +
                    "from wp_options as wo\n" +
                    "join data_unit as du\n" +
                    "on substring(wo.option_name, 11) = du.id_skpd\n" +
                    "join data_unit as du_unit\n" +
                    "on du.id_unit = du_unit.id_unit\n" +
                    "and du_unit.is_skpd = 1\n" +
                    "join wp_options as wo_unit\n" +
                    "on substring(wo_unit.option_name, 11) = du_unit.id_skpd\n" +
                    "where wo.option_name like '_crb_unit_%'\n" +
                    "and du.is_skpd = 0\n" +
                    "and du_unit.id = " + units.get(i).getId() + "\n" +
                    "order by kode_unit asc, kode_skpd asc, id_data_unit asc;";

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
                    sipdSubUnits.add(
                            new SipdSubUnit(
                                rs.getInt("id_data_unit"),
                                rs.getString("kode_skpd"),
                                rs.getString("nama_skpd"),
                                rs.getString("kode_unit"),
                                rs.getString("nama_unit")
                            )
                    );

                    log.info(String.valueOf(sipdSubUnits.size() - 1));
                    log.info(sipdSubUnits.get(sipdSubUnits.size() - 1).toString());
                }
            } catch (SQLException e) {
                log.error("SQLException Error: " + e.getMessage());

                e.printStackTrace();
            }
        }
    }

    public ArrayList<SipdUnit> getSipdOptionsBasedOnUnit() {
        log.debug("Get Option Data");

        ArrayList<SipdUnit> sipdUnits = new ArrayList<SipdUnit>();

        String QUERY = "SELECT wo.option_value, du.nama_skpd, du.is_skpd, du.id\n" +
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
                sipdUnits.add(new SipdUnit(rs.getInt("id"), rs.getString("nama_skpd"), rs.getString("option_value"), rs.getInt("is_skpd")));
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
                sipdUnits.add(new SipdUnit(rs.getInt("id"), rs.getString("nama_skpd"), rs.getString("option_value"), rs.getInt("is_skpd")));
            }

        } catch (SQLException e) {
            log.error("SQLException Error: " + e.getMessage());

            e.printStackTrace();
        }

        return sipdUnits;
    }
}