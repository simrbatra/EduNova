package com.edunova.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

object NavigationUtils {
    fun navigateWithAnimation(
        navController: NavController,
        destinationId: Int
    ) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(android.R.anim.fade_in)
            .setExitAnim(android.R.anim.fade_out)
            .setPopEnterAnim(android.R.anim.fade_in)
            .setPopExitAnim(android.R.anim.fade_out)
            .build()
        
        navController.navigate(destinationId, navOptions)
    }
}

