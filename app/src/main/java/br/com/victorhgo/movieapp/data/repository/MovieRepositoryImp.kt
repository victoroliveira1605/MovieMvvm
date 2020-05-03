package br.com.victorhgo.movieapp.data.repository

import br.com.victorhgo.movieapp.data.model.CastResponse
import br.com.victorhgo.movieapp.data.model.DetailResponse
import br.com.victorhgo.movieapp.data.model.PopularResponse
import br.com.victorhgo.movieapp.data.model.UpcomingResponse
import br.com.victorhgo.movieapp.data.remote.MovieService
import br.com.victorhgo.movieapp.domain.MovieRepository

class MovieRepositoryImp(
    private val apiService: MovieService
) : MovieRepository {
    override suspend fun getUpcoming(page: Int, language: String): UpcomingResponse {
        return apiService.getUpcoming(page, language)
    }

    override suspend fun getPopular(page: Int, language: String): PopularResponse {
        return apiService.getPopular(page, language)
    }

    override suspend fun getCast(id: Int): CastResponse {
        return apiService.getCast(id)
    }

    override suspend fun getDetail(id: Int): DetailResponse {
        return apiService.getDetail(id)
    }
}