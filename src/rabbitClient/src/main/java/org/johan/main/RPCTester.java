package org.johan.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Johan Nilsson Hansen
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class RPCTester {

    @Autowired
    ApplicationProperties applicationProperties;

    @Bean
    public RPCClient getRPCClient() throws IOException, TimeoutException {
        return new RPCClient(applicationProperties);
    }

    @PostConstruct
    public void sendMessages() throws IOException, TimeoutException, InterruptedException {
        while(true) {
            getRPCClient().call("SENDING MSG");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(RPCTester.class, args);
    }
}
