package com.luisenricke.helpyourself

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.luisenricke.helpyourself.service.LocationIntentService
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.android.controller.ServiceController
import org.robolectric.annotation.Config
import org.robolectric.shadows.*


// http://robolectric.org/javadoc/4.3/
// https://stackoverflow.com/questions/12025611/how-to-test-an-intentservice-with-robolectric
// https://stackoverflow.com/questions/30625378/shadownetworkinfo-is-always-type-mobile-when-testing-wifi-connectivity-with-robo
// https://stackoverflow.com/questions/27230186/change-android-connectivity-with-robolectric
// https://github.com/robolectric/robolectric/blob/master/robolectric/src/test/java/org/robolectric/shadows/ShadowWifiManagerTest.java
// https://stackoverflow.com/questions/32547006/connectivitymanager-getnetworkinfoint-deprecated
@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.P]
)
class LocationIntentServiceTest {

    lateinit var application: Application
    lateinit var activity: MainActivity

    lateinit var buildLocationService: ServiceController<LocationIntentService>
    lateinit var service: LocationIntentService

    @Before
    fun setup() {
        application = ApplicationProvider.getApplicationContext<Application>()
        activity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
            .get()

        buildLocationService = Robolectric.buildService(LocationIntentService::class.java)
        service = buildLocationService.create().get()
    }

    @After
    fun destroy() {
        buildLocationService.destroy()
    }

    @Test
    fun checkActivityNotNull() {
        Assert.assertNotNull(activity)
    }

    @Test
    fun checkLocationProviders() {
        val manager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val shadowLocationManager = Shadows.shadowOf(manager)

        shadowLocationManager.apply {
            this.setProviderEnabled(LocationManager.GPS_PROVIDER, true)
            this.setProviderEnabled(LocationManager.NETWORK_PROVIDER, true)
            this.setProviderEnabled(LocationManager.PASSIVE_PROVIDER, true)
        }

        Assert.assertEquals(true, manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        Assert.assertEquals(true, manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        Assert.assertEquals(true, manager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
    }

    @Test
    fun checkNetworks() {
//
//        val manager = application
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val wifi = application
//            .getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//
//        val shadowConnectivity = Shadows.shadowOf(manager)
//        val shadowNetwork: ShadowNetwork = Shadows.shadowOf(manager.activeNetwork)
//        val shadowCapabilities = ShadowNetworkCapabilities.newInstance()
//
//        val shadowWifi: ShadowWifiManager = Shadows.shadowOf(wifi)
//        wifi.isWifiEnabled = true
//        shadowWifi.en
//
//        val network: Network = ShadowNetwork.newInstance(shadowNetwork.netId)
//        val capabilities: NetworkCapabilities = manager.getNetworkCapabilities(network)
//            ?: NetworkCapabilities(shadowCapabilities)
//
//        Assert.assertTrue(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }

    @Test
    fun checkWifi() {

        val manager = application
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val shadowConnectivity = Shadows.shadowOf(manager)
        val shadowNetwork: ShadowNetwork = Shadows.shadowOf(manager.activeNetwork)
        val shadowCapabilities = ShadowNetworkCapabilities.newInstance()

        // Check connection
        Assert.assertTrue(manager.isActiveNetworkMetered)
        Assert.assertTrue(manager.isDefaultNetworkActive)

        val network: Network = ShadowNetwork.newInstance(shadowNetwork.netId)
        val capabilities: NetworkCapabilities = manager.getNetworkCapabilities(network)
            ?: NetworkCapabilities(shadowCapabilities)

        // Check objects
        Assert.assertNotNull(network)
        Assert.assertNotNull(capabilities)

        shadowConnectivity.setNetworkCapabilities(network, capabilities)
        //shadowConnectivity.reportedNetworkConnectivity.replace(network, false, true)


        // Check connection with shadows
        Assert.assertTrue(shadowConnectivity.reportedNetworkConnectivity.containsKey(network))


//        shadowConnectivity.reportedNetworkConnectivity.replace(network, false, true)
//

//        val testNetwork = manager.activeNetwork

//        Assert.assertTrue(
//                    shadowConnectivity.reportedNetworkConnectivity.containsKey(network)
//        )

        // Assertion now passes: Correctly returns TYPE_WIFI

//        val testCapabilities = manager.getNetworkCapabilities(shadowNetwork as Network)
        // Assertion now passes: Correctly returns TYPE_WIFI
//        Assert.assertEquals(
//            NetworkCapabilities.TRANSPORT_WIFI,
//            testCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//        )
    }

    @Throws(NullPointerException::class)
    @Test
    fun checkLocationIntentService() {

        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val shadowLocationManager = Shadows.shadowOf(locationManager)

        shadowLocationManager.apply {
            this.setProviderEnabled(LocationManager.GPS_PROVIDER, true)
        }

        val intent = Intent(RuntimeEnvironment.systemContext, LocationIntentService::class.java)
        buildLocationService.withIntent(intent).startCommand(0, 0)
        Assert.assertEquals(true, locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }
}
