package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.kp_2024_bd.R
import models.BranchDto

class SpinnerBranchAdapter(
    private var branchList: MutableList<BranchDto>,
    private val context: Context
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return branchList.size
    }

    override fun getItem(position: Int): BranchDto {
        return branchList[position]
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
            viewHolder.branchValue = convertView.findViewById(R.id.lolValue)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val branch = getItem(position)

        viewHolder.branchValue.text = branch.branchName ?: "Выберите филиал"

        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    fun updateData(newData: List<BranchDto>) {
        branchList.clear()
        branchList.addAll(newData)
        notifyDataSetChanged()
    }

    private class ViewHolder {
        lateinit var branchValue: TextView
    }
}
