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
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SipdService {
    @Autowired
    private Environment env;

    public void injectSkpd()
    {
        String QUERY = "SELECT du.id as id_data_unit, wo.option_value as kode_skpd, du.nama_skpd, wo_unit.option_value as kode_unit, du_unit.nama_skpd as nama_unit, wo.option_id as id_option, du.id_skpd, du.namakepala, du.nipkepala\n" +
                "from wp_options as wo\n" +
                "join data_unit as du\n" +
                "on substring(wo.option_name, 11) = du.id_skpd\n" +
                "join data_unit as du_unit\n" +
                "on du.id_unit = du_unit.id_unit\n" +
                "and du_unit.is_skpd = 1\n" +
                "join wp_options as wo_unit\n" +
                "on substring(wo_unit.option_name, 11) = du_unit.id_skpd\n" +
                "where wo.option_name like '_crb_unit_%'\n" +
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
               log.info(rs.getString("namakepala"));

                String[] explodedKodeUnit =  rs.getString("kode_skpd").split("\\.");

                QUERY = "insert into ref_sub_unit values (" + explodedKodeUnit[0] + "," + explodedKodeUnit[1] + "," + explodedKodeUnit[2] + ", " + explodedKodeUnit[3] + ", '" + rs.getString("nama_skpd") + "')";
                try(
                        Connection conn2 = DriverManager.getConnection(
                                env.getProperty("agungdh.jdbc.sqlserver.url"),
                                env.getProperty("agungdh.jdbc.sqlserver.user"),
                                env.getProperty("agungdh.jdbc.sqlserver.password")
                        );
                        Statement stmt2 = conn2.createStatement();
                        ResultSet rs2 = stmt2.executeQuery(QUERY);
                ) {

                } catch (SQLException e) {
                    log.error("SQLException Error: " + e.getMessage());

                    e.printStackTrace();
                }

                QUERY = "insert into ta_sub_unit (tahun, kd_urusan, kd_bidang, kd_unit, kd_sub, nm_pimpinan, nip_pimpinan) values (2022," + explodedKodeUnit[0] + "," + explodedKodeUnit[1] + "," + explodedKodeUnit[2] + ", " + explodedKodeUnit[3] + ", '" + rs.getString("namakepala") + "', '" + rs.getString("nipkepala") + "')";
                try(
                        Connection conn2 = DriverManager.getConnection(
                                env.getProperty("agungdh.jdbc.sqlserver.url"),
                                env.getProperty("agungdh.jdbc.sqlserver.user"),
                                env.getProperty("agungdh.jdbc.sqlserver.password")
                        );
                        Statement stmt2 = conn2.createStatement();
                        ResultSet rs2 = stmt2.executeQuery(QUERY);
                ) {

                } catch (SQLException e) {
                    log.error("SQLException Error: " + e.getMessage());

                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            log.error("SQLException Error: " + e.getMessage());

            e.printStackTrace();
        }
    }

    public void updateSubUnitValue()
    {
        ArrayList<SipdUnit> units = this.getSipdOptionsBasedOnUnit();
        ArrayList<SipdSubUnit> sipdSubUnits;
        String[] explodedKodeUnit;
        String QUERY;

        for (int i = 0; i < units.size(); i++) {
            sipdSubUnits = new ArrayList<SipdSubUnit>();

            QUERY = "SELECT du.id as id_data_unit, wo.option_value as kode_skpd, du.nama_skpd, wo_unit.option_value as kode_unit, du_unit.nama_skpd as nama_unit, wo.option_id as id_option, du.id_skpd\n" +
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
                                rs.getInt("id_option"),
                                rs.getInt("id_skpd"),
                                rs.getString("kode_skpd"),
                                rs.getString("nama_skpd"),
                                rs.getString("kode_unit"),
                                rs.getString("nama_unit"),
                                rs.getString("namakepala"),
                                rs.getString("nipkepala")
                            )
                    );

                    explodedKodeUnit = sipdSubUnits.get(sipdSubUnits.size() - 1).getKodeUnit().split("\\.");
                    explodedKodeUnit[3] = String.valueOf(sipdSubUnits.size() + 1);
                    log.info("ID: " + String.valueOf(sipdSubUnits.size() + 1));
                    log.info("Data: " + sipdSubUnits.get(sipdSubUnits.size() - 1).toString());
                    log.info("Become: " + String.join(".", explodedKodeUnit));

                    String QUERY2 = "UPDATE wp_options\n" +
                            "SET option_value = '" + String.join(".", explodedKodeUnit) + "'\n" +
                            "WHERE option_name = '_crb_unit_" + sipdSubUnits.get(sipdSubUnits.size() - 1).getIdSkpd() + "'";
                    log.info(QUERY2);
                    try(
                            Statement stmt2 = conn.createStatement();
                            ResultSet rs2 = stmt2.executeQuery(QUERY2);
                    ) {
                        log.info("Done");
                    } catch (SQLException e) {
                        log.error("SQLException Error: " + e.getMessage());

                        e.printStackTrace();
                    }
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

        String QUERY = "SELECT wo.option_value, du.nama_skpd, du.is_skpd, du.id, du.kode_skpd\n" +
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
                sipdUnits.add(new SipdUnit(rs.getInt("id"), rs.getString("nama_skpd"), rs.getString("option_value"), rs.getInt("is_skpd"), rs.getString("kode_skpd")));
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
                sipdUnits.add(new SipdUnit(rs.getInt("id"), rs.getString("nama_skpd"), rs.getString("option_value"), rs.getInt("is_skpd"), rs.getString("kode_skpd")));
            }

        } catch (SQLException e) {
            log.error("SQLException Error: " + e.getMessage());

            e.printStackTrace();
        }

        return sipdUnits;
    }

    public void simdaInjectRefUnitSkpd()
    {
        ArrayList<SipdUnit> units = this.getSipdOptionsBasedOnUnit();

        for (int i = 0; i < units.size(); i++) {
            log.info(units.get(i).getOptionValue());
            String[] explodedKodeUnit = units.get(i).getOptionValue().split("\\.");
            explodedKodeUnit = Arrays.copyOf(explodedKodeUnit, 3);
            String kodeOpd = String.join(".", explodedKodeUnit);
            log.info(kodeOpd);
            String[] explodedKodeUnit90 = units.get(i).getKodeSkpd90().split("\\.");
            String kodeUnit90 = explodedKodeUnit90[0] + '-' + explodedKodeUnit90[1] + '.' + explodedKodeUnit90[2] + '-' + explodedKodeUnit90[3] + '.' + explodedKodeUnit90[4] + '-' + explodedKodeUnit90[5] + '.' + explodedKodeUnit90[6];
            log.info(kodeUnit90);

            String QUERY = "insert into ref_unit values (" + explodedKodeUnit[0] + "," + explodedKodeUnit[1] + "," + explodedKodeUnit[2] + ", '" + units.get(i).getNamaSkpd() + "', '" + kodeUnit90 + "')";
//
            try(
                    Connection conn = DriverManager.getConnection(
                            env.getProperty("agungdh.jdbc.sqlserver.url"),
                            env.getProperty("agungdh.jdbc.sqlserver.user"),
                            env.getProperty("agungdh.jdbc.sqlserver.password")
                    );
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(QUERY);
            ) {

            } catch (SQLException e) {
                log.error("SQLException Error: " + e.getMessage());

                e.printStackTrace();
            }
        }
    }

    public void simdaInjectSkpd()
    {

    }
}