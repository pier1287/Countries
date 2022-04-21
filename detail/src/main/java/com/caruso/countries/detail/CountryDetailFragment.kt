package com.caruso.countries.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import coil.load
import com.caruso.countries.core.view.viewBinding
import com.caruso.countries.core.widget.gone
import com.caruso.countries.core.widget.visible
import com.caruso.countries.detail.databinding.CountryDetailFragmentBinding
import com.caruso.countries.repository.Country
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountryDetailFragment : Fragment(R.layout.country_detail_fragment) {

    private val viewModel: CountryDetailViewModel by viewModels()
    private val binding: CountryDetailFragmentBinding
            by viewBinding(CountryDetailFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleScope.launch { observeViewModelState() }
        }
    }

    private suspend fun CountryDetailFragmentBinding.observeViewModelState() {
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { state ->
                when (state) {
                    CountryDetailState.Loading -> handleLoader(true)
                    is CountryDetailState.Error -> TODO()
                    is CountryDetailState.Success -> showContent(state.country)
                }
            }
    }

    private fun CountryDetailFragmentBinding.showContent(country: Country) {
        flagImageView.load(country.flagImageUrl)
    }

    private fun CountryDetailFragmentBinding.handleLoader(isLoading: Boolean) {
        TransitionManager.beginDelayedTransition(root)
        if (isLoading) progressLoader.visible() else progressLoader.gone()
    }
}
