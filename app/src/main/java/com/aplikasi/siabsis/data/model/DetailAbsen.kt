package com.aplikasi.siabsis.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailAbsen(
    val nama: String? = null,
) : Parcelable