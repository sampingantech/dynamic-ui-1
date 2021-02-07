package id.adeds.dynamic_ui.adapter.holder

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.molecule_form_radio.view.*

class FormRadioViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        const val layout = R.layout.molecule_form_radio
    }
    var name = ""
    private val params = RadioGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    fun bind(data: DynamicView) {
        var id = 0
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        itemView.textTitle.text = title
        val value = if (data.jsonSchema.enum != null) {
            data.jsonSchema.enum.map { it.toString() }
        } else {
            listOf("Ya", "Tidak")
        }
        itemView.radioGroup.removeAllViews()
        value.forEach {
            val radioButton = generateRadioButton(it, id++)
            radioButton.setOnClickListener { _ ->
                name = it
                if (data.jsonSchema.type == "boolean") {
                    data.value = name == "Ya"
                    data.preview = name == "Ya"
                }
                else {
                    data.value = name
                    data.preview = name
                }
            }
            if (data.isError)
                radioButton.buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.reddish)
                )
            if (data.preview != null) {
                val preview = when (data.jsonSchema.enum) {
                    null -> if (data.preview == true) "Ya" else "Tidak"
                    else -> data.preview.toString()
                }
                if (preview == it) radioButton.isChecked = true
            }
            radioButton.isEnabled = data.isEnable
            itemView.radioGroup.addView(radioButton)
        }
        setError(data)
        itemView.buttonHelp.let {
            it.visible = data.uiSchemaRule.uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
    }

    private fun generateRadioButton(value: String, id: Int): RadioButton {
        params.setMargins(0, 24, 0, 0)
        val button = RadioButton(itemView.context)
        button.setPadding(36, 0, 0, 0)
        button.textSize = 12F
        button.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown_grey))
        button.id = id
        button.layoutParams = params
        button.text = value
        button.isChecked = name == value
        return button
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
}