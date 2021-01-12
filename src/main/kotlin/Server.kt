import java.lang.Thread.sleep
import java.net.ServerSocket
import java.net.SocketException


class Server {
    fun startServer() {
        try {
            val dataBase = DataBase() // создаю объект класса для работы с БД (разделяемый ресурс)
            ServerSocket(3345).use { server -> // открываю прослушку сокета на 3345 порту
                println("Server is running on port: ${server.localPort}")
                while (true) {
                    val sender = server.accept() // подключаю клиентов
                    if (sender.isConnected) { // если клиент законнектился, то стартую свой тред
                        println("Client connected: ${sender.isConnected} -> ${sender.inetAddress.hostAddress}:${sender.localPort}")
                        val byteArray = ByteArray(9)
                        sender.getInputStream().read(byteArray, 0, 9).toString()
                        val message = String(byteArray).replace("\u0000", "") // делаем из массива байтов стрингу и убираем остальной мусор
                        println("Server get message: [$message]")
                        if (message == "EXIT") {
                            ClientThread(dataBase, message, sender)
                            break
                        }
                        sleep(10)
                        ClientThread(dataBase, message, sender)
                    }
                }
            }
            println("Server was closed")
        } catch (e: SocketException) {
            println("Error: server was not closed or there was another problem")
            e.printStackTrace()
        } // если сокет не создался или что-то пошло не так принчу стактрейс
    }
}
