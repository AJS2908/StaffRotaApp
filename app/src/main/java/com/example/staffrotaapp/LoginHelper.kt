import com.google.firebase.auth.FirebaseAuth

class LoginHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(username: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // You can use the username as the email for authentication
        auth.signInWithEmailAndPassword("$username", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Unknown error occurred")
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