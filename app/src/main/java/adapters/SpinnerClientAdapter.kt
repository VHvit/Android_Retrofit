package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kp_2024_bd.R
import models.ClientDto

class SpinnerClientAdapter(
    private val clientList: List<ClientDto>,
    private val context: Context
) : BaseAdapter() {

    override fun getCount(): Int {
        // Добавляем 1 для дополнительного элемента "Выберите клиента"
        return clientList.size + 1
    }

    override fun getItem(position: Int): ClientDto? {
        // Если позиция равна 0, возвращаем null для дополнительного элемента "Выберите клиента"
        return if (position == 0) {
            null
        } else {
            clientList[position - 1] // Учитываем смещение на 1 для реального списка клиентов
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.client_spinner_element, parent, false)
            viewHolder = ViewHolder()
            viewHolder.clientLastname = convertView.findViewById(R.id.lolLastname)
            viewHolder.clientFirstname = convertView.findViewById(R.id.lolFirstname)
            viewHolder.clientMiddlename = convertView.findViewById(R.id.lolMiddlename)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val client = getItem(position)

        if (client != null) {
            viewHolder.clientLastname.text = client.lastname
            viewHolder.clientFirstname.text = client.firstname
            viewHolder.clientMiddlename.text = client.middlename
        } else {
            // Если клиент равен null, устанавливаем текст "Выберите клиента"
            viewHolder.clientLastname.text = "Выберите клиента"
            viewHolder.clientFirstname.text = ""
            viewHolder.clientMiddlename.text = ""
        }

        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    private class ViewHolder {
        lateinit var clientLastname: TextView
        lateinit var clientFirstname: TextView
        lateinit var clientMiddlename: TextView
    }
}
