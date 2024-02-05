import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        System.out.println("in Main method");

        Mqtt3AsyncClient client = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .serverPort(1883)
                .buildAsync();

        client.connectWith()
                .simpleAuth()
                .username("SebsMainPublisher")
                .password("hivemq".getBytes())
                .applySimpleAuth()
                .send()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        System.out.println("unable to connect... + " +throwable.getMessage());
                        System.out.println(throwable.getCause());
                        System.out.println("connAck: " + connAck);
                    } else {
                        System.out.println("connected");
                        System.out.println();
                    }
                });


        for(int i = 0; i <7; i++) {
            //for publishing
            client.publishWith()
                    .topic("the/topic")
                    .payload(("hello bellow world " + i).getBytes())
                    .send()
                    .whenComplete((publish, throwable) -> {
                        if (throwable != null) {
                            System.out.println("unable to publish... + "  +throwable.getMessage());
                        } else {
                            System.out.println("message published");
                        }
                    });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}