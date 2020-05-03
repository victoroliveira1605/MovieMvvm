package br.com.victorhgo.movieapp.domain

import br.com.victorhgo.movieapp.data.model.CastResponse
import br.com.victorhgo.movieapp.data.model.DetailResponse
import br.com.victorhgo.movieapp.data.model.PopularResponse
import br.com.victorhgo.movieapp.data.model.UpcomingResponse

interface MovieRepository {
    suspend fun getUpcoming(page: Int,language:String): UpcomingResponse
    suspend fun getPopular(page: Int,language:String): PopularResponse
    suspend fun getCast(id: Int): CastResponse
    suspend fun getDetail(id: Int): DetailResponse
}