package org.johan.main;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Johan Nilsson Hansen
 */
public class RPCClient {

    private Channel channel;
    private QueueingConsumer consumer;
    private String replyToQueueName;

    ApplicationProperties applicationProperties;

    public RPCClient(ApplicationProperties applicationProperties) throws IOException, TimeoutException {
        this.applicationProperties = applicationProperties;

        try {
            System.out.println("\n\nWill connect to: " + applicationProperties.getRabbitHost() + " with username: " + applicationProperties.getRabbitUser() + " and pass: " + applicationProperties.getRabbitPwd() + "\n\n");
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(applicationProperties.getRabbitHost());
            connectionFactory.setUsername(applicationProperties.getRabbitUser());
            connectionFactory.setPassword(applicationProperties.getRabbitPwd());

            Connection connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            consumer = new QueueingConsumer(channel);

            replyToQueueName = channel.queueDeclare().getQueue();
            channel.basicConsume(replyToQueueName, true, consumer);
        } catch (Exception e ) {
            System.out.println(e);
        }
    }

    public void call(String message) throws IOException, InterruptedException {
        String id = java.util.UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                .correlationId(id)
                .replyTo(replyToQueueName)
                .build();

        channel.basicPublish("", applicationProperties.getRpcQueueName(), props, message.getBytes());
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

        System.out.println("Client got response: "+ new String(delivery.getBody()) );
    }

}
