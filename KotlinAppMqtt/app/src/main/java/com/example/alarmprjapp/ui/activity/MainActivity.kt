package com.example.alarmprjapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.service.controls.actions.FloatAction
import android.util.Log
import android.view.View
import android.widget.Button
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
import androidx.compose.ui.Modifier
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

class MainActivity : AppCompatActivity(R.layout.mainpage) {


    private lateinit var mqttClient: MqttAndroidClient

    companion object {
        const val TAG = "AndroidMqttClient"
    }


    fun connect(context: Context)
    {

        //val serverMqtt = "broker.emqx.io"

        val serverMqtt = "tcp://broker.hivemq.com"

        mqttClient = MqttAndroidClient(context, serverMqtt, "hpr")


        mqttClient.setCallback(object : MqttCallback{

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }

        })

        val options = MqttConnectOptions()

        options.password = "10203045".toCharArray()
        options.userName = "hpterm-client-94:B9:7E:17:A3:00"

        try {
            mqttClient.connect(options, null, object : IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connection success")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure", exception)
                }
            })
        }catch (e : MqttException){
            e.printStackTrace()
            Log.d(TAG, "Connection error!!!", e)

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
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)


        val mqttConnect = findViewById<Button>(R.id.MqttConnect)
        mqttConnect.setOnClickListener {
            Log.i(TAG, "Mqtt connect pressed!!!!")

            connect(context = this)

        }

        val openRelay1 = findViewById<Button>(R.id.OpenRelay1)
        openRelay1.setOnClickListener {
            Log.i(TAG, "Relay 1 connect command clicked!!!!")

            publish("tst/esp32", "501")

        }

        val closeRelay1 = findViewById<Button>(R.id.CloseRelay1)
        closeRelay1.setOnClickListener {

            Log.i(TAG, "Relay 1 close command clicked!!!!")

            publish("tst/esp32", "500")
        }






    }



}

