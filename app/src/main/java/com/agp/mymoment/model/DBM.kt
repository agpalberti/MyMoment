package com.agp.mymoment.model

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

class DBM {



    companion object {

        var context: Resources? = null
        fun onLogin(email: String, password: String, callback: (Int) -> Unit) {
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
                            //TODO OBTENER DATOS
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
                                is FirebaseAuthInvalidCredentialsException ->{
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
                                val userMap = hashMapOf(
                                    "nickname" to nickname,
                                    "name" to name
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

    }
}