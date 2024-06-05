package com.example.kp_2024_bd

import adapters.*
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import models.*
import navigation.SwipeHelper
import navigation.SwipeHelper.UnderlayButtonClickListener
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import search_model.*
import java.util.*

class ListActivity : AppCompatActivity() {

    private lateinit var mainAdapter: MainAdapter
    private lateinit var mainAdapter1: MainAdapter
    private lateinit var drawerLayout: DrawerLayout
    private var clientId: UUID? = null



    val onResponse: (SearchContractDto) -> Unit = { result ->
        mainAdapter1.updateData(result.contract)
        mainAdapter1.notifyDataSetChanged()
    }

    private var itemList: MutableList<Any> = mutableListOf()
    private var itemmList: MutableList<Any> = mutableListOf()

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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        drawerLayout = findViewById(R.id.drawer_layout)


        val rvClients = findViewById<RecyclerView>(R.id.recView)
        rvClients.layoutManager = LinearLayoutManager(this)

        mainAdapter = MainAdapter(itemList, this, drawerLayout, onResponse)


        rvClients.adapter = mainAdapter

        val rvContracts = findViewById<RecyclerView>(R.id.recViewSostavnaya)
        rvContracts.layoutManager = LinearLayoutManager(this)


        mainAdapter1 = MainAdapter(itemmList, this, drawerLayout, onResponse)
        rvContracts.adapter = mainAdapter1


        val sheetDialog = BottomSheetDialog(this@ListActivity, R.style.BottomSheetStyle)

