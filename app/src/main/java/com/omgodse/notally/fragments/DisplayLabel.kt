package com.notally.plus.fragments

import androidx.lifecycle.LiveData
import com.notally.plus.R
import com.notally.plus.miscellaneous.Constants
import com.notally.plus.room.Item

class DisplayLabel : NotallyFragment() {

    override fun getBackground() = R.drawable.label

    override fun getObservable(): LiveData<List<Item>> {
        val label = requireNotNull(requireArguments().getString(Constants.SelectedLabel))
        return model.getNotesByLabel(label)
    }
}