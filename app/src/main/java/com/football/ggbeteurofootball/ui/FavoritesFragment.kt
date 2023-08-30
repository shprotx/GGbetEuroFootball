package com.football.ggbeteurofootball.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding
            .bind(inflater.inflate(R.layout.fragment_favorites, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hardCode()
        listeners()

    }

    private fun listeners() {
        binding.fab.setOnClickListener {
            binding.recyclerFavorites.scrollToPosition(0)
        }

        binding.drawerButton.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.open()
        }
    }


    private fun hardCode() {

    }
}