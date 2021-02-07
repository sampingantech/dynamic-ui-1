package id.adeds.dynamic_ui.adapter.holder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.adapter.DotTextAdapter
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.util.fullExpanded
import id.adeds.dynamic_ui.util.visible
import kotlinx.android.synthetic.main.dialog_criteria_submission.view.*

abstract class BaseMoleculeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BaseMolecule {

    @SuppressLint("InflateParams")
    override fun criteriaDialog(data: DynamicView) {
        val bottomDialog = BottomSheetDialog(itemView.context)
        val layout = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_criteria_submission, null)
        bottomDialog.setContentView(layout)
        bottomDialog.show()
        bottomDialog.fullExpanded(layout)

        val title = data.jsonSchema.title
        val helpImage = data.uiSchemaRule.uiHelpImage
        val help = data.uiSchemaRule.uiHelp

        layout.textTitle.text = title
        layout.cardImageCriteria.visible = helpImage != null
        if(helpImage != null) {
            layout.imageCriteria.load(helpImage)
        }

        val criteriaList = help?.split(";") ?: return
        if (criteriaList.size == 1) {
            layout.textCriteria.let{
                it.text = help
                it.visible = true
            }
        }
        if (criteriaList.size > 1) {
            val dotTextAdapter = DotTextAdapter(criteriaList)
            layout.listCriteria.visible = true
            layout.listCriteria.adapter = dotTextAdapter
        }
    }

}

interface BaseMolecule {
    fun criteriaDialog(data: DynamicView)
}