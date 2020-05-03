package br.com.victorhgo.movieapp.di

import br.com.victorhgo.movieapp.presentation.detail.DetailAdapter
import br.com.victorhgo.movieapp.presentation.detail.DetailViewModel
import br.com.victorhgo.movieapp.presentation.popular.PopularAdapter
import br.com.victorhgo.movieapp.presentation.popular.PopularViewModel
import br.com.victorhgo.movieapp.presentation.upcoming.UpcomingAdapter
import br.com.victorhgo.movieapp.presentation.upcoming.UpcomingViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    viewModel { UpcomingViewModel(get()) }
    viewModel { PopularViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    factory { UpcomingAdapter() }
    factory { PopularAdapter() }
    factory { DetailAdapter() }
    single { createMovieRepository(get()) }
}
