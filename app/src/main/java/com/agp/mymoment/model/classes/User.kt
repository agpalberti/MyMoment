package com.agp.mymoment.model.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.PropertyName


data class User(
    @get: PropertyName("name") @set: PropertyName("name") var name:String? = null,
    @get:PropertyName("nickname") @set:PropertyName("nickname") var nickname: String? = null,
    @get:PropertyName("description") @set:PropertyName("description") var description: String? = null,
    @get:PropertyName("posts") @set:PropertyName("posts") var posts: List<Post>? = null)

: Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Post)
    ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(nickname)
        parcel.writeString(description)
        parcel.writeList(posts)
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}