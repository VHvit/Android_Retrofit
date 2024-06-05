package com.example.kp_2024_bd;

import adapters.SpinnerBranchAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import models.*
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit.BranchRetrofit
import retrofit.QueryRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class UsersActivity: AppCompatActivity() {
    private lateinit var branchId: UUID

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

    @SuppressLint("MissingInflatedId")
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users_activity);

        val branch_spinner = findViewById<Spinner>(R.id.branch_spinner)

        val buttonAddEmployee = findViewById<Button>(R.id.buttonAddEmployee)
        val buttonAddAdmin = findViewById<Button>(R.id.buttonAddAdmin)
        val buttonAddStatist = findViewById<Button>(R.id.buttonAddStatist)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        val editAddUsername = findViewById<EditText>(R.id.editAddUsername)
        val editAddPassword = findViewById<EditText>(R.id.editAddPassword)

        val editUpdateUsername = findViewById<EditText>(R.id.editUpdateUsername)
        val editUpdatePassword = findViewById<EditText>(R.id.editUpdatePassword)

        val editDeleteUsername = findViewById<EditText>(R.id.editDeleteUsername)

        val adapterBranch = SpinnerBranchAdapter(mutableListOf(), this)
        branch_spinner.adapter = adapterBranch

        branch_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedBranch = branch_spinner.getItemAtPosition(position) as BranchDto
                branchId = selectedBranch.branchId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

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


        buttonAddEmployee.setOnClickListener {
            val username = editAddUsername.text.toString()
            val password = editAddPassword.text.toString()
            val credentials = EmployeeRequest(username, password, branchId.toString())
            createEmployee(credentials)
        }

        buttonAddAdmin.setOnClickListener {
            val username = editAddUsername.text.toString()
            val password = editAddPassword.text.toString()
            val credentials = UserRequest(username, password)
            createAdmin(credentials)
        }

        buttonAddStatist.setOnClickListener {
            val username = editAddUsername.text.toString()
            val password = editAddPassword.text.toString()
            val credentials = UserRequest(username, password)
            createStatist(credentials)
        }

        buttonUpdate.setOnClickListener {
            val username = editUpdateUsername.text.toString()
            val password = editUpdatePassword.text.toString()
            val credentials = UserRequest(username, password)
            updateUser(credentials)


        }

        buttonDelete.setOnClickListener {
            val username = editDeleteUsername.text.toString()

            AlertDialog.Builder(this)
                .setTitle("Подтверждение")
                .setMessage("Вы уверены, что хотите удалить пользователя?")
                .setPositiveButton("Да") { _, _ ->
                    AlertDialog.Builder(this)
                        .setTitle("Подтверждение")
                        .setMessage("Вы точно хотите удалить пользователя?")
                        .setPositiveButton("Да") { _, _ ->
                            deleteUser(username)
                        }
                        .setNegativeButton("Отмена") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        getEmployee()
        getAdmin()
        getStatist()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun createEmployee(credentials: EmployeeRequest) {
        val request = EmployeeRequest(credentials.username, credentials.password, credentials.branchId)
        val call: Call<Void> = queryRetrofit.createEmployee(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    getEmployee()
                    showToast("Сотрудник с логином ${credentials.username} и паролем ${credentials.password} был создан.")
                } else {
                    showToast("Произошла ошибка, пользователь с логином ${credentials.username} уже был создан.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Произошла ошибка при создании пользователя: ${t.message}")
            }
        })
    }

    fun createAdmin(credentials: UserRequest) {
        val request = UserRequest(credentials.username, credentials.password)
        val call: Call<Void> = queryRetrofit.createAdmin(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    getAdmin()
                    showToast("Администратор с логином ${credentials.username} и паролем ${credentials.password} был создан.")
                } else {
                    showToast("Произошла ошибка, пользователь с логином ${credentials.username} уже был создан.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Произошла ошибка при создании пользователя: ${t.message}")
            }
        })
    }

    fun createStatist(credentials: UserRequest) {
        val request = UserRequest(credentials.username, credentials.password)
        val call: Call<Void> = queryRetrofit.createStatist(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    getStatist()
                    showToast("Статист с логином ${credentials.username} и паролем ${credentials.password} был создан.")
                } else {
                    showToast("Произошла ошибка, пользователь с логином ${credentials.username} уже был создан.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Произошла ошибка при создании пользователя: ${t.message}")
            }
        })
    }

    fun updateUser(credentials: UserRequest) {
        val request = UserRequest(credentials.username, credentials.password)
        val call: Call<Void> = queryRetrofit.updateUser(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Пароль ${credentials.username} был успешно изменён на ${credentials.password}.")
                } else {
                    showToast("Ошибка, пользователь не найден")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Произошла ошибка при создании пользователя: ${t.message}")
            }
        })
    }

    fun deleteUser(username: String) {
        val call: Call<Void> = queryRetrofit.deleteUser(username)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    getEmployee()
                    getAdmin()
                    getStatist()
                    showToast("Пользователь с логином $username был успешно удалён.")
                } else {
                    showToast("Ошибка, пользователь не найден")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Произошла ошибка при создании пользователя: ${t.message}")
            }
        })
    }

    private fun getEmployee() {
        val call = queryRetrofit.getEmployee()

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val usersEmployee: TextView = findViewById(R.id.usersEmployee)
                    usersEmployee.text = "$count"
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
            }
        })
    }

    private fun getAdmin() {
        val call = queryRetrofit.getAdmin()

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val usersAdmin: TextView = findViewById(R.id.usersAdmin)
                    usersAdmin.text = "$count"
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
            }
        })
    }

    private fun getStatist() {
        val call = queryRetrofit.getStatist()

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val usersStatist: TextView = findViewById(R.id.usersStatist)
                    usersStatist.text = "$count"
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
            }
        })
    }

}