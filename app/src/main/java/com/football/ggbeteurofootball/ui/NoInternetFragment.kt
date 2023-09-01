package com.football.ggbeteurofootball.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.databinding.FragmentNoInternetBinding
import com.football.ggbeteurofootball.other.Checker
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class NoInternetFragment : Fragment() {

    private lateinit var binding: FragmentNoInternetBinding
    private val checker = Checker

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

        binding.swipeLayout.setOnRefreshListener {
            lifecycleScope.launch {
                while (isActive) {
                    delay(100)
                    if (checker.checkInternet(requireContext())) {
                        binding.swipeLayout.isRefreshing = false
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

}