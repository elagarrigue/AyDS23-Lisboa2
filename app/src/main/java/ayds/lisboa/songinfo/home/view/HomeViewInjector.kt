package ayds.lisboa.songinfo.home.view

import CalculatorFactoryImpl
import ayds.lisboa.songinfo.home.controller.HomeControllerInjector
import ayds.lisboa.songinfo.home.model.HomeModelInjector

object HomeViewInjector {

    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(CalculatorFactoryImpl())

    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}