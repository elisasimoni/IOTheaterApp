package com.example.iotheatre

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "IOTheater"
    private var btpermission = false
    private var macAddress = "98:DA:60:03:AC:23"
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var bluetoothSocket: BluetoothSocket? = null

    private var isRedLedSelected = false
    private var isGreenLedSelected = false
    private var isBlueLedSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val buttonRed = findViewById<Button>(R.id.buttonRed)
        val buttonGreen = findViewById<Button>(R.id.buttonGreen)
        val buttonBlue = findViewById<Button>(R.id.buttonBlue)

        buttonRed.setOnClickListener {
                isRedLedSelected = true
                buttonRed.setBackgroundColor(Color.RED)
        }

        buttonGreen.setOnClickListener {

                isGreenLedSelected = true
                buttonGreen.setBackgroundColor(Color.GREEN)

        }

        buttonBlue.setOnClickListener {

                isBlueLedSelected = true
                buttonBlue.setBackgroundColor(Color.BLUE)

        }




        val startButton = findViewById<Button>(R.id.startButton)
        val anotherButton = findViewById<Button>(R.id.anotherButton)
        startButton.setOnClickListener {
            val json = createJsonFromLayout()

            if (json != null) {
                //Toast.makeText(this, "Routine send Wait 3 second to start", Toast.LENGTH_LONG).show()
                saveJsonToFile(json)

                val jsonString = json.toString() + "\n"

                sendJsonToArduino(jsonString)
            }
        }
        anotherButton.setOnClickListener {

            val json2 = createJsonFromLayout2()

            if (json2 != null) {



                val jsonString2 = json2.toString() + "\n"

                sendJsonToArduino(jsonString2)
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 31) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 100)
                return
            }
        }
    }

    private fun btScan(device: String) {
       /* val json = createJsonFromLayoutMode()
        val jsonString = json.toString() + "\n"
        sendJsonToArduino(jsonString)*/
        Toast.makeText(this, "Bluetooth connected successfully to $device", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingPermission")
    private val btActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            HC06Connection()
        }
    }

    @SuppressLint("MissingPermission")
    private fun HC06Connection() {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
        val HC06BluetoothModule: BluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress)

        if (bluetoothSocket == null || !bluetoothSocket!!.isConnected) {
            bluetoothSocket = HC06BluetoothModule.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            btScan(HC06BluetoothModule.name)
        }

        val outputStream: OutputStream? = bluetoothSocket?.outputStream
    }


    private val blueToothPermissionLauncher = registerForActivityResult(
        RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
            btpermission = true
            if (!bluetoothAdapter?.isEnabled!!) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            } else {
                HC06Connection()
            }
        } else {
            btpermission = false
        }
    }

    fun scanBT(view: View) {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_LONG).show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }
    }

    @SuppressLint("MissingPermission")

    private fun sendJsonToArduino(jsonString: String) {
        if (bluetoothSocket == null || !bluetoothSocket!!.isConnected) {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
            val HC06BluetoothModule: BluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress)
            bluetoothSocket = HC06BluetoothModule.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            btScan(HC06BluetoothModule.name)
        }

        val outputStream: OutputStream? = bluetoothSocket?.outputStream

        if (outputStream == null) {
            Log.d(TAG, "Output stream error")
            return
        }

        try {
            outputStream.write(jsonString.toByteArray())
            Log.d("JSON Sent ->", jsonString)
            //bluetoothSocket?.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        // Non chiudere la connessione Bluetooth qui, in quanto potrebbe essere riutilizzata in futuro
    }

    private fun createJsonFromLayout2(): JSONObject?{
        val json = JSONObject()
        try {
            var g = "0"
            var b = "0"
            var r = "0"
            val songSpinner = findViewById<Spinner>(R.id.songSpinner)
            json.put("G", songSpinner.selectedItem.toString())

            val lightIntensitySeekBar = findViewById<SeekBar>(R.id.lightIntensitySeekBar)
            json.put("H", lightIntensitySeekBar.progress)
            if(lightIntensitySeekBar.progress == 0){
                g = "0"
                b = "0"
                r = "0"
            }else if(lightIntensitySeekBar.progress in 0..24){
                if (isBlueLedSelected) {
                    b = "65"
                }
                if (isRedLedSelected) {
                    r = "65"
                }
                if (isGreenLedSelected) {
                    g = "65"
                }
            }else if(lightIntensitySeekBar.progress in 25..49){
                if (isBlueLedSelected) {
                    b = "130"
                }
                if (isRedLedSelected) {
                    r = "130"
                }
                if (isGreenLedSelected) {
                    g = "130"
                }
            }else if(lightIntensitySeekBar.progress in 50..74){
                if (isBlueLedSelected) {
                    b = "195"
                }
                if (isRedLedSelected) {
                    r = "195"
                }
                if (isGreenLedSelected) {
                    g = "195"
                }
            }else if(lightIntensitySeekBar.progress in 75..100){
                if (isBlueLedSelected) {
                    b = "255"
                }
                if (isRedLedSelected) {
                    r = "255"
                }
                if (isGreenLedSelected) {
                    g = "255"
                }
            }

            json.put("I", r)
            json.put("J", g)
            json.put("K", b)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json

    }
    private fun createJsonFromLayout(): JSONObject? {
        val json = JSONObject()
        try {
            val durationEditText = findViewById<EditText>(R.id.durationEditText)
            json.put("A", durationEditText.text.toString())

            val lightOnEditText = findViewById<EditText>(R.id.lightOnEditText)
            json.put("B", lightOnEditText.text.toString())

            val lightOffEditText = findViewById<EditText>(R.id.lightOffEditText)
            json.put("C", lightOffEditText.text.toString())

            val songTimerEditText = findViewById<EditText>(R.id.songTimerEditText)
            json.put("D", songTimerEditText.text.toString())

            val songTimerEditText2 = findViewById<EditText>(R.id.songTimerEditText2)
            json.put("E", songTimerEditText2.text.toString())

            val songVolume = findViewById<SeekBar>(R.id.songVolumeSeekBar)
            json.put("F", songVolume.progress)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json
    }
    private fun createJsonFromLayoutMode(): JSONObject? {
        val json = JSONObject()
        try {

            json.put("btmode", "btmode")

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json
    }

    private fun saveJsonToFile(json: JSONObject) {
        try {
            val jsonString = json.toString()
            val file = File(filesDir, "routine.json")
            val writer = FileWriter(file)
            writer.write(jsonString)
            writer.close()
            System.out.println("JSON saved to: " + file.getAbsolutePath())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
