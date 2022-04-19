package com.caruso.countries.list

import androidx.fragment.app.Fragment
import com.caruso.countries.core.view.viewBinding
import com.caruso.countries.list.databinding.CountryListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryListFragment : Fragment(R.layout.country_list_fragment) {

    private val binding: CountryListFragmentBinding by viewBinding(CountryListFragmentBinding::bind)
}