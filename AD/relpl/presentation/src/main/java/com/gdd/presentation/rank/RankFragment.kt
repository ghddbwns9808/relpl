package com.gdd.presentation.rank

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gdd.domain.model.rank.Rank
import com.gdd.presentation.R
import com.gdd.presentation.base.BaseFragment
import com.gdd.presentation.databinding.FragmentRankBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "RankFragment_Genseong"
@AndroidEntryPoint
class RankFragment : BaseFragment<FragmentRankBinding>(
    FragmentRankBinding::bind, R.layout.fragment_rank
) {
    private val rankViewModel: RankViewModel by viewModels()
    private var rankAdapter: RankAdapter = RankAdapter()
    private var rank: Rank = Rank(listOf(),listOf(),listOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRankList.adapter = rankAdapter

        registerListener()
        registerObserve()
    }

    private fun registerListener(){
        binding.tlRankPeriod.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeRankList()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun registerObserve(){
        rankViewModel.rankResult.observe(viewLifecycleOwner){ result ->
            if (result.isSuccess){
                rank = result.getOrDefault(Rank(listOf(),listOf(),listOf()))
                changeRankList()
            } else {
                showSnackBar("랭킹 정보 호출에 실패했습니다.")
            }
        }
    }

    private fun changeRankList(){
        Log.d(TAG, "changeRankList: ${binding.tlRankPeriod.selectedTabPosition}")
        rankAdapter.submitList(
            when(binding.tlRankPeriod.selectedTabPosition){
                0 -> {
                    rank.dailyRanking
                }
                1 -> {
                    rank.weeklyRanking
                }
                2 -> {
                    rank.monthlyRanking
                }
                else -> {
                    listOf()
                }
            }
        )
    }

}