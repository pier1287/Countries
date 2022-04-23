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
import com.caruso.countries.core.view.viewBinding
import com.caruso.countries.core.widget.ErrorHandler
import com.caruso.countries.core.widget.gone
import com.caruso.countries.core.widget.visible
import com.caruso.countries.list.adapter.CountryListAdapter
import com.caruso.countries.list.databinding.CountryListFragmentBinding
import com.caruso.countries.repository.Country
import com.caruso.countries.repository.ErrorType
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
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { state ->
                when (state) {
                    is CountryListState.Error -> handleError(state.errorType)
                    CountryListState.Loading -> handleLoader(true)
                    is CountryListState.Success -> handleSuccess(state.countries)
                }
            }
    }

    private fun CountryListFragmentBinding.handleError(errorType: ErrorType) {
        errorHandler.handleError(root, errorType)
        handleLoader(false)
    }

    private fun CountryListFragmentBinding.handleLoader(isLoading: Boolean) {
        TransitionManager.beginDelayedTransition(root)
        if (isLoading) progressLoader.visible() else progressLoader.gone()
    }

    private fun CountryListFragmentBinding.handleSuccess(countries: List<Country>) {
        countriesRecyclerView.castAdapterTo<CountryListAdapter>().submitList(countries)
        handleLoader(false)
    }
}
