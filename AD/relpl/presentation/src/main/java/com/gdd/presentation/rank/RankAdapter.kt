package com.gdd.presentation.rank

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gdd.domain.model.rank.RankItem
import com.gdd.presentation.R
import com.gdd.presentation.databinding.ItemRankBinding

private const val TAG = "RankAdapter_Genseong"
class RankAdapter() : ListAdapter<RankItem, RankAdapter.RankViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        return RankViewHolder(
            ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.bind(currentList[position],position)
    }

    class RankViewHolder(val binding: ItemRankBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rankItem: RankItem, position: Int) {
            binding.tvRankNumber.apply {
                text = (position+1).toString()
                setRankNumberTextView(this,position+1)
            }
            binding.tvNickname.text = "${rankItem.nickname}님"
            binding.tvDistance.text = "${rankItem.distance}미터"
        }

        private fun setRankNumberTextView(textView: TextView, position: Int) {
            if (position <= 3) {
                Log.d(TAG, "setRankNumberTextView: $position")
                textView.setTextColor(binding.root.context.getColor(R.color.white))
                textView.background =
                    AppCompatResources.getDrawable(binding.root.context, R.drawable.ic_circle)
                textView.backgroundTintList = ColorStateList.valueOf(
                    binding.root.context.getColor(
                        when (position) {
                            1 -> {
                                R.color.gold
                            }

                            2 -> {
                                R.color.silver
                            }
                            3 -> {
                                R.color.copper
                            }
                            else -> {
                                R.color.black
                            }
                        }
                    )
                )
            } else {
                textView.background = null
                textView.setTextColor(binding.root.context.getColor(R.color.black))
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RankItem>() {
            override fun areItemsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
                return oldItem.nickname == newItem.nickname && oldItem.distance == newItem.distance
            }

            override fun areContentsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
                return oldItem.nickname == newItem.nickname && oldItem.distance == newItem.distance
            }
        }
    }
}