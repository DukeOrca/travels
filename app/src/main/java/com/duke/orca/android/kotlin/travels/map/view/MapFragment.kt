package com.duke.orca.android.kotlin.travels.map.view

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.application.API_KEY
import com.duke.orca.android.kotlin.travels.base.MainTabItemFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentMapBinding
import com.duke.orca.android.kotlin.travels.schedule.data.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.ui.IconGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class MapFragment: MainTabItemFragment<FragmentMapBinding>(), OnMapReadyCallback {
    private val locationWrapperQueue: Queue<LocationWrapper> = LinkedList()
    private var googleMap: GoogleMap? = null
    private var previousLocation: Location? = null

    private var lastLocationMarker: Marker? = null
    private val markers = arrayListOf<Marker>()
    private val polylineList = arrayListOf<Polyline>()

    private var dropLocation: Location? = null
    private var pickUpLocation: Location? = null
    private var tourLocation: Location? = null

    private var moveToLastLocation = false

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentMapBinding {
        return FragmentMapBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val supportMapFragment = this.childFragmentManager.findFragmentById(R.id.support_map_fragment)

        if (supportMapFragment is SupportMapFragment)
            supportMapFragment.getMapAsync(this)

        viewBinding.floatingActionButton.setOnClickListener {
            if (checkSelfPermissions()) {
                lastLocationMarker?.let {
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.position.latitude, it.position.longitude)))
                    googleMap?.animateCamera(CameraUpdateFactory.zoomTo(16F))
                } ?: run {
                    lastLocation {
                        it?.let {
                            locationWrapperQueue.add(LocationWrapper(Location(it.latitude, it.longitude), LocationType.My))
                            addMarkersPolyline()
                        }
                    }
                }
            } else {
                moveToLastLocation = true
                requestPermissions()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeLiveData()
    }

    override fun onResume() {
        super.onResume()

        viewModel.setViewPager2UserInputEnabled(false)
    }

    override fun onPause() {
        viewModel.setViewPager2UserInputEnabled(true)
        super.onPause()
    }

    override fun onDestroyView() {
        locationWrapperQueue.clear()
        super.onDestroyView()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        addMarkersPolyline()
    }

    @SuppressWarnings("MissingPermission")
    private fun checkSelfPermissions(): Boolean {
        val hasAccessCoarseLocationPermission = checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PERMISSION_GRANTED

        val hasAccessFineLocationPermission = checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PERMISSION_GRANTED

        return hasAccessCoarseLocationPermission && hasAccessFineLocationPermission
    }

    private fun requestPermissions() {
        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(viewBinding.root, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE).setAction("허용") {
                requestPermissions(permissions, RequestCode.permission)
            }.show()
        } else
            requestPermissions(permissions, RequestCode.permission)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RequestCode.permission) {
            val count = grantResults.filter { it == PERMISSION_GRANTED }.count()

            if (count == grantResults.count()) {
                lastLocation {
                    it?.let {
                        locationWrapperQueue.add(LocationWrapper(Location(it.latitude, it.longitude), LocationType.My))
                        addMarkersPolyline()

                        if (moveToLastLocation) {
                            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
                            googleMap?.animateCamera(CameraUpdateFactory.zoomTo(16F))
                            moveToLastLocation = false
                        }
                    }
                }
            }
        }
    }

    private fun initializeLiveData() {
        viewModel.mapSchedule.observe(viewLifecycleOwner, { schedule ->
            clearMap()

            tourLocation = schedule.tourLocation
            dropLocation = schedule.dropLocation
            pickUpLocation = schedule.pickUpLocation

            tourLocation?.let { locationWrapperQueue.add(LocationWrapper(it, LocationType.Tour)) }
            dropLocation?.let { locationWrapperQueue.add(LocationWrapper(it, LocationType.Drop)) }
            pickUpLocation?.let { locationWrapperQueue.add(LocationWrapper(it, LocationType.PickUp)) }

            if (checkSelfPermissions())
                lastLocation {
                    it?.let {
                        locationWrapperQueue.add(LocationWrapper(Location(it.latitude, it.longitude), LocationType.My))
                        addMarkersPolyline()
                    }
                }
            else
                requestPermissions()

            addMarkersPolyline()
        })
    }

    private fun addMarkersPolyline() {
        googleMap?.let { _ ->
            while (locationWrapperQueue.isNotEmpty()) {
                val locationWrapper = locationWrapperQueue.poll()
                val location = locationWrapper?.location
                val locationType = locationWrapper?.locationType ?: -1

                location?.let { it ->
                    addMarker(it, locationType)
                    previousLocation =
                            if (previousLocation == null)
                                it
                            else {
                                previousLocation?.let { previousLocation ->
                                    addPolyline(previousLocation, it)
                                }

                                it
                            }
                }
            }
        }
    }

    private fun addMarker(location: Location, locationType: Int) {
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions().apply {
            position(latLng)
        }

        when(locationType) {
            LocationType.Drop -> {
                val iconGenerator = IconGenerator(requireContext()).apply {
                    setColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    setTextAppearance(R.style.IconTextAppearance)
                }
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("2")))
            }
            LocationType.PickUp -> {
                val iconGenerator = IconGenerator(requireContext()).apply {
                    setColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    setTextAppearance(R.style.IconTextAppearance)
                }

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("1")))
            }
            LocationType.My -> {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_current_location_45)
                val bitmap = drawable?.toBitmap(96, 96)

                bitmap?.let {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it))
                } ?: run {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker())
                }

                lastLocationMarker = googleMap?.addMarker(markerOptions)
            }
            LocationType.Tour -> {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                googleMap?.animateCamera(CameraUpdateFactory.zoomTo(16F))
            }
        }

        if (LocationType.My != locationType)
            googleMap?.addMarker(markerOptions)?.let { markers.add(it) }
    }

    private fun addPolyline(origin: Location, destination: Location) {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            val paths = getPaths(origin, destination)

            if (paths.isEmpty())
                return@launchWhenResumed

            val polylineOptions = PolylineOptions()
                    .addAll(paths)
                    .color(ContextCompat.getColor(requireContext(), R.color.primary))
                    .width(5F)

            googleMap?.addPolyline(polylineOptions)?.let { polylineList.add(it) }
        }
    }

    private suspend fun getPaths(origin: Location, destination: Location) = withContext(Dispatchers.IO) {
        val paths = mutableListOf<LatLng>()

            val geoApiContext = GeoApiContext.Builder()
                    .apiKey(API_KEY)
                    .build()

            val directionsApiRequest = DirectionsApi.getDirections(
                    geoApiContext,
                    "${origin.latitude},${origin.longitude}",
                    "${destination.latitude},${destination.longitude}")

            try {
                val directionsResult = directionsApiRequest.await()
                        ?: throw NullPointerException("directionsResult: null")

                if (directionsResult.routes.isEmpty())
                    return@withContext paths

                val route = directionsResult.routes.first()
                val legs = route.legs ?: throw NullPointerException("legs: null")

                for (i in 0 until legs.count()) {
                    val directionsLeg = legs[i] ?: continue
                    val steps = directionsLeg.steps ?: continue

                    for (j in 0 until steps.count()) {
                        val directionsStep = steps[j]

                        if (directionsStep.steps.isNullOrEmpty()) {
                            directionsStep.polyline?.decodePath()?.forEach {
                                paths.add(LatLng(it.lat, it.lng))
                            }
                        } else {
                            for (k in 0 until directionsStep.steps.count()) {
                                val step = directionsStep.steps[k]
                                val polyline = step.polyline ?: continue

                                polyline.decodePath().forEach {
                                    paths.add(LatLng(it.lat, it.lng))
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }

        paths
    }

    private fun lastLocation(onLastLocation: (android.location.Location?) -> Unit) {
        val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())

        if (checkSelfPermissions()) {
            try {
                fusedLocationProviderClient.lastLocation
                        .addOnSuccessListener { onLastLocation(it) }
            } catch (e: SecurityException) {
                Timber.e(e)
            }
        }
    }

    private fun clearMap() {
        lastLocationMarker?.remove()
        markers.forEach {
            it.remove()
        }

        polylineList.forEach {
            it.remove()
        }
    }

    private object LocationType {
        const val Drop = 0
        const val PickUp = 1
        const val My = 2
        const val Tour = 3
    }

    private object RequestCode {
        const val permission = 1447
    }
}

data class LocationWrapper(
        val location: Location,
        val locationType: Int
)