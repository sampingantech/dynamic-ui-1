package id.adeds.dynamic_ui.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.data.model.Location
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.molecule_form_location.view.*

class FormLocationViewHolder (itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        const val layout = R.layout.molecule_form_location
    }

    fun bind(data: DynamicView, itemClickListener:(widget: String, key: String) -> Unit) {
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        itemView.textTitle.text = title
        itemView.frameLocation.setOnClickListener {
            itemClickListener.invoke(data.uiSchemaRule.uiWidget!!, data.componentName)
        }

        if (data.preview != null) {
            when (data.preview) {
                is Location -> {
                    val result = data.preview as Location
                    data.value = "${result.lat},${result.long}"
                    itemView.textValue.text = result.addressName
                }
                else -> itemView.textValue.text = data.preview.toString()
            }
        }

        itemView.frameLocation.isEnabled = data.isEnable
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
}