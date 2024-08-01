package com.example.studyinbetterlogin.db
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
@Parcelize
@Entity
data class User(
    @PrimaryKey(autoGenerate =true)
    val id:Int,
    @ColumnInfo(name = "account")
    var account:String,
    @ColumnInfo(name="passward")
    var password:String,
    @ColumnInfo(name = "pattern_password")
    var pattrenPassword:String
):Parcelable
