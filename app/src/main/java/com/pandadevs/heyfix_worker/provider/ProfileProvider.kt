package com.pandadevs.heyfix_worker.provider

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pandadevs.heyfix_worker.data.model.UserGet
import com.pandadevs.heyfix_worker.utils.datatype.Result
import kotlinx.coroutines.CompletableDeferred

class ProfileProvider {
    companion object {
        suspend fun updateProfile(imageUri: Uri, user: UserGet): Result<String> {
            val url = uploadUserImage(imageUri)
            if (url != null){
                user.picture = url.toString()
                updateUserData(user)
                return Result.success(user.picture)
            }else{
                return Result.error("error")
            }
        }

        fun updateUserData(user: UserGet): Result<String> {
            var result: Result<String>? = Result.success("data updated")
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.id)
                .set(user).addOnCompleteListener {
                    result = Result.success("data updated")
                }.addOnFailureListener {
                    result = Result.error("error: $it")
                }
            return result!!
        }

        suspend fun uploadUserImage(imageUri: Uri): String? {
            val def = CompletableDeferred<String?>()
            val imageFileName = "profile-picture/${System.currentTimeMillis()}.png"
            FirebaseStorage.getInstance().reference.child(imageFileName).putFile(imageUri)
                .addOnSuccessListener {
                    // Get download URL
                    FirebaseStorage.getInstance().reference.child(imageFileName).downloadUrl
                        .addOnCompleteListener { url ->
                            def.complete(if (url.isSuccessful) url.result.toString() else null)

                        }
                }
            return def.await()
        }
    }
}