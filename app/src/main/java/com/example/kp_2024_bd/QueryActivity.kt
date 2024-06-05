package com.example.kp_2024_bd;

import adapters.*
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import models.*
import okhttp3.Credentials
import okhttp3.OkHttpClient
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.util.*
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider


class QueryActivity : AppCompatActivity() {

    private lateinit var tvR: TextView
    private lateinit var tvPython: TextView
    private lateinit var tvCount: TextView
    private lateinit var pieChart: PieChart

    var user = Auth.user
    var pass = Auth.pass

    val basicAuth = Credentials.basic(user, pass)

    val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", basicAuth)
                .build()
            chain.proceed(request)
        }
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    val queryRetrofit = retrofit.create(QueryRetrofit::class.java)

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.query_activity);

        val status_spinner = findViewById<Spinner>(R.id.status_spinner)
        val company_spinner = findViewById<Spinner>(R.id.company_spinner)
        val branch_spinner = findViewById<Spinner>(R.id.branch_spinner)

        val editDateQuery = findViewById<EditText>(R.id.editDateQuery)
        val buttonData = findViewById<Button>(R.id.buttonData)


        val buttonQuery_13_1 = findViewById<Button>(R.id.buttonQuery_13_1)
        val editSummQuery_13_1 = findViewById<EditText>(R.id.editSummQuery_13_1)

        val buttonQuery_13_2 = findViewById<Button>(R.id.buttonQuery_13_2)
        val editValueQuery_13_2 = findViewById<EditText>(R.id.editValueQuery_13_2)

        val buttonQuery_14_1 = findViewById<Button>(R.id.buttonQuery_14_1)
        val editFirstnameQuery_14_1 = findViewById<EditText>(R.id.editFirstnameQuery_14_1)

        val buttonSaveExcel = findViewById<Button>(R.id.buttonSaveExcel)

        val adapterStatus = SpinnerStatusQueryAdapter(mutableListOf(), this)
        status_spinner.adapter = adapterStatus

        val adapterCompany = SpinnerCompanyQueryAdapter(mutableListOf(), this)
        company_spinner.adapter = adapterCompany

        val adapterBranch = SpinnerBranchAdapter(mutableListOf(), this)
        branch_spinner.adapter = adapterBranch

        status_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedStatus = status_spinner.getItemAtPosition(position) as StatusDto
                val statusId = selectedStatus.statusId
                query_1_1(statusId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        company_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCompany = company_spinner.getItemAtPosition(position) as CompanyDto
                val companyId = selectedCompany.companyId
                query_1_2(companyId)
                query_8(companyId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        branch_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedBranch = branch_spinner.getItemAtPosition(position) as BranchDto
                val branchId = selectedBranch.branchId
                query_5(branchId)
                query_7(branchId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        fun fetchDataStatus() {
            val statusService = retrofit.create(StatusRetrofit::class.java)
            val call = statusService.getStatusList()

            call.enqueue(object : Callback<List<StatusDto>> {
                override fun onResponse(
                    call: Call<List<StatusDto>>,
                    response: Response<List<StatusDto>>
                ) {
                    if (response.isSuccessful) {
                        val statusList = response.body()
                        statusList?.let {
                            adapterStatus.updateData(it)
                        }
                    }
                }

                override fun onFailure(call: Call<List<StatusDto>>, t: Throwable) {
                }
            })
        }
        fetchDataStatus()

        fun fetchDataCompany() {
            val clientService = retrofit.create(CompanyRetrofit::class.java)
            val call = clientService.getCompanyList()

            call.enqueue(object : Callback<List<CompanyDto>> {
                override fun onResponse(
                    call: Call<List<CompanyDto>>,
                    response: Response<List<CompanyDto>>
                ) {
                    if (response.isSuccessful) {
                        val companyList = response.body()
                        companyList?.let {
                            adapterCompany.updateData(it)
                        }
                    }
                }

                override fun onFailure(call: Call<List<CompanyDto>>, t: Throwable) {
                }
            })
        }
        fetchDataCompany()

        fun fetchDataBranch() {
            val clientService = retrofit.create(BranchRetrofit::class.java)
            val call = clientService.getBranchList()

            call.enqueue(object : Callback<List<BranchDto>> {
                override fun onResponse(
                    call: Call<List<BranchDto>>,
                    response: Response<List<BranchDto>>
                ) {
                    if (response.isSuccessful) {
                        val branchList = response.body()
                        branchList?.let {
                            adapterBranch.updateData(it)
                        }
                    }
                }

                override fun onFailure(call: Call<List<BranchDto>>, t: Throwable) {
                }
            })
        }
        fetchDataBranch()

        buttonData.setOnClickListener {
            val date = editDateQuery.text.toString()
            query_1_3(date)
            query_1_4(date)
            query_13_3(date)
        }

        query_2_1()
        query_2_2()
        query_2_3()
        query_3()
        query_4()
        query_6()
        query_9()
        query_11()
        query_12()

        buttonQuery_13_1.setOnClickListener {
            val summ = editSummQuery_13_1.text.toString()
            query_13_1(summ)
        }

        buttonQuery_13_2.setOnClickListener {
            val value = editValueQuery_13_2.text.toString()
            query_13_2(value)
            query_13_4(value)
        }

        buttonQuery_14_1.setOnClickListener {
            val firstname = editFirstnameQuery_14_1.text.toString()
            query_14_1(firstname)
        }

        tvR = findViewById(R.id.tvR)
        tvPython = findViewById(R.id.tvPython)
        tvCount = findViewById(R.id.tvCount)
        pieChart = findViewById(R.id.piechart)


        buttonSaveExcel.setOnClickListener {
            checkPermissionsAndSaveExcel(totalContracts, activeContracts, completedContracts)
        }

        query_14_2()
        query_14_3()
        query_14_4()
    }

    private fun query_1_1(statusId: UUID) {
        val call = queryRetrofit.query1_1(statusId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_1_1: TextView = findViewById(R.id.query_1_1)
                    query_1_1.text = "За всё время было заключено $count контрактов в системе, где у клиентов выбранное вами социальное положение."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_1_2(companyId: UUID) {
        val call = queryRetrofit.query1_2(companyId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_1_2: TextView = findViewById(R.id.query_1_2)
                    query_1_2.text = "За всё время было заключено $count контрактов в системе, у выбранной вами компании."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_1_3(date: String) {
        val call = queryRetrofit.query1_3(date)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_1_4: TextView = findViewById(R.id.query_1_4)
                    query_1_4.text = "Заключено $count контрактов после $date числа."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_1_4(date: String) {
        val call = queryRetrofit.query1_4(date)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_1_3: TextView = findViewById(R.id.query_1_3)
                    query_1_3.text = "Заключено $count контрактов до $date числа."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_2_1() {
        val call = queryRetrofit.query2_1()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_2_1: TextView = findViewById(R.id.query_2_1)
                    query_2_1.text = "Найдено всего $count контрактов в системе."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_2_2() {
        val call = queryRetrofit.query2_2()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_2_2: TextView = findViewById(R.id.query_2_2)
                    query_2_2.text = "Филиалов не имеющих данные о компании: $count."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_2_3() {
        val call = queryRetrofit.query2_3()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_2_3: TextView = findViewById(R.id.query_2_3)
                    query_2_3.text = "Клиентов у которых не указано социальное положение при создании: $count."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_3() {

        val call = queryRetrofit.query3()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_3: TextView = findViewById(R.id.query_3)
                    query_3.text = "Филиалов не имеющих данные о компании: $count."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_4() {
        val call = queryRetrofit.query4()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_4: TextView = findViewById(R.id.query_4)
                    query_4.text = "Клиентов у которых не указано социальное положение при создании: $count."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_5(branchId: UUID) {
        val call = queryRetrofit.query5(branchId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_5: TextView = findViewById(R.id.query_5)
                    query_5.text = "Было заключено $count контрактов с выбранным филиалом."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_6() {
        val call = queryRetrofit.query6()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_6: TextView = findViewById(R.id.query_6)
                    query_6.text = "Было заключено всего $count контрактов в системе."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_7(branchId: UUID) {
        val call = queryRetrofit.query7(branchId)

        call.enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful) {
                    val branchList = response.body()
                    branchList?.let {
                        if (it.isNotEmpty()) {
                            val branch = it[0]
                            val branchName = branch["branch_name"]
                            val totalContractsSum = branch["total_contracts_sum"]

                            val query_7: TextView = findViewById(R.id.query_7)
                            query_7.text = "Все контракты с филиалом $branchName были заключены на сумму: $totalContractsSum рублей."
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
            }
        })
    }

    private fun query_8(companyId: UUID) {
        val call = queryRetrofit.query8(companyId)

        call.enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful) {
                    val companyList = response.body()
                    companyList?.let {
                        if (it.isNotEmpty()) {
                            val company = it[0]
                            val companyName = company["company_name"]
                            val totalContractsSum = company["total_contracts_sum"]

                            val query_8: TextView = findViewById(R.id.query_8)
                            query_8.text = "Все контракты с компанией $companyName были заключены на сумму: $totalContractsSum рублей."
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
            }
        })
    }

    private fun query_9() {
        val call = queryRetrofit.query9()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_9: TextView = findViewById(R.id.query_9)
                    query_9.text = "Было найдено $count контрактов с правильно указанным видом страхования."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_11() {
        val call = queryRetrofit.query11()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_11: TextView = findViewById(R.id.query_11)
                    query_11.text = "Было заключено $count контрактов во всех компаниях."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_12() {
        val call = queryRetrofit.query12()

        call.enqueue(object : Callback<Map<String, Int>> {
            override fun onResponse(call: Call<Map<String, Int>>, response: Response<Map<String, Int>>) {
                if (response.isSuccessful) {
                    val company = response.body()
                    company?.let {
                        val totalContracts = it["Всего контрактов"] ?: 0
                        val activeContracts = it["Активен"] ?: 0
                        val completedContracts = it["Завершен"] ?: 0

                        tvR.text = activeContracts.toString()
                        tvPython.text = completedContracts.toString()
                        tvCount.text = totalContracts.toString()

                        pieChart.addPieSlice(
                            PieModel(
                                "Python",
                                activeContracts.toFloat(),
                                Color.parseColor("#66BB6A")
                            )
                        )
                        pieChart.addPieSlice(
                            PieModel(
                                "R",
                                completedContracts.toFloat(),
                                Color.parseColor("#FFA726")
                            )
                        )
                        pieChart.startAnimation()
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Int>>, t: Throwable) {
            }
        })
    }

    private fun query_13_1(summ: String) {
        val call = queryRetrofit.query13_1(summ)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_13_1: TextView = findViewById(R.id.query_13_1)
                    query_13_1.text = "Было заключено $count контрактов на сумму $summ рублей."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_13_2(value: String) {
        val call = queryRetrofit.query13_2(value)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_13_2: TextView = findViewById(R.id.query_13_2)
                    query_13_2.text = "Было заключено $count контрактов с видом страхования, частично соответствующим: $value."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_13_3(date: String) {
        val call = queryRetrofit.query13_3(date)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_13_3: TextView = findViewById(R.id.query_13_3)
                    query_13_3.text = "Было заключено $count контрактов за $date число."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_13_4(value: String) {
        val call = queryRetrofit.query13_4(value)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_13_4: TextView = findViewById(R.id.query_13_4)
                    query_13_4.text = "Было заключено $count контрактов с видом страхования, частично не соответствующим: $value."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_14_1(firstname: String) {
        val call = queryRetrofit.query14_1(firstname)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_14_1: TextView = findViewById(R.id.query_14_1)
                    query_14_1.text = "Было заключено $count контрактов где присутствует имя $firstname."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_14_2() {
        val call = queryRetrofit.query14_2()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_14_2: TextView = findViewById(R.id.query_14_2)
                    query_14_2.text = "В системе $count активных контрактов."
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_14_3() {
        val call = queryRetrofit.query14_3()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_14_3: TextView = findViewById(R.id.query_14_3)
                    query_14_3.text = "Было заключено $count контрактов в системе где сумма начинается от 5000 рублей"
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    private fun query_14_4() {
        val call = queryRetrofit.query14_4()

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val query_14_4: TextView = findViewById(R.id.query_14_4)
                    query_14_4.text = "Средняя сумма всех созданных контрактов в системе: $count"
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }


    private fun saveToExcel(totalContracts: Int, activeContracts: Int, completedContracts: Int) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Contracts")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0, CellType.STRING).setCellValue("Type")
        headerRow.createCell(1, CellType.STRING).setCellValue("Count")

        val totalRow = sheet.createRow(1)
        totalRow.createCell(0, CellType.STRING).setCellValue("Всего контрактов")
        totalRow.createCell(1, CellType.NUMERIC).setCellValue(totalContracts.toDouble())

        val activeRow = sheet.createRow(2)
        activeRow.createCell(0, CellType.STRING).setCellValue("Активен")
        activeRow.createCell(1, CellType.NUMERIC).setCellValue(activeContracts.toDouble())

        val completedRow = sheet.createRow(3)
        completedRow.createCell(0, CellType.STRING).setCellValue("Завершен")
        completedRow.createCell(1, CellType.NUMERIC).setCellValue(completedContracts.toDouble())

        try {
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadDir, "ContractsData.xlsx")
            val fileOut = FileOutputStream(filePath)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()

            Toast.makeText(this, "Файл сохранен в $filePath", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении файла: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    val totalContracts = 0
    val activeContracts = 0
    val completedContracts = 0
    private fun checkPermissionsAndSaveExcel(totalContracts: Int, activeContracts: Int, completedContracts: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        } else {
            saveToExcel(totalContracts, activeContracts, completedContracts)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Разрешение было предоставлено
                saveToExcel(totalContracts, activeContracts, completedContracts)
            } else {
                // Разрешение было отклонено
                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 1
    }
}

