package com.entin.worldnews.presentation.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun simpleShortSnackBar(view: View, msg: String) =
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

fun simpleLongSnackBar(view: View, msg: String) =
    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()