package com.example.alarmprjapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.service.controls.actions.FloatAction
import android.view.View
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

class MainActivity : AppCompatActivity(R.layout.mainpage) {


    private lateinit var mqttClient: MqttClient

    companion object {
        const val TAG = "AndroidMqttClient"
    }

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)


//        val mqttConnect = findViewById<FloatingActionButton>(R.id.MqttConnect)
//        mqttConnect.setOnClickListener {
//            fun connect(context : Context){
//
//
//
//
//
//            }
//        }

    }



}

