package id.adeds.dynamic_ui.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.util.DateHelper
import id.adeds.dynamic_ui.dialog.DateTimePickerDialog
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.molecule_form_date_picker.view.*
import java.util.*

class FormDatePickerViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        const val layout = R.layout.molecule_form_date_picker
    }
    private val calender = Calendar.getInstance()

    fun bind(data: DynamicView) {
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        itemView.textTitle.text = title
        val picker =
            DateTimePickerDialog(itemView.context)
        itemView.frameAction.setOnClickListener {
            when (data.uiSchemaRule.uiWidget) {
                "date" -> {
                    picker.setValueOfDate(calender) {
                        setValue(data, "dd MMM yyyy")
                    }
                }
                "time" -> {
                    picker.setValueOfTime(calender) {
                        setValue(data, "HH:mm:ss")
                    }
                }
                else -> {
                    picker.setValueOfDate(calender) {
                        picker.setValueOfTime(calender) {
                            setValue(data, "dd MMM yyyy HH:mm:ss")
                        }
                    }
                }
            }
        }
        if (data.preview != null) itemView.textValue.text = data.preview as String
        itemView.frameAction.isEnabled = data.isEnable
        setError(data)
        itemView.buttonHelp.let {
            it.visible = data.uiSchemaRule.uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
    }

    private fun setError(data: DynamicView) {
        val color = if (data.isError) R.color.reddish else R.color.very_light_grey
        itemView.viewLine.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
        if (data.isError && data.value == null)
            itemView.textError.let {
                it.text = itemView.context.getString(R.string.text_can_not_empty, data.jsonSchema.title)
                it.visible = data.isError
            }
        else if (data.isError && data.value != null)
            itemView.textError.let {
                it.text = data.errorValue
                it.visible = data.errorValue != null
            }
        else itemView.textError.visible = false
    }

    fun setValue(data: DynamicView, pattern: String) {
        itemView.textValue.requestFocus()
        val preview = DateHelper.convertDateToString(calender.time, pattern)
        itemView.textValue.text = preview
        data.preview = preview
        data.value = DateHelper.convertDateToString(calender.time, "yyyy-MM-dd'T'HH:mm:ss'Z'")
    }
}