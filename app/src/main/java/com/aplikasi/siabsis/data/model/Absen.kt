package com.aplikasi.siabsis.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Absen (
    val nama: String? = null,
    val tanggal : String? = null,
    val divisi: String? = null,
    val qrCode: String? = null,
    val status: String? = null,
    val email: String? = null,
    val tanggal_masuk: String? = null,
    val tanggal_keluar: String? = null,
    val keterangan: String? = null,
    val lat : String? = null,
    val long : String? = null,
    val alamat : String? = null,
) : Parcelable