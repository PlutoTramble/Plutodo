package org.plutotramble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PlutodoApiServerApplication {

    private static final Logger log = LoggerFactory.getLogger(PlutodoApiServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PlutodoApiServerApplication.class, args);
    }
}
