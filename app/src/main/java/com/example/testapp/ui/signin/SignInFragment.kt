package com.example.testapp.ui.signin

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.testapp.App
import com.example.testapp.R
import com.example.testapp.database.User
import com.example.testapp.databinding.FragmentSignInBinding
import com.example.testapp.ui.UserViewModel
import com.example.testapp.ui.UserViewModelFactory

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((activity?.application as App).repository)
    }

    private lateinit var newUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spannableLogIn = SpannableString(getString(R.string.already_have_account)).apply {
            makeSpannable(
                getString(R.string.already_have_account),
                getString(R.string.log_in)
            ) {
                userViewModel.navigated()
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToLoginFragment())
            }
        }

        binding.apply {
            textLogIn.text = spannableLogIn
            textLogIn.movementMethod = LinkMovementMethod.getInstance()

            buttonSignIn.setOnClickListener { button ->

                val firstName = binding.editTextFirstName.text.toString().trim()
                val lastName = binding.editTextLastName.text.toString().trim()
                val email = binding.editTextEmail.text.toString().trim()

                if (
                    firstName.isNotBlank()
                    && lastName.isNotBlank()
                    && email.isNotBlank()
                ) {
                    if (userViewModel.isValidEmail(email)){
                        newUser = User(firstName, lastName, email)
                        button.isActivated = false
                        userViewModel.createUser(newUser)
                    } else {
                        Toast.makeText(context, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
                }
            }
        }

        userViewModel.needToNavigate.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    userViewModel.navigated()
                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeHostFragment())
                }

                false -> Toast.makeText(
                    context,
                    getString(R.string.error_email_already_taken),
                    Toast.LENGTH_SHORT,
                ).show()

                else -> {}
            }
            binding.buttonSignIn.isActivated = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun SpannableString.makeSpannable(
        text: String,
        textToSpan: String,
        onClick: () -> Unit
    ) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(context!!, R.color.link_text)
            }

            override fun onClick(p0: View) {
                onClick()
            }
        }
        setSpan(
            clickableSpan,
            text.indexOf(textToSpan),
            text.indexOf(textToSpan) + textToSpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

}