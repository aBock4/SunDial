package com.sundial.v1001

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sundial.v1001.dto.LocationDetails
import com.sundial.v1001.dto.User
import com.sundial.v1001.ui.theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private val applicationViewModel : ApplicationViewModel by viewModel<ApplicationViewModel>()
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var user: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val location by applicationViewModel.getLocationLiveData().observeAsState()
            SunDialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TwilightFacts("Android", location)
                    LinkButton("https://support.google.com/maps/answer/18539?hl=en&co=GENIE.Platform%3DDesktop", "What to do with coordinates?")
                    //SearchBar()
                    LogInButton()
                }
            }
            prepLocationUpdates()
        }
    }

    private fun prepLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestSinglePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted ->
        if (isGranted){
            requestLocationUpdates()
        } else{
            Toast.makeText(this, "GPS Unavailable", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestLocationUpdates() {
        applicationViewModel.startLocationUpdates()
    }


    @Composable
    fun TwilightFacts(name: String, location: LocationDetails?) {
        var sunrise by remember { mutableStateOf("") }
        var sunset by remember { mutableStateOf("") }
        var locationName by remember{ mutableStateOf("") }
        val lexendFontFamily = FontFamily(
            Font(R.font.lexendregular, FontWeight.Normal),
            Font(R.font.lexendbold, FontWeight.Bold),
            Font(R.font.lexendblack, FontWeight.Black),
            Font(R.font.lexendlight, FontWeight.Light),
            Font(R.font.lexendmedium, FontWeight.Medium)
        )

        var currentLatitude = location?.latitude
        var currentLongitude = location?.longitude


        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    label = {
                        Text(
                            stringResource(R.string.sunrise),
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                OutlinedTextField(
                    value = sunset,
                    onValueChange = { sunset = it },
                    label = {
                        Text(
                            stringResource(R.string.sunset),
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    },

                    )
                Button(
                    onClick = {
                        Toast.makeText(context, "$sunrise $sunset", Toast.LENGTH_LONG).show()
                    }
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
                OutlinedTextField(
                    value = locationName,
                    onValueChange = { locationName = it },
                    label = {
                        Text(
                            text = "Name this Location",
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                GPS(location)
                Button(
                    onClick = {
                        //if(firebaseUser == null) {signIn()}
                            if (currentLongitude != null) {
                                if (currentLatitude != null) {
                                    val location = LocationDetails(currentLongitude,currentLatitude)
                                    viewModel.location = location
                                    viewModel.locationName = locationName
                                    viewModel.saveLocation()
                                }
                            }
                        //}

                    }
                ) {
                    Text(
                        text = "Save Location",
                        color = Color.White,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    private @Composable
    fun GPS(location: LocationDetails?) {
        location?.let {
            Text(text = location.latitude)
            Text(text = location.longitude)
        }
    }

    @Composable
    fun LinkButton(linkText: String, buttonText: String) {
        val context = LocalContext.current

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Button(
                onClick = {
                    val uri = Uri.parse(linkText)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = buttonText)
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
                    label = {
                        Text(
                            text = "Search Location",
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()

                )
            }
        }
    }


    @Composable
    fun LogInButton() {
        val lexendFontFamily = FontFamily(Font(R.font.lexendmedium, FontWeight.Medium))
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = { signIn() },
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
        val location by applicationViewModel.getLocationLiveData().observeAsState()
        SunDialTheme {
            TwilightFacts("Android", location)
            LogInButton()
            SearchBar()
        }
    }
    
    private fun signIn(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signinIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) {
            res -> this.signInResult(res)
    }
    private fun signInResult(result: FirebaseAuthUIAuthenticationResult){
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK){
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let{
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.saveUser()
            }
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }
    }
}