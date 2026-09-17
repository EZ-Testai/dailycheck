package com.dailycheck

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CheckWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(ctx: Context, mgr: AppWidgetManager, ids: IntArray) {
        for (id in ids) update(ctx, mgr, id)
    }

    override fun onReceive(ctx: Context, intent: Intent) {
        super.onReceive(ctx, intent)
        if (intent.action == ACTION_TOGGLE) {
            val index = intent.getIntExtra(EXTRA_INDEX, -1)
            if (index >= 0) {
                TaskStore.toggle(ctx, index)
                val mgr = AppWidgetManager.getInstance(ctx)
                val ids = mgr.getAppWidgetIds(ComponentName(ctx, CheckWidgetProvider::class.java))
                mgr.notifyAppWidgetViewDataChanged(ids, R.id.widget_list)
            }
        }
    }

    companion object {
        const val ACTION_TOGGLE = "com.dailycheck.TOGGLE"
        const val EXTRA_INDEX = "index"

        fun update(ctx: Context, mgr: AppWidgetManager, id: Int) {
            val views = RemoteViews(ctx.packageName, R.layout.widget_layout)
            val today = LocalDate.now()
            val dayName = today.format(DateTimeFormatter.ofPattern("EEEE", Locale.ITALIAN))
                .replaceFirstChar { it.uppercase() }
            val dateStr = today.format(DateTimeFormatter.ofPattern("d MMMM", Locale.ITALIAN))
                .replaceFirstChar { it.uppercase() }

            views.setTextViewText(R.id.widget_day, dayName)
            views.setTextViewText(R.id.widget_date, dateStr)

            // la lista delle attivita'
            views.setRemoteAdapter(R.id.widget_list, Intent(ctx, CheckWidgetService::class.java))
            views.setEmptyView(R.id.widget_list, R.id.widget_empty)

            // tocco su una riga -> broadcast che commuta la spunta
            val template = Intent(ctx, CheckWidgetProvider::class.java).setAction(ACTION_TOGGLE)
            views.setPendingIntentTemplate(
                R.id.widget_list,
                PendingIntent.getBroadcast(
                    ctx, 0, template,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
            )

            // tocco sull'intestazione -> apri l'app
            views.setOnClickPendingIntent(
                R.id.widget_header,
                PendingIntent.getActivity(ctx, 1, Intent(ctx, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE)
            )

            // tasto "+" in basso -> apri l'app pronto per aggiungere
            val add = Intent(ctx, MainActivity::class.java).putExtra("add", true)
            views.setOnClickPendingIntent(
                R.id.widget_footer,
                PendingIntent.getActivity(ctx, 2, add, PendingIntent.FLAG_IMMUTABLE)
            )

            mgr.updateAppWidget(id, views)
        }
    }
}
