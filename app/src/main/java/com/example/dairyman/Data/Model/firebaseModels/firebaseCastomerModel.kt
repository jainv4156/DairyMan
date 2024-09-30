package com.example.dairyman.Data.Model.firebaseModels

import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class FirebaseCustomerModel(
    val id:Long=0L,
    val name:String="",
    val rate:Int=0,
    val amount:Float=0F,
    val pendingAmount:Int=0,
    val dateCreated:Long=System.currentTimeMillis(),
    val dateUpdated:Long=System.currentTimeMillis(),
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(rate)
        parcel.writeFloat(amount)
        parcel.writeInt(pendingAmount)
        parcel.writeLong(dateCreated)
        parcel.writeLong(dateUpdated)
    }

    override fun describeContents()= 0

    companion object CREATOR : Parcelable.Creator<FirebaseCustomerModel> {
        override fun createFromParcel(parcel: Parcel): FirebaseCustomerModel {
            return FirebaseCustomerModel(parcel)
        }

        override fun newArray(size: Int): Array<FirebaseCustomerModel?> {
            return arrayOfNulls(size)
        }
    }
}