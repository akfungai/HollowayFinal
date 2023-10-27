package com.example.hollowayfinal

//import org.eclipse.paho.android.service.MqttAndroidClient removed
//import info.mqtt.android.service.MqttAndroidClient;


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val messageTextView: TextView = findViewById(R.id.textView)
        messageTextView.movementMethod = ScrollingMovementMethod.getInstance()

        val connectButton: Button = findViewById(R.id.connectButton)



        connectButton.setOnClickListener {
            messageTextView.text = "Connecting..."

            val intent = Intent(this, GraphsView::class.java)
            startActivity(intent)






        }






}
    }
