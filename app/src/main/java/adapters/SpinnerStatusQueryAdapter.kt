package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kp_2024_bd.R
import models.StatusDto

class SpinnerStatusQueryAdapter(
    private var statusList: MutableList<StatusDto>,
    private val context: Context
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return statusList.size
    }

    override fun getItem(position: Int): StatusDto {
        return statusList[position]
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
            viewHolder.statusValue = convertView.findViewById(R.id.lolValue)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val status = getItem(position)

        viewHolder.statusValue.text = status.value

        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    fun updateData(newData: List<StatusDto>) {
        statusList.clear()
        statusList.addAll(newData)
        notifyDataSetChanged()
    }

    private class ViewHolder {
        lateinit var statusValue: TextView
    }
}
