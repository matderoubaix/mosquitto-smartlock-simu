package com.iot.smartlock;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {
    public static void main(String[] args) throws MqttException, InterruptedException {
        SmartLockSimulator simulator = new SmartLockSimulator();
        simulator.start();
        Thread.currentThread().join();
    }
}
