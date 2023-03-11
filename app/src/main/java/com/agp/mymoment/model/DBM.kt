package com.agp.mymoment.model

import android.util.Log
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

class DBM {

    companion object {
        fun onLogin(email: String, password: String): Int? {
            var errorMessage: Int? = null
            try {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnFailureListener { exception ->
                        Log.w("Login", "Error al iniciar sesión", exception)
                        errorMessage = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> 0 //"Datos incorrectos"
                            is FirebaseAuthInvalidUserException -> 1 //"Usuario no existe"
                            else -> -1 // "Error de login no gestionado"
                        }
                    }
                    //Si no devuelve errores da un mensaje de éxito y pasa a la siguiente pantalla
                    .addOnSuccessListener { task ->
                        //TODO OBTENER DATOS
                        Log.i("Login", "Logeado correctamente")
                    }
                return errorMessage
            } catch (error: Exception) {
                Log.e("Login", "Error no controlado", error)
                return -2 // Error genérico no gestionado
            }
        }


        fun onRegister(email: String, password: String, nickname: String, name: String): Int? {
            //TODO Codigo de error
            var errorMessage: Int? = null
            try {
                val mAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password)
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
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        "Register",
                                        "Error almacenando datos del usuario en Firestore",
                                        e
                                    )
                                }
                        } else {
                            // El usuario no se ha podido crear
                            Log.w("Register", "Error al crear usuario", task.exception)
                        }
                    }
            } catch (error: Exception) {
                Log.e("Register", error.stackTraceToString())
                return -2 // Error genérico no gestionado
            }
            return TODO() 
        } 

    }
}