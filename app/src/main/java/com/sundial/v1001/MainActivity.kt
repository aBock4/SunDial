package com.sundial.v1001


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sundial.v1001.dto.Twilight
import com.sundial.v1001.ui.theme.SunDialTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchTwilight()
            val twilight by viewModel.twilight.observeAsState(initial = emptyList<Twilight>())
            SunDialTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    TwilightFacts()
                }
            }
        }
    }
}


@Composable
fun TwilightFacts() {
    var sunrise by remember { mutableStateOf(" ")}
    var sunset by remember { mutableStateOf("")}
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = sunrise,
                onValueChange = { sunrise = it },
                label = { Text(stringResource(R.string.sunrise)) }
            )
            OutlinedTextField(
                value = sunset,
                onValueChange = { sunset = it },
                label = { Text(stringResource(R.string.sunset)) }
            )
            Button(
                onClick = {
                    Toast.makeText(context, "$sunrise $sunset", Toast.LENGTH_LONG).show()
                }
            ){
                Text(text = "save")
            }
        }
    }
}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    Surface(
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "Search Location") },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
fun LogInButton() {
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Button(onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Log in")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SunDialTheme {
        TwilightFacts()
        LogInButton()
        SearchBar()
    }
}