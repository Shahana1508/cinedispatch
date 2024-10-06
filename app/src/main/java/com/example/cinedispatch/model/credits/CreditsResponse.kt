package com.example.cinedispatch.model.credits


import com.example.cinedispatch.model.credits.Cast
import com.example.cinedispatch.model.credits.Crew
import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("crew")
    val crew: List<Crew>,
    @SerializedName("id")
    val id: Int?
)