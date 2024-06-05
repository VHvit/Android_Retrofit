package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kp_2024_bd.R
import models.TypeDto

class SpinnerTypeAdapter(
    private val typeList: List<TypeDto>,
    private val context: Context
) : BaseAdapter() {

    override fun getCount(): Int {
        // Добавляем 1 для дополнительного элемента "Выберите тип"
        return typeList.size + 1
    }

    override fun getItem(position: Int): TypeDto? {
        // Если позиция равна 0, возвращаем null для дополнительного элемента "Выберите тип"
        return if (position == 0) {
            null
        } else {
            typeList[position - 1] // Учитываем смещение на 1 для реального списка типов
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.type_spinner_element, parent, false)
            viewHolder = ViewHolder()
            viewHolder.typeValue = convertView.findViewById(R.id.lolValue)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val type = getItem(position)

        viewHolder.typeValue.text = type?.value ?: "Выберите тип"

        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    private class ViewHolder {
        lateinit var typeValue: TextView
    }
}
