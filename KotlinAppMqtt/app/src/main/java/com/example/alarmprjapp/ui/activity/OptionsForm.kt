package com.example.alarmprjapp.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmprjapp.MainActivity.Companion.TAG
import com.example.alarmprjapp.R

class OptionsForm : AppCompatActivity(R.layout.configpage) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        val servers = arrayOf("Select Mqtt server","broker.emqx.io", "broker.hivemq.com")
        val mqttServer : Spinner = findViewById(R.id.Mqttservers)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, servers)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mqttServer.adapter = adapter

        mqttServer.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.i(TAG, "Item selecionado "+parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val salvarBtn = findViewById<Button>(R.id.Aplicarconfig)
        salvarBtn.setOnClickListener {

            finish() //returns to MainActivity

        }

    }


}