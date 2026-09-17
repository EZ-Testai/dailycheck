package com.dailycheck

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Salvataggio di tutti i dati in SharedPreferences, sul telefono.
 * - La lista attivita' e' unica (KEY_TASKS).
 * - Lo stato delle spunte e' salvato per giorno: "done_2026-09-17" -> set di indici.
 *   Cosi' il widget di ogni giorno resta consultabile (storico).
 */
object TaskStore {

    private const val PREFS = "dailycheck"
    private const val KEY_TASKS = "tasks"

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    // ---------- lista attivita' ----------

    fun tasks(ctx: Context): List<String> =
        prefs(ctx).getString(KEY_TASKS, null)
            ?.split("\n")
            ?.filter { it.isNotBlank() }
            ?: emptyList()

    fun addTask(ctx: Context, name: String) {
        if (name.isBlank()) return
        val list = tasks(ctx).toMutableList()
        list.add(name.trim())
        prefs(ctx).edit().putString(KEY_TASKS, list.joinToString("\n")).apply()
    }

    fun removeTask(ctx: Context, index: Int) {
        val list = tasks(ctx).toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            prefs(ctx).edit().putString(KEY_TASKS, list.joinToString("\n")).apply()
        }
    }

    // ---------- spunte per giorno ----------

    private fun doneKey(date: LocalDate) = "done_" + date.toString()

    fun doneSet(ctx: Context, date: LocalDate = LocalDate.now()): Set<Int> =
        prefs(ctx).getStringSet(doneKey(date), emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
            ?: emptySet()

    fun toggle(ctx: Context, index: Int) {
        val key = doneKey(LocalDate.now())
        val set = prefs(ctx).getStringSet(key, emptySet())!!.toMutableSet()
        if (!set.add(index.toString())) set.remove(index.toString())
        prefs(ctx).edit().putStringSet(key, set).apply()
    }

    // ---------- storico ----------

    fun historyDates(ctx: Context): List<LocalDate> =
        prefs(ctx).all.keys
            .filter { it.startsWith("done_") }
            .mapNotNull { runCatching { LocalDate.parse(it.removePrefix("done_")) }.getOrNull() }
            .sortedDescending()

    fun niceDate(date: LocalDate): String =
        date.format(DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.ITALIAN))
            .replaceFirstChar { it.uppercase() }
}

/** Dice ai widget sulla home che i dati sono cambiati e li fa aggiornare. */
object WidgetRefresher {
    fun refresh(ctx: Context) {
        val mgr = AppWidgetManager.getInstance(ctx)
        val ids = mgr.getAppWidgetIds(ComponentName(ctx, CheckWidgetProvider::class.java))
        mgr.notifyAppWidgetViewDataChanged(ids, R.id.widget_list)
    }
}
