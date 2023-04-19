package com.sundial.v1001

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
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
    private var selectedLocation by mutableStateOf(Location())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //viewModel.fetchTwilight()
            viewModel.fetchCities()
            val location by applicationViewModel.getLocationLiveData().observeAsState()
            val locations by viewModel.locations.observeAsState(initial = emptyList())
            val cities by viewModel.cities.observeAsState(initial = emptyList())
            SunDialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LocationFacts(location, locations, selectedLocation, cities)
                    LogInButton()
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
                .padding(24.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = locationText,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 8.dp),
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium
                )
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    locations.forEach { location ->
                        DropdownMenuItem(onClick = {
                            expanded = false

                            if (location.locationName == viewModel.NEW_LOCATION) {
                                locationText = ""
                                location.locationName = ""
                            } else {
                                locationText = location.toString()
                                selectedLocation = Location(
                                    longitude = "",
                                    latitude = "",
                                    locationName = location.locationName,
                                    locationId = location.locationId
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
            label = label
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
        Box(contentAlignment = Alignment.Center) {
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
                        fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp)
                    )
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.pin_drop),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 237.dp)
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
                onDismissRequest = onDismissRequest
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
        location: LocationDetails?,
        locations: List<Location> = ArrayList<Location>(),
        selectedLocation: Location = Location(),
        cities: List<City> = ArrayList<City>()
    ) {
        val currentLatitude = location?.latitude
        val currentLongitude = location?.longitude
        //var inLocation by remember(selectedLocation.locationId) { mutableStateOf(selectedLocation.locationName) }
        var inSunrise by remember(selectedLocation.locationId) { mutableStateOf(selectedLocation.sunrise) }
        var inSunset by remember(selectedLocation.locationId) { mutableStateOf(selectedLocation.sunset) }
        var inLongitude by remember(selectedLocation.longitude) { mutableStateOf(selectedLocation.longitude) }
        var inLatitude by remember(selectedLocation.latitude) { mutableStateOf(selectedLocation.latitude) }
        val context = LocalContext.current
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LocationSpinner(locations = locations)
            TextFieldWithDropdownUsage(dataIn = cities, stringResource(R.string.LocationName))
            OutlinedTextField(
                value = inSunrise,
                onValueChange = { inSunrise = it },
                label = {
                    Text(
                        stringResource(R.string.sunrise),
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp)
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
                label = {
                    Text(
                        stringResource(R.string.sunset),
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp)
                    )
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.baseline_bedtime),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 237.dp)
                    )
                },

                )
            /*OutlinedTextField(
                value = inLocation,
                onValueChange = { inLocation = it },
                label = {
                    Text(
                        stringResource(R.string.LocationName),
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp)
                    )
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.pin_drop),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 237.dp)
                    )
                }
            )*/
            OutlinedTextField(
                value = inLatitude,
                onValueChange = { inLatitude = it },
                label = {
                    Text(
                        stringResource(R.string.latitude),
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp)
                    )
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.baseline_public_24),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 237.dp)
                    )
                }
            )
            OutlinedTextField(
                value = inLongitude,
                onValueChange = { inLongitude = it },
                label = {
                    Text(
                        stringResource(R.string.longitude),
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, modifier = Modifier.padding(end = 10.dp)
                    )
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.baseline_public_24),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 237.dp)
                    )
                }
            )
            GPS(location)
            Button(
                onClick = {
                    selectedLocation.apply {
                        sunrise = inSunrise
                        sunset = inSunset
                        locationName = selectedCity?.let {
                            it.cityName
                        } ?: ""
                        inLocationName = locationName
                        if (currentLatitude != null) {
                            if (currentLongitude != null) {
                                latitude = currentLatitude
                                longitude = currentLongitude
                            }
                        }
                    }
                    viewModel.saveLocation()
                    Toast.makeText(
                        context,
                        "$inLocationName $currentLatitude $currentLongitude $inSunrise $inSunset",
                        Toast.LENGTH_LONG
                    ).show()
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


    @Composable
    private fun GPS(location: LocationDetails?) {
        location?.let {
            Text(
                text = "Current Latitude:   " + location.latitude,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Current Longitude:  " + location.longitude,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.Bold
            )
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
        val locations = ArrayList<Location>()
        locations.add(Location(locationName = "Home"))
        locations.add(Location(locationName = "Away"))
        locations.add(Location(locationName = "Work"))
        SunDialTheme {
            LocationFacts(location, locations)
            LogInButton()
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
                Toast.makeText(this, "Welcome, ${viewModel.user!!.displayName}", Toast.LENGTH_LONG).show()
            }
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }
    }
}