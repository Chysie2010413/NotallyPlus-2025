package com.notally.plus.recyclerview

interface ItemListener {

    fun onClick(position: Int)

    fun onLongClick(position: Int)
}