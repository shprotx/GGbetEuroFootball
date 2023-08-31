package com.football.ggbeteurofootball.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.databinding.FragmentNoInternetBinding

class NoInternetFragment : Fragment() {

    private lateinit var binding: FragmentNoInternetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoInternetBinding
            .bind(inflater.inflate(R.layout.fragment_no_internet, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}