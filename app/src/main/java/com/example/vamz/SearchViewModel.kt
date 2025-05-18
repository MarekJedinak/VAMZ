package com.example.vamz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _materialy = MutableStateFlow(allMaterials)

    private val materialTracker = MaterialTracker(application)
    val trackedMaterials = materialTracker.trackedMaterials

    val materialy = searchText
        .combine(_materialy) { text, materials ->
            val filtered = if (text.isBlank()) {
                materials
            } else {
                materials.filter { it.sediSVyhladavanim(text) }
            }

            materialTracker.getAvailableMaterials(filtered)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _materialy.value
        )

    private val _checkedMaterials = MutableStateFlow<MutableMap<Pair<String, String>, Boolean>>(mutableMapOf())
    val checkedMaterials: StateFlow<Map<Pair<String, String>, Boolean>> = _checkedMaterials.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun toggleMaterialChecked(material: Material, isChecked: Boolean) {
        val key = Pair(material.nazovMaterialu, material.lokacia)
        val currentChecked = _checkedMaterials.value.toMutableMap()
        currentChecked[key] = isChecked
        _checkedMaterials.value = currentChecked
    }

    fun saveMaterialSelections() {
        for ((pair, isChecked) in _checkedMaterials.value) {
            val (nazov, lokacia) = pair
            val material = allMaterials.find { it.nazovMaterialu == nazov && it.lokacia == lokacia }

            material?.let {
                materialTracker.trackMaterial(it, isChecked)
            }
        }

        _checkedMaterials.value = mutableMapOf()
    }

    fun clearExpiredMaterials() {
        materialTracker.clearExpiredMaterials()
    }
}

data class Material(
    val nazovMaterialu: String,
    val imageRes: Int,
    val lokacia: String
) {
    fun sediSVyhladavanim(query: String): Boolean {
        val searchTerms = listOf(
            nazovMaterialu,
            nazovMaterialu.split(" ").firstOrNull() ?: "",
            lokacia
        )
        return searchTerms.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

private val allMaterials = listOf(
    Material("White Iron Chunk", R.drawable.chizhangwallwhiteironchunk, "Chizhang Wall"),
    Material("White Iron Chunk", R.drawable.chiwangterracewhiteironchunk, "Chiwang Terrace"),
    Material("Crystal Chunk", R.drawable.mtlingmengcrystalchunk, "Mt. Lingmeng"),
    Material("Crystal Chunk", R.drawable.chizhangwallcrystalchunk, "Chizhang Wall"),
    Material("Magical Crystal Chunk", R.drawable.qiaoyingvillagemagicalcrystalchunk, "Qiaoying Village"),
    Material("Clearwater Jade", R.drawable.mtlingmengclearwaterjade, "Mt. Lingmeng"),
    Material("Clearwater Jade", R.drawable.chizhangwallclearwaterjade, "Chizhang Wall"),
    Material("Clearwater Jade", R.drawable.teatreeslopeclearwaterjade, "Teatree Slope"),
    Material("Clearwater Jade", R.drawable.yaodievalleyclearwaterjade, "Yaodie Valley"),
    Material("Qingxin", R.drawable.yaodievalleyqingxin, "Yaodie Valley"),
    Material("Qingxin", R.drawable.chiwangterraceqingxin, "Chiwang Terrace"),
    Material("Violetgrass", R.drawable.chiwangterracevioletgrass, "Chiwang Terrace")
)

class SearchViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}