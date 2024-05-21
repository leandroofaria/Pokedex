package com.example.pokedex.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.api.PokemonRepository
import com.example.pokedex.domain.Pokemon

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var pokemons = emptyList<Pokemon?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvPokemons)

        if (pokemons.isEmpty()) {
            Thread(Runnable {
                loadPokemons()
            }).start()
        } else {
            loadRecyclerView()
        }
    }

    private fun loadPokemons() {
        val pokemonsApiResult = PokemonRepository.listPokemons()

        pokemonsApiResult?.results?.let {
            pokemons = it.map { pokemonResult ->
                val number = pokemonResult.url
                    .replace("https://pokeapi.co/api/v2/pokemon/", "")
                    .replace("/", "").toInt()

                val pokemonApiResult = PokemonRepository.getPokemon(number)

                pokemonApiResult?.let {
                    Pokemon(
                        pokemonApiResult.id,
                        pokemonApiResult.name,
                        pokemonApiResult.types.map { type->
                            type.type
                        }
                    )
                }
            }
        }

        runOnUiThread {
            loadRecyclerView()
        }
    }

    private fun loadRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Set the number of columns to 2
        recyclerView.adapter = PokemonAdapter(pokemons)
    }
}
