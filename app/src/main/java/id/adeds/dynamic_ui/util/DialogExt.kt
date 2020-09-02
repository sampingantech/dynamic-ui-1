package id.adeds.dynamic_ui.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun BottomSheetDialog.fullExpanded(view: View) {
    val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
    val behavior = params.behavior
    val displayMetrics = DisplayMetrics()
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    (behavior as BottomSheetBehavior).peekHeight = height
}