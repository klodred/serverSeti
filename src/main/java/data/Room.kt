package data

import com.google.gson.annotations.SerializedName

data class Room(
    var id: Int = -1,
    val name: String = "",
    val password: String = "",
    val admin: String? = null,
    var players: List<User>? = null) {

    //constructor(): this(id=-1, name="", password="", admin="")
}