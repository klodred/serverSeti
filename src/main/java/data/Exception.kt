package data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RoomException() {
    var title: String = ""
    var message: String = ""

    constructor(_title: String, _message: String) : this() {
        title = _title
        message =_message
    }

    constructor(_message: String) : this() {
        message =_message
    }
}

data class ExceptionT(
    @SerializedName("status")
    val status: String,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("text")
    val text: String = ""
) : Serializable
