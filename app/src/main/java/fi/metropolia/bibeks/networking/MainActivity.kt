package fi.metropolia.bibeks.networking

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp {
                ImageLoader("https://users.metropolia.fi/~jarkkov/folderimage.jpg")
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = "This is a special test file",
                textAlign = TextAlign.Center
            )
            content()
        }
    }
}

@Composable
fun ImageLoader(url: String) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        try {
            val bitmap: ImageBitmap = withContext(Dispatchers.IO) {
                downloadImage(url)
            }
            imageBitmap = bitmap
        } catch (e: Exception) {
            Log.e("ImageLoader", "Error loading image: $e")
        }
    }

    // Display the downloaded image
    imageBitmap?.let {
        Image(
            bitmap = it,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private suspend fun downloadImage(url: String): ImageBitmap {
    val inputStream: InputStream = URL(url).openStream()
    val byteArray = withContext(Dispatchers.IO) {
        inputStream.readBytes()
    }
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    return bitmap.asImageBitmap()
}
