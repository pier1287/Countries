package com.caruso.countries.list

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.caruso.countries.core.extensions.castAdapterTo
import com.caruso.countries.core.extensions.gone
import com.caruso.countries.core.extensions.visible
import com.caruso.countries.core.view.viewBinding
import com.caruso.countries.core.widget.ErrorHandler
import com.caruso.countries.domain.Country
import com.caruso.countries.domain.ErrorType
import com.caruso.countries.list.adapter.CountryListAdapter
import com.caruso.countries.list.databinding.CountryListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CountryListFragment : Fragment(R.layout.country_list_fragment) {

    private val viewModel: CountryListViewModel by viewModels()
    private val binding: CountryListFragmentBinding by viewBinding(CountryListFragmentBinding::bind)

    @Inject
    lateinit var errorHandler: ErrorHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            initCountriesRecyclerView()
            lifecycleScope.launch { observeViewModelState() }
        }
    }

    private fun CountryListFragmentBinding.initCountriesRecyclerView() {
        countriesRecyclerView.adapter = CountryListAdapter { country ->
            val request = NavDeepLinkRequest.Builder
                .fromUri("com.caruso.countries://countries/${country.id}".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    private suspend fun CountryListFragmentBinding.observeViewModelState() {
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { state ->
                handleCountries(state.countries)
                handleError(state.errors)
                handleLoader(state.isLoading)
            }
    }

    private fun CountryListFragmentBinding.handleError(errors: List<ErrorType>) {
        errors.firstOrNull()?.let {
            errorHandler.handleError(root, it)
            viewModel.onErrorMessageShown(it)
        }
    }

    private fun CountryListFragmentBinding.handleLoader(isLoading: Boolean) {
        TransitionManager.beginDelayedTransition(root)
        if (isLoading) {
            countriesRecyclerView.gone()
            progress.visible()
        } else {
            progress.gone()
            countriesRecyclerView.visible()
        }
    }

    private fun CountryListFragmentBinding.handleCountries(countries: List<Country>) {
        countriesRecyclerView.castAdapterTo<CountryListAdapter>().submitList(countries)
    }
}
