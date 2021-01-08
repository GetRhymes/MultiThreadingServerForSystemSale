import java.io.File

class DataBase {

    var firstProduct: Int? = null
    var secondProduct: Int? = null
    var thirdProduct: Int? = null
    var fourthProduct: Int? = null
    var fifthProduct: Int? = null

    fun download() {
        val file = File("dataBase").bufferedReader().readLines()
        val listOfCurrentValue = parser(file)
        firstProduct = listOfCurrentValue[0]
        secondProduct = listOfCurrentValue[1]
        thirdProduct = listOfCurrentValue[2]
        fourthProduct = listOfCurrentValue[3]
        fifthProduct = listOfCurrentValue[4]
    }

    @Synchronized
    private fun upload(): String {
        val file = File("dataBase").bufferedReader().readLines()
        val listOfNewData = mutableListOf<String>()
        val listOfCurrentValue =  mutableListOf<Int?>()
        listOfCurrentValue.add(firstProduct)
        listOfCurrentValue.add(secondProduct)
        listOfCurrentValue.add(thirdProduct)
        listOfCurrentValue.add(fourthProduct)
        listOfCurrentValue.add(fifthProduct)
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
        return "$firstProduct|$secondProduct|$thirdProduct|$fourthProduct|$fifthProduct"
    }

     @Synchronized
     private fun subtraction(str: String) {
         val part = str.split(" ")
         firstProduct = firstProduct?.minus(part[0].toInt())
         secondProduct = secondProduct?.minus(part[1].toInt())
         thirdProduct = thirdProduct?.minus(part[2].toInt())
         fourthProduct = fourthProduct?.minus(part[3].toInt())
         fifthProduct = fifthProduct?.minus(part[4].toInt())
    }

    @Synchronized
    fun syncFun(str: String): String {
        subtraction(str)
        return upload()
    }

    private fun parser(list: List<String>): MutableList<Int> {
        val resultList = mutableListOf<Int>()
        for (i in list) {
            val part = i.split("_")
            resultList.add(part[2].toInt())
        }
        return resultList
    }
}