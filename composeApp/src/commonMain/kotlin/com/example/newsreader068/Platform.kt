package com.example.newsreader068

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform