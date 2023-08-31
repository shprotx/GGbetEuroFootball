package com.football.ggbeteurofootball.ui

import android.app.Dialog
import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.databinding.DialogCasheBinding

class CasheDialog(private val editor: Editor) : DialogFragment() {

    private val viewModel = MainViewModel
    private lateinit var binding: DialogCasheBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog)

        val inflater = requireActivity().layoutInflater
        binding = DialogCasheBinding.bind(inflater.inflate(R.layout.dialog_cashe, null))
        builder.setView(binding.root)

        binding.cancel.setOnClickListener { dismiss() }
        binding.delete.setOnClickListener {
            viewModel.favoriteMatches.clear()
            editor.putString("favorite", "")
            editor.apply()
            dismiss()
        }

        return builder.create()
    }

}