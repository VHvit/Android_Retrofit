package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kp_2024_bd.R
import models.StatusDto

class SpinnerStatusAdapter(
    private val statusList: List<StatusDto>,
    private val context: Context
) : BaseAdapter() {

    override fun getCount(): Int {
        // Добавляем 1 для дополнительного элемента "Выберите статус"
        return statusList.size + 1
    }

    override fun getItem(position: Int): StatusDto? {
        // Если позиция равна 0, возвращаем null для дополнительного элемента "Выберите статус"
        return if (position == 0) {
            null
        } else {
            statusList[position - 1] // Учитываем смещение на 1 для реального списка статусов
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.status_spinner_element, parent, false)
            viewHolder = ViewHolder()
            viewHolder.statusValue = convertView.findViewById(R.id.lolValue)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val status = getItem(position)

        viewHolder.statusValue.text = status?.value ?: "Выберите статус"

        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    private class ViewHolder {
        lateinit var statusValue: TextView
    }
}
