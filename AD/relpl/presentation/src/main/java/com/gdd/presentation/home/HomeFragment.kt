package com.gdd.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gdd.presentation.LoginActivity
import com.gdd.presentation.MainActivity
import com.gdd.presentation.MainViewModel
import com.gdd.presentation.R
import com.gdd.presentation.base.BaseFragment
import com.gdd.presentation.base.distanceFormat
import com.gdd.presentation.base.pointFormat
import com.gdd.presentation.databinding.FragmentHomeBinding
import com.gdd.presentation.profile.ProfileFragment
import com.gdd.presentation.rank.RankFragment
import com.gdd.presentation.report.ReportFragment
import com.gdd.presentation.relay.LoadRelayFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::bind, R.layout.fragment_home
) {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mainActivity: MainActivity

    private var backPressedTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = _activity as MainActivity

        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                   if (System.currentTimeMillis() - backPressedTime < 2000){
                       mainActivity.finish()
                   }else{
                       backPressedTime = System.currentTimeMillis()
                       showToast(resources.getString(R.string.all_finish_app))
                   }
                }
            })

        initView()
        registerObserver()
        registerListener()
    }

    private fun registerObserver(){
        mainViewModel.userInfoResult.observe(viewLifecycleOwner){

        }
    }

    private fun registerListener(){
        binding.profile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addSharedElement(binding.ivProfile,"profile_image")
                .replace(R.id.layout_main_fragment,ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.ivLogout.setOnClickListener {
            MaterialAlertDialogBuilder(_activity)
                .setMessage("로그아웃 하시겠습니까?")
                .setNegativeButton("취소") { dialog, which ->

                }
                .setPositiveButton("확인") { dialog, which ->
                    homeViewModel.logout()
                    startActivity(Intent(_activity,LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
                .show()
        }

        binding.reportCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_main_fragment,ReportFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.cvStartRelay.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_main_fragment, LoadRelayFragment())
                .addToBackStack("start_plogging")
                .commit()
        }

        binding.rankCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_main_fragment, RankFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initView(){
        binding.tvNickname.text = resources.getString(R.string.home_welcome, mainViewModel.user.nickname)
        binding.tvPoint.text = mainViewModel.user.totalCoin.pointFormat()
        binding.tvDistance.text = resources.getString(R.string.home_total_distance, mainViewModel.user.totalDistance.distanceFormat())

        if (mainViewModel.user.imageUri != null){
            Glide.with(this)
                .load(mainViewModel.user.imageUri)
                .fitCenter()
                .apply(RequestOptions().circleCrop())
                .into(binding.ivProfile)
        }
    }
}