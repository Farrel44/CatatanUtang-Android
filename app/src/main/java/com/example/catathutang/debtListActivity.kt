package com.example.catathutang

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class debtListActivity : AppCompatActivity() {
    private lateinit var main: MainActivity
    private lateinit var db: DebtDatabaseHelper
    private lateinit var debtAdapter: DebtAdapter
    private var debtList = mutableListOf<DebtDatabaseHelper.Debt>() // Ensure correct type

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_list)

        // Initialize database helper
        db = DebtDatabaseHelper(this)

        // Fetch all debts and convert to mutable list
        debtList = db.getAllDebt().toMutableList()

        // Initialize the adapter with the debt list and a callback for deletion
        debtAdapter = DebtAdapter(debtList) { debt ->
            debtList.remove(debt)
            debtAdapter.notifyDataSetChanged()
            db.deleteDebtById(debt.id)

            Toast.makeText(this, "Debt Deleted", Toast.LENGTH_LONG).show()
        }

        // Setup RecyclerView
        val rvDebts = findViewById<RecyclerView>(R.id.rvDebts)
        rvDebts.layoutManager = LinearLayoutManager(this)
        rvDebts.adapter = debtAdapter

        // Initialize UI elements
        val etDebts = findViewById<EditText>(R.id.etDebts)
        val etDebtsGiver = findViewById<EditText>(R.id.etDebtsGiver)
        val dpDebtsDueDate = findViewById<DatePicker>(R.id.dpDebtsDueDate)
        val btnAddDebt = findViewById<Button>(R.id.btnAddDebt)

        // Set button click listener to add new debt
        btnAddDebt.setOnClickListener {
            val debtAmount = etDebts.text.toString().toFloatOrNull()
            val debtsGiver = etDebtsGiver.text.toString()
            val debtDueDate = "${dpDebtsDueDate.year}-${dpDebtsDueDate.month + 1}-${dpDebtsDueDate.dayOfMonth}"

            if (debtAmount != null && debtsGiver.isNotBlank()) {
                val newDebt = DebtDatabaseHelper.Debt(0, debtAmount, debtsGiver, debtDueDate)
                debtList.add(newDebt)
                debtAdapter.notifyItemInserted(debtList.size - 1)
                db.insertDebt(newDebt) // Make sure this method is defined in your db helper
                Toast.makeText(this, "Debt Added", Toast.LENGTH_LONG).show()
                etDebts.text.clear()
                etDebtsGiver.text.clear()
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show()
            }
        }
    }
}
