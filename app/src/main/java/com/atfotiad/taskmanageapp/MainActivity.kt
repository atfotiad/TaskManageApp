package com.atfotiad.taskmanageapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.atfotiad.taskmanageapp.ui.theme.TaskManageAppTheme

class MainActivity : ComponentActivity() {

    private val tasks = listOf(
        Task(1, 1) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(2, 2) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(3, 3) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(4, 4) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(5, 5) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(6, 6) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(7, 7) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(8, 8) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(9, 9) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(10, 10) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(11, 11) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        },
        Task(12, 12) {
            var counter = 0
            while (counter < 1000000) {
                counter++
            }
            counter
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Intent(this, TaskService::class.java).also {
            it.putParcelableArrayListExtra("tasks", ArrayList(tasks))
            startService(it)
        }
        setContent {
            TaskManageAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                }
            }
        }
    }
}



