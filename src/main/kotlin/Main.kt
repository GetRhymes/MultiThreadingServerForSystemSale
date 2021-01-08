import java.io.BufferedReader
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.sql.Time
import java.util.*

fun main() {
    try {
        val dataBase = DataBase() // создаю объект класса для работы с БД (разделяемый ресурс)
        dataBase.download() // делаю инициализацию продуктов из БД
        ServerSocket(3346).use { server -> // открываю прослушку сокета на 3345 порту
            println("Server is running on port ${server.localPort}")
            while (true) {
                val client = server.accept() // подключаю клиентов
                if (client.isConnected) { // если клиент законнектился, то стартую свой тред
                    println("Client connected: ${client.inetAddress.hostAddress}:${client.localPort}")
                    val messageParts = BufferedReader(client.getInputStream().reader()).readLine().split("|")
                    println("Server: get message [$messageParts]")
                    MyThread(dataBase, messageParts[0], messageParts[1].toInt())
                }
            }
        }
    } catch (e: Exception) { e.printStackTrace() } // если сокет не создался или что-то пошло не так принчу стактрейс
}


class MyThread(private val dataBase: DataBase, private val message: String, private val port: Int): Thread() {
    init { this.start() } // стартую тред при инициализации
    override fun run() {
        val answer = dataBase.syncFun(message) // запускаю логику вычислений - синхорнный метод
        val socket = Socket("localhost", port) // сокет для отправки результата вычислений клиенту
        if (socket.isConnected) { // проверяем есть ли коннект
            socket.getOutputStream().write(answer.toByteArray()) // создаем сообщение и отпрвляем в стрим
            try {
                socket.close() // закрываем сокет
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (socket.isClosed) this.interrupt() // закрываем поток если сокет закрылся
        }

    }
}
