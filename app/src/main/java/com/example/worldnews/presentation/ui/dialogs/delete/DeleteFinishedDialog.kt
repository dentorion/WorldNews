package com.example.worldnews.presentation.ui.dialogs.delete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.worldnews.R
import com.example.worldnews.domain.entity.Country
import com.example.worldnews.presentation.navigation.NavManager
import com.example.worldnews.presentation.ui.country.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeleteFinishedDialog(
    private val country: Country
) : DialogFragment() {

    private val vm: NewsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.clear_country_news, container, false)

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