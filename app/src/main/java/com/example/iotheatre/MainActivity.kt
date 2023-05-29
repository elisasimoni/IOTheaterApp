package com.example.iotheatre

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var REQUEST_ENABLE_BLUETOOTH = 1
    private val deviceAddress = "D0:04:B0:22:17:1A" // Sostituisci con l'indirizzo MAC dell'Arduino
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothSocket: BluetoothSocket

    private val requestEnableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Il Bluetooth è stato abilitato con successo
            connectToArduino()
        } else {
            // L'utente ha annullato l'abilitazione del Bluetooth
            Toast.makeText(this, "Bluetooth non abilitato", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val routineButton: View = findViewById(R.id.imageView)
        val dashboardButton: View = findViewById(R.id.imageView2)

        // Inizializza il BluetoothAdapter utilizzando BluetoothManager
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        // Controlla se il Bluetooth è abilitato
        if (!bluetoothAdapter.isEnabled) {
            // Abilita il Bluetooth
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.BLUETOOTH),
                        REQUEST_ENABLE_BLUETOOTH
                    )
                }
            } else {
                requestEnableBluetoothLauncher.launch(enableBluetoothIntent)
            }
        } else {
            // Il Bluetooth è già abilitato, avvia la connessione
            connectToArduino()
        }

        routineButton.setOnClickListener {
            val intent = Intent(this, RoutineActivity::class.java)
            startActivity(intent)
        }

        dashboardButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun connectToArduino() {
        val device: BluetoothDevice? = bluetoothAdapter.getRemoteDevice(deviceAddress)
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.BLUETOOTH),
                        REQUEST_ENABLE_BLUETOOTH
                    )
                }
                return
            }
            bluetoothSocket = device?.createRfcommSocketToServiceRecord(UUID.randomUUID())!!
            bluetoothSocket.connect()
        } catch (e: IOException) {
            // Errore durante la connessione Bluetooth
            e.printStackTrace()
            Toast.makeText(this, "Errore di connessione Bluetooth", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permesso BLUETOOTH ottenuto, avvia la connessione
                connectToArduino()
            } else {
                // Permesso BLUETOOTH negato
                Toast.makeText(this, "Permesso Bluetooth negato", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Chiudi la connessione Bluetooth quando l'activity viene distrutta
        try {
            bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
