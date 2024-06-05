package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kp_2024_bd.R
import models.CompanyDto

class SpinnerCompanyAdapter(
    val companyList: List<CompanyDto>,
    private val context: Context
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return companyList.size + 1 // Добавляем 1 для дополнительного элемента "Выберите компанию"
    }

    override fun getItem(position: Int): CompanyDto? {
        return if (position > 0 && position <= companyList.size) {
            companyList[position - 1] // Учитываем дополнительный элемент "Выберите компанию"
        } else {
            null
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.city_spinner_element, parent, false)
            viewHolder = ViewHolder()
            viewHolder.companyValue = convertView.findViewById(R.id.lolValue)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val company = getItem(position)

        viewHolder.companyValue.text = company?.companyName ?: "Выберите компанию"

        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    private class ViewHolder {
        lateinit var companyValue: TextView
    }
}
