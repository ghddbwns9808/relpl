package com.gdd.presentation.point

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.viewModels
import com.gdd.presentation.R
import com.gdd.presentation.base.BaseFragment
import com.gdd.presentation.databinding.FragmentPointUseBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointUseFragment : BaseFragment<FragmentPointUseBinding>(
    FragmentPointUseBinding::bind, R.layout.fragment_point_use
) {
    private val pointUseViewModel: PointUseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DataBinding
        binding.lifecycleOwner = viewLifecycleOwner
        binding.pointUseViewModel = pointUseViewModel

    }

    fun registerObserve(){
        pointUseViewModel.userId.observe(viewLifecycleOwner){

        }
    }

    private fun createBarcode(content: String) {
        try {
            val widthPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 390f,
                resources.displayMetrics
            ).toInt()
            val heightPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 111f,
                resources.displayMetrics
            ).toInt()
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(
                "steelzoo",
                BarcodeFormat.CODE_128,
                widthPx,
                heightPx
            ).let { bitmap ->
                binding.ivPointBarcode.setImageBitmap(bitmap)
            }
        } catch (t: Throwable) {
            showToast("바코드 생성에 실패했습니다.")
        }
    }
}