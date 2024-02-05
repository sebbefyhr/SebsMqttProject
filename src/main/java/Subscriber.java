import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;

import java.util.UUID;

public class Subscriber {

    public static void main(String[] args){

        Mqtt3AsyncClient subClient = MqttClient.builder()
                .useMqttVersion3()
                .identifier(UUID.randomUUID().toString())
                .serverHost("broker.hivemq.com")
                .serverPort(1883)
                .buildAsync();


        subClient.connectWith()
                .simpleAuth()
                .username("subClient1")
                .password("hellowBellow84".getBytes())
                .applySimpleAuth()
                .send()
                .whenComplete((connAck, throwable) -> {
                   if(throwable != null){
                       System.out.println("subclient not connected: " + throwable.getMessage());
                   }
                   else {
                       System.out.println("subclient connected!" );
                   }
                });


        subClient.subscribeWith()
                .topicFilter("the/topic")
                .callback(msg -> {
                    System.out.println("Message received: " + msg);
                    System.out.println("Payload: " + (new String(msg.getPayloadAsBytes())));

                })
                .send()
                .whenComplete((subAck, throwable) -> {
                    if(throwable != null){
                        System.out.println("subclient not subscribed: " + throwable.getMessage());
                    }
                    else {
                        System.out.println("subclient subscribed!" );
                    }
                });

    }

}
