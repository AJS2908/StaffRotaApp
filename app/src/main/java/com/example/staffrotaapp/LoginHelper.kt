
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class LoginHelper {

    private val TAG = "LoginHelper"
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(username: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error occurred"
                    Log.e(TAG, "Login failed: $errorMessage")
                    onFailure(errorMessage)
                }
            }
    }

    fun logoutUser() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUsername(): String? {
        // Return the username directly
        return auth.currentUser?.email
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }


}