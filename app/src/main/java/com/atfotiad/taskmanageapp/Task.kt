package com.atfotiad.taskmanageapp

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: Int,
    val priority: Int,
    val work: suspend () -> Int
): Parcelable {
    @IgnoredOnParcel
    var result: Int = 0
}