        val sheetCityView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.city_bottom, null)
        val sheetEditCityView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.city_edit_bottom, null)
        val sheetSearchCityView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.city_search_bottom, null)

        val createValueCity = sheetCityView.findViewById<EditText>(R.id.cityValue)
        val cityEditValue = sheetEditCityView.findViewById<EditText>(R.id.cityValue)
        val searchValueCity = sheetSearchCityView.findViewById<EditText>(R.id.citySearchValue)


        val sheetStatusView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.status_bottom, null)
        val sheetEditStatusView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.status_edit_bottom, null)
        val sheetSearchStatusView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.status_search_bottom, null)

        val createValueStatus = sheetStatusView.findViewById<EditText>(R.id.statusValue)
        val statusEditValue = sheetEditStatusView.findViewById<EditText>(R.id.statusValue)
        val searchValueStatus = sheetSearchStatusView.findViewById<EditText>(R.id.statusSearchValue)


        val sheetTypeView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.type_bottom, null)
        val sheetEditTypeView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.type_edit_bottom, null)
        val sheetSearchTypeView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.type_search_bottom, null)

        val createValueType = sheetTypeView.findViewById<EditText>(R.id.typeValue)
        val typeEditText = sheetEditTypeView.findViewById<EditText>(R.id.typeValue)
        val searchValueType = sheetSearchTypeView.findViewById<EditText>(R.id.typeSearchValue)


        val sheetViewView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.view_bottom, null)
        val sheetEditViewView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.view_edit_bottom, null)
        val sheetSearchViewView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.view_search_bottom, null)

        val createValueView = sheetViewView.findViewById<EditText>(R.id.viewValue)
        val viewEditText = sheetEditViewView.findViewById<EditText>(R.id.viewValue)
        val searchValueView = sheetSearchViewView.findViewById<EditText>(R.id.viewSearchValue)


        val sheetContractView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.contract_bottom, null)
        val sheetEditContractView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.contract_edit_bottom, null)
        val sheetSearchContractView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.contract_search_bottom, null)

        val createSumm = sheetContractView.findViewById<EditText>(R.id.createSumm)
        val spinnerContractView = sheetContractView.findViewById<Spinner>(R.id.contractViewSpinner)
        val spinnerContractStatus = sheetContractView.findViewById<Spinner>(R.id.contractStatusSpinner)

        val spinnerEditContractStatus = sheetEditContractView.findViewById<Spinner>(R.id.contractEditStatusSpinner)

        val searchSumm = sheetSearchContractView.findViewById<EditText>(R.id.searchSumm)
        val searchDateContract = sheetSearchContractView.findViewById<EditText>(R.id.searchDate)
        val spinnerSearchContractView = sheetSearchContractView.findViewById<Spinner>(R.id.spinnerSearchContractView)
        val spinnerSearchContractStatus = sheetSearchContractView.findViewById<Spinner>(R.id.spinnerSearchContractStatus)


        val sheetClientView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.client_bottom, null)
        val sheetEditClientView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.client_edit_bottom, null)
        val sheetSearchClientView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.client_search_bottom, null)

        val createLastname = sheetClientView.findViewById<EditText>(R.id.clientLastname)
        val createFirstname = sheetClientView.findViewById<EditText>(R.id.clientFirstname)
        val createMiddlename = sheetClientView.findViewById<EditText>(R.id.clientMiddlename)
        val createBirthday = sheetClientView.findViewById<EditText>(R.id.createBirthday)
        val createPhone = sheetClientView.findViewById<EditText>(R.id.clientPhone)
        val spinnerClientStatus = sheetClientView.findViewById<Spinner>(R.id.clientStatusSpinner)

        val lastnameEditText = sheetEditClientView.findViewById<EditText>(R.id.clientLastname)
        val firstnameEditText = sheetEditClientView.findViewById<EditText>(R.id.clientFirstname)
        val middlenameEditText = sheetEditClientView.findViewById<EditText>(R.id.clientMiddlename)
        val clientBirthday = sheetEditClientView.findViewById<EditText>(R.id.updateBirthday)
        val phoneEditText = sheetEditClientView.findViewById<EditText>(R.id.clientPhone)
        val spinnerEditClientStatus = sheetEditClientView.findViewById<Spinner>(R.id.clientStatusSpinner)

        val searchLastname = sheetSearchClientView.findViewById<EditText>(R.id.searchLastname)
        val searchFirstname = sheetSearchClientView.findViewById<EditText>(R.id.searchFirstname)
        val searchMiddlename = sheetSearchClientView.findViewById<EditText>(R.id.searchMiddlename)
        val searchBirthday = sheetSearchClientView.findViewById<EditText>(R.id.searchBirthday)


        val sheetContractStatusView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.contract_status_bottom, null)
        val sheetEditContractStatusView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.contract_status_edit_bottom, null)
        val sheetSearchContractStatusView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.contract_status_search_bottom, null)

        val createValueContractStatus = sheetContractStatusView.findViewById<EditText>(R.id.contractStatusValue)
        val contractStatusEditText = sheetEditContractStatusView.findViewById<EditText>(R.id.contractStatusValue)
        val searchValueContractStatus = sheetSearchContractStatusView.findViewById<EditText>(R.id.contractStatusSearchValue)


        val sheetCompanyView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.company_bottom, null)
        val sheetEditCompanyView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.company_edit_bottom, null)
        val sheetSearchCompanyView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.company_search_bottom, null)

        val addCompanyName = sheetCompanyView.findViewById<EditText>(R.id.addCompanyName)
        val addLicenseYear = sheetCompanyView.findViewById<EditText>(R.id.addLicenseYear)
        val addPhoneCompany = sheetCompanyView.findViewById<EditText>(R.id.addPhoneCompany)
        val companyCitySpinner = sheetCompanyView.findViewById<Spinner>(R.id.companyCitySpinner)
        val companyTypeSpinner = sheetCompanyView.findViewById<Spinner>(R.id.companyTypeSpinner)

        val editCompanyName = sheetEditCompanyView.findViewById<EditText>(R.id.editCompanyName)
        val editLicenseYear = sheetEditCompanyView.findViewById<EditText>(R.id.editLicenseYear)
        val editPhoneCompany = sheetEditCompanyView.findViewById<EditText>(R.id.editPhoneCompany)
        val companyEditCitySpinner = sheetEditCompanyView.findViewById<Spinner>(R.id.companyEditCitySpinner)
        val companyEditTypeSpinner = sheetEditCompanyView.findViewById<Spinner>(R.id.companyEditTypeSpinner)

        val searchCompanyName = sheetSearchCompanyView.findViewById<EditText>(R.id.searchCompanyName)
        val companySearchCitySpinner = sheetSearchCompanyView.findViewById<Spinner>(R.id.companySearchCitySpinner)


        val sheetBranchView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.branch_bottom, null)
        val sheetEditBranchView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.branch_edit_bottom, null)
        val sheetSearchBranchView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.branch_search_bottom, null)

        val addBranchName = sheetBranchView.findViewById<EditText>(R.id.addBranchName)
        val addBranchAddress = sheetBranchView.findViewById<EditText>(R.id.addBranchAddress)
        val addPhoneBranch = sheetBranchView.findViewById<EditText>(R.id.addPhoneBranch)
        val addEmployeeCount = sheetBranchView.findViewById<EditText>(R.id.addEmployeeCount)
        val branchCitySpinner = sheetBranchView.findViewById<Spinner>(R.id.branchCitySpinner)
        val branchCompanySpinner = sheetBranchView.findViewById<Spinner>(R.id.branchCompanySpinner)

        val editBranchName = sheetEditBranchView.findViewById<EditText>(R.id.editBranchName)
        val editBranchAddress = sheetEditBranchView.findViewById<EditText>(R.id.editBranchAddress)
        val editPhoneBranch = sheetEditBranchView.findViewById<EditText>(R.id.editPhoneBranch)
        val editEmployeeCount = sheetEditBranchView.findViewById<EditText>(R.id.editEmployeeCount)
        val branchEditCitySpinner = sheetEditBranchView.findViewById<Spinner>(R.id.branchEditCitySpinner)
        val branchEditCompanySpinner = sheetEditBranchView.findViewById<Spinner>(R.id.branchEditCompanySpinner)

        val searchBranchName = sheetSearchBranchView.findViewById<EditText>(R.id.searchBranchName)
        val branchSearchCitySpinner = sheetSearchBranchView.findViewById<Spinner>(R.id.branchSearchCitySpinner)
        val branchSearchCompanySpinner = sheetSearchBranchView.findViewById<Spinner>(R.id.branchSearchCompanySpinner)

        fun fetchDataView() {
            val viewService = retrofit.create(ViewRetrofit::class.java)
            val call = viewService.getViewList()

            call.enqueue(object : Callback<List<ViewDto>> {
                override fun onResponse(
                    call: Call<List<ViewDto>>,
                    response: Response<List<ViewDto>>
                ) {
                    if (response.isSuccessful) {
                        val viewList = response.body()
                        viewList?.let {
                            spinnerContractView
                            spinnerSearchContractView
                            val adapter = SpinnerViewAdapter(it, this@ListActivity)
                            spinnerContractView.adapter = adapter
                            spinnerSearchContractView.adapter = adapter
                        }
                    } else {
                        showToast("Произошла ошибка при загрузке данных")
                    }
                }

                override fun onFailure(call: Call<List<ViewDto>>, t: Throwable) {
                    showToast("Произошла ошибка при загрузке данных")
                }
            })
        }

        fun fetchDataCity() {
            val cityService = retrofit.create(CityRetrofit::class.java)
            val call = cityService.getCityList()

            call.enqueue(object : Callback<List<CityDto>> {
                override fun onResponse(
                    call: Call<List<CityDto>>,
                    response: Response<List<CityDto>>
                ) {
                    if (response.isSuccessful) {
                        val cityList = response.body()
                        cityList?.let {
                            companyCitySpinner
                            branchCitySpinner
                            companyEditCitySpinner
                            branchEditCitySpinner
                            companySearchCitySpinner
                            branchSearchCitySpinner
                            val adapter = SpinnerCityAdapter(it, this@ListActivity)
                            companyCitySpinner.adapter = adapter
                            branchCitySpinner.adapter = adapter
                            companyEditCitySpinner.adapter = adapter
                            branchEditCitySpinner.adapter = adapter
                            companySearchCitySpinner.adapter = adapter
                            branchSearchCitySpinner.adapter = adapter
                        }
                    } else {
                        showToast("Произошла ошибка при загрузке данных")
                    }
                }

                override fun onFailure(call: Call<List<CityDto>>, t: Throwable) {
                    showToast("Произошла ошибка при загрузке данных")
                }
            })
        }

        fun fetchDataType() {
            val typeService = retrofit.create(TypeRetrofit::class.java)
            val call = typeService.getTypeList()

            call.enqueue(object : Callback<List<TypeDto>> {
                override fun onResponse(
                    call: Call<List<TypeDto>>,
                    response: Response<List<TypeDto>>
                ) {
                    if (response.isSuccessful) {
                        val typeList = response.body()
                        typeList?.let {
                            companyTypeSpinner
                            companyEditTypeSpinner
                            val adapter = SpinnerTypeAdapter(it, this@ListActivity)
                            companyTypeSpinner.adapter = adapter
                            companyEditTypeSpinner.adapter = adapter
                        }
                    } else {
                        showToast("Произошла ошибка при загрузке данных")
                    }
                }

                override fun onFailure(call: Call<List<TypeDto>>, t: Throwable) {
                    showToast("Произошла ошибка при загрузке данных")
                }
            })
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
                            spinnerClientStatus
                            spinnerEditClientStatus
                            val adapter = SpinnerStatusAdapter(it, this@ListActivity)
                            spinnerClientStatus.adapter = adapter
                            spinnerEditClientStatus.adapter = adapter
                        }
                    } else {
                        showToast("Произошла ошибка при загрузке данных")
                    }
                }

                override fun onFailure(call: Call<List<StatusDto>>, t: Throwable) {
                    showToast("Произошла ошибка при загрузке данных")
                }
            })
        }

        fun fetchDataContractStatus() {
            val apiService = retrofit.create(ContractStatusRetrofit::class.java)
            val call = apiService.getContractStatusList()

            call.enqueue(object : Callback<List<ContractStatusDto>> {
                override fun onResponse(
                    call: Call<List<ContractStatusDto>>,
                    response: Response<List<ContractStatusDto>>
                ) {
                    if (response.isSuccessful) {
                        val clientList = response.body()
                        clientList?.let {
                            spinnerContractStatus
                            spinnerEditContractStatus
                            spinnerSearchContractStatus
                            val adapter = SpinnerContractStatusAdapter(it, this@ListActivity)
                            spinnerContractStatus.adapter = adapter
                            spinnerEditContractStatus.adapter = adapter
                            spinnerSearchContractStatus.adapter = adapter
                        }
                    } else {
                        showToast("Произошла ошибка при загрузке данных")
                    }
                }

                override fun onFailure(call: Call<List<ContractStatusDto>>, t: Throwable) {
                    showToast("Произошла ошибка при загрузке данных")
                }
            })
        }

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
                            branchCompanySpinner
                            branchEditCompanySpinner
                            branchSearchCompanySpinner
                            val adapter = SpinnerCompanyAdapter(it, this@ListActivity)
                            branchCompanySpinner.adapter = adapter
                            branchEditCompanySpinner.adapter = adapter
                            branchSearchCompanySpinner.adapter = adapter
                        }
                    } else {
                        showToast("Произошла ошибка при загрузке данных")
                    }
                }

                override fun onFailure(call: Call<List<CompanyDto>>, t: Throwable) {
                    showToast("Произошла ошибка при загрузке данных")
                }
            })
        }

        val buttonAddCity = findViewById<FloatingActionButton>(R.id.buttonAddCity)
        val buttonAddStatus = findViewById<FloatingActionButton>(R.id.buttonAddStatus)
        val buttonAddType = findViewById<FloatingActionButton>(R.id.buttonAddType)
        val buttonAddView = findViewById<FloatingActionButton>(R.id.buttonAddView)
        val buttonAddContract = findViewById<FloatingActionButton>(R.id.buttonAddContract)
        val buttonAddContractSostavnaya = findViewById<FloatingActionButton>(R.id.buttonAddContractSostavnaya)
        val buttonAddClient = findViewById<FloatingActionButton>(R.id.buttonAddClient)
        val buttonAddContractStatus = findViewById<FloatingActionButton>(R.id.buttonAddContractStatus)
        val buttonAddCompany = findViewById<FloatingActionButton>(R.id.buttonAddCompany)
        val buttonAddBranch = findViewById<FloatingActionButton>(R.id.buttonAddBranch)

        val buttonAddCityBottom = sheetCityView.findViewById<Button>(R.id.buttonAddCityBottom)
        val buttonAddStatusBottom = sheetStatusView.findViewById<Button>(R.id.buttonAddStatusBottom)
        val buttonAddTypeBottom = sheetTypeView.findViewById<Button>(R.id.buttonAddTypeBottom)
        val buttonAddViewBottom = sheetViewView.findViewById<Button>(R.id.buttonAddViewBottom)
        val buttonAddClientBottom = sheetClientView.findViewById<Button>(R.id.buttonAddClientBottom)
        val buttonAddContractBottom = sheetContractView.findViewById<Button>(R.id.buttonAddContractBottom)
        val buttonAddContractStatusBottom = sheetContractStatusView.findViewById<Button>(R.id.buttonAddContractStatusBottom)
        val buttonAddCompanyBottom = sheetCompanyView.findViewById<Button>(R.id.buttonAddCompanyBottom)
        val buttonAddBranchBottom = sheetBranchView.findViewById<Button>(R.id.buttonAddBranchBottom)

        val buttonSearchCityBottom = sheetSearchCityView.findViewById<Button>(R.id.buttonSearchCityBottom)
        val buttonSearchStatusBottom = sheetSearchStatusView.findViewById<Button>(R.id.buttonSearchStatusBottom)
        val buttonSearchTypeBottom = sheetSearchTypeView.findViewById<Button>(R.id.buttonSearchTypeBottom)
        val buttonSearchViewBottom = sheetSearchViewView.findViewById<Button>(R.id.buttonSearchViewBottom)
        val buttonSearchClientBottom = sheetSearchClientView.findViewById<Button>(R.id.buttonSearchClientBottom)
        val buttonSearchContractBottom = sheetSearchContractView.findViewById<Button>(R.id.buttonSearchContractBottom)
        val buttonSearchContractStatusBottom = sheetSearchContractStatusView.findViewById<Button>(R.id.buttonSearchContractStatusBottom)
        val buttonSearchCompanyBottom = sheetSearchCompanyView.findViewById<Button>(R.id.buttonSearchCompanyBottom)
        val buttonSearchBranchBottom = sheetSearchBranchView.findViewById<Button>(R.id.buttonSearchBranchBottom)

        val buttonSearchCity = findViewById<FloatingActionButton>(R.id.buttonSearchCity)
        val buttonSearchStatus = findViewById<FloatingActionButton>(R.id.buttonSearchStatus)
        val buttonSearchType = findViewById<FloatingActionButton>(R.id.buttonSearchType)
        val buttonSearchView = findViewById<FloatingActionButton>(R.id.buttonSearchView)
        val buttonSearchContract = findViewById<FloatingActionButton>(R.id.buttonSearchContract)
        val buttonSearchClient = findViewById<FloatingActionButton>(R.id.buttonSearchClient)
        val buttonSearchContractStatus = findViewById<FloatingActionButton>(R.id.buttonSearchContractStatus)
        val buttonSearchCompany = findViewById<FloatingActionButton>(R.id.buttonSearchCompany)
        val buttonSearchBranch = findViewById<FloatingActionButton>(R.id.buttonSearchBranch)

        buttonAddCity.setOnClickListener {
            sheetDialog.setContentView(sheetCityView)
            sheetDialog.show()
        }

        buttonAddStatus.setOnClickListener {
            sheetDialog.setContentView(sheetStatusView)
            sheetDialog.show()
        }

        buttonAddType.setOnClickListener {
            sheetDialog.setContentView(sheetTypeView)
            sheetDialog.show()
        }

        buttonAddView.setOnClickListener {
            sheetDialog.setContentView(sheetViewView)
            sheetDialog.show()
        }

        buttonAddContract.setOnClickListener {
            fetchDataView()
            fetchDataContractStatus()
            sheetDialog.setContentView(sheetContractView)
            sheetDialog.show()
        }

        buttonAddContractSostavnaya.setOnClickListener {
            fetchDataView()
            fetchDataContractStatus()
            sheetDialog.setContentView(sheetContractView)
            sheetDialog.show()
        }

        buttonAddClient.setOnClickListener {
            fetchDataStatus()
            sheetDialog.setContentView(sheetClientView)
            sheetDialog.show()
        }

        buttonAddContractStatus.setOnClickListener {
            sheetDialog.setContentView(sheetContractStatusView)
            sheetDialog.show()
        }

        buttonAddCompany.setOnClickListener {
            fetchDataCity()
            fetchDataType()
            sheetDialog.setContentView(sheetCompanyView)
            sheetDialog.show()
        }

        buttonAddBranch.setOnClickListener {
            fetchDataCity()
            fetchDataCompany()
            sheetDialog.setContentView(sheetBranchView)
            sheetDialog.show()
        }


        buttonSearchCity.setOnClickListener {
            sheetDialog.setContentView(sheetSearchCityView)
            sheetDialog.show()
        }

        buttonSearchStatus.setOnClickListener {
            sheetDialog.setContentView(sheetSearchStatusView)
            sheetDialog.show()
        }

        buttonSearchType.setOnClickListener {
            sheetDialog.setContentView(sheetSearchTypeView)
            sheetDialog.show()
        }

        buttonSearchView.setOnClickListener {
            sheetDialog.setContentView(sheetSearchViewView)
            sheetDialog.show()
        }

        buttonSearchContract.setOnClickListener {
            fetchDataView()
            fetchDataContractStatus()
            sheetDialog.setContentView(sheetSearchContractView)
            sheetDialog.show()
        }

        buttonSearchClient.setOnClickListener {
            fetchDataStatus()
            sheetDialog.setContentView(sheetSearchClientView)
            sheetDialog.show()
        }

        buttonSearchContractStatus.setOnClickListener {
            sheetDialog.setContentView(sheetSearchContractStatusView)
            sheetDialog.show()
        }

        buttonSearchCompany.setOnClickListener {
            fetchDataCity()
            fetchDataType()
            sheetDialog.setContentView(sheetSearchCompanyView)
            sheetDialog.show()
        }

        buttonSearchBranch.setOnClickListener {
            fetchDataCity()
            fetchDataCompany()
            sheetDialog.setContentView(sheetSearchBranchView)
            sheetDialog.show()
        }

        val buttonLoadCity = findViewById<Button>(R.id.buttonLoadCity)
        buttonLoadCity.setOnClickListener {
            loadCityFromServer()
            buttonAddCity.visibility = View.VISIBLE
            buttonAddStatus.visibility = View.INVISIBLE
            buttonAddType.visibility = View.INVISIBLE
            buttonAddView.visibility = View.INVISIBLE
            buttonAddClient.visibility = View.INVISIBLE
            buttonAddContract.visibility = View.INVISIBLE
            buttonAddContractStatus.visibility = View.INVISIBLE
            buttonAddCompany.visibility = View.INVISIBLE
            buttonAddBranch.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.VISIBLE
            buttonSearchStatus.visibility = View.INVISIBLE
            buttonSearchType.visibility = View.INVISIBLE
            buttonSearchView.visibility = View.INVISIBLE
            buttonSearchClient.visibility = View.INVISIBLE
            buttonSearchContract.visibility = View.INVISIBLE
            buttonSearchContractStatus.visibility = View.INVISIBLE
            buttonSearchCompany.visibility = View.INVISIBLE
            buttonSearchBranch.visibility = View.INVISIBLE
        }

        val buttonLoadStatus = findViewById<Button>(R.id.buttonLoadStatus)
        buttonLoadStatus.setOnClickListener {
            loadStatusFromServer()
            buttonAddStatus.visibility = View.VISIBLE
            buttonAddCity.visibility = View.INVISIBLE
            buttonAddType.visibility = View.INVISIBLE
            buttonAddView.visibility = View.INVISIBLE
            buttonAddClient.visibility = View.INVISIBLE
            buttonAddContract.visibility = View.INVISIBLE
            buttonAddContractStatus.visibility = View.INVISIBLE
            buttonAddCompany.visibility = View.INVISIBLE
            buttonAddBranch.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.INVISIBLE
            buttonSearchStatus.visibility = View.VISIBLE
            buttonSearchType.visibility = View.INVISIBLE
            buttonSearchView.visibility = View.INVISIBLE
            buttonSearchClient.visibility = View.INVISIBLE
            buttonSearchContract.visibility = View.INVISIBLE
            buttonSearchContractStatus.visibility = View.INVISIBLE
            buttonSearchCompany.visibility = View.INVISIBLE
            buttonSearchBranch.visibility = View.INVISIBLE
        }

        val buttonLoadType = findViewById<Button>(R.id.buttonLoadType)
        buttonLoadType.setOnClickListener {
            loadTypeFromServer()
            buttonAddType.visibility = View.VISIBLE
            buttonAddStatus.visibility = View.INVISIBLE
            buttonAddCity.visibility = View.INVISIBLE
            buttonAddView.visibility = View.INVISIBLE
            buttonAddClient.visibility = View.INVISIBLE
            buttonAddContract.visibility = View.INVISIBLE
            buttonAddContractStatus.visibility = View.INVISIBLE
            buttonAddCompany.visibility = View.INVISIBLE
            buttonAddBranch.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.INVISIBLE
            buttonSearchStatus.visibility = View.INVISIBLE
            buttonSearchType.visibility = View.VISIBLE
            buttonSearchView.visibility = View.INVISIBLE
            buttonSearchClient.visibility = View.INVISIBLE
            buttonSearchContract.visibility = View.INVISIBLE
            buttonSearchContractStatus.visibility = View.INVISIBLE
            buttonSearchCompany.visibility = View.INVISIBLE
            buttonSearchBranch.visibility = View.INVISIBLE
        }

        val buttonLoadView = findViewById<Button>(R.id.buttonLoadView)
        buttonLoadView.setOnClickListener {
            loadViewFromServer()
            buttonAddView.visibility = View.VISIBLE
            buttonAddStatus.visibility = View.INVISIBLE
            buttonAddType.visibility = View.INVISIBLE
            buttonAddCity.visibility = View.INVISIBLE
            buttonAddClient.visibility = View.INVISIBLE
            buttonAddContract.visibility = View.INVISIBLE
            buttonAddContractStatus.visibility = View.INVISIBLE
            buttonAddCompany.visibility = View.INVISIBLE
            buttonAddBranch.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.INVISIBLE
            buttonSearchStatus.visibility = View.INVISIBLE
            buttonSearchType.visibility = View.INVISIBLE
            buttonSearchView.visibility = View.VISIBLE
            buttonSearchClient.visibility = View.INVISIBLE
            buttonSearchContract.visibility = View.INVISIBLE
            buttonSearchContractStatus.visibility = View.INVISIBLE
            buttonSearchCompany.visibility = View.INVISIBLE
            buttonSearchBranch.visibility = View.INVISIBLE
        }

        val buttonLoadContract = findViewById<Button>(R.id.buttonLoadContract)
        buttonLoadContract.setOnClickListener {
            loadContractFromServer()
            buttonAddContract.visibility = View.INVISIBLE
            buttonAddView.visibility = View.INVISIBLE
            buttonAddStatus.visibility = View.INVISIBLE
            buttonAddType.visibility = View.INVISIBLE
            buttonAddCity.visibility = View.INVISIBLE
            buttonAddClient.visibility = View.INVISIBLE
            buttonAddContractStatus.visibility = View.INVISIBLE
            buttonAddCompany.visibility = View.INVISIBLE
            buttonAddBranch.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.INVISIBLE
            buttonSearchStatus.visibility = View.INVISIBLE
            buttonSearchType.visibility = View.INVISIBLE
            buttonSearchView.visibility = View.INVISIBLE
            buttonSearchClient.visibility = View.INVISIBLE
            buttonSearchContract.visibility = View.VISIBLE
            buttonSearchContractStatus.visibility = View.INVISIBLE
            buttonSearchCompany.visibility = View.INVISIBLE
            buttonSearchBranch.visibility = View.INVISIBLE
        }

        val buttonLoadClient = findViewById<Button>(R.id.buttonLoadClient)
        buttonLoadClient.setOnClickListener {
            loadClientFromServer()
            fetchDataStatus()
            buttonAddClient.visibility = View.VISIBLE
            buttonAddView.visibility = View.INVISIBLE
            buttonAddStatus.visibility = View.INVISIBLE
            buttonAddType.visibility = View.INVISIBLE
            buttonAddCity.visibility = View.INVISIBLE
            buttonAddContract.visibility = View.INVISIBLE
            buttonAddContractStatus.visibility = View.INVISIBLE
            buttonAddCompany.visibility = View.INVISIBLE
            buttonAddBranch.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.INVISIBLE
            buttonSearchStatus.visibility = View.INVISIBLE
            buttonSearchType.visibility = View.INVISIBLE
            buttonSearchView.visibility = View.INVISIBLE
            buttonSearchClient.visibility = View.VISIBLE
            buttonSearchContract.visibility = View.INVISIBLE
            buttonSearchContractStatus.visibility = View.INVISIBLE
            buttonSearchCompany.visibility = View.INVISIBLE
            buttonSearchBranch.visibility = View.INVISIBLE
        }

        val buttonLoadContractStatus = findViewById<Button>(R.id.buttonLoadContractStatus)
        buttonLoadContractStatus.setOnClickListener {
            loadContractStatusFromServer()
            buttonAddContractStatus.visibility = View.VISIBLE
            buttonAddClient.visibility = View.INVISIBLE
            buttonAddView.visibility = View.INVISIBLE
            buttonAddStatus.visibility = View.INVISIBLE
            buttonAddType.visibility = View.INVISIBLE
            buttonAddCity.visibility = View.INVISIBLE
            buttonAddContract.visibility = View.INVISIBLE
            buttonAddCompany.visibility = View.INVISIBLE
            buttonAddBranch.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.INVISIBLE
            buttonSearchStatus.visibility = View.INVISIBLE
            buttonSearchType.visibility = View.INVISIBLE
            buttonSearchView.visibility = View.INVISIBLE
            buttonSearchClient.visibility = View.INVISIBLE
            buttonSearchContract.visibility = View.INVISIBLE
            buttonSearchContractStatus.visibility = View.VISIBLE
            buttonSearchCompany.visibility = View.INVISIBLE
            buttonSearchBranch.visibility = View.INVISIBLE
        }

        val buttonLoadCompany = findViewById<Button>(R.id.buttonLoadCompany)
        buttonLoadCompany.setOnClickListener {
            loadCompanyFromServer()
            buttonAddCompany.visibility = View.VISIBLE
            buttonAddBranch.visibility = View.INVISIBLE
            buttonAddContractStatus.visibility = View.INVISIBLE
            buttonAddClient.visibility = View.INVISIBLE
            buttonAddView.visibility = View.INVISIBLE
            buttonAddStatus.visibility = View.INVISIBLE
            buttonAddType.visibility = View.INVISIBLE
            buttonAddCity.visibility = View.INVISIBLE
            buttonAddContract.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.INVISIBLE
            buttonSearchStatus.visibility = View.INVISIBLE
            buttonSearchType.visibility = View.INVISIBLE
            buttonSearchView.visibility = View.INVISIBLE
            buttonSearchClient.visibility = View.INVISIBLE
            buttonSearchContract.visibility = View.INVISIBLE
            buttonSearchContractStatus.visibility = View.INVISIBLE
            buttonSearchCompany.visibility = View.VISIBLE
            buttonSearchBranch.visibility = View.INVISIBLE
        }

        val buttonLoadBranch = findViewById<Button>(R.id.buttonLoadBranch)
        buttonLoadBranch.setOnClickListener {
            loadBranchFromServer()
            buttonAddBranch.visibility = View.VISIBLE
            buttonAddCompany.visibility = View.INVISIBLE
            buttonAddContractStatus.visibility = View.INVISIBLE
            buttonAddClient.visibility = View.INVISIBLE
            buttonAddView.visibility = View.INVISIBLE
            buttonAddStatus.visibility = View.INVISIBLE
            buttonAddType.visibility = View.INVISIBLE
            buttonAddCity.visibility = View.INVISIBLE
            buttonAddContract.visibility = View.INVISIBLE

            buttonSearchCity.visibility = View.INVISIBLE
            buttonSearchStatus.visibility = View.INVISIBLE
            buttonSearchType.visibility = View.INVISIBLE
            buttonSearchView.visibility = View.INVISIBLE
            buttonSearchClient.visibility = View.INVISIBLE
            buttonSearchContract.visibility = View.INVISIBLE
            buttonSearchContractStatus.visibility = View.INVISIBLE
            buttonSearchCompany.visibility = View.INVISIBLE
            buttonSearchBranch.visibility = View.VISIBLE
        }

        val buttonLoadUsers = findViewById<Button>(R.id.buttonLoadUsers)
        buttonLoadUsers.setOnClickListener {
            val intent = Intent(this@ListActivity, UsersActivity::class.java)
            startActivity(intent)
        }

        if (user == "employee1" || user == "employee2" || user == "employee3" || user == "hvit" || user == "employee4" || user == "employee5" || user == "employee6" || user == "employee7") {
            buttonLoadCity.visibility = View.INVISIBLE
            buttonLoadStatus.visibility = View.INVISIBLE
            buttonLoadType.visibility = View.INVISIBLE
            buttonLoadView.visibility = View.INVISIBLE
            buttonLoadContract.visibility = View.VISIBLE
            buttonLoadClient.visibility = View.VISIBLE
            buttonLoadContractStatus.visibility = View.INVISIBLE
            buttonLoadCompany.visibility = View.INVISIBLE
            buttonLoadBranch.visibility = View.INVISIBLE
            buttonLoadUsers.visibility = View.INVISIBLE
        } else if (user == "postgre" || user == "postgre1") {
            buttonLoadCity.visibility = View.VISIBLE
            buttonLoadStatus.visibility = View.VISIBLE
            buttonLoadType.visibility = View.VISIBLE
            buttonLoadView.visibility = View.VISIBLE
            buttonLoadContract.visibility = View.INVISIBLE
            buttonLoadClient.visibility = View.INVISIBLE
            buttonLoadContractStatus.visibility = View.VISIBLE
            buttonLoadCompany.visibility = View.VISIBLE
            buttonLoadBranch.visibility = View.VISIBLE
        } else if (user == "statistic" || user == "statistic2") {
            val intent = Intent(this@ListActivity, QueryActivity::class.java)
            startActivity(intent)
        }

        buttonAddCityBottom?.setOnClickListener {
            try {
                val newCityDto = CityDto(
                    cityId = UUID.randomUUID(),
                    value = createValueCity.text.toString()
                )

                createCityOnServer(newCityDto)
                loadCityFromServer()
                showToast("Запись успешно создана")

                createValueCity.text.clear()
            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonAddStatusBottom?.setOnClickListener {
            try {
                val newStatusDto = StatusDto(
                    statusId = UUID.randomUUID(),
                    value = createValueStatus.text.toString()
                )

                createStatusOnServer(newStatusDto)
                loadStatusFromServer()
                showToast("Запись успешно создана")

                createValueStatus.text.clear()
            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonAddTypeBottom?.setOnClickListener {
            try {
                val newTypeDto = TypeDto(
                    typeId = UUID.randomUUID(),
                    value = createValueType.text.toString()
                )

                createTypeOnServer(newTypeDto)
                loadTypeFromServer()
                showToast("Запись успешно создана")

                createValueType.text.clear()
            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonAddViewBottom?.setOnClickListener {
            try {
                val newViewDto = ViewDto(
                    viewId = UUID.randomUUID(),
                    value = createValueView.text.toString()
                )

                createViewOnServer(newViewDto)
                loadViewFromServer()
                showToast("Запись успешно создана")

                createValueView.text.clear()
            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonAddContractBottom?.setOnClickListener {
            if (clientId == null) {
                showToast("Client ID is null")
                return@setOnClickListener // Выход из обработчика клика
            }
            try {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val formattedDate = String.format("%04d-%02d-%02d", year, month, day)

                val newContractDto = ContractCreateDto(
                    UUID.randomUUID(),
                    createSumm.text.toString(),
                    formattedDate,
                    clientId!!,
                    spinnerContractView.selectedItem as ViewDto,
                    spinnerContractStatus.selectedItem as ContractStatusDto
                )

                createContractOnServer(newContractDto)
                Handler(Looper.getMainLooper()).postDelayed({
                    getContractsByClientId(clientId!!, onResponse)
                }, 500L)

                showToast("Запись успешно создана")

                createSumm.text.clear()
                fetchDataView()
                fetchDataContractStatus()

            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonAddClientBottom?.setOnClickListener {
            try {
                val newClientDto = ClientDto(
                    clientId = UUID.randomUUID(),
                    lastname = createLastname.text.toString(),
                    firstname = createFirstname.text.toString(),
                    middlename = createMiddlename.text.toString(),
                    birthday = createBirthday.text.toString(),
                    phone = createPhone.text.toString(),
                    status = spinnerClientStatus.selectedItem as StatusDto,
                )

                createClientOnServer(newClientDto)
                loadClientFromServer()
                showToast("Запись успешно создана")

                createLastname.text.clear()
                createFirstname.text.clear()
                createMiddlename.text.clear()
                createPhone.text.clear()
            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonAddContractStatusBottom?.setOnClickListener {
            try {
                val newContractStatusDto = ContractStatusDto(
                    contractStatusId = UUID.randomUUID(),
                    value = createValueContractStatus.text.toString()
                )

                createContractStatusOnServer(newContractStatusDto)
                showToast("Запись успешно создана")

                createValueContractStatus.text.clear()
                loadContractStatusFromServer()
            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonAddCompanyBottom?.setOnClickListener {
            try {
                val newCompanyDto = CompanyDto(
                    companyId = UUID.randomUUID(),
                    companyName = addCompanyName.text.toString(),
                    licenseYear = addLicenseYear.text.toString(),
                    city = companyCitySpinner.selectedItem as CityDto,
                    phone = addPhoneCompany.text.toString(),
                    type = companyTypeSpinner.selectedItem as TypeDto,
                )

                createCompanyOnServer(newCompanyDto)
                loadCompanyFromServer()
                showToast("Запись успешно создана")

                addCompanyName.text.clear()
                addLicenseYear.text.clear()
                addPhoneCompany.text.clear()
            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonAddBranchBottom?.setOnClickListener {
            try {
                val newBranchDto = BranchDto(
                    branchId = UUID.randomUUID(),
                    branchName = addBranchName.text.toString(),
                    city = branchCitySpinner.selectedItem as CityDto,
                    address = addBranchAddress.text.toString(),
                    branchPhone = addPhoneBranch.text.toString(),
                    employeesCount = addEmployeeCount.text.toString(),
                    company = branchCompanySpinner.selectedItem as CompanyDto,
                )

                createBranchOnServer(newBranchDto)
                loadBranchFromServer()
                showToast("Запись успешно создана")

                addBranchName.text.clear()
                addBranchAddress.text.clear()
                addPhoneBranch.text.clear()
                addEmployeeCount.text.clear()
            } catch (e: Exception) {
                showToast("Произошла ошибка, проверьте данные ввода")
            }
        }

        buttonSearchCityBottom.setOnClickListener {
            val query = searchValueCity.text.toString()
            searchCities(query)
        }

        buttonSearchStatusBottom.setOnClickListener {
            val query = searchValueStatus.text.toString()
            searchStatus(query)
        }

        buttonSearchTypeBottom.setOnClickListener {
            val query = searchValueType.text.toString()
            searchType(query)
        }

        buttonSearchViewBottom.setOnClickListener {
            val query = searchValueView.text.toString()
            searchView(query)
        }

        buttonSearchContractStatusBottom.setOnClickListener {
            val query = searchValueContractStatus.text.toString()
            searchContractStatus(query)
        }

        buttonSearchContractBottom.setOnClickListener {


            val summ = if (searchSumm.text.isNotEmpty()) {
                searchSumm.text.toString()
            } else {
                null
            }

            val date = if (searchDateContract.text.isNotEmpty()) {
                searchDateContract.text.toString()
            } else {
                null
            }

            val viewSelectedItem = spinnerSearchContractView.selectedItem
            val viewId = if (viewSelectedItem != null && viewSelectedItem is ViewDto) {
                viewSelectedItem.viewId
            } else {
                null
            }

            val contractStatusSelectedItem = spinnerSearchContractStatus.selectedItem
            val contractStatusId = if (contractStatusSelectedItem != null && contractStatusSelectedItem is ContractStatusDto) {
                contractStatusSelectedItem.contractStatusId
            } else {
                null
            }

            searchContract(summ, date, viewId, contractStatusId,
                onResponse = { searchContractDto ->},
                onFailure = { throwable -> showToast("Произошла ошибка при загрузке данных")}
            )
        }

        buttonSearchClientBottom.setOnClickListener {

            val firstname = if (searchFirstname.text.isNotEmpty()) {
                searchFirstname.text.toString()
            } else {
                null
            }

            val lastname = if (searchLastname.text.isNotEmpty()) {
                searchLastname.text.toString()
            } else {
                null
            }

            val middlename = if (searchMiddlename.text.isNotEmpty()) {
                searchMiddlename.text.toString()
            } else {
                null
            }


            val birthday = if (searchBirthday.text.isNotEmpty()) {
                searchBirthday.text.toString()
            } else {
                null
            }

            searchClient(firstname, lastname, middlename, birthday,
                onResponse = { searchContractDto ->},
                onFailure = { throwable -> showToast("Произошла ошибка при загрузке данных")}
            )
        }

        buttonSearchCompanyBottom.setOnClickListener {

            val companyName = if (searchCompanyName.text.isNotEmpty()) {
                searchCompanyName.text.toString()
            } else {
                null
            }

            val companyCitySelectedItem = companySearchCitySpinner.selectedItem
            val cityId = if (companyCitySelectedItem != null && companyCitySelectedItem is CityDto) {
                companyCitySelectedItem.cityId
            } else {
                null
            }

            searchCompany(companyName, cityId,
                onResponse = { searchCompanyDto ->},
                onFailure = { throwable -> showToast("Произошла ошибка при загрузке данных")}
            )
        }

        buttonSearchBranchBottom.setOnClickListener {

            val branchName = if (searchBranchName.text.isNotEmpty()) {
                searchBranchName.text.toString()
            } else {
                null
            }

            val branchCitySelectedItem = branchSearchCitySpinner.selectedItem
            val cityId = if (branchCitySelectedItem != null && branchCitySelectedItem is CityDto) {
                branchCitySelectedItem.cityId
            } else {
                null
            }

            val branchCompanySelectedItem = branchSearchCompanySpinner.selectedItem
            val companyId = if (branchCompanySelectedItem != null && branchCompanySelectedItem is CompanyDto) {
                branchCompanySelectedItem.companyId
            } else {
                null
            }

            searchBranch(branchName, cityId, companyId,
                onResponse = { searchBranchDto ->},
                onFailure = { throwable -> showToast("Произошла ошибка при загрузке данных")}
            )
        }

        val swipeHelperMain: SwipeHelper = object : SwipeHelper(this, rvClients) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                underlayButtons.add(UnderlayButton(
                    this@ListActivity,
                    "Удалить",
                    resources.getDrawable(R.drawable.trash, null),
                    Color.parseColor("#ff2b2b"),
                    true,
                    UnderlayButtonClickListener {
                        val cityId = (itemList[it] as? CityDto)?.cityId
                        val statusId = (itemList[it] as? StatusDto)?.statusId
                        val typeId = (itemList[it] as? TypeDto)?.typeId
                        val viewId = (itemList[it] as? ViewDto)?.viewId
                        val contractId = (itemList[it] as? ContractDto)?.contractId
                        val clientId = (itemList[it] as? ClientDto)?.clientId
                        val contractStatusId = (itemList[it] as? ContractStatusDto)?.contractStatusId
                        val companyId = (itemList[it] as? CompanyDto)?.companyId
                        val branchId = (itemList[it] as? BranchDto)?.branchId

                        var deletedItemCount = 0

                        val alertDialog = AlertDialog.Builder(this@ListActivity)
                            .setPositiveButton("Удалить") { _, _ ->
                                cityId?.let { cityId -> deleteCity(cityId) }
                                statusId?.let { statusId -> deleteStatus(statusId) }
                                typeId?.let { typeId -> deleteType(typeId) }
                                viewId?.let { viewId -> deleteView(viewId) }
                                contractId?.let { contractId -> deleteContract(contractId) }
                                clientId?.let { clientId -> deleteClient(clientId) }
                                contractStatusId?.let { clientId -> deleteContractStatus(contractStatusId) }
                                companyId?.let { clientId -> deleteCompany(companyId) }
                                branchId?.let { clientId -> deleteBranch(branchId) }
                                itemList.removeAt(it)
                                mainAdapter.notifyDataSetChanged()
                            }
                            .setNegativeButton("Отмена", null)
                            .create()

                        when {
                            cityId != null -> {
                                cityDeleteCount(cityId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этот город? Удаление приведёт к удалению $deletedItemCount контрактов")
                                    alertDialog.show()
                                }
                            }
                            statusId != null -> {
                                statusDeleteCount(statusId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этот социальный статус? Удаление приведёт к удалению $deletedItemCount контрактов")
                                    alertDialog.show()
                                }
                            }
                            typeId != null -> {
                                typeDeleteCount(typeId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этот тип собственности? Удаление приведёт к удалению $deletedItemCount контрактов")
                                    alertDialog.show()
                                }
                            }
                            viewId != null -> {
                                viewDeleteCount(viewId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этот вид? Удаление приведёт к удалению $deletedItemCount контрактов")
                                    alertDialog.show()
                                }
                            }
                            contractId != null -> {
                                contractDeleteCount(contractId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этот контракт?")
                                    alertDialog.show()
                                }
                            }
                            clientId != null -> {
                                clientDeleteCount(clientId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этого клиента? Удаление приведёт к удалению $deletedItemCount контрактов")
                                    alertDialog.show()
                                }
                            }
                            contractStatusId != null -> {
                                contractStatusDeleteCount(contractStatusId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этот социальный статус? Удаление приведёт к удалению $deletedItemCount контрактов")
                                    alertDialog.show()
                                }
                            }
                            companyId != null -> {
                                companyDeleteCount(companyId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить эту компанию? Удаление приведёт к удалению $deletedItemCount контрактов")
                                    alertDialog.show()
                                }
                            }
                            branchId != null -> {
                                branchDeleteCount(branchId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этот филиал? Удаление приведёт к удалению $deletedItemCount контрактов?")
                                    alertDialog.show()
                                }
                            }
                        }
                    }
                ))

                underlayButtons.add(UnderlayButton(
                    this@ListActivity,
                    "Редактировать",
                    resources.getDrawable(R.drawable.penis, null),
                    Color.parseColor("#FFFFA726"),
                    false,
                    UnderlayButtonClickListener { position ->
                        val selectedItem = itemList[position]

                        when (selectedItem) {
                            is CityDto -> {
                                val cityId = selectedItem.cityId
                                cityEditValue.setText(selectedItem.value)

                                val saveButton = sheetEditCityView.findViewById<Button>(R.id.buttonEditCityBottom)
                                saveButton.setOnClickListener {
                                    val updatedCity = CityDto(selectedItem.cityId, cityEditValue.text.toString())
                                    updateCity(retrofit, cityId, updatedCity)
                                    sheetDialog.dismiss()
                                    loadCityFromServer()
                                }
                                sheetDialog.setContentView(sheetEditCityView)
                                sheetDialog.show()
                            }
                            is StatusDto -> {
                                val statusId = selectedItem.statusId

                                statusEditValue.setText(selectedItem.value)

                                val saveButton = sheetEditStatusView.findViewById<Button>(R.id.buttonEditStatusBottom)
                                saveButton.setOnClickListener {
                                    val updatedStatus = StatusDto(selectedItem.statusId, statusEditValue.text.toString())
                                    updateStatus(retrofit, statusId, updatedStatus)
                                    sheetDialog.dismiss()
                                    loadStatusFromServer()
                                }
                                sheetDialog.setContentView(sheetEditStatusView)
                                sheetDialog.show()
                            }
                            is TypeDto -> {
                                val typeId = selectedItem.typeId

                                typeEditText.setText(selectedItem.value)

                                val saveButton = sheetEditTypeView.findViewById<Button>(R.id.buttonEditTypeBottom)
                                saveButton.setOnClickListener {
                                    val updatedType = TypeDto(selectedItem.typeId, typeEditText.text.toString())
                                    updateType(retrofit, typeId, updatedType)
                                    sheetDialog.dismiss()
                                    loadTypeFromServer()
                                }
                                sheetDialog.setContentView(sheetEditTypeView)
                                sheetDialog.show()
                            }
                            is ViewDto -> {
                                val viewId = selectedItem.viewId

                                viewEditText.setText(selectedItem.value)

                                val saveButton = sheetEditViewView.findViewById<Button>(R.id.buttonEditViewBottom)
                                saveButton.setOnClickListener {
                                    val updatedView = ViewDto(selectedItem.viewId, viewEditText.text.toString())
                                    updateView(retrofit, viewId, updatedView)
                                    sheetDialog.dismiss()
                                    loadViewFromServer()
                                }
                                sheetDialog.setContentView(sheetEditViewView)
                                sheetDialog.show()
                            }
                            is ContractDto -> {
                                fetchDataContractStatus()
                                val contractId = selectedItem.contractId

                                val saveButton = sheetEditContractView.findViewById<Button>(R.id.buttonEditContractBottom)
                                saveButton.setOnClickListener {
                                    val updatedContract = ContractUpdateDto(
                                        selectedItem.contractId,
                                        spinnerEditContractStatus.selectedItem as ContractStatusDto)

                                    updateContract(retrofit, contractId, updatedContract)
                                    sheetDialog.dismiss()
                                    loadContractFromServer()
                                }
                                sheetDialog.setContentView(sheetEditContractView)
                                sheetDialog.show()
                            }
                            is ClientDto -> {
                                fetchDataStatus()
                                val clientId = selectedItem.clientId



                                lastnameEditText.setText(selectedItem.lastname)
                                firstnameEditText.setText(selectedItem.firstname)
                                middlenameEditText.setText(selectedItem.middlename)
                                phoneEditText.setText(selectedItem.phone)
                                clientBirthday.setText(selectedItem.birthday)

                                val saveButton = sheetEditClientView.findViewById<Button>(R.id.buttonEditClientBottom)
                                saveButton.setOnClickListener {
                                    val updatedClient = ClientDto(
                                        selectedItem.clientId,
                                        lastnameEditText.text.toString(),
                                        firstnameEditText.text.toString(),
                                        middlenameEditText.text.toString(),
                                        clientBirthday.text.toString(),
                                        phoneEditText.text.toString(),
                                        spinnerEditClientStatus.selectedItem as StatusDto)
                                    updateClient(retrofit, clientId, updatedClient)
                                    sheetDialog.dismiss()
                                    loadClientFromServer()
                                }
                                sheetDialog.setContentView(sheetEditClientView)
                                sheetDialog.show()
                            }
                            is ContractStatusDto -> {
                                val contractStatusId = selectedItem.contractStatusId

                                contractStatusEditText.setText(selectedItem.value)

                                val saveButton = sheetEditContractStatusView.findViewById<Button>(R.id.buttonEditContractStatusBottom)
                                saveButton.setOnClickListener {
                                    val updatedContractStatus = ContractStatusDto(selectedItem.contractStatusId, contractStatusEditText.text.toString())
                                    updateContractStatus(retrofit, contractStatusId, updatedContractStatus)
                                    sheetDialog.dismiss()
                                    loadContractStatusFromServer()
                                }
                                sheetDialog.setContentView(sheetEditContractStatusView)
                                sheetDialog.show()
                            }
                            is CompanyDto -> {
                                fetchDataCity()
                                fetchDataType()
                                val companyId = selectedItem.companyId

                                editCompanyName.setText(selectedItem.companyName)
                                editLicenseYear.setText(selectedItem.licenseYear)
                                editPhoneCompany.setText(selectedItem.phone)

                                val saveButton = sheetEditCompanyView.findViewById<Button>(R.id.buttonEditCompanyBottom)
                                saveButton.setOnClickListener {
                                    val updatedCompany = CompanyDto(
                                        selectedItem.companyId,
                                        editCompanyName.text.toString(),
                                        editLicenseYear.text.toString(),
                                        companyEditCitySpinner.selectedItem as CityDto,
                                        editPhoneCompany.text.toString(),
                                        companyEditTypeSpinner.selectedItem as TypeDto)
                                    updateCompany(retrofit, companyId, updatedCompany)
                                    sheetDialog.dismiss()
                                    loadCompanyFromServer()
                                }
                                sheetDialog.setContentView(sheetEditCompanyView)
                                sheetDialog.show()
                            }
                            is BranchDto -> {
                                fetchDataCity()
                                fetchDataCompany()
                                val branchId = selectedItem.branchId

                                editBranchName.setText(selectedItem.branchName)
                                editBranchAddress.setText(selectedItem.address)
                                editPhoneBranch.setText(selectedItem.branchPhone)
                                editEmployeeCount.setText(selectedItem.employeesCount)

                                val saveButton = sheetEditBranchView.findViewById<Button>(R.id.buttonEditBranchBottom)
                                saveButton.setOnClickListener {
                                    val updatedBranch = BranchDto(
                                        selectedItem.branchId,
                                        editBranchName.text.toString(),
                                        branchEditCitySpinner.selectedItem as CityDto,
                                        editBranchAddress.text.toString(),
                                        editPhoneBranch.text.toString(),
                                        editEmployeeCount.text.toString(),
                                        branchEditCompanySpinner.selectedItem as CompanyDto)
                                    updateBranch(retrofit, branchId, updatedBranch)
                                    sheetDialog.dismiss()
                                    loadBranchFromServer()
                                }
                                sheetDialog.setContentView(sheetEditBranchView)
                                sheetDialog.show()
                            }
                        }
                    }
                ))
            }
        }

        val listCounterTextView: TextView = findViewById(R.id.listCounterSostavnaya)
        val swipeHelperSecond = object : SwipeHelper(this, rvContracts) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                underlayButtons.add(UnderlayButton(
                    this@ListActivity,
                    "Удалить",
                    resources.getDrawable(R.drawable.trash, null),
                    Color.parseColor("#ff2b2b"),
                    true,
                    UnderlayButtonClickListener {
                        val contractId = (itemmList[it] as? ContractDto)?.contractId

                        var deletedItemCount = 0

                        val alertDialog = AlertDialog.Builder(this@ListActivity)
                            .setPositiveButton("Удалить") { _, _ ->
                                contractId?.let { contractId -> deleteContract(contractId) }
                                itemmList.removeAt(it)
                                mainAdapter1.notifyDataSetChanged()
                                val newCount = itemmList.size
                                listCounterTextView.text = "Количество найденых контрактов: $newCount"
                            }
                            .setNegativeButton("Отмена", null)
                            .create()

                        when {
                            contractId != null -> {
                                contractDeleteCount(contractId) { count ->
                                    deletedItemCount = count
                                    alertDialog.setMessage("Вы уверены, что хотите удалить этот контракт?")
                                    alertDialog.show()
                                }
                            }
                        }
                    }
                ))

                underlayButtons.add(UnderlayButton(
                    this@ListActivity,
                    "Редактировать",
                    resources.getDrawable(R.drawable.penis, null),
                    Color.parseColor("#FFFFA726"),
                    false,
                    UnderlayButtonClickListener { position ->
                        val selectedItem = itemmList[position]

                        when (selectedItem) {
                            is ContractDto -> {
                                fetchDataContractStatus()
                                val contractId = selectedItem.contractId
                                val clientId = (itemmList[position] as ContractDto).client.clientId


                                val saveButton = sheetEditContractView.findViewById<Button>(R.id.buttonEditContractBottom)
                                saveButton.setOnClickListener {
                                    val updatedContract = ContractUpdateDto(
                                        selectedItem.contractId,
                                        spinnerEditContractStatus.selectedItem as ContractStatusDto)

                                    val handler = Handler(Looper.getMainLooper())
                                    handler.postDelayed({
                                        if (clientId != null) {
                                            getContractsByClientId(clientId, onResponse)
                                        }
                                    }, 500L)

                                    updateContract(retrofit, contractId, updatedContract)
                                    sheetDialog.dismiss()
                                    mainAdapter1.notifyDataSetChanged()
                                }
                                sheetDialog.setContentView(sheetEditContractView)
                                sheetDialog.show()
                            }
                        }
                    }
                ))
            }
        }
    }


    fun getContractsByClientId(
        clientId: UUID,
        onResponse: (SearchContractDto) -> Unit
    ) {
        this.clientId = clientId
        val apiService = retrofit.create(ContractRetrofit::class.java)

        val call: Call<SearchContractDto> = apiService.getContractsByClientId(clientId)

        call.enqueue(object : Callback<SearchContractDto> {
            override fun onResponse(
                call: Call<SearchContractDto>, response: Response<SearchContractDto>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()

                    val contractList = result?.contract ?: emptyList()

                    mainAdapter1.updateData(contractList)

                    val count = result?.numberOfContract ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounterSostavnaya)
                    listCounterTextView.text = "Найдено контрактов клиента: $count"

                    onResponse(response.body()!!)
                } else {
                    showToast("Произошла ошибка при загрузке данных. Код: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SearchContractDto>, t: Throwable) {
                showToast("Запрос не удался: ${t.message}")
            }
        })
    }


    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //CITY
    fun loadCityFromServer() {
        val apiService = retrofit.create(CityRetrofit::class.java)
        val call: Call<List<CityDto>> = apiService.getCityList()

        call.enqueue(object : Callback<List<CityDto>> {
            override fun onResponse(
                call: Call<List<CityDto>>,
                response: Response<List<CityDto>>
            ) {
                if (response.isSuccessful) {
                    val cityList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(cityList)
                    mainAdapter.notifyDataSetChanged()
                    cityCount()
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<List<CityDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    fun deleteCity(cityId: UUID) {
        val service = retrofit.create(CityRetrofit::class.java)
        val call = service.deleteCityFromList(cityId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    cityCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun cityCount() {
        val service = retrofit.create(CityRetrofit::class.java)
        val call = service.cityCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество городов: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun cityDeleteCount(cityId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(CityRetrofit::class.java)
        val call = service.deleteCityCount(cityId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val deletedCount = response.body() ?: 0
                callback(deletedCount)
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    fun createCityOnServer(cityDto: CityDto) {
        val apiService = retrofit.create(CityRetrofit::class.java)
        val call: Call<CityDto> = apiService.createCity(cityDto)

        call.enqueue(object : Callback<CityDto> {
            override fun onResponse(
                call: Call<CityDto>,
                response: Response<CityDto>
            ) {
                if (response.isSuccessful) {
                    val createdCity = response.body()
                } else {
                    showToast("Произошла ошибка при создании города")
                }
            }

            override fun onFailure(call: Call<CityDto>, t: Throwable) {
                showToast("Произошла ошибка при создании города")
            }
        })
    }

    fun updateCity(retrofit: Retrofit, cityId: UUID, updatedCity: CityDto) {
        val retrofitService = retrofit.create(CityRetrofit::class.java)

        retrofitService.updateCity(cityId, updatedCity).enqueue(object : Callback<CityDto> {
            override fun onResponse(call: Call<CityDto>, response: Response<CityDto>) {
                if (response.isSuccessful) {
                    val updatedCityDto = response.body()
                } else {
                }
            }

            override fun onFailure(call: Call<CityDto>, t: Throwable) {
            }
        })
    }

    fun searchCities(query: String) {
        val apiService = retrofit.create(CityRetrofit::class.java)
        val call: Call<SearchCityDto> = apiService.searchCities(query)

        call.enqueue(object : Callback<SearchCityDto> {
            override fun onResponse(
                call: Call<SearchCityDto>,
                response: Response<SearchCityDto>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val cityList = result?.city ?: emptyList()

                    mainAdapter.updateData(cityList)

                    val count = result?.numberOfCities ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено городов: $count"
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<SearchCityDto>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    //STATUS
    fun loadStatusFromServer() {
        val apiService = retrofit.create(StatusRetrofit::class.java)
        val call: Call<List<StatusDto>> = apiService.getStatusList()

        call.enqueue(object : Callback<List<StatusDto>> {
            override fun onResponse(
                call: Call<List<StatusDto>>,
                response: Response<List<StatusDto>>
            ) {
                if (response.isSuccessful) {
                    val statusList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(statusList)
                    mainAdapter.notifyDataSetChanged()
                    statusCount()
                } else {
                    showToast("Произошла ошибка при загрузке статусов")
                }
            }

            override fun onFailure(call: Call<List<StatusDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке статусов")
            }
        })
    }


    fun deleteStatus(statusId: UUID) {
        val service = retrofit.create(StatusRetrofit::class.java)
        val call = service.deleteStatusFromList(statusId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    statusCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun statusCount() {
        val service = retrofit.create(StatusRetrofit::class.java)
        val call = service.statusCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество социальных положений: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun statusDeleteCount(statusId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(StatusRetrofit::class.java)
        val call = service.deleteStatusCount(statusId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val deletedCount = response.body() ?: 0 // Проверяем, что сервер вернул количество удаленных записей
                    callback(deletedCount)
                } else {
                    // Обработка неудачного запроса
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                // Обработка ошибки
            }
        })
    }

    fun createStatusOnServer(statusDto: StatusDto) {
        val apiService = retrofit.create(StatusRetrofit::class.java)
        val call: Call<StatusDto> = apiService.createStatus(statusDto)

        call.enqueue(object : Callback<StatusDto> {
            override fun onResponse(
                call: Call<StatusDto>,
                response: Response<StatusDto>
            ) {
                if (response.isSuccessful) {
                    val createdStatus = response.body()
                } else {
                    showToast("Произошла ошибка при создании города")
                }
            }

            override fun onFailure(call: Call<StatusDto>, t: Throwable) {
                showToast("Произошла ошибка при создании города")
            }
        })
    }

    fun updateStatus(retrofit: Retrofit, statusId: UUID, updatedStatus: StatusDto) {
        val retrofitService = retrofit.create(StatusRetrofit::class.java)

        retrofitService.updateStatus(statusId, updatedStatus).enqueue(object : Callback<StatusDto> {
            override fun onResponse(call: Call<StatusDto>, response: Response<StatusDto>) {
                val updatedStatusDto = response.body()
            }

            override fun onFailure(call: Call<StatusDto>, t: Throwable) {
            }
        })
    }

    fun searchStatus(query: String) {
        val apiService = retrofit.create(StatusRetrofit::class.java)
        val call: Call<SearchStatusDto> = apiService.searchStatus(query)

        call.enqueue(object : Callback<SearchStatusDto> {
            override fun onResponse(
                call: Call<SearchStatusDto>,
                response: Response<SearchStatusDto>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val statusList = result?.status ?: emptyList()

                    mainAdapter.updateData(statusList)

                    val count = result?.numberOfStatus ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено социальных статусов: $count"
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<SearchStatusDto>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    //TYPE
    fun loadTypeFromServer() {
        val apiService = retrofit.create(TypeRetrofit::class.java)
        val call: Call<List<TypeDto>> = apiService.getTypeList()

        call.enqueue(object : Callback<List<TypeDto>> {
            override fun onResponse(
                call: Call<List<TypeDto>>,
                response: Response<List<TypeDto>>
            ) {
                if (response.isSuccessful) {
                    val statusList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(statusList)
                    mainAdapter.notifyDataSetChanged()
                    typeCount()
                } else {
                    showToast("Произошла ошибка при загрузке статусов")
                }
            }

            override fun onFailure(call: Call<List<TypeDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке статусов")
            }
        })
    }

    fun deleteType(typeId: UUID) {
        val service = retrofit.create(TypeRetrofit::class.java)
        val call = service.deleteTypeFromList(typeId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    typeCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun typeCount() {
        val service = retrofit.create(TypeRetrofit::class.java)
        val call = service.typeCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество типов собственности: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun typeDeleteCount(typeId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(TypeRetrofit::class.java)
        val call = service.deleteTypeCount(typeId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val deletedCount = response.body() ?: 0
                callback(deletedCount)
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    fun createTypeOnServer(typeDto: TypeDto) {
        val apiService = retrofit.create(TypeRetrofit::class.java)
        val call: Call<TypeDto> = apiService.createType(typeDto)

        call.enqueue(object : Callback<TypeDto> {
            override fun onResponse(
                call: Call<TypeDto>,
                response: Response<TypeDto>
            ) {
                if (response.isSuccessful) {
                    val createdType = response.body()
                } else {
                    showToast("Произошла ошибка при создании города")
                }
            }

            override fun onFailure(call: Call<TypeDto>, t: Throwable) {
                showToast("Произошла ошибка при создании города")
            }
        })
    }

    fun updateType(retrofit: Retrofit, typeId: UUID, updatedType: TypeDto) {
        val retrofitService = retrofit.create(TypeRetrofit::class.java)

        retrofitService.updateType(typeId, updatedType).enqueue(object : Callback<TypeDto> {
            override fun onResponse(call: Call<TypeDto>, response: Response<TypeDto>) {
                val updatedTypeDto = response.body()
            }

            override fun onFailure(call: Call<TypeDto>, t: Throwable) {
            }
        })
    }

    fun searchType(query: String) {
        val apiService = retrofit.create(TypeRetrofit::class.java)
        val call: Call<SearchTypeDto> = apiService.searchType(query)

        call.enqueue(object : Callback<SearchTypeDto> {
            override fun onResponse(
                call: Call<SearchTypeDto>,
                response: Response<SearchTypeDto>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val typeList = result?.type ?: emptyList()

                    mainAdapter.updateData(typeList)

                    val count = result?.numberOfType ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено типов собственности: $count"
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<SearchTypeDto>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    //VIEW
    fun loadViewFromServer() {
        val apiService = retrofit.create(ViewRetrofit::class.java)
        val call: Call<List<ViewDto>> = apiService.getViewList()

        call.enqueue(object : Callback<List<ViewDto>> {
            override fun onResponse(
                call: Call<List<ViewDto>>,
                response: Response<List<ViewDto>>
            ) {
                if (response.isSuccessful) {
                    val statusList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(statusList)
                    mainAdapter.notifyDataSetChanged()
                    viewCount()
                } else {
                    showToast("Произошла ошибка при загрузке статусов")
                }
            }

            override fun onFailure(call: Call<List<ViewDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке статусов")
            }
        })
    }

    fun deleteView(viewId: UUID) {
        val service = retrofit.create(ViewRetrofit::class.java)
        val call = service.deleteViewFromList(viewId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    viewCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun viewCount() {
        val service = retrofit.create(ViewRetrofit::class.java)
        val call = service.viewCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество видов страхования: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun viewDeleteCount(viewId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(ViewRetrofit::class.java)
        val call = service.deleteViewCount(viewId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val deletedCount = response.body() ?: 0 // Проверяем, что сервер вернул количество удаленных записей
                    callback(deletedCount)
                } else {
                    // Обработка неудачного запроса
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                // Обработка ошибки
            }
        })
    }

    fun createViewOnServer(viewDto: ViewDto) {
        val apiService = retrofit.create(ViewRetrofit::class.java)
        val call: Call<ViewDto> = apiService.createView(viewDto)

        call.enqueue(object : Callback<ViewDto> {
            override fun onResponse(
                call: Call<ViewDto>,
                response: Response<ViewDto>
            ) {
                if (response.isSuccessful) {
                    val createdView = response.body()
                } else {
                    showToast("Произошла ошибка при создании города")
                }
            }

            override fun onFailure(call: Call<ViewDto>, t: Throwable) {
                showToast("Произошла ошибка при создании города")
            }
        })
    }

    fun updateView(retrofit: Retrofit, viewId: UUID, updatedView: ViewDto) {
        val retrofitService = retrofit.create(ViewRetrofit::class.java)

        retrofitService.updateView(viewId, updatedView).enqueue(object : Callback<ViewDto> {
            override fun onResponse(call: Call<ViewDto>, response: Response<ViewDto>) {
                val updatedViewDto = response.body()
            }

            override fun onFailure(call: Call<ViewDto>, t: Throwable) {
            }
        })
    }

    fun searchView(query: String) {
        val apiService = retrofit.create(ViewRetrofit::class.java)
        val call: Call<SearchViewDto> = apiService.searchView(query)

        call.enqueue(object : Callback<SearchViewDto> {
            override fun onResponse(
                call: Call<SearchViewDto>,
                response: Response<SearchViewDto>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val viewList = result?.view ?: emptyList()

                    mainAdapter.updateData(viewList)

                    val count = result?.numberOfView ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено видов страхования: $count"
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<SearchViewDto>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    //CONTRACT
    fun loadContractFromServer() {
        val apiService = retrofit.create(ContractRetrofit::class.java)
        val call: Call<List<ContractDto>> = apiService.getContractList()

        call.enqueue(object : Callback<List<ContractDto>> {
            override fun onResponse(
                call: Call<List<ContractDto>>,
                response: Response<List<ContractDto>>
            ) {
                if (response.isSuccessful) {
                    val contractList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(contractList)
                    mainAdapter.notifyDataSetChanged()
                    contractCount()
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<List<ContractDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    fun deleteContract(userId: UUID) {
        val service = retrofit.create(ContractRetrofit::class.java)
        val call = service.deleteContractFromList(userId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    contractCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun deleteContractClient(contractId: UUID) {
        val service = retrofit.create(ContractRetrofit::class.java)
        val call = service.deleteContractFromList(contractId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun contractCount() {
        val service = retrofit.create(ContractRetrofit::class.java)
        val call = service.contractCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество контрактов: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun contractDeleteCount(contractId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(ContractRetrofit::class.java)
        val call = service.deleteContractCount(contractId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val deletedCount = response.body() ?: 0
                callback(deletedCount)
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    fun createContractOnServer(contractCreateDto: ContractCreateDto) {
        val apiService = retrofit.create(ContractRetrofit::class.java)
        val call: Call<ContractCreateDto> = apiService.createContract(contractCreateDto)

        call.enqueue(object : Callback<ContractCreateDto> {
            override fun onResponse(
                call: Call<ContractCreateDto>,
                response: Response<ContractCreateDto>
            ) {
                if (response.isSuccessful) {
                    val createdContract = response.body()
                } else {
                    showToast("Произошла ошибка при создании контракта")
                }
            }

            override fun onFailure(call: Call<ContractCreateDto>, t: Throwable) {
                showToast("Произошла ошибка при создании контракта")
            }
        })
    }

    fun updateContract(retrofit: Retrofit, contractId: UUID, contractUpdateDto: ContractUpdateDto) {
        val retrofitService = retrofit.create(ContractRetrofit::class.java)

        retrofitService.updateContract(contractId, contractUpdateDto).enqueue(object : Callback<ContractUpdateDto> {
            override fun onResponse(call: Call<ContractUpdateDto>, response: Response<ContractUpdateDto>) {
                val updatedContractDto = response.body()
            }

            override fun onFailure(call: Call<ContractUpdateDto>, t: Throwable) {
            }
        })
    }

    fun searchContract(
        summ: String?,
        date: String?,
        viewId: UUID?,
        contractStatusId: UUID?,
        onResponse: (SearchContractDto) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val apiService = retrofit.create(ContractRetrofit::class.java)
        val call: Call<SearchContractDto> = apiService.SearchContracts(summ, date, viewId, contractStatusId)

        call.enqueue(object : Callback<SearchContractDto> {
            override fun onResponse(call: Call<SearchContractDto>, response: Response<SearchContractDto>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val contractList = result?.contract ?: emptyList()

                    mainAdapter.updateData(contractList)

                    val count = result?.numberOfContract ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено контрактов: $count"

                    onResponse(response.body()!!)
                } else {
                    onFailure(Throwable("Произошла ошибка при загрузке данных"))
                }
            }

            override fun onFailure(call: Call<SearchContractDto>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    //CLIENT
    fun loadClientFromServer() {
        val apiService = retrofit.create(ClientRetrofit::class.java)
        val call: Call<List<ClientDto>> = apiService.getClientList()

        call.enqueue(object : Callback<List<ClientDto>> {
            override fun onResponse(
                call: Call<List<ClientDto>>,
                response: Response<List<ClientDto>>
            ) {
                if (response.isSuccessful) {
                    val clientList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(clientList)
                    mainAdapter.notifyDataSetChanged()
                    clientCount()
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<List<ClientDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    fun deleteClient(clientId: UUID) {
        val service = retrofit.create(ClientRetrofit::class.java)
        val call = service.deleteClientFromList(clientId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    clientCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun clientCount() {
        val service = retrofit.create(ClientRetrofit::class.java)
        val call = service.clientCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество клиентов: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun clientDeleteCount(clientId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(ClientRetrofit::class.java)
        val call = service.deleteClientCount(clientId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val deletedCount = response.body() ?: 0 // Проверяем, что сервер вернул количество удаленных записей
                    callback(deletedCount)
                } else {
                    // Обработка неудачного запроса
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                // Обработка ошибки
            }
        })
    }

    fun createClientOnServer(clientDto: ClientDto) {
        val apiService = retrofit.create(ClientRetrofit::class.java)
        val call: Call<ClientDto> = apiService.createClient(clientDto)

        call.enqueue(object : Callback<ClientDto> {
            override fun onResponse(
                call: Call<ClientDto>,
                response: Response<ClientDto>
            ) {
                if (response.isSuccessful) {
                    val createdClient = response.body()
                } else {
                    showToast("Произошла ошибка при создании города")
                }
            }

            override fun onFailure(call: Call<ClientDto>, t: Throwable) {
                showToast("Произошла ошибка при создании города")
            }
        })
    }

    fun updateClient(retrofit: Retrofit, clientId: UUID, updatedClient: ClientDto) {
        val retrofitService = retrofit.create(ClientRetrofit::class.java)

        retrofitService.updateClient(clientId, updatedClient).enqueue(object : Callback<ClientDto> {
            override fun onResponse(call: Call<ClientDto>, response: Response<ClientDto>) {
                val updatedClientDto = response.body()
            }

            override fun onFailure(call: Call<ClientDto>, t: Throwable) {
            }
        })
    }

    fun searchClient(
        firstname: String?,
        lastname: String?,
        middlename: String?,
        birthday: String?,
        onResponse: (SearchClientDto) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val apiService = retrofit.create(ClientRetrofit::class.java)
        val call: Call<SearchClientDto> = apiService.SearchClient(firstname, lastname, middlename, birthday)

        call.enqueue(object : Callback<SearchClientDto> {
            override fun onResponse(call: Call<SearchClientDto>, response: Response<SearchClientDto>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val clientList = result?.client ?: emptyList()

                    mainAdapter.updateData(clientList)

                    val count = result?.numberOfClient ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено клиентов: $count"

                    onResponse(response.body()!!)
                } else {
                    onFailure(Throwable("Произошла ошибка при загрузке данных"))
                }
            }

            override fun onFailure(call: Call<SearchClientDto>, t: Throwable) {
                onFailure(t)
            }
        })
    }


    //CONTRACT_STATUS
    fun loadContractStatusFromServer() {
        val apiService = retrofit.create(ContractStatusRetrofit::class.java)
        val call: Call<List<ContractStatusDto>> = apiService.getContractStatusList()

        call.enqueue(object : Callback<List<ContractStatusDto>> {
            override fun onResponse(
                call: Call<List<ContractStatusDto>>,
                response: Response<List<ContractStatusDto>>
            ) {
                if (response.isSuccessful) {
                    val contractStatusDtoList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(contractStatusDtoList)
                    mainAdapter.notifyDataSetChanged()
                    contractStatusCount()
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<List<ContractStatusDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    fun deleteContractStatus(contractStatusId: UUID) {
        val service = retrofit.create(ContractStatusRetrofit::class.java)
        val call = service.deleteContractStatusFromList(contractStatusId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    contractStatusCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun contractStatusCount() {
        val service = retrofit.create(ContractStatusRetrofit::class.java)
        val call = service.contractStatusCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество контрактов: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun contractStatusDeleteCount(contractStatusId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(ContractStatusRetrofit::class.java)
        val call = service.deleteContractStatusCount(contractStatusId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val deletedCount = response.body() ?: 0
                callback(deletedCount)
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    fun createContractStatusOnServer(contractStatusDto: ContractStatusDto) {
        val apiService = retrofit.create(ContractStatusRetrofit::class.java)
        val call: Call<ContractStatusDto> = apiService.createContractStatus(contractStatusDto)

        call.enqueue(object : Callback<ContractStatusDto> {
            override fun onResponse(
                call: Call<ContractStatusDto>,
                response: Response<ContractStatusDto>
            ) {
                if (response.isSuccessful) {
                    val createdContractStatus = response.body()
                } else {
                    showToast("Произошла ошибка при создании города")
                }
            }

            override fun onFailure(call: Call<ContractStatusDto>, t: Throwable) {
                showToast("Произошла ошибка при создании города")
            }
        })
    }

    fun updateContractStatus(retrofit: Retrofit, contractStatusId: UUID, updatedContractStatus: ContractStatusDto) {
        val retrofitService = retrofit.create(ContractStatusRetrofit::class.java)

        retrofitService.updateContractStatus(contractStatusId, updatedContractStatus).enqueue(object : Callback<ContractStatusDto> {
            override fun onResponse(call: Call<ContractStatusDto>, response: Response<ContractStatusDto>) {
                val updatedContractStatusDto = response.body()
            }

            override fun onFailure(call: Call<ContractStatusDto>, t: Throwable) {
            }
        })
    }

    fun searchContractStatus(query: String) {
        val apiService = retrofit.create(ContractStatusRetrofit::class.java)
        val call: Call<SearchContractStatusDto> = apiService.searchContractStatus(query)

        call.enqueue(object : Callback<SearchContractStatusDto> {
            override fun onResponse(
                call: Call<SearchContractStatusDto>,
                response: Response<SearchContractStatusDto>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val contractStatusList = result?.contractStatus ?: emptyList()

                    mainAdapter.updateData(contractStatusList)

                    val count = result?.numberOfContractStatus ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено состояний контрактов: $count"
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<SearchContractStatusDto>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    //COMPANY
    fun loadCompanyFromServer() {
        val apiService = retrofit.create(CompanyRetrofit::class.java)
        val call: Call<List<CompanyDto>> = apiService.getCompanyList()

        call.enqueue(object : Callback<List<CompanyDto>> {
            override fun onResponse(
                call: Call<List<CompanyDto>>,
                response: Response<List<CompanyDto>>
            ) {
                if (response.isSuccessful) {
                    val companyList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(companyList)
                    mainAdapter.notifyDataSetChanged()
                    companyCount()
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<List<CompanyDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    fun deleteCompany(companyId: UUID) {
        val service = retrofit.create(CompanyRetrofit::class.java)
        val call = service.deleteCompanyFromList(companyId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    companyCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun companyCount() {
        val service = retrofit.create(CompanyRetrofit::class.java)
        val call = service.companyCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество компаний: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun companyDeleteCount(companyId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(CompanyRetrofit::class.java)
        val call = service.deleteCompanyCount(companyId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val deletedCount = response.body() ?: 0
                callback(deletedCount)
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    fun createCompanyOnServer(companyDto: CompanyDto) {
        val apiService = retrofit.create(CompanyRetrofit::class.java)
        val call: Call<CompanyDto> = apiService.createCompany(companyDto)

        call.enqueue(object : Callback<CompanyDto> {
            override fun onResponse(
                call: Call<CompanyDto>,
                response: Response<CompanyDto>
            ) {
                if (response.isSuccessful) {
                    val createdCompany = response.body()
                } else {
                    showToast("Произошла ошибка при создании города")
                }
            }

            override fun onFailure(call: Call<CompanyDto>, t: Throwable) {
                showToast("Произошла ошибка при создании города")
            }
        })
    }

    fun updateCompany(retrofit: Retrofit, companyId: UUID, updatedCompany: CompanyDto) {
        val retrofitService = retrofit.create(CompanyRetrofit::class.java)

        retrofitService.updateCompany(companyId, updatedCompany).enqueue(object : Callback<CompanyDto> {
            override fun onResponse(call: Call<CompanyDto>, response: Response<CompanyDto>) {
                val updatedCompanyDto = response.body()
            }

            override fun onFailure(call: Call<CompanyDto>, t: Throwable) {
            }
        })
    }

    fun searchCompany(
        companyName: String?,
        cityId: UUID?,
        onResponse: (SearchCompanyDto) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val apiService = retrofit.create(CompanyRetrofit::class.java)
        val call: Call<SearchCompanyDto> = apiService.SearchCompany(companyName, cityId)

        call.enqueue(object : Callback<SearchCompanyDto> {
            override fun onResponse(call: Call<SearchCompanyDto>, response: Response<SearchCompanyDto>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val companyList = result?.company ?: emptyList()

                    mainAdapter.updateData(companyList)

                    val count = result?.numberOfCompany ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено компаний: $count"

                    onResponse(response.body()!!)
                } else {
                    onFailure(Throwable("Произошла ошибка при загрузке данных"))
                }
            }

            override fun onFailure(call: Call<SearchCompanyDto>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    //BRANCH
    fun loadBranchFromServer() {
        val apiService = retrofit.create(BranchRetrofit::class.java)
        val call: Call<List<BranchDto>> = apiService.getBranchList()

        call.enqueue(object : Callback<List<BranchDto>> {
            override fun onResponse(
                call: Call<List<BranchDto>>,
                response: Response<List<BranchDto>>
            ) {
                if (response.isSuccessful) {
                    val branchList = response.body() ?: emptyList()
                    itemList.clear()
                    itemList.addAll(branchList)
                    mainAdapter.notifyDataSetChanged()
                    branchCount()
                } else {
                    showToast("Произошла ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<List<BranchDto>>, t: Throwable) {
                showToast("Произошла ошибка при загрузке данных")
            }
        })
    }

    fun deleteBranch(branchId: UUID) {
        val service = retrofit.create(BranchRetrofit::class.java)
        val call = service.deleteBranchFromList(branchId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    branchCount()
                    showToast("Удаление прошло успешно")
                } else {
                    showToast("Ошибка удаления записи")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Ошибка удаления записи")
            }
        })
    }

    fun branchCount() {
        val service = retrofit.create(BranchRetrofit::class.java)
        val call = service.branchCount()

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val count = response.body()
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Количество филиалов: $count"
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
            }
        })
    }

    fun branchDeleteCount(branchId: UUID, callback: (Int) -> Unit) {
        val service = retrofit.create(BranchRetrofit::class.java)
        val call = service.deleteBranchCount(branchId)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val deletedCount = response.body() ?: 0
                callback(deletedCount)
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
            }
        })
    }

    fun createBranchOnServer(branchDto: BranchDto) {
        val apiService = retrofit.create(BranchRetrofit::class.java)
        val call: Call<BranchDto> = apiService.createBranch(branchDto)

        call.enqueue(object : Callback<BranchDto> {
            override fun onResponse(
                call: Call<BranchDto>,
                response: Response<BranchDto>
            ) {
                if (response.isSuccessful) {
                    val createdBranch = response.body()
                } else {
                    showToast("Произошла ошибка при создании города")
                }
            }

            override fun onFailure(call: Call<BranchDto>, t: Throwable) {
                showToast("Произошла ошибка при создании города")
            }
        })
    }

    fun updateBranch(retrofit: Retrofit, branchId: UUID, updatedBranch: BranchDto) {
        val retrofitService = retrofit.create(BranchRetrofit::class.java)

        retrofitService.updateBranch(branchId, updatedBranch).enqueue(object : Callback<BranchDto> {
            override fun onResponse(call: Call<BranchDto>, response: Response<BranchDto>) {
                val updatedBranchDto = response.body()
            }

            override fun onFailure(call: Call<BranchDto>, t: Throwable) {
            }
        })
    }

    fun searchBranch(
        branchName: String?,
        cityId: UUID?,
        companyId: UUID?,
        onResponse: (SearchBranchDto) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val apiService = retrofit.create(BranchRetrofit::class.java)
        val call: Call<SearchBranchDto> = apiService.SearchBranch(branchName, cityId, companyId)

        call.enqueue(object : Callback<SearchBranchDto> {
            override fun onResponse(call: Call<SearchBranchDto>, response: Response<SearchBranchDto>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val branchList = result?.branch ?: emptyList()

                    mainAdapter.updateData(branchList)

                    val count = result?.numberOfBranch ?: 0
                    val listCounterTextView: TextView = findViewById(R.id.listCounter)
                    listCounterTextView.text = "Найдено филиалов: $count"

                    onResponse(response.body()!!)
                } else {
                    onFailure(Throwable("Произошла ошибка при загрузке данных"))
                }
            }

            override fun onFailure(call: Call<SearchBranchDto>, t: Throwable) {
                onFailure(t)
            }
        })
    }

}