package id.adeds.dynamic_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.adeds.dynamic_ui.adapter.holder.*
import id.adeds.dynamic_ui.data.model.DynamicView

class TaskSubmissionAdapter(
    private val itemClickListener: (widget: String, key: String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var datas: ArrayList<DynamicView> = ArrayList()

    companion object {
        const val EDIT_TEXT = 1
        const val FILE_PHOTO = 2
        const val DROPDOWN = 3
        const val CHECKBOX = 4
        const val RADIO = 5
        const val DATE_PICKER = 6
        const val LOCATION = 7
        const val RATING = 8
        const val ADDRESS = 9
    }

    fun setQuestions(list: ArrayList<DynamicView>) {
        datas = list
        notifyDataSetChanged()
    }

    fun updateItem(key: String) {
        val item = datas.find { it.componentName == key }
        notifyItemChanged(datas.indexOf(item))
    }

    fun refreshAdapter() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = when (viewType) {
            EDIT_TEXT -> FormEditTextViewHolder(getView(parent, FormEditTextViewHolder.layout))
            FILE_PHOTO -> FormFilePhotoViewHolder(getView(parent, FormFilePhotoViewHolder.layout))
            DROPDOWN -> FormDropDownViewHolder(getView(parent, FormDropDownViewHolder.layout))
            CHECKBOX -> FormCheckBoxViewHolder(getView(parent, FormCheckBoxViewHolder.layout))
            RADIO -> FormRadioViewHolder(getView(parent, FormRadioViewHolder.layout))
            DATE_PICKER -> FormDatePickerViewHolder(getView(parent, FormDatePickerViewHolder.layout))
            LOCATION -> FormLocationViewHolder(getView(parent, FormLocationViewHolder.layout))
            RATING -> FormRatingViewHolder(getView(parent, FormRatingViewHolder.layout))
            ADDRESS -> FormAddressViewHolder(getView(parent, FormAddressViewHolder.layout))
            else -> null
        }
        return view!!
    }

    private fun getView(parent: ViewGroup, view: Int): View {
        return LayoutInflater.from(parent.context).inflate(view, parent, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder.itemViewType
        val listData = datas[position]
        when (viewType) {
            EDIT_TEXT -> (holder as FormEditTextViewHolder).bind(listData )
            FILE_PHOTO -> (holder as FormFilePhotoViewHolder).bind(listData, itemClickListener)
            DROPDOWN -> (holder as FormDropDownViewHolder).bind(listData)
            CHECKBOX -> (holder as FormCheckBoxViewHolder).bind(listData)
            RADIO -> (holder as FormRadioViewHolder).bind(listData)
            DATE_PICKER -> (holder as FormDatePickerViewHolder).bind(listData)
            LOCATION -> (holder as FormLocationViewHolder).bind(listData, itemClickListener)
            RATING -> (holder as FormRatingViewHolder).bind(listData)
            ADDRESS -> (holder as FormAddressViewHolder).bind(listData)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (datas[position].uiSchemaRule.uiWidget) {
            "file", "photo", "camera" -> FILE_PHOTO
            "updown" -> EDIT_TEXT
            "select" -> DROPDOWN
            "checkboxes" -> CHECKBOX
            "radio" -> RADIO
            "fixedlocation", "dynamiclocation" -> LOCATION
            "date", "datetime", "time" -> DATE_PICKER
            "text", "textarea", "phone" -> EDIT_TEXT
            "rating" -> RATING
            "address" -> ADDRESS
            else -> EDIT_TEXT
        }
    }
}