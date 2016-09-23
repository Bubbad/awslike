package org.johan.main;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.rabbitmq.client.AMQP.BasicProperties;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Johan Nilsson Hansenb
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class RPCServer {

    @Autowired
    ApplicationProperties applicationProperties;

    @PostConstruct
    public void startServer() throws IOException, TimeoutException, InterruptedException {
        System.out.println("SERVER STARTING");
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(applicationProperties.getRabbitHost());

        connectionFactory.setUsername(applicationProperties.getRabbitUser());
        connectionFactory.setPassword(applicationProperties.getRabbitPwd());

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        QueueingConsumer consumer = new QueueingConsumer(channel);

        System.out.println("Attaching to queue: " + applicationProperties.getRpcQueueName());
        channel.basicConsume(applicationProperties.getRpcQueueName(), false, consumer);

        while(true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            BasicProperties incomingProps = delivery.getProperties();
            BasicProperties replyProps = new BasicProperties
                    .Builder()
                    .correlationId(incomingProps.getCorrelationId())
                    .build();

            System.out.println("Server recieved message: " + new String(delivery.getBody()));
            System.out.println("Server will respond to queue: " + incomingProps.getReplyTo());

            channel.basicPublish("", incomingProps.getReplyTo(), replyProps, "RESPONSE".getBytes());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RPCServer.class, args);
    }
}
