package com.example.iotheatre

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException


class DashboardActivity : AppCompatActivity() {
    private lateinit var redImageView: ImageView
    private lateinit var greenImageView: ImageView
    private lateinit var blueImageView: ImageView
    private lateinit var spotlightImageView: ImageView
    private lateinit var curtainImageView: ImageView
    private lateinit var musicImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        // Esempio di accesso all'oggetto BluetoothSocket dal singleton
        //val bluetoothSocket = BluetoothSocketSingleton.bluetoothSocket
        //val bluetoothAdapter = BluetoothAdapterSingleton.bluetoothAdapter

        redImageView = findViewById(R.id.red)
        greenImageView = findViewById(R.id.green)
        blueImageView = findViewById(R.id.blue)
        spotlightImageView = findViewById(R.id.imageView3)
        curtainImageView = findViewById(R.id.imageView4)
        musicImageView = findViewById(R.id.imageView5)

        /*if (bluetoothSocket != null && bluetoothSocket.isConnected) {
                try {
                    // Invia il comando per controllare lo stato del LED 1 all'Arduino
                    bluetoothSocket.outputStream.write("CHECK_LED_1".toByteArray())

                    // Leggi la risposta dall'Arduino
                    val buffer = ByteArray(1024)
                    val bytesRead = bluetoothSocket.inputStream.read(buffer)
                    val response = String(buffer, 0, bytesRead)

                    // Verifica lo stato del LED 1 e imposta il testo corrispondente
                    val statusText = if (response.trim() == "LED_1_ON") "ON" else "OFF"
                    Toast.makeText(this, "LED 1 è $statusText", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(this, "Errore nella comunicazione con l'Arduino", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Bluetooth non è connesso", Toast.LENGTH_SHORT).show()
            }

*/

        greenImageView.setOnClickListener {
            // Azione eseguita quando l'utente fa clic sull'immagine verde
            Toast.makeText(this, "Cliccato immagine verde", Toast.LENGTH_SHORT).show()



        }

        blueImageView.setOnClickListener {
            // Azione eseguita quando l'utente fa clic sull'immagine blu
            Toast.makeText(this, "Cliccato immagine blu", Toast.LENGTH_SHORT).show()
            // Esegui le azioni desiderate
        }

        spotlightImageView.setOnClickListener {
            // Azione eseguita quando l'utente fa clic sull'immagine spotlight
            Toast.makeText(this, "Cliccato immagine spotlight", Toast.LENGTH_SHORT).show()
            // Esegui le azioni desiderate
        }

        curtainImageView.setOnClickListener {
            // Azione eseguita quando l'utente fa clic sull'immagine del sipario
            Toast.makeText(this, "Cliccato immagine sipario", Toast.LENGTH_SHORT).show()
            // Esegui le azioni desiderate
        }

        musicImageView.setOnClickListener {
            // Azione eseguita quando l'utente fa clic sull'immagine della musica
            Toast.makeText(this, "Cliccato immagine musica", Toast.LENGTH_SHORT).show()
            // Esegui le azioni desiderate
        }
    }
}
