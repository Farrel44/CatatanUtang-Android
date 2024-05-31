package com.example.catathutang

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    private lateinit var db : DebtDatabaseHelper
    private lateinit var dataAdapter: DebtAdapter

    private var debtTotal = mutableListOf<DebtDatabaseHelper.Debt>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnDebtList = findViewById<Button>(R.id.btnDebtList)
        btnDebtList.setOnClickListener{
            val intent = Intent(this, debtListActivity::class.java)
            this.startActivity((intent))
        }

        db = DebtDatabaseHelper(this)
        debtTotal = db.getAllDebt().toMutableList()
        totalDebt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun totalDebt(){
        db = DebtDatabaseHelper(this)
        val tvTotalDebt = findViewById<TextView>(R.id.tvTotalDebt)
        var debt: List<Float>
        debt = db.getDebt()
        tvTotalDebt.text = debt.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        totalDebt()
    }
}