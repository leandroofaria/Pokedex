package com.example.pokedex.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.bd.DatabaseHelper
import com.example.pokedex.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val databaseHelper = DatabaseHelper(this)

        // Assuming you have a method to get the logged-in user's ID
        val loggedInUserId = 1  // Replace with actual logged-in user ID
        val user = databaseHelper.getUserById(loggedInUserId)

        if (user != null) {
            binding.tvUsername.text = "Usuário: ${user.username}"
            binding.tvPassword.text = "Senha: ${user.password}"
        } else {
            // Handle case where user is not found
            binding.tvUsername.text = "Usuário não encontrado"
            binding.tvPassword.text = ""
        }

        binding.btnFavorites.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }
}
