package com.example.uts.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.example.uts.R

fun showAboutDialog(context: Context) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_about_version, null)
    val versionTextView = dialogView.findViewById<TextView>(R.id.textViewAppVersion)

    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName
    versionTextView.text = "App Version: $versionName"

    val builder = AlertDialog.Builder(context)
    builder.setView(dialogView)
    builder.setPositiveButton("OK", null)
    builder.create().show()
}
