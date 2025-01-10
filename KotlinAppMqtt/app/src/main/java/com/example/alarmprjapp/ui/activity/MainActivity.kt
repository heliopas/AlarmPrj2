package com.example.alarmprjapp

import android.adservices.topics.Topic
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.service.controls.actions.FloatAction
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.MqttTopic

class MainActivity : AppCompatActivity(R.layout.mainpage) {


    private lateinit var mqttClient: MqttAndroidClient

    companion object {
        const val TAG = "AndroidMqttClient"
    }

    fun connect(topic: String, context: Context){

        //val serverMqtt = "broker.emqx.io"

        val serverMqtt = "tcp://broker.hivemq.com:1883"

        mqttClient = MqttAndroidClient(context, serverMqtt, "AndroidAlarm")

        val options = MqttConnectOptions()

        //options.password = "10203045".toCharArray()
        //options.userName = "hpterm-client-94:B9:7E:17:A3:00"
        options.connectionTimeout = 40
        options.isCleanSession = true
        options.isAutomaticReconnect = true


        try {
            mqttClient.connect(options, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connection success")
                    printConsole("Connection success")

                    subscribe(topic, 1)

                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure", exception)
                    printConsole("Connection failure")
                }

            })
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.d(TAG, "Connection error!!!", e)

        }

        mqttClient.setCallback(object : MqttCallback {

            override fun messageArrived(topic: String, message: MqttMessage) {
                //Thread{
                    Log.d(TAG, "Received message: ${String(message.payload)} from topic: $topic")

                    printConsole("Received message: ${String(message.payload)}")

                    when(topic){
                        "tst2/esp32" -> Log.d(TAG, "       Something happens here!!: $topic")
                        else -> Log.d(TAG, "Unhandled topic: $topic")
                    }
                //}.start()

            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(TAG, "        Message delivered successfully!!")


            }


        })

    }

    fun subscribe(topic: String, qos: Int = 1){

        try {
            mqttClient.subscribe(topic, qos, null,object : IMqttActionListener{
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to subscribe $topic")
                }

                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Subscribed to $topic")
                }
            })
        }catch ( e : MqttException){
            e.printStackTrace()
            Log.d(TAG, "subscribe error!!!", e)
        }


    }

    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {

        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "$msg published to $topic")
                    printConsole("$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to publish $msg to $topic")
                    printConsole("Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.d(TAG, "Publish error!!!", e)
        }
    }

    fun disconnect() {

        try {
            mqttClient.disconnect(null, object : IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Client disconnection success!!")
                    printConsole("Client disconnection success!!")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "Client disconnection failed!!")
                    printConsole("Client disconnection failed!!")
                }
            })

        }catch (e : MqttException){
            e.printStackTrace()
            Log.d(TAG, "Disconnection error!!!", e)
        }


    }

    fun printConsole( topic: String, opt: Int = 0){

        val console = findViewById<EditText>(R.id.terminal)
        console.append("\n"+topic)

        if (console.lineCount > 15) {
            console.setText("")
        }

        when (opt){
            1 -> console.setText("")
        }

    }

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)


        val mqttConnect = findViewById<Button>(R.id.MqttConnect)
        mqttConnect.setOnClickListener {

            try {

                    Log.i(TAG, "Mqtt connect pressed!!!!")
                    connect("tst2/esp32",context = this)


            }catch (e : MqttException){
                e.printStackTrace()
                Log.d(TAG, "Disconnection error!!!", e)
            }

        }

        val openRelay1 = findViewById<Button>(R.id.OpenRelay1)
        openRelay1.setOnClickListener {

            if (mqttClient.isConnected){
            Log.i(TAG, "Relay 1 connect command clicked!!!!")

            publish("tst2/esp32", "101")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val closeRelay1 = findViewById<Button>(R.id.CloseRelay1)
        closeRelay1.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Relay 1 close command clicked!!!!")

                publish("tst2/esp32", "100")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val openRelay2 = findViewById<Button>(R.id.OpenRelay2)
        openRelay2.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Buzzer on command clicked!!!!")

                publish("tst2/esp32", "201")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val closeRelay2 = findViewById<Button>(R.id.CloseRelay2)
        closeRelay2.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Buzzer off command clicked!!!!")

                publish("tst2/esp32", "200")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val openRelay3 = findViewById<Button>(R.id.OpenRelay3)
        openRelay3.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Buzzer on command clicked!!!!")

                publish("tst2/esp32", "301")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val closeRelay3 = findViewById<Button>(R.id.CloseRelay3)
        closeRelay3.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Buzzer off command clicked!!!!")

                publish("tst2/esp32", "300")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val openRelay4 = findViewById<Button>(R.id.OpenRelay4)
        openRelay4.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Buzzer on command clicked!!!!")

                publish("tst2/esp32", "401")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val closeRelay4 = findViewById<Button>(R.id.CloseRelay4)
        closeRelay4.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Buzzer off command clicked!!!!")

                publish("tst2/esp32", "400")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val buzzon = findViewById<Button>(R.id.BuzzOn)
        buzzon.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Buzzer on command clicked!!!!")

                publish("tst2/esp32", "501")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val buzzoff = findViewById<Button>(R.id.BuzzOff)
        buzzoff.setOnClickListener {

            if (mqttClient.isConnected){
                Log.i(TAG, "Buzzer off command clicked!!!!")

                publish("tst2/esp32", "500")
            }
            else{printConsole("Mqtt não conectado!!!!")}

        }

        val mqttClose = findViewById<Button>(R.id.MqttDisconnect)
        mqttClose.setOnClickListener {

            Log.i(TAG, "Client disconnected!!!!")
            disconnect()
        }

    }



}

