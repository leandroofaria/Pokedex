package com.example.pokedex.view

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.bd.DatabaseHelper
import com.example.pokedex.databinding.ActivityFavoritesBinding
import com.example.pokedex.bd.DatabaseHelper.Favorite
import com.example.pokedex.R


class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: FavoritesAdapter
    private var favoritesList = mutableListOf<Favorite>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        favoritesList = databaseHelper.getAllFavorites().toMutableList()

        adapter = FavoritesAdapter(favoritesList)
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter

        binding.btnAddFavorite.setOnClickListener {
            showAddFavoriteDialog()
        }
    }

    private fun showAddFavoriteDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_favorite, null)
        val etPokemonName = dialogLayout.findViewById<EditText>(R.id.etPokemonName)
        val etPokemonType = dialogLayout.findViewById<EditText>(R.id.etPokemonType)

        with(builder) {
            setTitle("Adicionar Novo Favorito")
            setPositiveButton("Adicionar") { dialog, which ->
                val name = etPokemonName.text.toString()
                val type = etPokemonType.text.toString()
                if (name.isNotEmpty() && type.isNotEmpty()) {
                    val id = databaseHelper.insertFavorite(name, type)
                    if (id != -1L) {
                        favoritesList.add(Favorite(id.toInt(), name, type))
                        adapter.notifyItemInserted(favoritesList.size - 1)
                    }
                }
            }
            setNegativeButton("Cancelar") { dialog, which ->
                // Do nothing
            }
            setView(dialogLayout)
            show()
        }
    }
}
