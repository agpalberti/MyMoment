package com.agp.mymoment.model.classes

import com.google.firebase.firestore.PropertyName


class User(
    @get: PropertyName("name") @set: PropertyName("name") var name:String? = null,
    @get:PropertyName("nickname") @set:PropertyName("nickname") var nickname: String? = null,
    @get:PropertyName("description") @set:PropertyName("description") var description: String? = null)