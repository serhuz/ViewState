package xyz.randomcode.viewstate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import xyz.randomcode.viewstate.ui.theme.ViewStateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewStateTheme {
                MainScreen()
            }
        }
    }
}
