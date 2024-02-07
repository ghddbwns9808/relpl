package com.gdd.presentation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gdd.domain.model.Point
import com.gdd.presentation.base.BaseFragment
import com.gdd.presentation.base.location.LocationProviderController
import com.gdd.presentation.databinding.FragmentTestBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val TAG = "TestFragment_Genseong"

class TestFragment : BaseFragment<FragmentTestBinding>(
    FragmentTestBinding::bind, R.layout.fragment_test
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var naverMap: NaverMap
    private lateinit var locationProviderController: LocationProviderController

    private var markerOne: Marker = Marker()
    private var markerTwo: Marker = Marker()
    private var myPosition: Marker = Marker()
    private var circleOverlay: CircleOverlay = CircleOverlay()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = _activity as MainActivity
        locationProviderController = LocationProviderController(mainActivity, viewLifecycleOwner)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(mapReadyCallback)

        registerListener()
    }

    @SuppressLint("MissingPermission")
    private val mapReadyCallback = OnMapReadyCallback { map ->
        naverMap = map
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
        naverMap.uiSettings.apply {
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = false
        }
        binding.fabCurLocation.performClick()
    }

    private fun registerListener() {
        binding.fabCurLocation.setOnClickListener {
            binding.fabCurLocation.isEnabled = false
            locationProviderController.getCurrnetLocation { task ->
                if (!task.isCanceled) {
                    if (task.isSuccessful) {
                        task.result.also {
                            val latLng = LatLng(it)
                            naverMap.moveCamera(
                                CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Easing)
                                    .finishCallback {
                                        naverMap.moveCamera(
                                            CameraUpdate.zoomTo(16.0)
                                                .animate(CameraAnimation.Easing)
                                        )
                                    }
                            )
                            naverMap.locationOverlay.isVisible = true
                            naverMap.locationOverlay.position = latLng
                        }
                    } else {
                        showSnackBar("위치정보 호출에 실패했습니다.")
                    }
                    binding.fabCurLocation.isEnabled = true
                }
            }
        }

        binding.btnMarkerOne.setOnClickListener {

            markerOne.apply {
                map = null
                position = LatLng(
                    naverMap.cameraPosition.target.latitude,
                    naverMap.cameraPosition.target.longitude
                )
                map = naverMap
                icon = OverlayImage.fromResource(R.drawable.ic_marker)
                iconTintColor = resources.getColor(R.color.sage_blue)
                captionText = "1번 마커"

            }

        }

        binding.btnMarkerTwo.setOnClickListener {

            markerTwo.apply {
                map = null
                position = LatLng(
                    naverMap.cameraPosition.target.latitude,
                    naverMap.cameraPosition.target.longitude
                )
                map = naverMap
                icon = OverlayImage.fromResource(R.drawable.ic_marker)
                iconTintColor = resources.getColor(R.color.sage_blue)
                captionText = "2번 마커"
            }


        }

        binding.btnMyPosition.setOnClickListener {

            myPosition.apply {
                map = null
                position = LatLng(
                    naverMap.cameraPosition.target.latitude,
                    naverMap.cameraPosition.target.longitude
                )
                map = naverMap
                icon = OverlayImage.fromResource(R.drawable.ic_marker)
                iconTintColor = resources.getColor(R.color.sage_orange)
                captionText = "내위치"
            }
            circleOverlay.apply {
                map = null
                center = LatLng(
                    naverMap.cameraPosition.target.latitude,
                    naverMap.cameraPosition.target.longitude
                )
                color = resources.getColor(R.color.semi_transparent_black)
                radius = 50.0
                map = naverMap
            }

        }

        binding.btnCalc.setOnClickListener {
            val d =calculateDistanceToLine(
                LatLng(markerOne.position.latitude, markerOne.position.longitude),
                LatLng(markerTwo.position.latitude, markerTwo.position.longitude),
                LatLng(myPosition.position.latitude, myPosition.position.longitude)
            )

            showToast(d.toString())
        }
    }

    fun calculateDistanceToLine(marker1: LatLng, marker2: LatLng, myPosition: LatLng): Double {
        val a = marker1.distanceTo(myPosition)
        val b = marker2.distanceTo(myPosition)
        val c = marker1.distanceTo(marker2)
        val s = (a+b+c) / 2.0

        return (2* sqrt(s*(s-a)*(s-b)*(s-c))) / c
    }


}
