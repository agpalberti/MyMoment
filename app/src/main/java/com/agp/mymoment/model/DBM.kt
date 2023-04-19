package com.agp.mymoment.model

import android.net.Uri
import android.util.Log
import com.agp.mymoment.config.MyPreferences
import com.agp.mymoment.model.classes.Post
import com.agp.mymoment.model.classes.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DBM {

    companion object {
        fun onLogin(email: String, password: String, callback: (Int) -> Unit) {
            //todo gestionar bloqueo cuenta
            try {
                if (email.isNotBlank() && password.isNotEmpty()) {
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnFailureListener { e ->
                            Log.w("Login", "Error al iniciar sesión", e)
                            when (e) {
                                is FirebaseAuthInvalidCredentialsException -> {
                                    Log.w("Login", "7: Contraseña incorrecta")
                                    callback(7)
                                } //"Datos incorrectos"
                                is FirebaseAuthInvalidUserException -> {
                                    Log.w("Login", "8: Usuario inexistente")
                                    callback(8)
                                } //"Usuario no existe"
                                else -> {
                                    Log.e("Register", "-1: Error no gestionado de Firebase", e)
                                    callback(-1)
                                } // "Error de login no gestionado"
                            }
                        }
                        //Si no devuelve errores da un mensaje de éxito y pasa a la siguiente pantalla
                        .addOnSuccessListener {
                            Log.i("Login", "Logeado correctamente")
                            callback(0)
                        }
                } else {
                    if (email.isBlank()) {
                        Log.w("Login", "3: El email no puede estar vacío")
                        callback(3)
                    }
                    if (password.isEmpty()) {
                        Log.w("Login", "4: La contraseña no puede estar vacía")
                        callback(4)
                    }
                }
            } catch (error: Exception) {
                Log.e("Login", "-2: Error no controlado", error)
                callback(-2) // Error genérico no gestionado
            }
        }

        fun onRegister(
            email: String,
            password: String,
            nickname: String,
            name: String,
            callback: (Int) -> Unit
        ) {
            try {
                if (email.isNotBlank() && password.isNotEmpty() && nickname.isNotBlank() && name.isNotBlank()) {
                    val mAuth = FirebaseAuth.getInstance()
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnFailureListener { e ->
                            when (e) {
                                is FirebaseAuthUserCollisionException -> {
                                    Log.w("Register", "1: Correo electrónico ya registrado", e)
                                    callback(1)
                                }
                                is FirebaseAuthWeakPasswordException -> {
                                    Log.w("Register", "2: Contraseña demasiado débil", e)
                                    callback(2)
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    Log.w("Register", "9: Formato de correo incorrecto", e)
                                    callback(9)
                                }
                                else -> {
                                    Log.e("Register", "-1: Error no gestionado de Firebase", e)
                                    callback(-1)
                                }
                            }
                        }
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.i("Register", "Usuario creado correctamente")
                                val user = mAuth.currentUser
                                val db = FirebaseFirestore.getInstance()
                                val userMap = User(
                                    name,
                                    nickname,
                                    MyPreferences.resources?.getString(com.agp.mymoment.R.string.default_desc)
                                        ?: "",
                                    emptyList(),
                                    emptyList(),
                                    emptyList()
                                )
                                db.collection("users")
                                    .document(user?.uid!!)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        Log.i(
                                            "Register",
                                            "Usuario creado exitosamente y datos almacenados en Firestore"
                                        )
                                        callback(0)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(
                                            "Register",
                                            "-1: Error almacenando datos del usuario en Firestore",
                                            e
                                        )
                                        callback(-1)
                                    }
                            } else {
                                Log.e("Register", "-1: Error al crear usuario", task.exception)
                                callback(-1)

                            }
                        }
                } else {
                    if (email.isBlank()) {
                        Log.w("Register", "3: El email no puede estar vacío")
                        callback(3)
                    }
                    if (password.isEmpty()) {

                        Log.w("Register", "4: La contraseña no puede estar vacía")
                        callback(4)
                    }
                    if (name.isBlank()) {

                        Log.w("Register", "5: El nombre no puede estar vacío")
                        callback(5)
                    }
                    if (nickname.isBlank()) {
                        Log.w("Register", "6: El nickname no puede estar vacío")
                        callback(6)
                    }
                }
            } catch (error: Exception) {
                Log.e("Register", "-2: Error no gestionado", error)
                callback(-2)
            }
        }

        fun onLogOut() {
            FirebaseAuth.getInstance().signOut()
        }

        fun uploadImage(image: File): Task<Pair<Uri, String>> {
            val db = Firebase.storage.reference.child("users")
            val date = Date()
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val ref = db.child("${Firebase.auth.uid}/posts/${dateFormat.format(date)}.png")
            return ref.putBytes(image.readBytes()).continueWithTask { task ->
                if (!task.isSuccessful) {
                    Log.e("Camara", "Firebase: Error al subir nuevo post", task.exception)
                    task.exception?.let { throw it }
                }
                Log.i("Camara", "La foto se ha subido correctamente")
                ref.downloadUrl
            }.continueWith { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                Pair(task.result!!, dateFormat.format(date))
            }
        }

        suspend fun uploadNewPost(image: File) {
            if (Firebase.auth.currentUser != null) {
                var user: User?
                user = getUserData(getLoggedUserUid()).first()
                uploadImage(image)
                    .addOnSuccessListener { pair ->
                        val post = Post(
                            date = pair.second,
                            likes = emptyList(),
                            download_link = pair.first.toString()
                        )
                        val db = FirebaseFirestore.getInstance().collection("users")
                        val postList = user!!.posts!!.toMutableList()
                        postList.removeIf { it.date == post.date }
                        postList.add(post)

                        val updatedUser = User(
                            user!!.name,
                            user!!.nickname,
                            user!!.description,
                            postList,
                            user!!.follows,
                            user!!.followers
                        )

                        db.document(getLoggedUserUid()).set(updatedUser)
                            .addOnSuccessListener {
                                Log.i("Post", "Nuevo post subido correctamente")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Post", "Error al subir nuevo post", e)
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Camara", "Firebase: Error al subir la imagen", e)
                    }
            } else Log.e("User", "Error en la sesión")
        }

        fun uploadNewPfp(pfp: File) {
            val db = Firebase.storage.reference.child("users")
            if (Firebase.auth.uid != null) {
                db.child("${Firebase.auth.uid}/pfp.png").putBytes(pfp.readBytes())
                    .addOnSuccessListener {
                        Log.i("User", "La pfp se ha subido correctamente")
                    }
                    .addOnFailureListener { e ->
                        Log.e("User", "Error al subir la foto de perfil", e)
                    }

            } else Log.e("User", "Error en la sesión")
        }

        fun uploadNewBanner(banner: File) {
            val db = Firebase.storage.reference.child("users")
            if (Firebase.auth.uid != null) {
                db.child("${Firebase.auth.uid}/banner.png").putBytes(banner.readBytes())
                    .addOnSuccessListener {
                        Log.i("User", "El banner se ha subido correctamente")
                    }
                    .addOnFailureListener { e ->
                        Log.e("User", "Error al subir el banner", e)
                    }

            } else Log.e("User", "Error en la sesión")
        }

        suspend fun getPFP(uid: String): String = suspendCoroutine { c ->
            val pfp = Firebase.storage.reference.child("users/${uid}/pfp.png")
            if (Firebase.auth.uid != null) {

                pfp.downloadUrl
                    .addOnSuccessListener { url ->
                        c.resume(url.toString())
                    }
                    .addOnFailureListener { e ->
                        Log.w("User", "No hay PFP", e)
                        c.resume("https://mario.wiki.gallery/images/thumb/3/31/Green_Star_Artwork_-_Super_Mario_3D_World.png/800px-Green_Star_Artwork_-_Super_Mario_3D_World.png")
                    }

            } else Log.e("User", "Error en la sesión")
        }

        suspend fun getBanner(uid: String): String = suspendCoroutine { c ->
            val banner = Firebase.storage.reference.child("users/${uid}/banner.png")
            if (Firebase.auth.uid != null) {

                banner.downloadUrl
                    .addOnSuccessListener { url ->
                        c.resume(url.toString())
                    }
                    .addOnFailureListener { e ->
                        Log.w("User", "No hay banner", e)
                        c.resume("https://sftool.gov/Content/Images/GPC/gpc-jumbotron-bg.jpg")
                    }

            } else Log.e("User", "Error en la sesión")
        }

        fun getUserData(userUid: String): Flow<User> = callbackFlow {
            val db = FirebaseFirestore.getInstance()
            var user = User()
            db.collection("users").document(userUid).get().addOnSuccessListener {
                Log.i("User", "Obtenido información")
                val data = it.toObject(User::class.java)
                if (data != null) user = data
                trySend(user)
            }
            awaitClose { channel.close() }
        }

        /*
        fun getRandomUserData(): Flow<User> = flow {
            val db = FirebaseFirestore.getInstance()
            var user = User()


            db.collection("users").get().addOnSuccessListener {
                Log.i("User", "Obtenido información")
                val randomIndex = (0 until it.documents.size).random()
                val data: User? = it.documents[randomIndex].toObject(User::class.java)
                if (data?.posts != null) user = data

                Log.i("Buscar", "${data ?: "null"}")
                trySend(user)
            }
            awaitClose { channel.close() }
        }
*/
        fun getAllExplorePosts():Flow<List<Post>> = flow {
            val list = mutableListOf<Post>()

            getAllUsers().collect(){users ->
                users.forEach {user ->
                    user.posts?.forEach {
                        if(it.download_link != null){
                            list.add(it)
                        }
                    }
                }
            }
            emit(list)
        }

        fun getAllUsers(): Flow<List<User>> = flow {
            val db = FirebaseFirestore.getInstance()
            val list = mutableListOf<User>()
            db.collection("users").get().await().forEach{
                if(it.id != getLoggedUserUid()) list.add(getUserData(it.id).first())
            }
            emit(list)
        }

        fun uploadUserData(
            name: String,
            nickname: String,
            description: String,
            posts: List<Post>,
            follows: List<String>,
            followers: List<String>
        ) {
            val user = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()
            val userMap =
                User(name, nickname, description, posts, follows = follows, followers = followers)
            db.collection("users")
                .document(user?.uid!!)
                .set(userMap).addOnSuccessListener {
                    Log.i("User", "Actualizado correctamente")
                }.addOnSuccessListener {
                    Log.e("User", "No se pudo actualizar")
                }
        }

        suspend fun followUser(uid: String) {
            val user = getUserData(getLoggedUserUid()).first()
            val db = FirebaseFirestore.getInstance()

            val follows: MutableSet<String>? = user.follows?.toMutableSet()
            follows?.add(uid)

            val updatedUser = User(
                user!!.name,
                user!!.nickname,
                user!!.description,
                user.posts,
                follows?.toList(),
                user!!.followers
            )

            db.collection("users")
                .document(getLoggedUserUid())
                .set(updatedUser).addOnSuccessListener {
                    Log.i("User", "Actualizado correctamente")
                }.addOnSuccessListener {
                    Log.e("User", "No se pudo actualizar")
                }

        }

        suspend fun unfollowUser(uid: String) {
            val user = getUserData(getLoggedUserUid()).first()
            val db = FirebaseFirestore.getInstance()

            val follows: MutableSet<String>? = user.follows?.toMutableSet()
            follows?.removeIf { it == uid }

            val updatedUser = User(
                user!!.name,
                user!!.nickname,
                user!!.description,
                user.posts,
                follows?.toList(),
                user!!.followers
            )

            db.collection("users")
                .document(getLoggedUserUid())
                .set(updatedUser).addOnSuccessListener {
                    Log.i("User", "Actualizado correctamente")
                }.addOnSuccessListener {
                    Log.e("User", "No se pudo actualizar")
                }

        }

        suspend fun isFollowing(userUid: String, uid: String): Boolean {
            val user = getUserData(userUid).first()
            return user.follows?.any { it == uid } ?: false
        }

        fun getLoggedUserUid(): String {
            try {
                return FirebaseAuth.getInstance().currentUser?.uid ?: ""
            } catch (e: java.lang.NullPointerException) {
                throw Exception("Se ha intentado recuperar el uid cuando no hay sesión")
            }
        }


    }
}