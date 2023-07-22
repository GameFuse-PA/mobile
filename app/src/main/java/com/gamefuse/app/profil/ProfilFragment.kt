package com.gamefuse.app.profil

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.ApiClient
import com.gamefuse.app.api.model.request.UpdateProfil
import com.gamefuse.app.api.model.response.LoginResponse
import com.gamefuse.app.api.model.response.UserFromBack
import com.gamefuse.app.myFriendsList.FriendsListActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class ProfilFragment: Fragment() {

    private var progressBar: ProgressBar? = null
    private val userObject = Gson().fromJson(Connect.authToken, LoginResponse::class.java)
    private var textViewError: TextView? = null

    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var birthdate: EditText
    private lateinit var email: EditText

    private lateinit var pattern: DateTimeFormatter

    private lateinit var dateChoosen: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        val profilPic: ImageView = view.findViewById(R.id.image_profil)
        firstname = view.findViewById(R.id.firstname)
        lastname = view.findViewById(R.id.lastname)
        email = view.findViewById(R.id.email)
        birthdate = view.findViewById(R.id.birthdate)

        this.textViewError = view.findViewById(R.id.textViewError)
        val friendsSection = view.findViewById<View>(R.id.my_friends_section)
        val saveButton: Button = view.findViewById(R.id.save_changes)

        pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        birthdate.inputType = InputType.TYPE_NULL
        firstname.hint = if(userObject.user.firstname.isNullOrBlank()){
                                getString(R.string.firstname)
                            }else{
                                userObject.user.firstname
                            }
        lastname.hint = if(userObject.user.lastname.isNullOrBlank()){
            getString(R.string.lastname)
        }else{
            userObject.user.lastname
        }
        email.hint = userObject.user.email

        if (userObject.user.birthdate != null){
            try {
                birthdate.hint = LocalDate.parse(userObject.user.birthdate.split('T')[0]).toString()
            }catch (e: Exception){
                birthdate.hint = getString(R.string.birthdate)
            }
        }
        loadProfilePic(profilPic)


        birthdate.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)

            DatePickerDialog(requireContext(),
                { _: DatePicker, selectedYear: Int, monthOfYear: Int, dayOfMonth: Int ->
                    birthdate.setText("$dayOfMonth/${monthOfYear + 1}/$selectedYear")
                    dateChoosen = "$selectedYear-${monthOfYear + 1}-$dayOfMonth"
                }, year, month, day
            ).show()
        }

        friendsSection.setOnClickListener{
            val intent = Intent(requireContext(), FriendsListActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        saveButton.setOnClickListener {
            updateProfile()
        }

        return view
    }


    private fun loadProfilePic(profilPic: ImageView) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val profilPicUrl = userObject.user.avatar
                if (profilPicUrl != null) {
                    val url = URL(profilPicUrl.location)
                    val bitmap = withContext(Dispatchers.IO){
                        BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    }
                    profilPic.post{
                        profilPic.setImageBitmap(bitmap)
                        Picasso.get().load(url.toString()).resize(300, 300).transform(
                            CropCircleTransformation()
                        ).into(profilPic)
                    }
                } else {
                    profilPic.post{
                        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.photo_avatar_profil)
                        profilPic.setImageBitmap(bitmap)
                        Picasso.get().load(R.drawable.photo_avatar_profil).resize(300, 300).transform(
                            CropCircleTransformation()
                        ).into(profilPic)
                    }
                }
            }catch (e: Exception){
                Toast.makeText(context, "Erreur lors du chargement de l'image de profil", Toast.LENGTH_SHORT).show()
                profilPic.setImageResource(R.drawable.photo_avatar_profil)
            }
        }

    }

    private fun updateProfile() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                startLoading()
                val response = withContext(Dispatchers.IO){
                    ApiClient.apiService.updateProfile("Bearer ${userObject.access_token}", UpdateProfil(
                        email.text.toString().ifEmpty {
                            email.hint.toString()
                        },
                        firstname.text.toString().ifEmpty {
                            firstname.hint.toString()
                        },
                        dateChoosen.ifEmpty {
                            birthdate.hint.toString()
                        },
                        lastname.text.toString().ifEmpty {
                            lastname.hint.toString()
                        }
                    )
                    )
                }

                if (response.isSuccessful){
                    val body = response.body()
                    if (body != null){
                        Toast.makeText(context, body.message, Toast.LENGTH_SHORT).show()

                        val user = UserFromBack(
                            userObject.user._id,
                            userObject.user.username,
                            firstname.text.toString(),
                            lastname.text.toString(),
                            if (body.user.birthdate.isNullOrBlank()){
                                ""
                            }else{
                                LocalDate.parse(body.user.birthdate.split('T')[0]).format(pattern).toString()
                            },
                            email.text.toString(),
                            userObject.user.createdAt,
                            userObject.user.updatedAt,
                            userObject.user.avatar
                        )

                        Connect.authToken = Gson().toJson(LoginResponse(user, userObject.access_token, userObject.token_type, userObject.expires_in))
                        //remplacement des placeholders par les valeurs entrées
                        try {
                            birthdate.hint = LocalDate.parse(body.user.birthdate!!.split('T')[0]).format(pattern).toString()
                        }catch (e: Exception){
                            birthdate.hint = getString(R.string.birthdate)
                        }
                        firstname.hint = firstname.text.toString()
                        lastname.hint = lastname.text.toString()
                        email.hint = email.text.toString().ifEmpty { email.hint.toString() }

                        //remise à zéro des champs
                        birthdate.text.clear()
                        firstname.text.clear()
                        lastname.text.clear()
                        email.text.clear()
                        stopLoading()
                    }else{
                        Toast.makeText(context, getText(R.string.api_error), Toast.LENGTH_SHORT).show()
                        stopLoading()
                    }
                }else{
                    Toast.makeText(context, getText(R.string.api_error), Toast.LENGTH_SHORT).show()
                    stopLoading()
                }
            }catch (e: Exception){
                Toast.makeText(context, getText(R.string.api_error), Toast.LENGTH_SHORT).show()
                stopLoading()
            }
        }
    }


    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
    }

}