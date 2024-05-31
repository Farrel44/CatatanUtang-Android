package com.example.catathutang

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DebtAdapter(
    private val debts: MutableList<DebtDatabaseHelper.Debt>,
    private val onDelete: (DebtDatabaseHelper.Debt) -> Unit
) : RecyclerView.Adapter<DebtAdapter.DebtViewHolder>() {

    inner class DebtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDebtAmount: TextView = itemView.findViewById(R.id.tvDebtValue)
        val tvDebtGiver: TextView = itemView.findViewById(R.id.tvDebtGiver)
        val tvDebtDueDate: TextView = itemView.findViewById(R.id.tvDebtDueDate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteDebt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_debt, parent, false)
        return DebtViewHolder(view)
    }

    override fun onBindViewHolder(holder: DebtViewHolder, position: Int) {
        val amount = debts[position]
        holder.tvDebtAmount.text = amount.amount.toLong().toString()
        holder.tvDebtGiver.text = amount.debtor
        holder.tvDebtDueDate.text = amount.dueDate

        holder.btnDelete.setOnClickListener {
            onDelete(amount)
        }
    }

    override fun getItemCount(): Int {
        return debts.size
    }
}
