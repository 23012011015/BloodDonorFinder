package com.example.blooddonorfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.blooddonorfinder.databinding.ActivityMainBinding

data class Donor(val name: String, val blood: String, val contact: String)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val donors = mutableListOf<Donor>()
    private lateinit var adapter: DonorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Login: no username/password — just a welcome screen
        binding.btnContinue.setOnClickListener {
            binding.cardLogin.visibility = View.GONE
            binding.contentGroup.visibility = View.VISIBLE
            Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
        }

        // Spinner options for blood groups (pictures show in list items)
        val groups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
        binding.spBlood.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, groups)

        // RecyclerView
        adapter = DonorAdapter(donors) { donor ->
            Toast.makeText(this, "${donor.name} • ${donor.contact}", Toast.LENGTH_SHORT).show()
        }
        binding.rvDonors.adapter = adapter

        // Add Donor
        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim().orEmpty()
            val contact = binding.etContact.text?.toString()?.trim().orEmpty()
            val blood = binding.spBlood.selectedItem?.toString() ?: ""

            if (name.isEmpty() || blood.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            donors.add(Donor(name, blood, contact))
            adapter.notifyItemInserted(donors.lastIndex)

            binding.etName.text?.clear()
            binding.etContact.text?.clear()
        }
    }
}

/* ---------- Adapter with picture row ---------- */
class DonorAdapter(
    private val items: List<Donor>,
    private val onClick: (Donor) -> Unit
) : RecyclerView.Adapter<DonorAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvLine1: TextView = view.findViewById(R.id.tvLine1)
        val tvLine2: TextView = view.findViewById(R.id.tvLine2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donor, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.tvLine1.text = "${it.name}  •  ${it.blood}"
        holder.tvLine2.text = it.contact
        holder.itemView.setOnClickListener { _ -> onClick(it) }
    }

    override fun getItemCount(): Int = items.size
}
