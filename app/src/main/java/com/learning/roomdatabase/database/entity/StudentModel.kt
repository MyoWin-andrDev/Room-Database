package com.learning.roomdatabase.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Entity("tbl_student")
data class StudentModel(
    @ColumnInfo("s_name")
    val name : String,
    @ColumnInfo("s_grade")
    val grade : String,
    @ColumnInfo("s_room")
    val room : String,
    @ColumnInfo("s_gender")
    val gender: Int,
    @ColumnInfo("s_fatherName")
    val fatherName : String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("s_id")
    val id : Int  = 0,
) : Parcelable