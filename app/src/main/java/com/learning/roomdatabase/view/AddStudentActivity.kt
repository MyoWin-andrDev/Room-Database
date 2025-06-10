package com.learning.roomdatabase.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.learning.roomdatabase.R
import com.learning.roomdatabase.database.database.StudentDBInstance
import com.learning.roomdatabase.database.database.StudentDb
import com.learning.roomdatabase.database.entity.StudentModel
import com.learning.roomdatabase.databinding.ActivityAddStudentBinding
import com.learning.roomdatabase.utils.Constants
import com.learning.roomdatabase.utils.showToast
import com.learning.roomdatabase.utils.validateNotEmpty
import kotlinx.coroutines.launch

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
    private lateinit var studentDB: StudentDb
    private var student: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDatabase()
        loadStudentData()
        setupViews()
    }

    private fun initializeDatabase() {
        studentDB = StudentDBInstance.getDatabaseInstance(this)
    }

    private fun loadStudentData() = with(binding){
        student = intent.getParcelableExtra<StudentModel>(Constants.STUDENT_DATA)
        tbHome.title = if (student != null) Constants.Titles.EDIT_STUDENT else Constants.Titles.ADD_STUDENT
        tbHome.setNavigationOnClickListener { finish() }
        student?.let { populateFields(it) }
    }

    private fun setupViews() {
        binding.btSave.setOnClickListener {
            if (validateFields()) {
                saveStudent()
            }
        }
    }

    private fun populateFields(student: StudentModel) {
        with(binding) {
            etName.setText(student.name)
            etGrade.setText(student.grade)
            etRoomNo.setText(student.room)
            etFatherName.setText(student.fatherName)
            when (student.gender) {
                Constants.GENDER_MALE -> rbMale.isChecked = true
                Constants.GENDER_FEMALE -> rbFemale.isChecked = true
            }
        }
    }

    private fun validateFields(): Boolean {
        with(binding) {
            val isNameValid = etName.validateNotEmpty(Constants.Validation.NAME)
            val isGradeValid = etGrade.validateNotEmpty(Constants.Validation.GRADE)
            val isRoomValid = etRoomNo.validateNotEmpty(Constants.Validation.ROOM_NO)
            val isFatherNameValid = etFatherName.validateNotEmpty(Constants.Validation.FATHER_NAME)

            val isGenderValid = if (rgGender.checkedRadioButtonId == -1) {
                tvGenderError.visibility = View.VISIBLE
                false
            } else {
                tvGenderError.visibility = View.GONE
                true
            }

            return isNameValid && isGradeValid && isRoomValid && isFatherNameValid && isGenderValid
        }
    }

    private fun saveStudent() {
        lifecycleScope.launch {
            val successMessage = if (student == null) {
                handleAddStudent()
            } else {
                handleUpdateStudent()
            }

            showToast(successMessage)
            finish()
        }
    }

    private suspend fun handleAddStudent(): String {
        val studentData = createStudentFromInput()
        return if (studentDB.getStudentDAO().addStudent(studentData) > 0) {
            Constants.Messages.RECORD_ADDED.format(studentData.name)
        } else {
            Constants.Messages.ADD_FAILED
        }
    }

    private suspend fun handleUpdateStudent(): String {
        return if (student != null && updateStudent(student!!)) {
            Constants.Messages.RECORD_UPDATED.format(student!!.name)
        } else {
            Constants.Messages.UPDATE_FAILED
        }
    }

    private suspend fun updateStudent(studentData: StudentModel): Boolean = with(binding) {
        return studentDB.getStudentDAO().updateStudent(
            id = studentData.id,
            name = etName.text.toString().trim(),
            grade = etGrade.text.toString().trim(),
            room = etRoomNo.text.toString().trim(),
            gender = getSelectedGender(),
            fatherName = etFatherName.text.toString().trim()
        ) > 0
    }

    private fun createStudentFromInput(): StudentModel = with(binding) {
        return StudentModel(
            name = etName.text.toString().trim(),
            grade = etGrade.text.toString().trim(),
            room = etRoomNo.text.toString().trim(),
            gender = getSelectedGender(),
            fatherName = etFatherName.text.toString().trim(),
            id = 0,
        )
    }

    private fun getSelectedGender(): Int {
        return when (binding.rgGender.checkedRadioButtonId) {
            R.id.rbMale -> Constants.GENDER_MALE
            R.id.rbFemale -> Constants.GENDER_FEMALE
            else -> Constants.GENDER_UNSELECTED
        }
    }
}