package app.petermiklanek.currency.ui.main

import app.futured.arkitekt.core.factory.BaseViewModelFactory
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class MainViewModelFactory @Inject constructor(
    override val viewModelProvider: Provider<MainViewModel>
) : BaseViewModelFactory<MainViewModel>() {
    override val viewModelClass: KClass<MainViewModel> = MainViewModel::class
}
