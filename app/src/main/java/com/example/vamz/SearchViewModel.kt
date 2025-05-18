package com.example.vamz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SearchViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _materialy = MutableStateFlow(allMaterials)
    val materialy = searchText
        .combine(_materialy) { text, materials ->
            if (text.isBlank()) {
                materials
            } else {
                materials.filter { it.sediSVyhladavanim(text) }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _materialy.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
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