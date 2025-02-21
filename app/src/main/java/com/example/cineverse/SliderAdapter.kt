package com.example.cineverse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.cineverse.databinding.ItemSliderBinding
import com.squareup.picasso.Picasso

class SliderAdapter(
    private val items: ArrayList<SliderItems>,
    private val viewPager: ViewPager2
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(private val binding: ItemSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SliderItems) {
            Picasso.get().load(item.imageUrl).into(binding.imageViewSlider)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
