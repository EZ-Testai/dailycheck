package com.dailycheck

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/** Elenco dei giorni passati: quante attività completate e dettaglio. */
class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        findViewById<MaterialButton>(R.id.back_button).setOnClickListener { finish() }

        val list = findViewById<ListView>(R.id.history_list)
        val empty = findViewById<TextView>(R.id.history_empty)

        val dates = TaskStore.historyDates(this)
        val tasks = TaskStore.tasks(this)

        if (dates.isEmpty()) {
            empty.visibility = View.VISIBLE
        }

        val rows = dates.map { date ->
            val doneCount = TaskStore.doneSet(this, date).size
            TaskStore.niceDate(date) + "  —  " + doneCount + "/" + tasks.size + " completate"
        }

        list.adapter = ArrayAdapter(this, R.layout.item_text, rows)

        list.setOnItemClickListener { _, _, pos, _ ->
            val date = dates[pos]
            val done = TaskStore.doneSet(this, date)
            val message = tasks
                .mapIndexed { i, t -> (if (i in done) "✅" else "⬜") + " " + t }
                .joinToString("
")
            AlertDialog.Builder(this)
                .setTitle(TaskStore.niceDate(date))
                .setMessage(message)
                .setPositiveButton("Chiudi", null)
                .show()
        }
    }
}
