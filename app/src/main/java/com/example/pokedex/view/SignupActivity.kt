package com.example.pokedex.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pokedex.R
import com.example.pokedex.bd.DatabaseHelper
import com.example.pokedex.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()
            signupDatabase(signupUsername, signupPassword)
        }

        binding.loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivty::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signupDatabase(username: String, password: String) {
        val insertRowId = databaseHelper.insertUser(username, password)
        if (insertRowId != -1L) {
            Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivty::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Cadastro falhou", Toast.LENGTH_SHORT).show()
        }
    }


}