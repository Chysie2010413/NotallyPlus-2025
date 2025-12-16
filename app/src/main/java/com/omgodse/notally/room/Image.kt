package com.notally.plus.room

import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(var name: String, val mimeType: String) : Attachment