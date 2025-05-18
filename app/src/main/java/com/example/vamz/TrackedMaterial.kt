package com.example.vamz

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class TrackedMaterial(
    val id: Int,
    val nazovMaterialu: String,
    val lokacia: String,
    val imageRes: Int,
    val respawnTime: Long
) {
    fun isAvailable(): Boolean {
        return System.currentTimeMillis() >= respawnTime
    }

    fun getFormattedRespawnTime(): String {
        if (isAvailable()) {
            return "Dostupné"
        }

        val now = System.currentTimeMillis()
        val diff = respawnTime - now

        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60

        return String.format("%d h %d min", hours, minutes)
    }

    fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date(respawnTime))
    }
}

class MaterialTracker(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("material_tracker", Context.MODE_PRIVATE)
    private val _trackedMaterials = MutableStateFlow<List<TrackedMaterial>>(emptyList())
    val trackedMaterials: StateFlow<List<TrackedMaterial>> = _trackedMaterials.asStateFlow()

    init {
        loadTrackedMaterials()
    }

    private fun loadTrackedMaterials() {
        val trackedJson = preferences.getString("tracked_materials", "[]")
        val trackedList = mutableListOf<TrackedMaterial>()

        try {
            val jsonArray = JSONArray(trackedJson)
            for (i in 0 until jsonArray.length()) {
                val itemObject = jsonArray.getJSONObject(i)
                val nazovMaterialu = itemObject.getString("nazov")
                val lokacia = itemObject.getString("lokacia")
                val respawnTime = itemObject.getLong("respawn_time")
                val imageRes = itemObject.getInt("image_res")

                val id = trackedList.size + 1

                trackedList.add(
                    TrackedMaterial(
                        id = id,
                        nazovMaterialu = nazovMaterialu,
                        lokacia = lokacia,
                        imageRes = imageRes,
                        respawnTime = respawnTime
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        _trackedMaterials.value = trackedList
    }

    fun trackMaterial(material: Material, checked: Boolean) {
        val currentList = _trackedMaterials.value.toMutableList()

        currentList.removeIf {
            it.nazovMaterialu == material.nazovMaterialu && it.lokacia == material.lokacia
        }

        if (checked) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 2)
            val respawnTime = calendar.timeInMillis

            val newId = if (currentList.isEmpty()) 1 else currentList.maxOf { it.id } + 1

            currentList.add(
                TrackedMaterial(
                    id = newId,
                    nazovMaterialu = material.nazovMaterialu,
                    lokacia = material.lokacia,
                    imageRes = material.imageRes,
                    respawnTime = respawnTime
                )
            )
        }

        _trackedMaterials.value = currentList
        saveTrackedMaterials()
    }

    fun clearExpiredMaterials() {
        val currentTime = System.currentTimeMillis()
        val currentList = _trackedMaterials.value.toMutableList()

        currentList.removeIf { it.respawnTime <= currentTime }

        _trackedMaterials.value = currentList
        saveTrackedMaterials()
    }

    private fun saveTrackedMaterials() {
        val jsonArray = JSONArray()

        for (material in _trackedMaterials.value) {
            val itemObject = JSONObject().apply {
                put("nazov", material.nazovMaterialu)
                put("lokacia", material.lokacia)
                put("respawn_time", material.respawnTime)
                put("image_res", material.imageRes)
            }
            jsonArray.put(itemObject)
        }

        preferences.edit().putString("tracked_materials", jsonArray.toString()).apply()
    }

    fun getAvailableMaterials(allMaterials: List<Material>): List<Material> {
        val trackedMaterialPairs = _trackedMaterials.value.map {
            Pair(it.nazovMaterialu, it.lokacia)
        }

        return allMaterials.filter { material ->
            !trackedMaterialPairs.contains(Pair(material.nazovMaterialu, material.lokacia))
        }
    }
}