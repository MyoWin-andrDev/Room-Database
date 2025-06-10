package com.learning.roomdatabase.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learning.roomdatabase.database.dao.StudentDAO
import com.learning.roomdatabase.database.entity.StudentModel

@Database(entities = [StudentModel::class], version = 1, exportSchema = false)
abstract class StudentDb : RoomDatabase() {
    abstract fun getStudentDAO() : StudentDAO
}