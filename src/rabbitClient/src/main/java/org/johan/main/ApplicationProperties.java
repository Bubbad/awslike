package org.johan.main;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Johan Nilsson Hansen
 */
@ConfigurationProperties
public class ApplicationProperties {

    String rpcQueueName;
    String rabbitHost;
    String rabbitUser;
    String rabbitPwd;

    public String getRpcQueueName() {
        return rpcQueueName;
    }

    public void setRpcQueueName(String rpcQueueName) {
        this.rpcQueueName = rpcQueueName;
    }

    public String getRabbitHost() {
        return rabbitHost;
    }

    public void setRabbitHost(String rabbitHost) {
        this.rabbitHost = rabbitHost;
    }

    public String getRabbitUser() {
        return rabbitUser;
    }

    public void setRabbitUser(String rabbitUser) {
        this.rabbitUser = rabbitUser;
    }

    public String getRabbitPwd() {
        return rabbitPwd;
    }

    public void setRabbitPwd(String rabbitPwd) {
        this.rabbitPwd = rabbitPwd;
    }

}
