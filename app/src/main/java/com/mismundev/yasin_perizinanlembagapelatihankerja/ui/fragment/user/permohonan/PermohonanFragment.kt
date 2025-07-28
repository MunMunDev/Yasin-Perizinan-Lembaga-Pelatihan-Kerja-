package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.permohonan

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mismundev.yasin_perizinanlembagapelatihankerja.R

class PermohonanFragment : Fragment() {

    companion object {
        fun newInstance() = PermohonanFragment()
    }

    private val viewModel: PermohonanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_permohonan, container, false)
    }
}