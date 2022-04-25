package com.caruso.countries.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import coil.load
import com.caruso.countries.core.view.viewBinding
import com.caruso.countries.core.widget.ErrorHandler
import com.caruso.countries.core.widget.gone
import com.caruso.countries.core.widget.visible
import com.caruso.countries.detail.databinding.CountryDetailFragmentBinding
import com.caruso.countries.domain.Country
import com.caruso.countries.domain.ErrorType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CountryDetailFragment : Fragment(R.layout.country_detail_fragment) {

    private val viewModel: CountryDetailViewModel by viewModels()
    private val binding: CountryDetailFragmentBinding
        by viewBinding(CountryDetailFragmentBinding::bind)

    @Inject
    lateinit var errorHandler: ErrorHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleScope.launch { observeViewModelState() }
        }
    }

    private suspend fun CountryDetailFragmentBinding.observeViewModelState() {
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { state ->
                state.country?.let { handleCountryDetail(it) }
                handleError(state.errors)
                handleLoader(state.isLoading)
            }
    }

    private fun CountryDetailFragmentBinding.handleError(errors: List<ErrorType>) {
        errors.firstOrNull()?.let {
            errorHandler.handleError(root, it)
            viewModel.onErrorMessageShown(it)
        }
    }

    private fun CountryDetailFragmentBinding.handleCountryDetail(country: Country) {
        val appCompatActivity = requireActivity() as? AppCompatActivity
        appCompatActivity?.supportActionBar?.title = country.name

        flagImageView.load(country.flagImageUrl)

        continentTextView.text = country.continent
        capitalTextView.text = country.capital
    }

    private fun CountryDetailFragmentBinding.handleLoader(isLoading: Boolean) {
        TransitionManager.beginDelayedTransition(root)
        if (isLoading) progressLoader.visible() else progressLoader.gone()
    }
}
