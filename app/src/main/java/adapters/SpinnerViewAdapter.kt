package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kp_2024_bd.R
import models.ViewDto

class SpinnerViewAdapter(
    private val viewList: List<ViewDto>,
    private val context: Context
) : BaseAdapter() {

    override fun getCount(): Int {
        // Добавляем 1 для дополнительного элемента "Выберите вид"
        return viewList.size + 1
    }

    override fun getItem(position: Int): ViewDto? {
        // Если позиция равна 0, возвращаем null для дополнительного элемента "Выберите вид"
        return if (position == 0) {
            null
        } else {
            viewList[position - 1] // Учитываем смещение на 1 для реального списка видов
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_spinner_element, parent, false)
            viewHolder = ViewHolder()
            viewHolder.viewValue = convertView.findViewById(R.id.lolValue)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val view = getItem(position)

        viewHolder.viewValue.text = view?.value ?: "Выберите вид"

        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    private class ViewHolder {
        lateinit var viewValue: TextView
    }
}
