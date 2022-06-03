package com.example.choriblogapp.core

import java.util.*
import java.util.concurrent.TimeUnit

private const val SECOND = 1
private const val MINUTE_SECONDS = 60 * SECOND
private const val HOUR_SECONDS = 60 * MINUTE_SECONDS
private const val DAY_SECONDS = 24 * HOUR_SECONDS


object TimeUtils {
    fun getTimeAgo(time: Int): String {
        val now_seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

        if (time > now_seconds || time <= 0) {
            return "in the future"
        }
        val diff = now_seconds - time
        return when {
            diff < MINUTE_SECONDS -> "Just now"
            diff < 2 * MINUTE_SECONDS -> "a minute ago"
            diff < HOUR_SECONDS -> "${diff / MINUTE_SECONDS} minutes ago"
            diff < 2 * HOUR_SECONDS -> "an hour ago"
            diff < DAY_SECONDS -> "${diff / HOUR_SECONDS} hours ago"
            diff < 48 * HOUR_SECONDS -> "yesterday"
            else -> "${diff / DAY_SECONDS} days ago"
        }
    }

    fun getTimeAgo(date: Date): String {

        val createdAt = (date?.time?.div(1000L))?.let {
            getTimeAgo(it.toInt())
        }
        return createdAt
    }
}