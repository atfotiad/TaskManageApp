package com.atfotiad.taskmanageapp

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

class TaskService : Service() {
    private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)
    private val taskQueue =
        PriorityBlockingQueue<Task>(QUEUE_CAPACITY, compareByDescending { it.priority })
    private val cachedTasks = mutableListOf<Task>()
    private val mutex = Mutex()
    private var completedTasks = AtomicInteger(0)
    private var startTime = 0L
    private val totalResult = AtomicInteger(0)


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val tasks = intent?.getParcelableArrayListExtra("tasks", Task::class.java)
        tasks?.forEach { addTask(it) }
        startTime = System.currentTimeMillis()
        val deferredResults = mutableListOf<Deferred<Int>>()
        serviceScope.launch {
            Log.d("TaskService", "onStartCommand: ")
            while (true) {
                mutex.withLock {
                    val task =
                        taskQueue.poll()?.also {
                            if (cachedTasks.isNotEmpty()) {
                                taskQueue.offer(cachedTasks.removeFirst())
                            }
                        }

                    task?.let { currentTask ->
                        executeTask(currentTask)
                        if (completedTasks.incrementAndGet() == tasks?.size) {
                            val results = deferredResults.awaitAll()
                            results.forEach { totalResult.addAndGet(it) }
                            val endTime = System.currentTimeMillis()
                            val elapsedTime = endTime - startTime
                            Log.d("TaskService", "result: ${currentTask.result}")
                            Log.d("TaskService", "All tasks completed in $elapsedTime ms")
                            Log.d("TaskService", "Total sum is  ${totalResult.get()} ")
                            stopSelf()
                        }
                    }
                }
            }
        }
        return START_STICKY
    }


    private suspend fun executeTask(task: Task) {

        try {
            task.result = task.work()
            Log.d("executeTask", "executeTask: ${task.result}")
            totalResult.addAndGet(task.result)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addTask(task: Task) {
        serviceScope.launch {
            mutex.withLock {
                if (taskQueue.size < QUEUE_CAPACITY) {
                    taskQueue.offer(task)
                } else {
                    cachedTasks.add(task)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        private const val QUEUE_CAPACITY = 10
    }
}