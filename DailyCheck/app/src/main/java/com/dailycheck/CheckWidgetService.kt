package com.dailycheck

import android.content.Context
import android.content.Intent
import android.text.Html
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.text.HtmlCompat

/** Fornisce le righe della lista al widget. */
class CheckWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        ChecklistFactory(applicationContext)
}

class ChecklistFactory(private val ctx: Context) : RemoteViewsService.RemoteViewsFactory {

    private var tasks: List<String> = emptyList()
    private var done: Set<Int> = emptySet()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        tasks = TaskStore.tasks(ctx)
        done = TaskStore.doneSet(ctx)
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(ctx.packageName, R.layout.widget_item)
        val task = tasks[position]
        val isDone = position in done

        rv.setTextViewText(R.id.item_check, if (isDone) "✓" else "")

        val safe = task.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        val text = if (isDone)
            HtmlCompat.fromHtml("<s>$safe</s>", Html.FROM_HTML_MODE_LEGACY)
        else task
        rv.setTextViewText(R.id.item_text, text)

        // quando la riga viene toccata, il provider riceve EXTRA_INDEX
        rv.setOnClickFillInIntent(
            R.id.item_row,
            Intent().putExtra(CheckWidgetProvider.EXTRA_INDEX, position)
        )
        return rv
    }

    override fun getCount() = tasks.size
    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount() = 1
    override fun getItemId(position: Int) = position.toLong()
    override fun hasStableIds() = true
    override fun onDestroy() {}
}
