package id.adeds.dynamic_ui.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.molecule_form_rating.view.*

class FormRatingViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        const val layout = R.layout.molecule_form_rating
    }

    fun bind(data: DynamicView) {
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        itemView.textTitle.text = title
        setError(data)
        itemView.buttonHelp.let {
            it.visible = data.uiSchemaRule.uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
        if (data.preview is Float) itemView.ratingBar.rating = data.preview as Float
        itemView.ratingBar.let {
            it.numStars = data.jsonSchema.maximum ?: 5
            it.setOnRatingBarChangeListener { _, value, _ ->
                data.value = value
                data.preview = value
            }
            it.isEnabled = data.isEnable
        }
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