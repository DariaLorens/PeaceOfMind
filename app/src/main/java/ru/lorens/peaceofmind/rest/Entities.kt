package ru.lorens.peaceofmind.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Date(
    @Expose @SerializedName("id") val id: Int,
    @Expose @SerializedName("date") val date: Long,
    @Expose @SerializedName("desc") val desc: String?
)

data class Respons(
    @Expose @SerializedName("date") val date: List<Date>,
    @Expose @SerializedName("firstday") val firstday: Long
)