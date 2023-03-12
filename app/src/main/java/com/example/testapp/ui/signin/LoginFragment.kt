package com.example.testapp.ui.signin

import android.os.Bundle
import android.text.Selection
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.testapp.App
import com.example.testapp.R
import com.example.testapp.databinding.FragmentLoginBinding
import com.example.testapp.ui.UserViewModel
import com.example.testapp.ui.UserViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((activity?.application as App).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            inputFieldPassword.buttonShowPassword.setOnClickListener {
                val passField = inputFieldPassword.editTextPassword
                val eyeIcon = inputFieldPassword.buttonShowPassword

                when (passField.transformationMethod) {
                    HideReturnsTransformationMethod.getInstance() -> {
                        val cursorPosition = Selection.getSelectionStart(passField.text)
                        eyeIcon.setImageResource(R.drawable.ic_eye_off)
                        passField.transformationMethod = PasswordTransformationMethod.getInstance()
                        Selection.setSelection(passField.text, cursorPosition)
                    }

                    else -> {
                        val cursorPosition = Selection.getSelectionStart(passField.text)
                        eyeIcon.setImageResource(R.drawable.ic_eye_off_pressed)
                        passField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        Selection.setSelection(passField.text, cursorPosition)
                    }
                }
            }

            buttonLogin.setOnClickListener {button ->
                val firstName = editTextFirstName.text.toString().trim()
                val password = inputFieldPassword.editTextPassword.text.toString().trim()
                if (firstName.isNotBlank() && password.isNotBlank()){
                    button.isActivated = false
                    userViewModel.checkUserExist(firstName, password)
                } else {
                    Toast.makeText(context, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
                }
            }
        }

        userViewModel.needToNavigate.observe(viewLifecycleOwner) {
            when(it) {
                true -> {
                    userViewModel.navigated()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeHostFragment())
                }
                false -> {
                    Toast.makeText(context, getString(R.string.error_wrong_credentials), Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
            binding.buttonLogin.isActivated = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}