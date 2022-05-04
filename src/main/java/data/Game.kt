package data

import data.Game.game
import java.util.*

/* Игровая комната. Класс отвечает за игру в пределах одной комнаты
   В каждом раунде игры есть один активный игрок - тот, кто рисует
*/

class GameRoom(private val room: Room = Room()) {
    private var activePlayer: Int? = null
        //get() {return field}

    fun getName(): String {
        return room.name
    }

    fun getPassword(): String {
        return room.name
    }
}

// Синголтон класс. Главный класс, который отвечает за обслуживание всех комнат
// game - список всех комнат. Имя комнаты дублируется для лучшего поиска

object Game {
    private var game: MutableMap<String, GameRoom> = mutableMapOf()
    private var exception: RoomException? = null
    private var timer: Timer? = null

    private fun addRoom(room: GameRoom) {
        game.plusAssign(room.getName() to room)

    }

    private fun addPlayer(player: User, nameRoom: String) {
        TODO()
    }

    private fun existRoom(name: String): Boolean {
        return name in game
    }

    private fun checkPassword(nameRoom: String, inputPassword: String): Boolean {
        return game[nameRoom]!!.getPassword() == inputPassword
    }

    private fun startTimer() {
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                println("timer stop, game start")
            }
        }, 7000)
    }

    // Если роль - админ: проверяет уникальность комнаты и создает ее,
    // иначе сообщает клиенту, что комната с таким названием уже есть
    // Если роль - игрок: ищет комнату и сверяет пароли, в случае ошибки отправляет клиенту описание
    fun processRole(player: User, room: GameRoom): ExceptionT {

        if (player.role == "admin") {
            println("Комната создана")
            addRoom(room)
            startTimer()
        }

        else if (player.role == "soldier") {
            if (existRoom(room.getName())) {
                if (checkPassword(room.getName(), room.getPassword())) {
                    println("Игрок добавлен комнату")
                }

                else {
                    //return RoomException("Ошибка сервера!", "Пароль введен неверно")
                    return ExceptionT("OK", "Ошибка сервера!", "Пароль введен неверно")
                }
            }

            else {
                return ExceptionT("OK", "Ошибка сервера!", "Не найдена комната с таким названием")
                //return RoomException("Ошибка сервера!", "Не найдена комната с таким названием")
            }
        }

        else
            throw Exception("Unknown role")

        return ExceptionT("OK")
    }
}