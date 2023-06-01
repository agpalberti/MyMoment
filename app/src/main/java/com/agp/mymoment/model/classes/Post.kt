package com.agp.mymoment.model.classes

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.PropertyName


data class Post(
    @get: PropertyName("date") @set: PropertyName("date") var date: String? = null,
    @get:PropertyName("likes") @set:PropertyName("likes") var likes: List<String>? = null,
    @get:PropertyName("download_link") @set:PropertyName("download_link") var download_link: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeList(likes)
        parcel.writeString(download_link)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }

}