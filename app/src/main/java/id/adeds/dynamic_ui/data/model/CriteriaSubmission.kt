package id.adeds.dynamic_ui.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CriteriaSubmission(
    val title: String,
    val help: String?,
    val helpImage: String?
) : Parcelable
