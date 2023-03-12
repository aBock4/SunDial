package com.sundial.v1001

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sundial.v1001.dto.Twilight
import com.sundial.v1001.ui.theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModel<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunDialTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background){
                    TwilightFacts("Android")
                }
            }
        }
    }
}


@Composable
fun TwilightFacts(name: String) {
    var sunrise by remember { mutableStateOf("")}
    var sunset by remember { mutableStateOf("")}
    val lexendFontFamily = FontFamily(
        Font(R.font.lexendregular, FontWeight.Normal),
        Font(R.font.lexendbold, FontWeight.Bold),
        Font(R.font.lexendblack, FontWeight.Black),
        Font(R.font.lexendlight, FontWeight.Light),
        Font(R.font.lexendmedium, FontWeight.Medium)
    )
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Champagne),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = sunrise,
                onValueChange = { sunrise = it },
                label = { Text(stringResource(R.string.sunrise), fontFamily = lexendFontFamily, fontWeight = FontWeight.Bold)}
            )
            OutlinedTextField(
                value = sunset,
                onValueChange = { sunset = it },
                label = { Text(stringResource(R.string.sunset), fontFamily = lexendFontFamily, fontWeight = FontWeight.Bold)},

                )
            Button(
                onClick = {
                    Toast.makeText(context, "$sunrise $sunset", Toast.LENGTH_LONG).show()
                }
            ){
                Text(text = "Save", color = Color.White, fontFamily = lexendFontFamily, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    val lexendFontFamily = FontFamily(Font(R.font.lexendbold, FontWeight.Bold))
    Surface(
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .background(color = Orange)
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "Search Location", fontFamily = lexendFontFamily, fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .fillMaxWidth()

            )
        }
    }
}


@Composable
fun LogInButton() {
    val lexendFontFamily = FontFamily(Font(R.font.lexendmedium, FontWeight.Medium))
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Button(onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Log in", fontFamily = lexendFontFamily, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SunDialTheme {
        TwilightFacts("Android")
        LogInButton()
        SearchBar()
    }
}