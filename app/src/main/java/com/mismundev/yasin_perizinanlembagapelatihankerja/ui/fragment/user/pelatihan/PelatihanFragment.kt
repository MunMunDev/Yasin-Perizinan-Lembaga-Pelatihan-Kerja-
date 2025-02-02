package com.mismundev.yasin_perizinanlembagapelatihankerja.ui.fragment.user.pelatihan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mismundev.yasin_perizinanlembagapelatihankerja.databinding.FragmentPelatihanBinding

class PelatihanFragment : Fragment() {
    private lateinit var view : FragmentPelatihanBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view = FragmentPelatihanBinding.inflate(layoutInflater)
        return view.root

//        return inflater.inflate(R.layout.fragment_pelatihan, container, false)
    }
}