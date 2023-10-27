package com.example.hollowayfinal

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import info.mqtt.android.service.Ack
//import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import info.mqtt.android.service.MqttAndroidClient


class MQTTClient(
    context: Context,
    brokerUrl: String,
    clientId: String = MqttClient.generateClientId()
) {
    private var mqttClient: MqttAndroidClient = MqttAndroidClient(context, brokerUrl, clientId, Ack.AUTO_ACK)

    fun setCallback(callback: MqttCallback) {
        mqttClient.setCallback(callback)
    }

    fun connect(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Throwable?) -> Unit
    ) {
        val options = MqttConnectOptions().apply {
            this.userName = username
            this.password = password.toCharArray()
        }
        try {


            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    onSuccess()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    onFailure(exception)
                }
            })
        } catch (ex: MqttException) {
            ex.printStackTrace()
        }
    }
        fun subscribe(topic: String, qos: Int = 0) {
            mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    // Successfully subscribed to topic
                    Log.d(TAG, "subscibed")

                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    // Failed to subscribe
                    Log.d(TAG, "no subscibed")


                }
            })
        }

        fun disconnect() {
            mqttClient.disconnect()
        }
    }


