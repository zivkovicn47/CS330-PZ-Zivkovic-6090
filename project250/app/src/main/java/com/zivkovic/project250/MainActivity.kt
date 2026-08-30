package com.zivkovic.project250

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zivkovic.project250.navigation.AppNavGraph
import com.zivkovic.project250.ui.feature.home.MainScreen
import com.zivkovic.project250.ui.theme.Project250Theme
import com.zivkovic.project250.viewModel.CarViewModel
import com.zivkovic.project250.viewModel.CategoryViewModel
import okhttp3.OkHttpClient
import coil.ImageLoader
import coil.Coil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (com.google.firebase.auth.FirebaseAuth.getInstance().currentUser == null) {
            startActivity(android.content.Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val imageLoader = coil.ImageLoader.Builder(this)
            .okHttpClient {
                okhttp3.OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            }
            .crossfade(true)
            .build()
        coil.Coil.setImageLoader(imageLoader)

        setContent {
            AppNavGraph()
        }
    }
}
