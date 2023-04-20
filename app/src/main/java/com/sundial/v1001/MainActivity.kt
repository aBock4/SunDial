package com.sundial.v1001

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sundial.v1001.dto.City
import com.sundial.v1001.dto.Location
import com.sundial.v1001.dto.LocationDetails
import com.sundial.v1001.dto.User
import com.sundial.v1001.ui.theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private val applicationViewModel: ApplicationViewModel by viewModel<ApplicationViewModel>()
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var selectedCity: City? = null
    private var inLocationName: String = ""
    private val lexendFontFamily = FontFamily(
        Font(R.font.lexendregular, FontWeight.Normal),
        Font(R.font.lexendbold, FontWeight.Bold),
        Font(R.font.lexendblack, FontWeight.Black),
        Font(R.font.lexendlight, FontWeight.Light),
        Font(R.font.lexendmedium, FontWeight.Medium)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchCities()
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToLocations()
            }

            val cities by viewModel.cities.observeAsState(initial = emptyList())
            val locations by viewModel.locations.observeAsState(initial = emptyList())
            val location by applicationViewModel.getLocationLiveData().observeAsState()
            SunDialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LocationFacts(cities, locations, viewModel.selectedLocation, location)
                    LinkButton("https://support.google.com/maps/answer/18539?hl=en&co=GENIE.Platform%3DDesktop", "How do I use Coordinates?")
                    LogInButton()
                    WeatherAppButton()
                }
            }
            prepLocationUpdates()
        }
    }

    private fun prepLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED
        ) {
            requestLocationUpdates()
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestSinglePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                requestLocationUpdates()
            } else {
                Toast.makeText(this, "GPS Unavailable", Toast.LENGTH_LONG).show()
            }
        }

    private fun requestLocationUpdates() {
        applicationViewModel.startLocationUpdates()
    }

    @Composable
    fun LocationSpinner(locations: List<Location>) {
        var locationText by remember { mutableStateOf("Location Collection") }
        var expanded by remember { mutableStateOf(false) }
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(Modifier
                .padding(10.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = locationText,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 8.dp),
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Bold
                )
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    locations.forEach { location ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            if (location.locationName == viewModel.newLocation) {
                                // New Location
                                locationText = viewModel.newLocation
                                location.locationName = ""
                            } else {
                                // Existing Location
                                locationText = location.toString()
                                selectedCity = City(
                                    country = "",
                                    cityName = location.locationName,
                                    id = location.cityId
                                )
                                inLocationName = location.locationName
                            }
                            viewModel.selectedLocation = location
                        }) {
                            Text(
                                text = location.toString(),
                                fontFamily = lexendFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(
        dataIn: List<City>,
        label: String = "",
        take: Int = 3,
        selectedLocation: Location = Location()
    ) {

        val dropDownOptions = remember { mutableStateOf(listOf<City>()) }
        val textFieldValue =
            remember(selectedLocation.locationId) { mutableStateOf(TextFieldValue(selectedLocation.locationName)) }
        val dropDownExpanded = remember { mutableStateOf(false) }

        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            inLocationName = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = dataIn.filter {
                it.toString().startsWith(value.text) && it.toString() != value.text
            }.take(take)
        }

        TextFieldWithDropdown(
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = label,
        )
    }

    @Composable
    fun TextFieldWithDropdown(
        value: TextFieldValue,
        setValue: (TextFieldValue) -> Unit,
        onDismissRequest: () -> Unit,
        dropDownExpanded: Boolean,
        list: List<City>,
        label: String = ""
    ) {
        Box(modifier = Modifier.padding(top = 5.dp)) {
            OutlinedTextField(
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                onValueChange = setValue,
                label = {
                    Text(
                        label,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.pin_drop),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 235.dp)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest,
                modifier = Modifier.background(colorResource(R.color.Gray))
            ) {
                list.forEach { text ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                text.toString(),
                                TextRange(text.toString().length)
                            )
                        )
                        selectedCity = text
                    }) {
                        Text(text = text.toString())
                    }
                }
            }
        }
    }

    @Composable
    fun LocationFacts(
        cities: List<City> = ArrayList<City>(),
        locations: List<Location> = ArrayList<Location>(),
        selectedLocation: Location = Location(),
        currentLocation: LocationDetails?
    ) {
        var inSunrise by remember(selectedLocation.locationId) { mutableStateOf(selectedLocation.sunrise) }
        var inSunset by remember(selectedLocation.locationId) { mutableStateOf(selectedLocation.sunset) }
        var inLongitude by remember(selectedLocation.longitude) { mutableStateOf(selectedLocation.longitude) }
        var inLatitude by remember(selectedLocation.latitude) { mutableStateOf(selectedLocation.latitude) }
        val context = LocalContext.current
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LocationSpinner(locations = locations)
                GPS(currentLocation)
                TextFieldWithDropdownUsage(
                    dataIn = cities,
                    stringResource(R.string.LocationName),
                    selectedLocation = selectedLocation
                )
                OutlinedTextField(
                    value = inSunrise,
                    modifier = Modifier.padding(top = 10.dp),
                    onValueChange = { inSunrise = it },
                    label = {
                        Text(
                            stringResource(R.string.sunrise),
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.baseline_wb_sunny),
                            contentDescription = "",
                            modifier = Modifier.padding(start = 235.dp)
                        )
                    }
                )
                OutlinedTextField(
                    value = inSunset,
                    onValueChange = { inSunset = it },
                    modifier = Modifier.padding(top = 10.dp),
                    label = {
                        Text(
                            stringResource(R.string.sunset),
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.baseline_bedtime),
                            contentDescription = "",
                            modifier = Modifier.padding(start = 235.dp)
                        )
                    }
                )
                OutlinedTextField(
                    value = inLatitude,
                    onValueChange = { inLatitude = it },
                    modifier = Modifier.padding(top = 10.dp),
                    label = {
                        Text(
                            stringResource(R.string.latitude),
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.baseline_public_24),
                            contentDescription = "",
                            modifier = Modifier.padding(start = 235.dp)
                        )
                    }
                )
                OutlinedTextField(
                    value = inLongitude,
                    onValueChange = { inLongitude = it },
                    modifier = Modifier.padding(top = 10.dp),
                    label = {
                        Text(
                            stringResource(R.string.longitude),
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.baseline_public_24),
                            contentDescription = "",
                            modifier = Modifier.padding(start = 235.dp)
                        )
                    }
                )
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                    Column(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .padding(top = 10.dp)
                    ) {
                        Button(
                            onClick = {
                                // Check to see if we have a valid user
                                if (firebaseUser != null && firebaseUser.toString().isNotEmpty())
                                {
                                    selectedLocation.apply {
                                        sunrise = inSunrise
                                        sunset = inSunset
                                        cityId = selectedCity?.id ?: 0
                                        locationName = inLocationName
                                        longitude = inLongitude
                                        latitude = inLatitude
                                    }
                                    viewModel.saveLocation()
                                    Toast.makeText(
                                        context,
                                        "$inLocationName $inSunrise $inSunset",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else
                                {
                                    Toast.makeText(context, "Please Log In to Save a Location", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text(
                                text = "Save Location",
                                color = Color.White,
                                fontFamily = lexendFontFamily,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Icon(
                                ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                                contentDescription = "Add",
                            )
                        }
                    }
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Button(
                            onClick = {
                                if (firebaseUser != null && firebaseUser.toString().isNotEmpty())
                                {
                                    if(selectedLocation.locationName.isEmpty() || selectedLocation.locationName == ""){
                                        Toast.makeText(context, "You cannot delete an unsaved location. Please save the location and try again.", Toast.LENGTH_LONG).show()
                                    } else
                                    {
                                        viewModel.deleteLocation(selectedLocation)
                                    }
                                }
                                else
                                {
                                    Toast.makeText(context, "Please Log In to Delete a Location", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text(
                                text = "Delete Location",
                                color = Color.White,
                                fontFamily = lexendFontFamily,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
    @Composable
    fun WeatherAppButton() {
        val context = LocalContext.current

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Button(
                onClick = {
                    val intent =
                        context.packageManager.getLaunchIntentForPackage("com.example.weatherapp")
                    if (intent != null) {
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Weather app not found", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Open Weather App")
            }
        }
    }


    private @Composable
    fun GPS(location: LocationDetails?) {
        location?.let {
            Column(modifier = Modifier.padding(bottom = 5.dp)) {
                Text(
                    text = "Current Latitude:   " + location.latitude,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
            Column(modifier = Modifier.padding(bottom = 5.dp)) {
                Text(
                    text = "Current Longitude:  " + location.longitude,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
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
    fun LogInButton() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = { signIn() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            ) {
                Text(
                    text = "Log in",
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

            }
        }
    }


    @Preview(name = "Light Mode", showBackground = true)
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
    @Composable
    fun DefaultPreview() {
        val cities by viewModel.cities.observeAsState(initial = emptyList())
        val location by applicationViewModel.getLocationLiveData().observeAsState()
        val locations = ArrayList<Location>()
        locations.add(Location(locationName = "Home"))
        locations.add(Location(locationName = "Away"))
        locations.add(Location(locationName = "Work"))
        SunDialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                LocationFacts(cities, locations, viewModel.selectedLocation, location)
                LogInButton()
            }
        }
    }

    private fun signIn() {
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
    ) { res ->
        this.signInResult(res)
    }

    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.saveUser()
                Toast.makeText(this, "Welcome, ${viewModel.user!!.displayName}", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }
    }
}