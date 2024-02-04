package fi.metropolia.bibeks.networking

import android.os.Handler
import android.util.Log
import java.net.URL

private const val BASE_URL = "https://users.metropolia.fi/~jarkkov/koe.txt"
private const val TAG = "TEXT"

class Connection(
    handler: Handler
) : Runnable {
    private val myHandler = handler

    override fun run() {
        try {
            val url = URL(BASE_URL)
            val myConnection = url.openConnection()
            val inputStream = myConnection.getInputStream()
            val allText = inputStream.bufferedReader().use { it.readText() }
            val result = StringBuilder()
            result.append(allText)

            val str = result.toString()
            val message = myHandler.obtainMessage()
            message.what = 0
            message.obj = str
            myHandler.sendMessage(message)
        } catch (exception: Exception) {
            Log.d(TAG, "Error: $exception")
        }
    }
}