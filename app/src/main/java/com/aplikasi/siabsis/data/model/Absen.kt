package com.aplikasi.siabsis.data.model

data class Absen (
    var nama: String? = null,
    var divisi: String? = null,
    var qrCode: String? = null,
    var status: String? = null,
    var email: String? = null,
    var tanggal_masuk: String? = null,
    var tanggal_keluar: String? = null,
    var keterangan: String? = null,
)