package data

import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("name")
    val name: String,

    @SerializedName("role")
    val role: String,

    var active: Boolean = false) : Serializable
