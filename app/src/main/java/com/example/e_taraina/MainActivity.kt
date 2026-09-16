package com.example.e_taraina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.e_taraina.ui.common.theme.ETarainaTheme
import com.example.e_taraina.ui.nav.ETarainaNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ETarainaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ETarainaNavGraph()
                }
            }
        }
    }
}