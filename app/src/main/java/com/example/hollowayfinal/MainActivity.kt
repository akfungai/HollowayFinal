package com.example.hollowayfinal

//import org.eclipse.paho.android.service.MqttAndroidClient removed
//import info.mqtt.android.service.MqttAndroidClient;


import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val messageTextView: TextView = findViewById(R.id.textView)
        messageTextView.movementMethod = ScrollingMovementMethod.getInstance()

        val connectButton: Button = findViewById(R.id.connectButton)

        val checkBox: CheckBox = findViewById(R.id.checkBox)

        val SubscribeTextView: EditText = findViewById(R.id.SubscribeTextView)



        connectButton.setOnClickListener {
            messageTextView.text = "Connecting..."

            val intent = Intent(this, GraphsView::class.java)
            startActivity(intent)
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // When CheckBox is checked
                SubscribeTextView.alpha = 0.0f // Make TextView transparent
                SubscribeTextView.isClickable = false // Make TextView unclickable
            } else {
                // When CheckBox is unchecked
                SubscribeTextView.alpha = 1.0f // Make TextView opaque
                SubscribeTextView.isClickable = true // Make TextView clickable
            }
        }









}
    }
