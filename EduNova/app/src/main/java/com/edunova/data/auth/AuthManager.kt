package com.edunova.data.auth

import android.content.Context

class AuthManager(context: Context) {

	private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

	fun isLoggedIn(): Boolean = getCurrentStudentId() != null

	fun getCurrentStudentId(): String? = prefs.getString(KEY_STUDENT_ID, null)

	fun setCurrentStudentId(studentId: String) {
		prefs.edit().putString(KEY_STUDENT_ID, studentId).apply()
	}

	fun logout() {
		prefs.edit().remove(KEY_STUDENT_ID).apply()
	}

	private companion object {
		const val PREFS_NAME = "edunova_auth_prefs"
		const val KEY_STUDENT_ID = "current_student_id"
	}
}


