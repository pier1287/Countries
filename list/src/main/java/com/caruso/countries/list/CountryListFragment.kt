package com.caruso.countries.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.caruso.countries.core.view.viewBinding
import com.caruso.countries.list.databinding.CountryListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryListFragment : Fragment(R.layout.country_list_fragment) {

    private val viewModel: CountryListViewModel by viewModels()
    private val binding: CountryListFragmentBinding by viewBinding(CountryListFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val a = viewModel
    }
}