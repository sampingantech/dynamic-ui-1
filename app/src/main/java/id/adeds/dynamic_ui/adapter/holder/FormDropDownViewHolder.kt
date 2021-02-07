package id.adeds.dynamic_ui.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.adapter.DropDownRecyclerAdapter
import id.adeds.dynamic_ui.data.model.DropDownValue
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.util.afterTextChanged
import id.adeds.dynamic_ui.util.fullExpanded
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.dialog_drop_down.view.*
import kotlinx.android.synthetic.main.molecule_form_drop_down.view.*
import java.util.*
import kotlin.collections.ArrayList

class FormDropDownViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        const val layout = R.layout.molecule_form_drop_down
        const val LIMIT_SHOW_SEARCH = 15
    }

    fun bind(data: DynamicView) {
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        itemView.textTitle.text = title
        itemView.textDesc.let {
            it.text = data.jsonSchema.description
            it.visible = !data.jsonSchema.description.isNullOrEmpty()
        }
        val placeholder = data.uiSchemaRule.uiPlaceholder ?: data.jsonSchema.title

        var values = ArrayList<DropDownValue>()
        data.jsonSchema.enum?.map { DropDownValue(it.toString()) }?.let { values.addAll(it) }
        if (data.preview != null) values.find { it.value == data.preview }?.isSelected = true
        showSelectedValue(placeholder, values)

        itemView.frameValue.setOnClickListener {
            dropDownDialog(placeholder, values) { list ->
                values = list
                val selected = list.find { it.isSelected }?.value
                data.value = selected
                data.preview = selected
                showSelectedValue(placeholder, values)
            }
        }
        itemView.buttonHelp.let {
            it.visible = data.uiSchemaRule.uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
        setError(data)
    }

    private fun showSelectedValue(placeholder: String, list: ArrayList<DropDownValue>) {
        val selected = list.find { it.isSelected }
        itemView.textValue.text = selected?.value ?: placeholder
    }

    private fun dropDownDialog(
        title: String,
        list: ArrayList<DropDownValue>,
        callback: (ArrayList<DropDownValue>) -> Unit
    ) {
        val listCopy = ArrayList(list.map { it.copy() })
        val bottomDialog = BottomSheetDialog(itemView.context, R.style.DialogWithKeyboardStyle)
        val layout = View.inflate(itemView.context, R.layout.dialog_drop_down, null)
        bottomDialog.setContentView(layout)
        bottomDialog.fullExpanded(layout)
        val adapter = DropDownRecyclerAdapter(listCopy)
        with(layout) {
            editTextSearch.visible = list.size > LIMIT_SHOW_SEARCH
            textDialogTitle.text = title
            recyclerView.adapter = adapter
            imageClose.setOnClickListener { bottomDialog.dismiss() }
            buttonApply.setOnClickListener {
                callback.invoke(listCopy)
                bottomDialog.dismiss()
            }
            editTextSearch.afterTextChanged { query ->
                val filter = listCopy.filter {
                    it.value.toLowerCase(Locale.getDefault())
                        .contains(query.toString().toLowerCase(Locale.getDefault()))
                }
                adapter.showDataFilter(filter)
            }
        }
        bottomDialog.show()
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