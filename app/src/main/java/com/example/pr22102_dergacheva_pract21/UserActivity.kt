package com.example.pr22102_dergacheva_pract21

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class UserActivity : ComponentActivity() {
    private lateinit var sqlHelper: DatabaseHelper
    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqlHelper = DatabaseHelper(this)
        userId = intent.getLongExtra("userId", 0)

        setContent {
            UserForm(
                userId = userId,
                databaseHelper = sqlHelper,
                onSave = { finish() },
                onDelete = { finish() }
            )
        }
    }
}

@Composable
fun UserForm(
    userId: Long,
    databaseHelper: DatabaseHelper,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (userId > 0) {
            val cursor = databaseHelper.getUserById(userId)
            cursor?.use {
                if (it.moveToFirst()) {
                    name = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                    year = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_YEAR)).toString()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Год рождения") },
            isError = errorMessage.isNotEmpty()
        )
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val yearInt = year.toIntOrNull()
            if (yearInt != null) {
                if (userId > 0) {
                    databaseHelper.updateUser(userId, name, yearInt)
                } else {
                    databaseHelper.insertUser(name, yearInt)
                }
                onSave()
            } else {
                errorMessage = "Пожалуйста, введите корректный год."
            }
        }) {
            Text("Сохранить")
        }

        if (userId > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                databaseHelper.deleteUser(userId)
                onDelete()
            }) {
                Text("Удалить")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onSave() }) {
            Text("Назад")
        }
    }
}
