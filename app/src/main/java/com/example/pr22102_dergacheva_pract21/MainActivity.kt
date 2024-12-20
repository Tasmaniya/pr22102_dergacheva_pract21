package com.example.pr22102_dergacheva_pract21

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pr22102_dergacheva_pract21.ui.theme.Pr22102_dergacheva_pract21Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pr22102_dergacheva_pract21Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }
    var users by remember { mutableStateOf(dbHelper.getAllUsers()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra("userId", 0L)
            context.startActivity(intent)
        }) {
            Text("Добавить пользователя")
        }

        Spacer(modifier = Modifier.height(16.dp))

        users.forEach { user ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Имя: ${user.second.first}, Год: ${user.second.second}",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    dbHelper.deleteUser(user.first)

                    users = dbHelper.getAllUsers()
                }) {
                    Text("Удалить")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}