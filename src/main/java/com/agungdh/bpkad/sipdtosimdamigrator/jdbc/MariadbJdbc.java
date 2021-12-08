package com.agungdh.bpkad.sipdtosimdamigrator.jdbc;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Data
@Configuration
@PropertySource("classpath:application.properties")
public class MariadbJdbc {
    @Autowired
    private Environment env;

    @Value( "${agungdh.jdbc.mariadb.url}" )
    String DB_URL;
    @Value( "${agungdh.jdbc.mariadb.user}" )
    String USER;
    @Value( "${agungdh.jdbc.mariadb.password}" )
    String PASS;
}
