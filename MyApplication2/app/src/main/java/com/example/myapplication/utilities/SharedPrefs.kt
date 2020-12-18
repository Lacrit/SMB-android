package com.example.myapplication.utilities


import android.content.Context
import android.content.SharedPreferences
import android.view.Menu
import android.view.MenuInflater
import com.example.myapplication.R

open class SharedPrefs(context: Context) {
    var settings : SharedPreferences = context.getSharedPreferences("miniprojekt1",0);

    companion object {
        const val SMALL = "small"
        const val NORMAL = "normal"
        const val LARGE = "large"
        const val NIGHT_MODE = "NightMode"
        const val FONT_SIZE = "FontSize"
        const val LIST_MODE = "ListMode"
        const val INDIVIDUAL = "Individual"
        const val SHARED = "Shared"
    }

    init {
        val style : Int = when (getFontSizeState()) {
            SMALL -> if (getNightModeState()) R.style.darktheme_small else R.style.light_theme_common
            NORMAL ->  if (getNightModeState()) R.style.darktheme_normal else R.style.lighttheme_normal
            LARGE ->  if (getNightModeState()) R.style.darktheme_large else R.style.lighttheme_large
            else -> {
                if (getNightModeState())  R.style.dark_theme_common else R.style.light_theme_common
            }
        }

        context.setTheme(style)
    }

    fun setNightModeState(state : Boolean) {
        settings.edit().putBoolean(NIGHT_MODE, state).apply();
    }

    fun getNightModeState() : Boolean {
        return settings.getBoolean(NIGHT_MODE, false);
    }

    fun setFontSizeState(state : String) {
        settings.edit().putString(FONT_SIZE, state).apply();
    }

    fun getFontSizeState() : String? {
        return settings.getString(FONT_SIZE, SMALL);
    }

    fun setListModeState(state : String) {
        settings.edit().putString(LIST_MODE, state).apply();
    }

    fun getListModeState() : String? {
        return settings.getString(LIST_MODE, INDIVIDUAL);
    }

}