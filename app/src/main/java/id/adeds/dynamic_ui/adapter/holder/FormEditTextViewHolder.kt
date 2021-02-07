package id.adeds.dynamic_ui.adapter.holder

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.util.Validation
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.molecule_form_edit_text.view.*

class FormEditTextViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        const val layout = R.layout.molecule_form_edit_text
    }

    fun bind(data: DynamicView) {
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        itemView.textTitle.text = title
        val inputType = when(data.jsonSchema.type) {
            "integer" -> InputType.TYPE_CLASS_NUMBER
            else -> InputType.TYPE_CLASS_TEXT
        }

        val prefix = when (data.uiSchemaRule.uiWidget) {
            "phone" -> "+62"
            "price" -> "Rp"
            else -> ""
        }
        itemView.textPrefix.let {
            it.text = prefix
            it.visible = prefix.isNotEmpty()
        }
        if (data.preview != null) itemView.editTextValue.setText(data.preview.toString())
        itemView.editTextValue.isEnabled = data.isEnable
        itemView.editTextValue.inputType = inputType
        itemView.editTextValue.isSingleLine = false

        itemView.editTextValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(string: Editable?) {
                if (itemView.editTextValue.text.hashCode() == string.hashCode()) {
                    var value: String? = itemView.editTextValue.text.toString()
                    value = if (value.isNullOrEmpty()) null else value.toString().trim()
                    val isValid = checkValidate(data.uiSchemaRule.uiWidget ?: "", value ?: "")
                    data.value = value
                    data.preview = value
                    data.isValidated = isValid
                    if (!isValid) data.errorValue = "Format ${data.jsonSchema.title} salah"
                }
            }
            override fun beforeTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        setError(data)
        itemView.buttonHelp.let {
            it.visible = data.uiSchemaRule.uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
    }

    fun checkValidate(widget: String, value: String): Boolean {
        return when (widget) {
            "email" -> Validation.isEmailValid(value)
            else -> true
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
}