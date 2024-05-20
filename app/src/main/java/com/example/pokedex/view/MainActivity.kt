package com.example.pokedex.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.api.PokemonRepository
import com.example.pokedex.domain.Pokemon
import com.example.pokedex.domain.PokemonType

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvPokemons)

        Thread(Runnable {
            loadPokemons(recyclerView)
        }).start()
    }

    private fun loadPokemons(recyclerView: RecyclerView) {
        val pokemonsApiResult = PokemonRepository.listPokemons()

        val pokemons: List<Pokemon?> = pokemonsApiResult?.results?.map { pokemonResult ->
            val number = pokemonResult.url
                .replace("https://pokeapi.co/api/v2/pokemon/", "")
                .replace("/", "").toInt()

            val pokemonApiResult = PokemonRepository.getPokemon(number)

            pokemonApiResult?.let { apiResult ->
                Pokemon(
                    apiResult.id,
                    apiResult.name,
                    apiResult.types.map { typeSlot ->
                        typeSlot.type
                    }
                )
            }
        }?.filterNotNull() ?: emptyList()

        val layoutManager = LinearLayoutManager(this)

        recyclerView.post {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = PokemonAdapter(pokemons)
        }
    }
}
