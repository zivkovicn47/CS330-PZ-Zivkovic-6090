package com.zivkovic.project250.domain

data class UserProfile(
    val fullName: String,
    val phoneNumber: String,
    val profilePicUrl: String,
    val email: String,
    val isAdmin: Boolean = false
)
