package com.example.alarmprjapp.ui.activity

class connection {

    var serverMqttHivemq: String = "tcp://broker.hivemq.com:1883"
        get() {
            return field
        }

    var serverMqttEmqc = "tcp://broker.emqx.io:1883"
        get() {
            return field
        }


}