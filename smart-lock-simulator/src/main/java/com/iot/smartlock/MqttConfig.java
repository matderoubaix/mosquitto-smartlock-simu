package com.iot.smartlock;

public final class MqttConfig {
    public static final String BROKER = System.getenv().getOrDefault("MQTT_BROKER", "tcp://localhost:1883");
    public static final String CLIENT_ID = "smart-lock-simulator";
    public static final String COMMAND_TOPIC = "smartlock/command";
    public static final String STATUS_TOPIC = "smartlock/status";

    private MqttConfig() {
    }
}
