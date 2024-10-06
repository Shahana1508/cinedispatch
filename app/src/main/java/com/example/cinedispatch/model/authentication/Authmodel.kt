package com.example.cinedispatch.model.authentication

import com.google.firebase.auth.FirebaseUser

data class Authmodel (
    val user: FirebaseUser? = null,
    val error: String? = null,
    val loading: Boolean = false
    )