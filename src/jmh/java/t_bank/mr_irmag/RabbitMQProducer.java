package t_bank.mr_irmag;

import com.rabbitmq.client.*;

public class RabbitMQProducer {
    private final static String QUEUE_NAME = "testQueue";
    private Connection connection;
    private Channel channel;

    public RabbitMQProducer() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            channel.confirmSelect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitForConfirms() {
        try {
            channel.waitForConfirmsOrDie();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (channel != null) channel.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



