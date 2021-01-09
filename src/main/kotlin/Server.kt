import java.io.BufferedReader
import java.lang.Exception
import java.lang.Thread.sleep
import java.net.ServerSocket

class Server {
    fun startServer() {
        try {
            val dataBase = DataBase() // создаю объект класса для работы с БД (разделяемый ресурс)
            dataBase.download() // делаю инициализацию продуктов из БД
            ServerSocket(3345).use { server -> // открываю прослушку сокета на 3345 порту
                println("Server is running on port ${server.localPort}")
                while (true) {
                    val client = server.accept() // подключаю клиентов
                    if (client.isConnected) { // если клиент законнектился, то стартую свой тред
                        println("Client connected: ${client.inetAddress.hostAddress}:${client.localPort}")
                        val messageParts = BufferedReader(client.getInputStream().reader()).readLine().split("|")
                        println("Server: get message [$messageParts]")
                        sleep(10)
                        ClientThread(dataBase, messageParts[0], messageParts[1].toInt())
                    }
                }
            }
        } catch (e: Exception) { e.printStackTrace() } // если сокет не создался или что-то пошло не так принчу стактрейс
    }
}