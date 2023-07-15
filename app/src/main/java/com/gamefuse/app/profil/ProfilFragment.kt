package com.gamefuse.app.profil

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gamefuse.app.Connect
import com.gamefuse.app.R
import com.gamefuse.app.api.model.response.LoginResponse
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

class ProfilFragment: Fragment() {

    private var progressBar: ProgressBar? = null
    private val userObject = Gson().fromJson(Connect.authToken, LoginResponse::class.java)
    private var textViewError: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        val profilPic: ImageView = view.findViewById(R.id.image_profil)
        val firstname: TextView = view.findViewById(R.id.firstname)
        val lastname: TextView = view.findViewById(R.id.lastname)
        val email: TextView = view.findViewById(R.id.email)
        val birthdate: TextView = view.findViewById(R.id.birthdate)
        this.textViewError = view.findViewById(R.id.textViewError)
        val friendsSection = view.findViewById<View>(R.id.my_friends_section)
        val saveButton: Button = view.findViewById(R.id.save_changes)

        val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        firstname.hint = userObject.user.firstname ?: getString(R.string.firstname)
        lastname.hint = userObject.user.lastname ?: getString(R.string.lastname)
        email.hint = userObject.user.email

        if (userObject.user.birthdate != null)
            birthdate.hint = LocalDate.parse(userObject.user.birthdate.toString(), pattern).toString()

        loadProfilePic(profilPic)

        friendsSection.setOnClickListener{
            val intent = Intent(requireContext(), FriendsListActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        saveButton.setOnClickListener {
            saveChanges(firstname.text.toString(), lastname.text.toString(), email.text.toString(), birthdate.text.toString())
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


    private fun saveChanges(firstname: String, lastname: String, email: String, birthdate: String) {}


}