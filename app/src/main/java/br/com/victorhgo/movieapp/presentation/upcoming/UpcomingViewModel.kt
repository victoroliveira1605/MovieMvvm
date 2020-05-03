package br.com.victorhgo.movieapp.presentation.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.victorhgo.movieapp.data.model.Movie
import br.com.victorhgo.movieapp.domain.MovieRepository
import br.com.victorhgo.movieapp.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class UpcomingViewModel(private val upcomingRepository: MovieRepository) : BaseViewModel() {
    var page: Int = 1
    private var listMovieHelper: MutableList<Movie> = mutableListOf()

    val upcomingList: MutableLiveData<MutableList<Movie>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorConnection: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun getUpcoming() {
        isLoading.value = true
        scope.launch {
            try {
                val response = upcomingRepository.getUpcoming(
                    page,
                    Locale.getDefault().language + "-" + Locale.getDefault().country
                )
                listMovieHelper.addAll(response.results)
                upcomingList.value = listMovieHelper
                errorConnection.value = false
            } catch (e: Exception) {
                errorMessage.value = getError(e).getErrorMessage()
                errorConnection.value = true
            }
            isLoading.value = false
        }
    }
}
