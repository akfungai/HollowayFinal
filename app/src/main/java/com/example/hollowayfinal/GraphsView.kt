package com.example.hollowayfinal

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class GraphsView : AppCompatActivity() {

    lateinit var mqttClient: MQTTClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphs_view)

        val graph: GraphView = findViewById(R.id.graph)
        val series = LineGraphSeries<DataPoint>()
        graph.addSeries(series)

        val graph2: GraphView = findViewById(R.id.graph2)
        val series2 = LineGraphSeries<DataPoint>()
        graph2.addSeries(series2)

        series.setOnDataPointTapListener { series, dataPoint ->
            Toast.makeText(
                this,
                "Series1: On Data Point clicked: $dataPoint",
                Toast.LENGTH_SHORT
            ).show()
        }

        series2.setOnDataPointTapListener { series2, dataPoint ->
            Toast.makeText(
                this,
                "Series2: On Data Point clicked: $dataPoint",
                Toast.LENGTH_SHORT
            ).show()
        }






// Some basic graph configuration
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(10.0) // for instance, display 40 data points and scroll after that
        graph.viewport.isScrollable = true
        graph.viewport.isScalable = true
        graph.gridLabelRenderer.horizontalAxisTitle="Data Point"
        graph.gridLabelRenderer.verticalAxisTitle="Temperature (°C)"
        graph.gridLabelRenderer.padding=40
        graph.title="Temperature Over Time"

        val nf: NumberFormat = NumberFormat.getInstance()
        nf.maximumFractionDigits = 2
        graph.gridLabelRenderer.labelFormatter = DefaultLabelFormatter(nf, nf)



        graph2.viewport.isXAxisBoundsManual = true
        graph2.viewport.setMinX(0.0)
        graph2.viewport.setMaxX(10.0) // for instance, display 40 data points and scroll after that
        graph2.viewport.isScrollable = true
        graph2.viewport.isScalable = true
        graph2.gridLabelRenderer.horizontalAxisTitle="Data Point"
        graph2.gridLabelRenderer.verticalAxisTitle="Pressure (kPa)"
        graph2.gridLabelRenderer.padding=40
        graph2.title="Pressure Over Time"


        nf.maximumFractionDigits = 3
        nf.maximumIntegerDigits = 3
        graph2.gridLabelRenderer.labelFormatter = DefaultLabelFormatter(nf, nf)



        var startScrolling = false
        var lastX = 0.0




        mqttClient = MQTTClient(this, "ssl://au1.cloud.thethings.network:8883")
        mqttClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                mqttClient.subscribe("v3/waynes-test1@ttn/devices/+/up")

            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {

                // Handle the incoming message
                val payload = String(message?.payload!!)
                // Update your UI with the payload
                Log.d(ContentValues.TAG, "Message arrived: $message")

                // Extract the "received_at" field, which contains the time data
                val (time,temperature,pressure) = parsePayloadToDataPoint(payload)

                if (temperature<12){
                    Toast.makeText(this@GraphsView, "Warning: Temps Low on node 1", Toast.LENGTH_SHORT).show()
                }

                val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
                val date = sdf.parse(time)




                series.appendData(DataPoint(lastX, temperature), startScrolling, 40)

                series2.appendData(DataPoint(lastX++, pressure), startScrolling, 40)


                if (lastX==10.0){
                    startScrolling = true
                }

//                runOnUiThread {//maybe delete
//                    messageTextView.text = "Message arrived: $message"
//                }
                //messageTextView.text = payload

            }

            override fun connectionLost(cause: Throwable?) {
                // Handle connection lost
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // Message delivered

            }
        })

        mqttClient.connect(
            "waynes-test1",
            "NNSXS.6JNIFRLAPKILNWV6VX7CFIFH7OMS44DWC7JRBDI.ER2DZQ6MIF3KZLNBM46B5SW5R6SAC3MN567N3VECDMIEJR3RUPTQ", //try delete before the dot and the dot
            { //maybe add @ttn to end of username
                Toast.makeText(this, "Connected successfully to waynes-test1", Toast.LENGTH_SHORT).show()
               // messageTextView.text = "Waiting for data..."

            },
            { error ->
                // Handle connection failure
                Toast.makeText(this, "Connection error: $error", Toast.LENGTH_SHORT).show()
            })



    }
    private fun parsePayloadToDataPoint(payload: String): Triple<String, Double, Double>  {
        val jsonObject = JSONObject(payload)

        val fullTime: String = jsonObject.getString("received_at")
        val startIndex = fullTime.indexOf('T') + 1
        val endIndex = fullTime.indexOf('Z')-6
        val extractedTime = fullTime.substring(startIndex, endIndex)

        val uplinkMessage = jsonObject.getJSONObject("uplink_message")
        val decodedPayload = uplinkMessage.getJSONObject("decoded_payload")
        val temperature = decodedPayload.getDouble("Temp")

        val pressure = (decodedPayload.getDouble("Pressure"))/1000


        return Triple(extractedTime, temperature, pressure)
    }
    fun timeStringToSecondsSinceMidnight(time: String): Double {
        val parts = time.split(":")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val seconds = parts[2].split(".")[0].toInt()
        return (hours * 3600 + minutes * 60 + seconds).toDouble()
    }

    override fun onDestroy() {
        mqttClient.disconnect()
        super.onDestroy()
    }
}