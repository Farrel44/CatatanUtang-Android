package com.example.catathutang

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.database.getFloatOrNull
import java.time.LocalDate

class DebtDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "debt.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE debts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount REAL,
                debtor TEXT,
                due_date TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS debts")
        onCreate(db)
    }


    data class Debt(val id: Int, val amount: Float, val debtor: String, val dueDate: String)


    fun getAllDebt(): List<Debt> {
        val debtList = mutableListOf<Debt>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM debts", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val amount = cursor.getFloat(cursor.getColumnIndexOrThrow("amount"))
                val debtor = cursor.getString(cursor.getColumnIndexOrThrow("debtor"))
                val dueDate = cursor.getString(cursor.getColumnIndexOrThrow("due_date"))
                val debt = Debt(id, amount, debtor, dueDate)
                debtList.add(debt)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return debtList
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion > 1 && newVersion <= 1) {
            // Handle schema downgrade to version 1

            // Drop the existing table
            db.execSQL("DROP TABLE IF EXISTS debts")

            // Recreate the table with the original schema
            onCreate(db)

            // Restore the data if needed
        }
    }

    fun insertDebt(debt: Debt) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("amount", debt.amount)
            put("debtor", debt.debtor)
            put("due_date", debt.dueDate)
        }
        db.insert("depts", null, values)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDebt(): List<Float> {
        val debtnumeral = ArrayList<Float>()
        val selectquery = "SELECT SUM(amount) FROM debts"
        val db = readableDatabase
        val cursor =db.rawQuery(selectquery, null)

        if(cursor.moveToFirst()){
            do{
                val debt = cursor.getFloat(cursor.getColumnIndexOrThrow("SUM(amount)"))
                debtnumeral.add(debt)
            }while (cursor.moveToNext())

        }
        cursor.close()
        return debtnumeral
    }

    fun getDebtGiver(): List<String> {
        val debt_giver = ArrayList<String>()
        val selectquery = "SELECT debt_giver FROM debts"
        val db = readableDatabase
        val cursor =db.rawQuery(selectquery, null)

        if(cursor.moveToFirst()){
            do{
                val giver = cursor.getString(cursor.getColumnIndexOrThrow("debt"))
                debt_giver.add(giver)
            }while (cursor.moveToNext())

        }
        cursor.close()
        return debt_giver
    }



    fun getDebtDate(): List<String> {
        val due_date = ArrayList<String>()
        val selectquery = "SELECT due_date FROM debts"
        val db = readableDatabase
        val cursor =db.rawQuery(selectquery, null)

        if(cursor.moveToFirst()){
            do{
                val date = cursor.getString(cursor.getColumnIndexOrThrow("debt"))
                due_date.add(date)
            }while (cursor.moveToNext())

        }
        cursor.close()
        return due_date
    }


    fun getTotalDebt():List<Float> {
        val debtTotal = mutableListOf<Float>()
        val db = readableDatabase

        val query = "SELECT debt FROM debts"
        val cursor = db.rawQuery(query, null)

        if (cursor != null && cursor.moveToFirst()) {
            val debtColumnIndex = cursor.getColumnIndexOrThrow("debt") // Get column index
            do {
                val debtValue = cursor.getFloat(debtColumnIndex)
                debtTotal.add(debtValue)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return debtTotal
    }

    fun deleteDebt(amount: Float): Int {
        val db = writableDatabase
        val success = db.delete("debts", "amount = ?", arrayOf(amount.toString()))
        db.close()
        return success
    }

    fun deleteDebtById(id: Int): Int {
        val db = writableDatabase
        val success = db.delete("debts", "id = ?", arrayOf(id.toString()))
        db.close()
        return success
    }
}