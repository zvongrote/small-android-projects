package com.zachvg.login

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationSet
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zachvg.login.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assign listeners
        binding?.loginButton?.setOnClickListener {
            hideKeyboard()

            // Check if login information is valid
            val username = binding?.username?.text?.toString() ?: ""
            val password = binding?.password?.text?.toString() ?: ""

            loginViewModel.onLoginClick(username, password)
        }

        binding?.forgotLogin?.setOnClickListener {
            val message = getString(R.string.too_bad)
            showSnackbar(view, message)
        }

        binding?.constraintLayout?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hideKeyboard()
            }
        }

        // Add observers
        loginViewModel.loginSuccessful.observe(viewLifecycleOwner) { loginSuccessful ->
            if (loginSuccessful) {
                // Run animations and navigate to the success screen
                runSuccessfulLoginAnimations(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {
                        // Do nothing
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        // Clear the text fields
                        binding?.username?.text = null
                        binding?.password?.text = null

                        findNavController().navigate(R.id.action_loginFragment_to_successFragment)
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                        // Do nothing
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                        // Do nothing
                    }

                })
            } else {
                val message = getString(R.string.login_failed)
                showSnackbar(view, message)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    private fun showSnackbar(view: View, message: String) =
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

    private fun hideKeyboard() {
        activity?.let { activity ->
            activity.currentFocus?.let { viewWithFocus ->
                val inputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(viewWithFocus.windowToken, 0)
            }
        }

    }

    private fun runSuccessfulLoginAnimations(onEndListener: Animator.AnimatorListener) =
        binding?.let {
            // Get width and height
            val screenWidth: Float
            val screenHeight: Float

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowBounds = requireActivity().windowManager.currentWindowMetrics.bounds

                screenWidth = windowBounds.right.toFloat()
                screenHeight = windowBounds.bottom.toFloat()
            } else {
                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

                screenWidth = displayMetrics.widthPixels.toFloat()
                screenHeight = displayMetrics.heightPixels.toFloat()
            }

            val animationTime = 800L

            // Move logo to the center of the screen and fade out
            val centerY = it.constraintLayout.y + it.constraintLayout.height / 2
            val logoFinalY = centerY - (it.logo.height)

            val logoToCenterAnimator = ObjectAnimator.ofFloat(it.logo, "y", logoFinalY).apply {
                duration = animationTime * 2
                interpolator = AccelerateDecelerateInterpolator()
            }

            val logoFadeOutAnimator = ObjectAnimator.ofFloat(it.logo, "alpha", 0f).apply {
                duration = (animationTime * 1.5).toLong()
            }

            val logoAnimation = AnimatorSet().apply {
                playSequentially(logoToCenterAnimator, logoFadeOutAnimator)
                startDelay = 300
            }

            // Move username field off right of the screen
            val usernameAnimator = ObjectAnimator.ofFloat(it.username, "x", screenWidth).apply {
                duration = animationTime
                interpolator = AnticipateInterpolator()
            }

            // Move password field off left side of the screen
            val passwordFinalX = (-it.password.width).toFloat()
            val passwordAnimator = ObjectAnimator.ofFloat(it.password, "x", passwordFinalX).apply {
                duration = animationTime
                interpolator = AnticipateInterpolator()
            }

            // Move login button off bottom of the screen
            val loginButtonAnimator =
                ObjectAnimator.ofFloat(it.loginButton, "y", screenHeight).apply {
                    duration = animationTime
                }

            // Move forgot login text off bottom of the screen
            val forgotLoginAnimator =
                ObjectAnimator.ofFloat(it.forgotLogin, "y", screenHeight).apply {
                    duration = animationTime
                }

            /*
            Animation order:
            1) Forgot login text view
            2) Login button
            3) Username and password field at the same time
            3) Logo
             */
            AnimatorSet().apply {
                play(forgotLoginAnimator).before(loginButtonAnimator)
                play(loginButtonAnimator).before(passwordAnimator)
                play(passwordAnimator).with(usernameAnimator)
                play(logoAnimation).after(usernameAnimator)

                addListener(onEndListener)

                start()
            }
        }

}