package app.petermiklanek.currency.ui.navigation

import app.futured.arkitekt.crusecases.BaseCrViewModel
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
    override val viewState: NavigationViewState
) : BaseCrViewModel<NavigationViewState>()
