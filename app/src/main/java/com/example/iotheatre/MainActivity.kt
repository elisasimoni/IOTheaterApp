package com.example.iotheatre

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
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
    var uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

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
            isRedLedSelected = !isRedLedSelected
            updateButtonState(buttonRed, "R",isRedLedSelected)
        }

        buttonGreen.setOnClickListener {
            isGreenLedSelected = !isGreenLedSelected
            updateButtonState(buttonGreen,"G", isGreenLedSelected)
        }

        buttonBlue.setOnClickListener {
            isBlueLedSelected = !isBlueLedSelected
            updateButtonState(buttonBlue,"B" ,isBlueLedSelected)
        }


        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener { // Salva le condizioni degli elementi in un oggetto JSON
            val json = createJsonFromLayout()

            if (json != null) {
                Toast.makeText(this,"Routine send Wait 3 second to start",Toast.LENGTH_LONG).show()
                saveJsonToFile(json)

            }

        }





        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT > 31) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT), 100)
                return
            }
        }





    }
    fun btScan(device:String){
        Toast.makeText(this,"Bluetooth connected succesfully to"+device, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("MissingPermission")
    private val btActivityResultLauncher=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result: ActivityResult ->
        if(result.resultCode==RESULT_OK){


            HC06Connection()


        }
    }


    @SuppressLint("MissingPermission")
    fun HC06Connection(){
        val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter:BluetoothAdapter=bluetoothManager.adapter
        val HC06BluetoothModule: BluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress)
        val deviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID universale
        var bluetoothSocket: BluetoothSocket? = null
        bluetoothSocket = HC06BluetoothModule.createRfcommSocketToServiceRecord(deviceUUID)
        bluetoothSocket?.connect()
        btScan(HC06BluetoothModule.name)
        var outputStream = bluetoothSocket?.outputStream


        if (outputStream == null) {
            Log.d(TAG, "Output stream error")
            return
        }
        try {
            var command = "a"

            command = """ $command""".trimIndent()
            outputStream.write(command.toByteArray())
            Log.d("Command- >", command)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }






      //  bluetoothSocket?.close()
    }



    private val blueToothPermissionLauncher=registerForActivityResult(
        RequestPermission()
    ){isGranted:Boolean ->
        if(isGranted){
            val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter:BluetoothAdapter=bluetoothManager.adapter
            btpermission = true
            if(bluetoothAdapter?.isEnabled==false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            }else{
                HC06Connection()

            }
        }else{
            btpermission = false
        }
    }

    fun scanBT(view: View){
        val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter:BluetoothAdapter=bluetoothManager.adapter
        if (bluetoothAdapter==null){
            Toast.makeText(this,"Device doesn't support Bluetooth",Toast.LENGTH_LONG).show()

        }else{
            if(VERSION.SDK_INT>=Build.VERSION_CODES.S){
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            }else{
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }

    }
   /* private fun sendJsonToArduino(json: JSONObject) {
        val jsonString = json.toString()

        var outputStream: OutputStream? = null
        try {
            outputStream = bluetoothSocket?.outputStream
            if (outputStream == null) {
                Log.d(TAG, "Output stream error")
                return
            }

            val command = jsonString

            outputStream.write(command.toByteArray())
            Log.d("Command- >", command)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } finally {
            outputStream?.close()
        }
    }

    */

    private fun createJsonFromLayout(): JSONObject? {
        val json = JSONObject()
        try {
            // Aggiungi i valori delle EditText all'oggetto JSON
            val durationEditText = findViewById<EditText>(R.id.durationEditText)
            json.put("routineDuration", durationEditText.text.toString())
            val lightOnEditText = findViewById<EditText>(R.id.lightOnEditText)
            json.put("lightOn", lightOnEditText.text.toString())
            val lightOffEditText = findViewById<EditText>(R.id.lightOffEditText)
            json.put("lightOff", lightOffEditText.text.toString())
            val curtainsOpenEditText = findViewById<EditText>(R.id.curtainsOpenEditText)
            json.put("curtainsOpen", curtainsOpenEditText.text.toString())
            val curtainsCloseEditText = findViewById<EditText>(R.id.curtainsCloseEditText)
            json.put("curtainsClose", curtainsCloseEditText.text.toString())
            val songTimerEditText = findViewById<EditText>(R.id.songTimerEditText)
            json.put("songPlayStart", songTimerEditText.text.toString())
            val songTimerEditText2 = findViewById<EditText>(R.id.songTimerEditText2)
            json.put("songPlayStop", songTimerEditText2.text.toString())
            val songVolume = findViewById<SeekBar>(R.id.songVolumeSeekBar)
            json.put("songVolume", songVolume.progress)



            // Aggiungi i valori degli Spinner all'oggetto JSON
            val songSpinner = findViewById<Spinner>(R.id.songSpinner)
            json.put("selectedSong", songSpinner.selectedItem.toString())

            // Aggiungi il valore del SeekBar all'oggetto JSON
            val lightIntensitySeekBar = findViewById<SeekBar>(R.id.lightIntensitySeekBar)
            json.put("lightIntensity", lightIntensitySeekBar.progress)

            // Esempio per i pulsanti (led) usando il loro stato selezionato come valore booleano
            val buttonRed = findViewById<Button>(R.id.buttonRed)
            val isRedLedSelected = buttonRed.isSelected
            json.put("isRedLedSelected", isRedLedSelected)
            val buttonGreen = findViewById<Button>(R.id.buttonGreen)
            val isGreenLedSelected = buttonGreen.isSelected
            json.put("isGreenLedSelected", isGreenLedSelected)
            val buttonBlue = findViewById<Button>(R.id.buttonBlue)
            val isBlueLedSelected = buttonBlue.isSelected
            json.put("isBlueLedSelected", isBlueLedSelected)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return json
    }

    private fun saveJsonToFile(json: JSONObject) {
        try {
            // Converti l'oggetto JSON in una stringa
            val jsonString = json.toString()

            val file = File(filesDir, "routine.json")

            // Scrivi la stringa JSON sul file
            val writer = FileWriter(file)
            writer.write(jsonString)
            writer.close()

            // Stampa il percorso del file di output
            System.out.println("JSON saved to: " + file.getAbsolutePath())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun updateButtonState(button: Button, name:String, isSelected: Boolean) {
        if (isSelected) {
            button.isSelected = true
            if(name=="R"){
            button.setBackgroundColor(Color.RED)
            } else if (name=="G"){
                button.setBackgroundColor(Color.GREEN)
            }else{
                button.setBackgroundColor(Color.BLUE)
            }
        } else {
            button.isSelected = false
            button.alpha = 0.5f
        }
    }



}
