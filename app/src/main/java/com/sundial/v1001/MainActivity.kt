package com.sundial.v1001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sundial.v1001.ui.theme.SunDialTheme

/**
 * This class represents the main activity for the SunDial app and sets up the UI layout and theme.
 */
class MainActivity : ComponentActivity() {
    /**
     * Creates the layout of the app.
     * @author Summer Gasaway
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunDialTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

/**
 * Creates a simple message to greet the user.
 * @author Summer Gasaway
 */
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

/**
 * Displays a preview for our layout in the IDE without AVD.
 * @author Summer Gasaway
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SunDialTheme {
        Greeting("Android")
    }
}