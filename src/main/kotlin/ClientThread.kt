import java.net.Socket
import java.net.SocketException

class ClientThread(private val dataBase: DataBase, private val message: String, private val sender: Socket): Thread() {

    init { this.start() } // стартую тред при инициализации
    override fun run() {
        println("Thread id: ${this.id}")
        val answer = if (message == "EXIT") "Closed"
        else dataBase.upload(message) // запускаю логику вычислений - синхорнный метод
        if (sender.isConnected) { // проверяем есть ли коннект
            sender.getOutputStream().write(answer.toByteArray()) // создаем сообщение и отпрвляем в стрим
            try {
                sender.close() // закрываем сокет
                println("Socket(sender) was closed: ${sender.isClosed} ")
            } catch (e: SocketException) {
                println("Error: socket was not closed")
                e.printStackTrace()
            }
        }
    }
}