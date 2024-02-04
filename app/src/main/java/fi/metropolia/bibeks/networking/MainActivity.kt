package fi.metropolia.bibeks.networking

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    var fetchedData = ""
    var isLoading by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (isNetworkAvailable(this)) {
                val runnable = Connection(handler)
                val thread = Thread(runnable)
                if (!isLoading) {
                    thread.start()
                    isLoading = true
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DisplayText(
                    fetchedData = fetchedData,
                    onReadFileClick = {
                        val runnable = Connection(handler)
                        val thread = Thread(runnable)
                        thread.start()
                        isLoading = true
                    }
                )
            }
        }
    }

    private val handler: Handler = object :
        Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            if (inputMessage.what == 0) {
                fetchedData = inputMessage.obj.toString()
                isLoading = false
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager).isDefaultNetworkActive
}

@Composable
fun DisplayText(fetchedData: String, onReadFileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = fetchedData)
        Button(
            onClick = { onReadFileClick() },
            modifier = Modifier.padding(0.dp)
                .offset(y = (-5).dp)
        ) {
            Text(text = "Read File")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}