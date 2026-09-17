package com.dailycheck

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/** Schermata per aggiungere / eliminare le attivita'. */
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = findViewById<EditText>(R.id.task_input)
        val addBtn = findViewById<MaterialButton>(R.id.add_button)
        val historyBtn = findViewById<MaterialButton>(R.id.history_button)
        val list = findViewById<ListView>(R.id.task_list)

        adapter = ArrayAdapter(this, R.layout.item_text, mutableListOf())
        list.adapter = adapter
        reload()

        addBtn.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            TaskStore.addTask(this, text)
            input.text.clear()
            reload()
            WidgetRefresher.refresh(this)
        }

        list.setOnItemClickListener { _, _, pos, _ ->
            val task = TaskStore.tasks(this)[pos]
            AlertDialog.Builder(this)
                .setTitle(task)
                .setItems(arrayOf("Elimina attività", "Annulla")) { dialog, which ->
                    if (which == 0) {
                        TaskStore.removeTask(this, pos)
                        reload()
                        WidgetRefresher.refresh(this)
                    }
                    dialog.dismiss()
                }
                .show()
        }

        historyBtn.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // se aperto dal tasto "+" del widget, porta il cursore nel campo testo
        if (intent.getBooleanExtra("add", false)) {
            input.requestFocus()
        }
    }

    private fun reload() {
        adapter.clear()
        adapter.addAll(TaskStore.tasks(this))
        adapter.notifyDataSetChanged()
    }
}
