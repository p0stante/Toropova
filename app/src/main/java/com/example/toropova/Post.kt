package com.example.toropova
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Post (
    @SerializedName("id")
    val id: Long,
    @SerializedName("description")
    val description: String?,
    @SerializedName("gifURL")
    val gifURL: String?
        ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(description)
        parcel.writeString(gifURL)
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