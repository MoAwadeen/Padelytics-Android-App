package grad.project.padelytics.features.auth.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import grad.project.padelytics.data.UserModel
import grad.project.padelytics.data.UserModelExtra

class AuthViewModel : ViewModel(){

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore


    fun signup(email: String, password: String, firstName: String, lastName: String,onResult: (Boolean, String?)-> Unit){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val userid = it.result.user?.uid ?: ""
                    val userModel = UserModel(firstName,lastName,userid,email,password)
                    firestore.collection("users")
                        .document(userid)
                        .set(userModel)
                        .addOnCompleteListener { dbTask ->
                            if(dbTask.isSuccessful){
                                onResult(true,null)
                            }else{
                                onResult(false,"Something went wrong")
                            }

                        }

            }else{
                onResult(false,it.exception?.localizedMessage)}
            }

    }

    fun addExtraFeature(gender: String, level: String, onResult: (Boolean, String?) -> Unit) {
        val userid = auth.currentUser?.uid ?: return onResult(false, "User not found")

        val userUpdates = mapOf(
            "gender" to gender,
            "level" to level
        )

        firestore.collection("users")
            .document(userid)
            .update(userUpdates)
            .addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, dbTask.exception?.localizedMessage ?: "Something went wrong")
                }
            }
    }


    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, it.exception?.localizedMessage)
                }

                }
    }

}