package com.notally.plus.activities

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.notally.plus.R
import com.notally.plus.databinding.ActivityConfigureWidgetBinding
import com.notally.plus.miscellaneous.IO
import com.notally.plus.preferences.Preferences
import com.notally.plus.preferences.View
import com.notally.plus.recyclerview.ItemListener
import com.notally.plus.recyclerview.adapter.BaseNoteAdapter
import com.notally.plus.room.BaseNote
import com.notally.plus.room.Header
import com.notally.plus.room.NotallyDatabase
import com.notally.plus.viewmodels.BaseNoteModel
import com.notally.plus.widget.WidgetProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.Collections

class ConfigureWidget : AppCompatActivity(), ItemListener {

    private lateinit var adapter: BaseNoteAdapter
    private val id by lazy {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityConfigureWidgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = Intent()
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        setResult(RESULT_CANCELED, result)

        val preferences = Preferences.getInstance(application)

        val maxItems = preferences.maxItems
        val maxLines = preferences.maxLines
        val maxTitle = preferences.maxTitle
        val textSize = preferences.textSize.value
        val dateFormat = preferences.dateFormat.value
        val fullFormat = DateFormat.getDateInstance(DateFormat.FULL)
        val shortFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        val mediaRoot = IO.getExternalImagesDirectory(application)

        adapter = BaseNoteAdapter(Collections.emptySet(), dateFormat, textSize, maxItems, maxLines, maxTitle, fullFormat, shortFormat, mediaRoot, this)

        binding.RecyclerView.adapter = adapter
        binding.RecyclerView.setHasFixedSize(true)

        binding.RecyclerView.layoutManager = if (preferences.view.value == View.grid) {
            StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        } else LinearLayoutManager(this)

        val database = NotallyDatabase.getDatabase(application)

        val pinned = Header(getString(R.string.pinned))
        val others = Header(getString(R.string.others))

        lifecycleScope.launch {
            val notes = withContext(Dispatchers.IO) {
                val raw = database.getBaseNoteDao().getAllNotes()
                BaseNoteModel.transform(raw, pinned, others)
            }
            adapter.submitList(notes)
        }
    }


    override fun onClick(position: Int) {
        if (position != -1) {
            val preferences = Preferences.getInstance(application)
            val noteId = (adapter.currentList[position] as BaseNote).id
            preferences.updateWidget(id, noteId)

            val manager = AppWidgetManager.getInstance(this)
            WidgetProvider.updateWidget(this, manager, id, noteId)

            val success = Intent()
            success.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
            setResult(RESULT_OK, success)
            finish()
        }
    }

    override fun onLongClick(position: Int) {}
}