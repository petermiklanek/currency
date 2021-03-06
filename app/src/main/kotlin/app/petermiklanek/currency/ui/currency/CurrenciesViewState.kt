package app.petermiklanek.currency.ui.currency

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import app.futured.arkitekt.core.ViewState
import app.futured.arkitekt.core.livedata.DefaultValueLiveData
import app.futured.arkitekt.core.livedata.combineLiveData
import app.petermiklanek.currency.ui.common.ui.placeholder.PlaceholderLayoutState
import app.petermiklanek.currency.data.model.database.Currency
import app.petermiklanek.currency.data.model.database.FavouriteCurrency
import app.petermiklanek.currency.data.model.state.*
import app.petermiklanek.currency.data.model.ui.UICurrencyData
import app.petermiklanek.currency.tools.extensions.visible
import javax.inject.Inject

class CurrenciesViewState @Inject constructor() : ViewState {

    val currencies = MutableLiveData<List<Currency>>(listOf())

    val favouriteCurrencies = MutableLiveData<List<FavouriteCurrency>>(listOf())

    val search = MutableLiveData<String>("")

    val isRefreshing = DefaultValueLiveData(false)

    internal val state = DefaultValueLiveData<State<Unit>>(StateLoading)

    val placeholderLayoutState = state.map {
        when (it) {
            is StateContent -> PlaceholderLayoutState.CONTENT
            is StateLoading -> PlaceholderLayoutState.LOADING
            is StateEmpty -> PlaceholderLayoutState.EMPTY
            is StateError -> PlaceholderLayoutState.ERROR
            else -> error("Invalid state")
        }
    }

    val currenciesData = combineLiveData(currencies, favouriteCurrencies, search) { currencies, favouriteCurrencies, search ->
        val filteredCurrencies = if (search.isNotEmpty()) currencies.filter { it.code.contains(search, true) } else currencies

        val data = filteredCurrencies.map { currency ->
            val favouriteCurrencyCodes = favouriteCurrencies.map { it.currency_code }
            UICurrencyData(
                currency = currency,
                isFavourite = currency.code in favouriteCurrencyCodes
            )
        }

        state.value = if (currencies.isEmpty()) StateEmpty else StateContent(Unit)

        data
    }
}
