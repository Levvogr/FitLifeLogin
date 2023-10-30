package com.example.myapplicationjetpcomp.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplicationjetpcomp.ui.theme.MyApplicationJetpCompTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrintToken()
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrintToken() {
    val context = LocalContext.current
    MyApplicationJetpCompTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val intent = (context as ResultActivity).intent
            val token = intent.getStringExtra("token")
            Text(
                text = "token: " + token
            )
        }
    }
}
