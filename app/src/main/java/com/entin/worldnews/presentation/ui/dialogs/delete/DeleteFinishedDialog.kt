package com.entin.worldnews.presentation.ui.dialogs.delete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.entin.worldnews.R
import com.entin.worldnews.domain.model.Country
import com.entin.worldnews.presentation.ui.country.components.CountryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFinishedDialog(
    private val country: Country
) : DialogFragment() {

    private val vm: CountryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.dialog_clear_country_news, container, false)

        rootView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            dismiss()
        }

        rootView.findViewById<TextView>(R.id.okBtn).setOnClickListener {
            vm.deleteNewsByCountry(country)
            dismiss()
        }

        return rootView
    }

}