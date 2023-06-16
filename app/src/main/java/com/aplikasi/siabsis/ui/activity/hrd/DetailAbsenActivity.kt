package com.aplikasi.siabsis.ui.activity.hrd

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.siabsis.data.model.Absen
import com.aplikasi.siabsis.databinding.ActivityDetailAbsenBinding
import com.aplikasi.siabsis.ui.adapter.AbsenAdapter
import com.google.firebase.database.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class DetailAbsenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAbsenBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var absenRecyclerView: RecyclerView
    private lateinit var list: ArrayList<Absen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        dbRef = FirebaseDatabase.getInstance().reference

        val intent = intent
        val absen = intent.getStringExtra("key")
        Log.d("TAG", "onCreate: $absen")

        getListAbsen(absen!!)

        absenRecyclerView = binding.recyclerViewAbsensi
        absenRecyclerView.layoutManager = LinearLayoutManager(this@DetailAbsenActivity)
        absenRecyclerView.setHasFixedSize(true)
        list = ArrayList<Absen>()
    }

    private fun getListAbsen(absen: String) {
        dbRef
            .child("absen")
            .child(absen)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                        val value = data.getValue(Absen::class.java)
                        list.add(value!!)
                        Log.d("TAG", "onDataChangevalue: $value")
                        binding.keterangan.text = list.size.toString()
                    }
                    absenRecyclerView.adapter = AbsenAdapter(list)
                    createXlxs(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: ${error.message}")
                }

            })
    }

    private fun createXlxs(data: java.util.ArrayList<Absen>) {
        try {
            val date = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(System.currentTimeMillis())
            val root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )
            if (!root.exists()) {
                root.mkdirs()
            }
            
            val path = File(root, "/$date.xlsx")
            val outputFileStream = FileOutputStream(path)
            val workBook = XSSFWorkbook()


            val sheet = workBook.createSheet("Absensi")
            val row = sheet.createRow(0)

            val cell = row.createCell(0)
            cell.setCellValue("No")

            val cell1 = row.createCell(1)
            cell1.setCellValue("Nama")

            val cell2 = row.createCell(2)
            cell2.setCellValue("Tanggal")

            val cell3 = row.createCell(3)
            cell3.setCellValue("Status")

            val cell4 = row.createCell(4)
            cell4.setCellValue("Keterangan")

            val cell5 = row.createCell(5)
            cell5.setCellValue("Tanggal Masuk")

            val cell6 = row.createCell(6)
            cell6.setCellValue("Tanggal Keluar")

            for (i in data.indices) {
                val row = sheet.createRow(i + 1)
                val cell = row.createCell(0)
                cell.setCellValue(i + 1.toDouble())
                val cell1 = row.createCell(1)
                cell1.setCellValue(data[i].nama)
                val cell2 = row.createCell(2)
                cell2.setCellValue(data[i].tanggal)
                val cell3 = row.createCell(3)
                cell3.setCellValue(data[i].status)
                val cell4 = row.createCell(4)
                cell4.setCellValue(data[i].keterangan)
                val cell5 = row.createCell(5)
                cell5.setCellValue(data[i].tanggal_masuk)
                val cell6 = row.createCell(6)
                cell6.setCellValue(data[i].tanggal_keluar)
            }

            workBook.write(outputFileStream)
            outputFileStream.flush()
            outputFileStream.close()
            Toast.makeText(this, "Berhasil membuat file excel", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "createXlxs: Berhasil membuat file excel")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "createXlxs: ${e.message}")
        }
    }
}