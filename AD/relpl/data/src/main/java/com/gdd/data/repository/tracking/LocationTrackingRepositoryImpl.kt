package com.gdd.data.repository.tracking

import android.util.Log
import com.gdd.data.mapper.toTrackData
import com.gdd.data.repository.tracking.local.LocationTrackingLocalDataSource
import com.gdd.domain.model.TrackingData
import com.gdd.domain.repository.LocationTrackingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "LocationTrackingReposit_Genseong"
class LocationTrackingRepositoryImpl @Inject constructor(
    private val locationTrackingLocalDataSource: LocationTrackingLocalDataSource
): LocationTrackingRepository {
    override suspend fun saveLocationTrackingData(
        milliTime: Long,
        latitude: Double,
        longitude: Double
    ) {
        locationTrackingLocalDataSource.saveLocationTrackingData(milliTime, latitude, longitude)
    }

    override fun getAllLocationTrackingData(): Flow<List<TrackingData>> {
        return locationTrackingLocalDataSource.getAllLocationTrackingData().map { list ->
            list.map {
                Log.d(TAG, "getAllLocationTrackingData: trackingStateFlow")
                it.toTrackData()
            }
        }
    }

    override suspend fun deleteAllLocationTrackingData() {
        locationTrackingLocalDataSource.deleteAllLocationTrackingData()
    }
}