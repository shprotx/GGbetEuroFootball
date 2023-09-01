package com.football.ggbeteurofootball.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.adapters.AdapterMatchWithStatistic
import com.football.ggbeteurofootball.adapters.AdapterMatchWithoutStatistic
import com.football.ggbeteurofootball.api.FootballApiImplementation
import com.football.ggbeteurofootball.data.ItemH2H
import com.football.ggbeteurofootball.data.ItemStatisticBody
import com.football.ggbeteurofootball.data.ItemStatisticHeader
import com.football.ggbeteurofootball.databinding.FragmentMatchDetailedBinding
import com.football.ggbeteurofootball.models.football.Response
import com.football.ggbeteurofootball.other.Checker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MatchDetailFragment : Fragment() {

    @Inject
    lateinit var editor: Editor
    @Inject
    lateinit var apiImplementation: FootballApiImplementation
    private lateinit var binding: FragmentMatchDetailedBinding
    private val viewModel = MainViewModel
    private val response: Response? = initResponse()
    private var isMatchInFavorites = false
    private val checker = Checker

    private val TAG = "MatchDetailFragment"


    companion object {
        private const val PICK_CONTACT_REQUEST = 1
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchDetailedBinding
            .bind(inflater.inflate(R.layout.fragment_match_detailed, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checker.checkInternet(requireContext())) {
            someBaseSettings()
            if (viewModel.currentType == 2) showWithStatistic()
            else showWithH2H()
            listeners(view)
        } else {
            findNavController().navigate(R.id.noInternetFragment)
        }

    }






    private fun initResponse(): Response? {
        var response: Response? = null
        for (match in viewModel.listLoadedFootball) {
            response =  match?.response?.find { it.fixture.id == viewModel.currentMatchId }
            if (response != null)
                break
        }
        return response
    }







    private fun someBaseSettings() {
        isMatchInFavorites = if (viewModel.favoriteMatches.contains(viewModel.currentMatchId)) {
            binding.favoriteButton.setImageResource(R.drawable.star_checked)
            true
        } else {
            binding.favoriteButton.setImageResource(R.drawable.star_unchecked)
            false
        }
    }








    private fun showWithStatistic() {
        lifecycleScope.launch(Dispatchers.IO) {
            apiImplementation.getDetailMatchByFixture(viewModel.currentMatchId) {
                if (it != null) {
                    val list = mutableListOf<ItemStatisticBody>()

                    val headerHomeValue = it.response[0].statistics[9].value?.substring(0, 2)?.toInt()?: 0
                    val headerAwayValue = if (headerHomeValue == 0) 0 else 100 - headerHomeValue
                    val headerItem = ItemStatisticHeader(headerHomeValue, headerAwayValue)

                    for (i in 0..15) {
                        if (i == 9) continue
                        val title = it.response[0].statistics[i].type
                        val homeValue = it.response[0].statistics[i].value?.let { it1 ->
                            convertToPercentlessInt(
                                it1
                            )
                        }
                        val awayValue = it.response[1].statistics[i].value?.let { it1 ->
                                convertToPercentlessInt(
                                    it1
                                )
                            }
                        list.add(ItemStatisticBody(title, homeValue, awayValue))
                    }

                    if (response != null) {
                        val adapter = AdapterMatchWithStatistic(
                            requireContext(),
                            list,
                            response,
                            headerItem
                        )
                        binding.recyclerMatchDetail.adapter = adapter
                    }

                } else {
                    binding.progressBarMain.isVisible = false
                    showWithH2H()
                }

            }
        }

    }








    private fun convertToPercentlessInt(input: String): Int? {
        val trimmedInput = input.trimEnd('%')
        return trimmedInput.toIntOrNull()
    }








    private fun showWithH2H() {
        if (response != null) {
            val h2h = mutableListOf<ItemH2H>()
            if (response.score.halftime.home != null)
                h2h.add(
                    ItemH2H(
                        response.teams.home.logo,
                        response.teams.away.logo,
                        convertDateFormat(response.fixture.date),
                        response.score.halftime.home!!,
                        response.score.halftime.away!!
                    )
                )
            if (response.score.fulltime.home != null)
                h2h.add(
                    ItemH2H(
                        response.teams.home.logo,
                        response.teams.away.logo,
                        convertDateFormat(response.fixture.date),
                        response.score.fulltime.home!!,
                        response.score.fulltime.away!!
                    )
                )
            if (response.score.extratime.home != null)
                h2h.add(
                    ItemH2H(
                        response.teams.home.logo,
                        response.teams.away.logo,
                        convertDateFormat(response.fixture.date),
                        response.score.extratime.home!!,
                        response.score.extratime.away!!
                    )
                )
            if (response.score.penalty.home != null)
                h2h.add(
                    ItemH2H(
                        response.teams.home.logo,
                        response.teams.away.logo,
                        convertDateFormat(response.fixture.date),
                        response.score.penalty.home!!,
                        response.score.penalty.away!!
                    )
                )

            val adapter = AdapterMatchWithoutStatistic(
                requireContext(),
                h2h,
                response,
                viewModel.currentType,
                viewModel.placeholderSize1,
                viewModel.placeholderSize2
            )
            binding.recyclerMatchDetail.adapter = adapter
       }
    }








    private fun convertDateFormat(inputDate: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)
        val outputDateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale.ENGLISH)

        val date: Date = inputDateFormat.parse(inputDate) ?: return ""

        return outputDateFormat.format(date)
    }








    private fun listeners(view: View) {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.favoriteButton.setOnClickListener {
            if (response != null) {
                isMatchInFavorites = !isMatchInFavorites
                if (isMatchInFavorites) {
                    viewModel.favoriteMatches.add(viewModel.currentMatchId)
                    saveFavoritesToPrefs()
                    binding.favoriteButton.setImageResource(R.drawable.star_checked)
                    showSnackBar(view, "Match was saved to favorites", "Cancel") {
                        viewModel.favoriteMatches.remove(viewModel.currentMatchId)
                        saveFavoritesToPrefs()
                        binding.favoriteButton.setImageResource(R.drawable.star_unchecked)
                        isMatchInFavorites = !isMatchInFavorites
                    }
                } else {
                    viewModel.favoriteMatches.remove(viewModel.currentMatchId)
                    viewModel.favoriteMatchesList.clear()
                    saveFavoritesToPrefs()
                    binding.favoriteButton.setImageResource(R.drawable.star_unchecked)
                    showSnackBar(view, "Match was deleted from favorites", "Cancel") {
                        viewModel.favoriteMatches.add(viewModel.currentMatchId)
                        saveFavoritesToPrefs()
                        binding.favoriteButton.setImageResource(R.drawable.star_checked)
                        isMatchInFavorites = !isMatchInFavorites
                        Log.d(TAG, "listeners: shack canceled")
                    }
                }
            } else {
                showSnackBar(view, "Something went wrong. Try again", "Back") {
                    findNavController().popBackStack()
                }
            }
        }

        binding.shareButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CONTACTS), 123132)
            } else {
                val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST)
            }
        }
    }
    







    private fun showSnackBar(
        view: View,
        message: String,
        button: String,
        toDo: () -> Unit
    ) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.parseColor("#FE8001"))
            .setBackgroundTint(Color.parseColor("#2A3040"))
            .setTextColor(Color.WHITE)
            .setAction(button) {
                toDo()
            }.show()
    }








    private fun intListToString(intList: MutableList<Int>): String {
        return intList.joinToString(",")
    }








    override fun onDestroyView() {
        super.onDestroyView()
        saveFavoritesToPrefs()
    }






    private fun saveFavoritesToPrefs() {
        val favorites = intListToString(viewModel.favoriteMatches)
        editor.putString("favorite", favorites)
        editor.apply()
    }






    private fun getMessageForSending(): String {
        return if (response != null) {
            "Look at this match! ${response.teams.home.name} VS ${response.teams.away.name} on ${convertDateTime(response.fixture.date)}"
        } else "I wanted to show you a very interesting match, but something went wrong."
    }





    private fun convertDateTime(dateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEE HH:mm", Locale.ENGLISH)
        val date: Date = inputFormat.parse(dateTime)
        return outputFormat.format(date)
    }







    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { contactUri ->
                val cursor = requireActivity().contentResolver.query(contactUri, null, null, null, null)
                cursor?.use {
                    if (cursor.moveToFirst()) {
                        if (cursor.getColumnIndex(ContactsContract.Contacts._ID) > -1) {
                            val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                            val phoneCursor = requireActivity().contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(contactId),
                                null
                            )
                            phoneCursor?.use {
                                if (phoneCursor.moveToFirst()) {
                                    val phoneNumber = phoneCursor.getString(
                                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                    val smsUri = Uri.parse("smsto:$phoneNumber")
                                    val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri)
                                    smsIntent.putExtra("sms_body", getMessageForSending())
                                    startActivity(smsIntent)
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}