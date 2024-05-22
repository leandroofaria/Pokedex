package com.example.pokedex.bd

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2  // Updated version
        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val TABLE_FAVORITES = "favorites"  // New table for favorites
        private const val COLUMN_POKEMON_NAME = "pokemon_name"
        private const val COLUMN_POKEMON_TYPE = "pokemon_type"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT)")
        db?.execSQL(createTableQuery)

        val createFavoritesTableQuery = ("CREATE TABLE $TABLE_FAVORITES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_POKEMON_NAME TEXT, " +
                "$COLUMN_POKEMON_TYPE TEXT)")
        db?.execSQL(createFavoritesTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val createFavoritesTableQuery = ("CREATE TABLE $TABLE_FAVORITES (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_POKEMON_NAME TEXT, " +
                    "$COLUMN_POKEMON_TYPE TEXT)")
            db?.execSQL(createFavoritesTableQuery)
        }
    }

    fun insertUser(username: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun insertFavorite(pokemonName: String, pokemonType: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_POKEMON_NAME, pokemonName)
            put(COLUMN_POKEMON_TYPE, pokemonType)
        }
        val db = writableDatabase
        return db.insert(TABLE_FAVORITES, null, values)
    }

    fun getAllFavorites(): List<Favorite> {
        val db = readableDatabase
        val cursor = db.query(TABLE_FAVORITES, null, null, null, null, null, null)
        val favorites = mutableListOf<Favorite>()

        if (cursor.moveToFirst()) {
            do {
                val favorite = Favorite(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POKEMON_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POKEMON_TYPE))
                )
                favorites.add(favorite)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return favorites
    }

    fun getUserById(userId: Int): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD),
            "$COLUMN_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    data class Favorite(val id: Int, val name: String, val type: String)
    data class User(val id: Int, val username: String, val password: String)
}
