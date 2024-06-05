package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.kp_2024_bd.ListActivity
import com.example.kp_2024_bd.R
import models.*
import search_model.SearchContractDto

class MainAdapter(
    var itemList: MutableList<Any>,
    private val listActivity: ListActivity,  // Передаем ListActivity
    private val drawerLayout: DrawerLayout,
    private val onResponse: (SearchContractDto) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val CITY_TYPE = 1
    private val STATUS_TYPE = 2
    private val TYPE_TYPE = 3
    private val VIEW_TYPE = 4
    private val CONTRACT_TYPE = 5
    private val CLIENT_TYPE = 6
    private val CONTRACT_STATUS_TYPE = 7
    private val COMPANY_TYPE = 8
    private val BRANCH_TYPE = 9

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CITY_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.guide_item, parent, false)
                CityViewHolder(itemView)
            }
            STATUS_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.guide_item, parent, false)
                StatusViewHolder(itemView)
            }
            TYPE_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.guide_item, parent, false)
                TypeViewHolder(itemView)
            }
            VIEW_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.guide_item, parent, false)
                ViewViewHolder(itemView)
            }
            CONTRACT_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.contract_item, parent, false)
                ContractViewHolder(itemView)
            }
            CLIENT_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.client_item, parent, false)
                ClientViewHolder(itemView)
            }
            CONTRACT_STATUS_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.guide_item, parent, false)
                ContractStatusViewHolder(itemView)
            }
            COMPANY_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.company_item, parent, false)
                CompanyViewHolder(itemView)
            }
            BRANCH_TYPE -> {
                val itemView = layoutInflater.inflate(R.layout.branch_item, parent, false)
                BranchViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            CITY_TYPE -> {
                val viewHolder = holder as CityViewHolder
                val item = itemList[position] as CityDto
                viewHolder.bind(item)
            }
            STATUS_TYPE -> {
                val viewHolder = holder as StatusViewHolder
                val item = itemList[position] as StatusDto
                viewHolder.bind(item)
            }
            TYPE_TYPE -> {
                val viewHolder = holder as TypeViewHolder
                val item = itemList[position] as TypeDto
                viewHolder.bind(item)
            }
            VIEW_TYPE -> {
                val viewHolder = holder as ViewViewHolder
                val item = itemList[position] as ViewDto
                viewHolder.bind(item)
            }
            CONTRACT_TYPE -> {
                val viewHolder = holder as ContractViewHolder
                val item = itemList[position] as ContractDto
                viewHolder.bind(item)
            }
            CLIENT_TYPE -> {
                val viewHolder = holder as ClientViewHolder
                val item = itemList[position] as ClientDto

                viewHolder.bind(item)

                viewHolder.itemView.setOnClickListener {
                    if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                        drawerLayout.closeDrawer(GravityCompat.END)
                    } else {
                        drawerLayout.openDrawer(GravityCompat.END)
                    }

                    val clientId = item.clientId
                    listActivity.getContractsByClientId(clientId, onResponse)
                }
            }
            CONTRACT_STATUS_TYPE -> {
                val viewHolder = holder as ContractStatusViewHolder
                val item = itemList[position] as ContractStatusDto
                viewHolder.bind(item)
            }
            COMPANY_TYPE -> {
                val viewHolder = holder as CompanyViewHolder
                val item = itemList[position] as CompanyDto
                viewHolder.bind(item)
            }
            BRANCH_TYPE -> {
                val viewHolder = holder as BranchViewHolder
                val item = itemList[position] as BranchDto
                viewHolder.bind(item)
            }
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is CityDto -> CITY_TYPE
            is StatusDto -> STATUS_TYPE
            is TypeDto -> TYPE_TYPE
            is ViewDto -> VIEW_TYPE
            is ContractDto -> CONTRACT_TYPE
            is ClientDto -> CLIENT_TYPE
            is ContractStatusDto -> CONTRACT_STATUS_TYPE
            is CompanyDto -> COMPANY_TYPE
            is BranchDto -> BRANCH_TYPE
            else -> throw IllegalArgumentException("Invalid type of data at position $position")
        }
    }

    fun updateData(newList: List<Any>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }


    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityValue: TextView = itemView.findViewById(R.id.lolValue)

        fun bind(cityDto: CityDto) {
            cityValue.text = cityDto.value
        }
    }

    inner class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val statusValue: TextView = itemView.findViewById(R.id.lolValue)

        fun bind(statusDto: StatusDto) {
            statusValue.text = statusDto.value
        }
    }

    inner class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeValue: TextView = itemView.findViewById(R.id.lolValue)

        fun bind(typeDto: TypeDto) {
            typeValue.text = typeDto.value
        }
    }

    inner class ViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewValue: TextView = itemView.findViewById(R.id.lolValue)

        fun bind(viewDto: ViewDto) {
            viewValue.text = viewDto.value
        }
    }

    inner class ContractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val summ: TextView = itemView.findViewById(R.id.summContract)
        private val date: TextView = itemView.findViewById(R.id.dateContract)
        private val viewValue: TextView = itemView.findViewById(R.id.viewContract)
        private val firstname: TextView = itemView.findViewById(R.id.clientFirstnameContract)
        private val lastname: TextView = itemView.findViewById(R.id.clientLastnameContract)
        private val middlename: TextView = itemView.findViewById(R.id.clientMiddlenameContract)
        private val branchName: TextView = itemView.findViewById(R.id.branchNameContract)
        private val contractStatus: TextView = itemView.findViewById(R.id.contractStatus)

        fun bind(contractDto: ContractDto) {
            summ.text = contractDto.summ
            date.text = contractDto.date
            viewValue.text = contractDto.view.value
            firstname.text = contractDto.client.firstname
            lastname.text = contractDto.client.lastname
            middlename.text = contractDto.client.middlename
            branchName.text = contractDto.branch.branchName
            contractStatus.text = contractDto.contractStatus.value
        }
    }

    inner class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lastname: TextView = itemView.findViewById(R.id.lastname)
        private val firstname: TextView = itemView.findViewById(R.id.firstname)
        private val middlename: TextView = itemView.findViewById(R.id.middlename)
        private val date: TextView = itemView.findViewById(R.id.birthday)
        private val phone: TextView = itemView.findViewById(R.id.phone)
        private val status: TextView = itemView.findViewById(R.id.status)

        fun bind(clientDto: ClientDto) {
            firstname.text = clientDto.firstname
            lastname.text = clientDto.lastname
            middlename.text = clientDto.middlename
            date.text = clientDto.birthday
            phone.text = clientDto.phone
            status.text = clientDto.status.value
        }
    }

    inner class ContractStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contractStatusValue: TextView = itemView.findViewById(R.id.lolValue)

        fun bind(contractStatusDto: ContractStatusDto) {
            contractStatusValue.text = contractStatusDto.value
        }
    }

    inner class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val companyName: TextView = itemView.findViewById(R.id.companyName)
        private val yearLicense: TextView = itemView.findViewById(R.id.yearLicense)
        private val companyCity: TextView = itemView.findViewById(R.id.companyCity)
        private val companyPhone: TextView = itemView.findViewById(R.id.companyPhone)
        private val companyType: TextView = itemView.findViewById(R.id.companyType)

        fun bind(companyDto: CompanyDto) {
            companyName.text = companyDto.companyName
            yearLicense.text = companyDto.licenseYear
            companyCity.text = companyDto.city.value
            companyPhone.text = companyDto.phone
            companyType.text = companyDto.type.value
        }
    }

    inner class BranchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val branchName: TextView = itemView.findViewById(R.id.branchName)
        private val branchCity: TextView = itemView.findViewById(R.id.branchCity)
        private val branchAddress: TextView = itemView.findViewById(R.id.branchAddress)
        private val branchPhone: TextView = itemView.findViewById(R.id.branchPhone)
        private val employeeCount: TextView = itemView.findViewById(R.id.employeeCount)
        private val branchCompany: TextView = itemView.findViewById(R.id.branchCompany)

        fun bind(branchDto: BranchDto) {
            branchName.text = branchDto.branchName
            branchCity.text = branchDto.city.value
            branchAddress.text = branchDto.address
            branchPhone.text = branchDto.branchPhone
            employeeCount.text = branchDto.employeesCount
            branchCompany.text = branchDto.company.companyName
        }
    }

    fun removeMessage(pos: Int) {
        itemList = itemList.toMutableList().apply { removeAt(pos) }
        notifyItemRemoved(pos)
    }
}