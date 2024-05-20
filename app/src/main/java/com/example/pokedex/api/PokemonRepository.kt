package com.example.pokedex.api

import android.util.Log
import com.example.pokedex.api.model.PokemonsApiResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonRepository {
    private val service: PokemonService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")  // Corrigido a URL base
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(PokemonService::class.java)
    }

    fun getPokemon(number: Int): PokemonsApiResult? {
        val call = service.listPokemons(number)

        return call.execute().body()
//
    }
}
