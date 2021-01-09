import java.lang.Exception
import java.net.Socket

class ClientThread(private val dataBase: DataBase, private val message: String, private val port: Int): Thread() {

    init { this.start() } // стартую тред при инициализации
    override fun run() {
        println(this.hashCode())
        val answer = dataBase.syncFun(message) // запускаю логику вычислений - синхорнный метод
        val socket = Socket("127.0.0.1", port) // сокет для отправки результата вычислений клиенту
        if (socket.isConnected) { // проверяем есть ли коннект
            socket.getOutputStream().write(answer.toByteArray()) // создаем сообщение и отпрвляем в стрим
            try {
                socket.close() // закрываем сокет
            } catch (e: Exception) {
                e.printStackTrace()
            }
            println("${socket.isClosed} Socket.close")
            if (socket.isClosed) this.interrupt() // закрываем поток если сокет закрылся
        }
    }
}