import java.io.File

class DataBase {

    private fun download(): MutableList<Int> { // выгружаем данные из БД
        val file = File("dataBase").bufferedReader().readLines()
        return parser(file)
    }

    @Synchronized
    fun upload(str: String): String { // метод который обновляет данные в БД
        val file = File("dataBase").bufferedReader().readLines()
        val listOfNewData = mutableListOf<String>()
        val listOfCurrentValue = subtraction(str)

        for (i in 0 until file.size) {
            val part = file[i].split("_")
            listOfNewData.add("${part[0]}_${part[1]}_${listOfCurrentValue[i]}")
        }
        val output = File("dataBase").bufferedWriter()
        for (i in listOfNewData) {
            output.write(i)
            output.newLine()
        }
        output.close()
        return "${listOfCurrentValue[0]}|${listOfCurrentValue[1]}|${listOfCurrentValue[2]}|${listOfCurrentValue[3]}|${listOfCurrentValue[4]}\r\n"
    }

     private fun subtraction(str: String): MutableList<Int> { // проводим операцию вычитания с разделяемым ресурсом (БД)
         val dataFromBD = download()
         val part = str.split(" ")
         dataFromBD[0] = dataFromBD[0] - part[0].toInt()
         if (dataFromBD[0] - part[0].toInt() < 0) dataFromBD[0] = 0
         dataFromBD[1] = dataFromBD[1] - part[1].toInt()
         if (dataFromBD[1] - part[1].toInt() < 0) dataFromBD[1] = 0
         dataFromBD[2] = dataFromBD[2] - part[2].toInt()
         if (dataFromBD[2] - part[2].toInt() < 0) dataFromBD[2] = 0
         dataFromBD[3] = dataFromBD[3] - part[3].toInt()
         if (dataFromBD[3] - part[3].toInt() < 0) dataFromBD[3] = 0
         dataFromBD[4] = dataFromBD[4] - part[4].toInt()
         if (dataFromBD[4] - part[4].toInt() < 0) dataFromBD[4] = 0
         return dataFromBD
    }

    private fun parser(list: List<String>): MutableList<Int> { // парсер данных из бд
        val resultList = mutableListOf<Int>()
        for (i in list) {
            val part = i.split("_")
            resultList.add(part[2].toInt())
        }
        return resultList
    }
}