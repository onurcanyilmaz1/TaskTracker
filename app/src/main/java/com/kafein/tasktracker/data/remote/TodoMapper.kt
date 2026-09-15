package com.kafein.tasktracker.data.remote

import com.kafein.tasktracker.data.local.TaskEntity

fun TodoDto.toTaskEntity(): TaskEntity {
    return TaskEntity(
        title = title,
        isCompleted = completed
    )
}