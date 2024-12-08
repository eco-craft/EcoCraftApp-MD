package com.afrinacapstone.ecocraft.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afrinacapstone.ecocraft.data.model.CraftIdea

class HomeViewModel : ViewModel() {

    private val _craftIdeas = MutableLiveData<List<CraftIdea>>()
    val craftIdeas: LiveData<List<CraftIdea>> get() = _craftIdeas

    init {
        loadCraftIdeas()
    }

    private fun loadCraftIdeas() {
        val dummyCraftIdeas = listOf(
            //CraftIdea("Craft Idea 1", "https://example.com/image1.jpg"),
            //CraftIdea("Craft Idea 2", "https://example.com/image2.jpg"),
            //CraftIdea("Craft Idea 3", "https://example.com/image3.jpg")
        )

        _craftIdeas.value = dummyCraftIdeas
    }
}
