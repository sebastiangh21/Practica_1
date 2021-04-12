package com.sgh21.practica1

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sgh21.practica1.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

const val EMPTY = ""
const val SPACE = " "

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private var users: MutableList<User> = mutableListOf()
    private var dateOfBirth: String = EMPTY
    private var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.saveButton.setOnClickListener {
            val email = mainBinding.emailEditText.text.toString()
            val password = mainBinding.passwordEditText.text.toString()
            val repPassword = mainBinding.repPasswordEditText.text.toString()
            val genre = if (mainBinding.femaleRadioButton.isChecked) getString(R.string.female) else getString(R.string.male)
            var hobbies = if (mainBinding.danceCheckBox.isChecked) getString(R.string.dance) + SPACE else EMPTY
            hobbies += if (mainBinding.eatCheckBox.isChecked) getString(R.string.eat) + SPACE else EMPTY
            hobbies += if(mainBinding.readCheckBox.isChecked) getString(R.string.read) + SPACE else EMPTY
            hobbies += if (mainBinding.sportCheckBox.isChecked) getString(R.string.sport) else EMPTY
            val birthplace = mainBinding.citySpinner.selectedItem.toString()

            if (email.isNotEmpty()) {
                if (password == repPassword && password != EMPTY) {
                    mainBinding.repPasswordTextInputLayout.error = null
                    if (dateOfBirth != EMPTY){
                        mainBinding.infoTextView.text = email
                        saveUser(email, password, genre, hobbies, dateOfBirth, birthplace)
                    } else{
                        Toast.makeText(this,getString(R.string.date_of_birth_error), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    mainBinding.repPasswordTextInputLayout.error = getString(R.string.password_error)
                }
            } else{
                Toast.makeText(this, getString(R.string.email_error), Toast.LENGTH_SHORT).show()
                mainBinding.repPasswordTextInputLayout.error = null
            }
            cleanView()
        }
        val dateSetListener = DatePickerDialog.OnDateSetListener{ _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val format = "MM/dd/yyyy"
            val sdf = SimpleDateFormat(format, Locale.US)
            dateOfBirth = sdf.format(cal.time).toString()
            mainBinding.dateOfBirthEditText.setText(dateOfBirth)
        }

        mainBinding.dateOfBirthEditText.setOnClickListener {
            val picker = DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            picker.datePicker.maxDate = Calendar.getInstance().timeInMillis
            picker.show()
        }
    }

    private fun cleanView() {
        with(mainBinding){
            emailEditText.setText(EMPTY)
            passwordEditText.setText(EMPTY)
            repPasswordEditText.setText(EMPTY)
            dateOfBirthEditText.setText(EMPTY)
            citySpinner.setSelection(0)
            dateOfBirth = EMPTY
            femaleRadioButton.isChecked = true
            danceCheckBox.isChecked = false
            eatCheckBox.isChecked = false
            sportCheckBox.isChecked = false
            readCheckBox.isChecked = false
        }
    }

    private fun saveUser(email: String, password: String, genre: String, hobbies: String, dateofbirth: String, birthplace: String) {
        val newUser = User(email, password, genre, hobbies, dateofbirth, birthplace)
        users.add(newUser)
        printUserData()
    }

    private fun printUserData() {
        var info = ""
        for (user in users){
            info += "\n\n" + user.email + "\n" + user.genre + "\n" + user.hobbies + "\n" + user.dateofbirth + "\n" + user.birthplace
        }
        mainBinding.infoTextView.text = info
    }
}