package com.example.pokedex.view

import PokemonAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.api.PokemonRepository
import com.example.pokedex.domain.Pokemon

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var btnProfile: Button
    private lateinit var adapter: PokemonAdapter
    private var pokemons = emptyList<Pokemon?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvPokemons)
        searchView = findViewById(R.id.searchView)
        btnProfile = findViewById(R.id.btnProfile)

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        adapter = PokemonAdapter(pokemons)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPokemon(newText)
                return true
            }
        })

        if (pokemons.isEmpty()) {
            loadPokemons()
        }
    }

    private fun loadPokemons() {
        Thread {
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
                            pokemonApiResult.types.map { type ->
                                type.type
                            }
                        )
                    }
                }
            }

            runOnUiThread {
                adapter.setItems(pokemons)
            }
        }.start()
    }

    private fun filterPokemon(text: String?) {
        val filteredList = pokemons.filter { pokemon ->
            pokemon?.name?.contains(text.orEmpty(), ignoreCase = true) ?: false
        }
        adapter.setItems(filteredList)
    }
}
