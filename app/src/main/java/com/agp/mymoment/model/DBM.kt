package com.agp.mymoment.model

import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Log.i
import androidx.annotation.RequiresApi
import com.agp.mymoment.config.MyResources
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
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
                            i("Login", "Logeado correctamente")
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
                                i("Register", "Usuario creado correctamente")
                                val user = mAuth.currentUser
                                val db = FirebaseFirestore.getInstance()
                                val userMap = User(
                                    name,
                                    nickname,
                                    MyResources.resources?.getString(com.agp.mymoment.R.string.default_desc)
                                        ?: "",
                                    emptyList(),
                                    emptyList(),
                                    emptyList()
                                )
                                db.collection("users")
                                    .document(user?.uid!!)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        i(
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

        @RequiresApi(Build.VERSION_CODES.O)
        fun uploadImage(image: File): Task<Pair<Uri, String>> {
            val db = Firebase.storage.reference.child("users")
            val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            val ref = db.child("${Firebase.auth.uid}/posts/${date}.png")
            return ref.putBytes(image.readBytes()).continueWithTask { task ->
                if (!task.isSuccessful) {
                    Log.e("Camara", "Firebase: Error al subir nuevo post", task.exception)
                    task.exception?.let { throw it }
                }
                i("Camara", "La foto se ha subido correctamente")
                ref.downloadUrl
            }.continueWith { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                Pair(task.result!!, date)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
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
                        val postList = user.posts!!.toMutableList()
                        postList.removeIf { it.date == post.date }
                        postList.add(post)

                        val updatedUser = User(
                            user.name,
                            user.nickname,
                            user.description,
                            postList,
                            user.follows,
                            user.followers
                        )

                        db.document(getLoggedUserUid()).set(updatedUser)
                            .addOnSuccessListener {
                                i("Post", "Nuevo post subido correctamente")
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
                        i("User", "La pfp se ha subido correctamente")
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
                        i("User", "El banner se ha subido correctamente")
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
                        c.resume("https://w7.pngwing.com/pngs/869/370/png-transparent-low-polygon-background-green-banner-low-poly-materialized-flat.png")
                    }

            } else Log.e("User", "Error en la sesión")
        }

        fun getUserData(userUid: String): Flow<User> = callbackFlow {
            val db = FirebaseFirestore.getInstance()
            var user = User()
            i("GetUser", "User Uid: $userUid")
            db.collection("users").document(userUid).get().addOnSuccessListener {
                i("User", "Obtenido información")
                val data = it.toObject(User::class.java)
                if (data != null) user = data
                trySend(user)
            }
            awaitClose { channel.close() }
        }

        fun getAllExplorePosts(): Flow<List<Post>> = flow {
            val list = mutableListOf<Post>()

            getAllUsers().collect { users ->
                users.values.forEach { user ->
                    user.posts?.forEach {
                        if (it.download_link != null) {
                            list.add(it)
                        }
                    }
                }
            }
            emit(list)
        }

        fun getAllUsers(): Flow<Map<String, User>> = flow {
            val db = FirebaseFirestore.getInstance()
            val map = mutableMapOf<String, User>()
            db.collection("users").get().await().forEach {
                if (it.id != getLoggedUserUid()) map[it.id] = (getUserData(it.id).first())
            }
            emit(map)
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
                    i("User", "Actualizado correctamente")
                }.addOnFailureListener {
                    Log.e("User", "No se pudo actualizar")
                }
        }

        suspend fun followUser(uid: String) {
            val me = getUserData(getLoggedUserUid()).first()
            val user = getUserData(uid).first()
            val db = FirebaseFirestore.getInstance()


            val followers: MutableSet<String>? = user.followers?.toMutableSet()
            val myFollows: MutableSet<String>? = me.follows?.toMutableSet()
            myFollows?.add(uid)
            followers?.add(getLoggedUserUid())

            val myUpdatedUser = User(
                me.name,
                me.nickname,
                me.description,
                me.posts,
                myFollows?.toList(),
                me.followers
            )

            val updatedUser = User(
                user.name,
                user.nickname,
                user.description,
                user.posts,
                user.follows,
                followers?.toList()
            )


            db.collection("users")
                .document(getLoggedUserUid())
                .set(myUpdatedUser).addOnSuccessListener {
                    i("User", "Actualizado correctamente")
                }.addOnFailureListener {
                    Log.e("User", "No se pudo actualizar")
                }

            db.collection("users")
                .document(uid)
                .set(updatedUser).addOnSuccessListener {
                    i("User", "Actualizado correctamente")
                }.addOnFailureListener {
                    Log.e("User", "No se pudo actualizar")
                }

        }

        suspend fun unfollowUser(uid: String) {
            val me = getUserData(getLoggedUserUid()).first()
            val user = getUserData(uid).first()
            val db = FirebaseFirestore.getInstance()


            val followers: MutableSet<String>? = user.followers?.toMutableSet()
            followers?.removeIf { it == getLoggedUserUid() }
            val myFollows: MutableSet<String>? = me.follows?.toMutableSet()
            myFollows?.removeIf { it == uid }

            val myUpdatedUser = User(
                me.name,
                me.nickname,
                me.description,
                me.posts,
                myFollows?.toList(),
                me.followers
            )

            val updatedUser = User(
                user.name,
                user.nickname,
                user.description,
                user.posts,
                user.follows,
                followers?.toList()
            )

            db.collection("users")
                .document(getLoggedUserUid())
                .set(myUpdatedUser).addOnSuccessListener {
                    i("User", "Actualizado correctamente")
                }.addOnSuccessListener {
                    Log.e("User", "No se pudo actualizar")
                }

            db.collection("users")
                .document(uid)
                .set(updatedUser).addOnSuccessListener {
                    i("User", "Actualizado correctamente")
                }.addOnFailureListener {
                    Log.e("User", "No se pudo actualizar")
                }

        }

        suspend fun isFollowing(userUid: String, uid: String): Boolean {
            i("Following", "User: $userUid")
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



        suspend fun likePost(postDate: String, userUid: String) {
            val db = FirebaseFirestore.getInstance()
            val user = getUserData(userUid).first()

            val updatedPostList = user.posts!!.toMutableSet()


            val postToUpdate = updatedPostList.find { post ->

                post.date == postDate
            }

            if (postToUpdate != null) {
                updatedPostList.remove(postToUpdate)
                val likes = postToUpdate.likes?.toMutableList()
                likes?.add(getLoggedUserUid())
                postToUpdate.likes = likes
                updatedPostList.add(postToUpdate)
            }

            Log.i("Like", "Post: $postToUpdate")

            val updatedUser = User(
                user.name,
                user.nickname,
                user.description,
                updatedPostList.toList(),
                user.follows,
                user.followers
            )

            db.collection("users")
                .document(userUid)
                .set(updatedUser).addOnSuccessListener {
                    i("Like", "Like correctamente")
                }.addOnFailureListener {
                    Log.e("Like", "No se pudo actualizar")
                }

        }


        suspend fun dislikePost(postDate: String, userUid: String) {
            val db = FirebaseFirestore.getInstance()
            val user = getUserData(userUid).first()

            val updatedPostList = user.posts!!.toMutableSet()

            val postToUpdate = updatedPostList.find { post ->
                post.date == postDate
            }

            if (postToUpdate != null) {
                updatedPostList.remove(postToUpdate)
                val likes = postToUpdate.likes?.toMutableList()
                likes?.remove(getLoggedUserUid())
                postToUpdate.likes = likes
                updatedPostList.add(postToUpdate)
            }

            val updatedUser = User(
                user.name,
                user.nickname,
                user.description,
                updatedPostList.toList(),
                user.follows,
                user.followers
            )

            db.collection("users")
                .document(userUid)
                .set(updatedUser).addOnSuccessListener {
                    i("Like", "Dislike correctamente")
                }.addOnFailureListener {
                    Log.e("Like", "No se pudo actualizar")
                }

        }

        suspend fun deletePost(postDate: String){
            val db = FirebaseFirestore.getInstance()
            val user = getUserData(getLoggedUserUid()).first()

            val updatedPostList = user.posts!!.toMutableSet()
            updatedPostList.removeIf { it.date == postDate }

            val updatedUser = User(
                user.name,
                user.nickname,
                user.description,
                updatedPostList.toList(),
                user.follows,
                user.followers
            )

            db.collection("users")
                .document(getLoggedUserUid())
                .set(updatedUser).addOnSuccessListener {
                    i("Delete", "Borrado correctamente")
                }.addOnFailureListener {
                    Log.e("Delete", "No se pudo borrar")
                }
        }


        /*
        suspend fun getUserUid(user: User): String? {
            val firestore = FirebaseFirestore.getInstance()
            val collection = firestore.collection("users")
            val query = collection.whereEqualTo("nickname", user.nickname).limit(1)
            val snapshot = query.get().await()
            val uid = snapshot.documents.firstOrNull()?.id

            Log.i("GetUser", user.nickname + ": $uid")
            return uid
        }

        fun getPostById(id: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collection = firestore.collection("users")
            val query = collection.whereArrayContains("posts", id)
        }



        fun getUserByPostId(id: String): Flow<User?> = flow {
            getAllUsers().collect {
                emit( it.values.find { user -> user.posts!!.any { post -> post.date == id } })
            }
        }
        fun getPostByUrl(url: String): Flow<Post?> = flow {

            getAllUsers().collect {
                emit( it.values.find { user -> user.posts!!.any { post -> post.download_link == url } }
                    ?.posts!!.find { post -> post.download_link == url })
            }
        }
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

    }
}