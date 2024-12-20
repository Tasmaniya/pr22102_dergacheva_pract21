package com.example.pr22102_dergacheva_pract21

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {
    companion object {
        private const val DATABASE_NAME = "userstore.db"
        private const val SCHEMA = 1
        const val TABLE = "users"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_YEAR = "year"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, $COLUMN_YEAR INTEGER);"
        )
        db.execSQL("INSERT INTO $TABLE ($COLUMN_NAME, $COLUMN_YEAR) VALUES ('Том Смит', 1981);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun insertUser(name: String, year: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_YEAR, year)
        }
        return db.insert(TABLE, null, values)
    }

    fun updateUser(id: Long, name: String, year: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_YEAR, year)
        }
        db.update(TABLE, values, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    fun deleteUser(id: Long) {
        val db = this.writableDatabase
        db.delete(TABLE, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    fun getUserById(id: Long): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE WHERE $COLUMN_ID=?", arrayOf(id.toString()))
    }

    fun getAllUsers(): List<Pair<Long, Pair<String, Int>>> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE, arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_YEAR), null, null, null, null, null)
        val users = mutableListOf<Pair<Long, Pair<String, Int>>>()
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val year = it.getInt(it.getColumnIndexOrThrow(COLUMN_YEAR))
                users.add(Pair(id, Pair(name, year)))
            }
        }
        return users
    }
}