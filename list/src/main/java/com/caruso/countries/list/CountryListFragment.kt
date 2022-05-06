@file:OptIn(ExperimentalMaterialApi::class)

package com.caruso.countries.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.caruso.countries.core.widget.ErrorHandler
import com.caruso.countries.domain.Country
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CountryListFragment : Fragment() {

    private val viewModel: CountryListViewModel by viewModels()

    @Inject
    lateinit var errorHandler: ErrorHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                CountryListPage(viewModel = viewModel)
            }
        }
    }

    @Composable
    private fun CountryListPage(viewModel: CountryListViewModel) {
        val state by viewModel.uiState.collectAsState()
        CountryList(countries = state.countries, onClick = {
            val request = NavDeepLinkRequest.Builder
                .fromUri("com.caruso.countries://countries/${it.id}".toUri())
                .build()
            findNavController().navigate(request)
        })
    }
}

@Composable
private fun CountryList(countries: List<Country>, onClick: (Country) -> Unit = {}) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = countries, key = { it.id }) {
            CountryItemCard(country = it, onClick = onClick)
        }
    }
}

@Composable
fun CountryItem(country: Country) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = country.name, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(16.dp))
        AsyncImage(
            model = country.flagImageUrl,
            contentDescription = "Country flag",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
        )
    }
}

@Composable
fun CountryItemCard(country: Country, onClick: (Country) -> Unit = {}) {
    Card(onClick = { onClick(country) }, elevation = 8.dp) {
        CountryItem(country = country)
    }
}

@Preview
@Composable
private fun CountryItemCardPreview() {
    CountryItemCard(country = Country("", "Italy", ""))
}

@Preview
@Composable
private fun CountryListPreview() {
    val country = Country("", "Italy", "")
    MaterialTheme {
        CountryList((1..5).map { country })
    }
}
