package com.example.testapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.example.testapp.App
import com.example.testapp.MainActivity
import com.example.testapp.database.User
import com.example.testapp.databinding.FragmentProfileBinding
import com.example.testapp.ui.UserViewModel
import com.example.testapp.ui.UserViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var pickPhoto: ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var currentUser: User

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((activity?.application as App).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        pickPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoChooser", "Chose photo: $uri")
                currentUser.profilePictureUri = uri.toString()
                userViewModel.updateProfilePicture(currentUser)
            } else {
                Toast.makeText(context, "No photo selected", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getCurrentUser()
        userViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                currentUser = user
                binding.textUserName.text = "${currentUser.firstName} ${currentUser.lastName}"

                binding.profilePicture.setImageURI(
                    if (currentUser.profilePictureUri != null)
                        currentUser.profilePictureUri!!.toUri()
                    else
                        null
                )
            }
        }

        binding.changePhoto.setOnClickListener {
            choosePhoto()
        }

        //for test use only
        binding.restorePurchase.setOnClickListener {
            userViewModel.deleteAll()
            userViewModel.logOut()
            (activity as MainActivity).logOut()
        }

        binding.logOut.setOnClickListener {
            userViewModel.logOut()
            (activity as MainActivity).logOut()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun choosePhoto() {
        pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}