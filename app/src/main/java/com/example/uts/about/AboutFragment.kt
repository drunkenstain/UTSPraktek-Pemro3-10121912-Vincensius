package com.example.uts.about

import android.app.AlertDialog
import android.content.pm.PackageInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.uts.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        val aboutButton: Button = view.findViewById(R.id.button_about)
        aboutButton.setOnClickListener {
            showAboutDialog()
        }

        return view
    }

    private fun showAboutDialog() {
        val packageInfo: PackageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        val versionName = packageInfo.versionName

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("About")
        dialogBuilder.setMessage("App Version: $versionName")
        dialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val aboutDialog = dialogBuilder.create()
        aboutDialog.show()
    }
}
