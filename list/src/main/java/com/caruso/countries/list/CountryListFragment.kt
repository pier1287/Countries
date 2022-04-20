package com.caruso.countries.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.caruso.countries.core.extensions.castAdapterTo
import com.caruso.countries.core.view.viewBinding
import com.caruso.countries.list.adapter.CountryListAdapter
import com.caruso.countries.list.databinding.CountryListFragmentBinding
import com.caruso.countries.repository.Country
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountryListFragment : Fragment(R.layout.country_list_fragment) {

    private val viewModel: CountryListViewModel by viewModels()
    private val binding: CountryListFragmentBinding by viewBinding(CountryListFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCountriesRecyclerView()
        lifecycleScope.launch { observeViewModelState() }
    }

    private fun initCountriesRecyclerView() {
        binding.countriesRecyclerView.adapter = CountryListAdapter()
    }

    private suspend fun observeViewModelState() {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect {
                showList(it.countries)
            }
    }

    private fun showList(countries: List<Country>) {
        binding.countriesRecyclerView.castAdapterTo<CountryListAdapter>()
            .submitList(countries)
    }
}

