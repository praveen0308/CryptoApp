package com.example.cryptoapp.model


import com.google.gson.annotations.SerializedName

data class Whitepaper(
    @SerializedName("link")
    val link: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?
)