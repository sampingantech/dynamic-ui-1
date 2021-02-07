package id.adeds.dynamic_ui.adapter.holder

import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import android.widget.TableRow
import androidx.core.content.ContextCompat
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.molecule_form_check_box.view.*
import kotlinx.serialization.json.jsonArray

class FormCheckBoxViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        const val layout = R.layout.molecule_form_check_box
    }

    private val datas = ArrayList<String>()

    fun bind(data: DynamicView) {
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        if (data.preview != null) {
            val result = data.preview.toString().split(";")
            datas.addAll(result)
        }
        itemView.textTitle.text = title
        itemView.textDesc.let {
            it.visible = data.jsonSchema.description != ""
            it.text = data.jsonSchema.description
        }
        itemView.linearLayoutCheckBox.removeAllViews()
        var index = 0
        val value = data.jsonSchema.items?.getValue("enums")?.jsonArray?.map { it.toString() }
        value?.forEach {
            val checkBox = generateCheckBox(it, index++)
            if (data.isError)
                checkBox.buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.reddish)
                )
            checkBox.setOnClickListener { _ ->
                setValue(it, checkBox.isChecked)
                val values = if (datas.isEmpty()) null else datas.joinToString(";")
                data.value = values
                data.preview = values
            }
            if (datas.any { data -> data == it }) checkBox.isChecked
            checkBox.isEnabled = data.isEnable
            itemView.linearLayoutCheckBox.addView(checkBox)
        }
        setError(data)
        itemView.buttonHelp.let {
            it.visible = data.uiSchemaRule.uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
    }

    private fun generateCheckBox(value: String, index: Int) : CheckBox {
        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 24, 0, 0)
        val checkBox = CheckBox(itemView.context)
        checkBox.layoutParams = params
        checkBox.id = index
        checkBox.setPadding(36,0,0,0)
        checkBox.textSize = 12F
        checkBox.setTextColor(ContextCompat.getColor(itemView.context,R.color.brown_grey))
        checkBox.text = value
        checkBox.isChecked = datas.contains(value)
        return checkBox
    }

    private fun setError(data: DynamicView) {
        val color = if (data.isError && data.value == null) R.color.reddish else R.color.very_light_grey
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

    fun setValue(value: String, isSelected: Boolean) {
        if (isSelected) {
            datas.add(value)
        } else datas.remove(value)
    }
}