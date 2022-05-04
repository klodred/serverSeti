import com.google.gson.Gson
import data.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.*
import java.nio.charset.Charset
import kotlin.concurrent.thread

/*
    Отвечает за сокет соединение и общение с клиентами, отправку ошибок клиентам
 */

class Server(private val config: Map<String, String>) {
    private lateinit var serverSocket: ServerSocket
    private val listClient = mutableListOf<SocketAddress>()

    private fun getSystemIP(): String? {
        var ip: String?

        try {
            val address = InetAddress.getLocalHost()
            ip = address.hostAddress
            println("IP Address = $ip")

        } catch (e: UnknownHostException) {
            e.printStackTrace()
            ip = null
        }

        return ip
    }

    fun startServer() {
        val ip = getSystemIP()
        serverSocket = ServerSocket(config["port"]!!.toInt(), config["backlog"]!!.toInt(), InetAddress.getByName(ip))

        // Ждем подключения клиентов в бесконечном цикле
        // Новый клиент - новый поток
        while(true) {
            val client = serverSocket.accept()
            listClient.add(client.remoteSocketAddress)
            println("Client connect")
            thread { ClientHandler(client).run() }
        }
    }
}


// Класс для обработки действий клиентов
class ClientHandler(client: Socket) {
    private val client: Socket = client
    private val reader = ObjectInputStream(client.getInputStream())
    private lateinit var writer: ObjectOutputStream
    private var running: Boolean = false
    //private var exception: RoomException? = null

    init {
        writer = ObjectOutputStream(client.getOutputStream())
    }

    // Принимаем от клиента Юзера (позволяет узнать роль)
    // а также название и пароль комнаты
    fun run() {
        val jsonUser = reader.readObject() as String
        println(jsonUser)
        val user = Gson().fromJson(jsonUser, User::class.java)
        val gameSett = reader.readObject() as Map<*, *>
        val room = Room(id=1, name= gameSett["name"].toString(), password = gameSett["password"].toString())
        val exception = Game.processRole(user, GameRoom(room))
        writer.writeObject(Gson().toJson(exception))
        /*
        if (exception.status == "Error") {
            writer.writeObject(Gson().toJson(exception))
            //writer.writeObject("Error")
            //writer.writeObject(exception!!.title to exception!!.message)
        }

        else {
            writer.writeObject("OK")
        }

         */


        //val gson = Gson()
        //val tut = gson.fromJson(user, ::class.java)
        //println(tut.name)

        //} catch (ex: Exception) {
        //  println(ex)
        // TODO: Implement exception handling
        //shutdown()
        //} finally {

        //}

        //}
    }



    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }
}