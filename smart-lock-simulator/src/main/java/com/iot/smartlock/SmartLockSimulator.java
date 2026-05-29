package com.iot.smartlock;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SmartLockSimulator {
    private final MqttClient client;
    private volatile boolean locked = true;

    public SmartLockSimulator() throws MqttException {
        this.client = new MqttClient(MqttConfig.BROKER, MqttConfig.CLIENT_ID);
    }

    public void start() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws MqttException {
                String payload = new String(message.getPayload(), StandardCharsets.UTF_8).trim().toLowerCase(Locale.ROOT);
                if ("lock".equals(payload)) {
                    locked = true;
                    publishStatus();
                } else if ("unlock".equals(payload)) {
                    locked = false;
                    publishStatus();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });

        client.connect(options);
        client.subscribe(MqttConfig.COMMAND_TOPIC);
        publishStatus();
    }

    private void publishStatus() throws MqttException {
        String status = locked ? "LOCKED" : "UNLOCKED";
        MqttMessage message = new MqttMessage(status.getBytes(StandardCharsets.UTF_8));
        message.setQos(1);
        message.setRetained(true);
        client.publish(MqttConfig.STATUS_TOPIC, message);
    }
}
