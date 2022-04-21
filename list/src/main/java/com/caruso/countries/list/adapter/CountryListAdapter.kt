package com.caruso.countries.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.caruso.countries.list.databinding.CountryListItemBinding
import com.caruso.countries.repository.Country

class CountryListAdapter(private val onItemClick: (Country) -> Unit) :
    ListAdapter<Country, CountryListAdapter.ViewHolder>(CountriesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CountryListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder) {
            this.item = item
            nameTextView.text = item.name
            flagImageView.load(item.flagImageUrl)
        }
    }

    class ViewHolder(binding: CountryListItemBinding, onItemClick: (Country) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        lateinit var item: Country
        val nameTextView = binding.nameTextView
        val flagImageView = binding.flagImageView

        init {
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }
}

class CountriesDiffUtil : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean =
        oldItem.name == newItem.name

}