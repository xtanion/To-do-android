package com.example.todo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.todo.data.User

class Utility {
    fun openUrl(context:Context, url: String) {
        val uri: Uri = Uri.parse(url)
        val intent: Intent = Intent(Intent.ACTION_VIEW,uri)
        context.startActivity(intent)
    }

    fun crateFirebaseUid(user: User):String{
        val uid = StringBuilder()
        uid.append(Constants.UID_START).append(user.username).append(user.confirmPassword).append(Constants.UID_END)
        return uid.toString()
    }
}