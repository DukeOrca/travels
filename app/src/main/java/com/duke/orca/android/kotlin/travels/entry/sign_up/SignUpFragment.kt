package com.duke.orca.android.kotlin.travels.entry.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.base.BaseFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentSignUpBinding
import com.duke.orca.android.kotlin.travels.entry.EntryActivity
import com.duke.orca.android.kotlin.travels.network.EntryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpFragment: BaseFragment() {

    private var viewBinding: FragmentSignUpBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSignUpBinding.inflate(inflater, container, false)

        initializeView()

        return viewBinding?.root
    }

    override fun initializeToolbar() {
        (requireActivity() as EntryActivity).supportActionBar?.apply {
            title = getString(R.string.sign_up)
            setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
        }
    }

    override fun initializeView() {
        viewBinding?.materialButtonSignUp?.setOnClickListener {
            if (isUserIdAvailable().not()) {
                viewBinding?.sugarEditTextUserId?.setError(getString(R.string.error_login_000))
                return@setOnClickListener
            }

            if (isPasswordAvailable().not()) {
                viewBinding?.sugarEditTextPassword?.setError(getString(R.string.error_login_001))
                return@setOnClickListener
            }

            if (viewBinding?.sugarEditTextPassword?.text() == viewBinding?.sugarEditTextPasswordConfirm?.text()) {
                // TODO: ADD: Sign up.
                    fragmentScope.launch {
                        withContext(Dispatchers.IO) {
                            val signUp = EntryApi.signUpService().getSignUpAsync(
                                email = "l123aaocustman111",
                                password = "tst2s3213",
                                provider = "K"
                            ).await()
                            println("SSSSSS: $signUp")
                        }
                    }

                showToast("Sign Up")
            } else {
                viewBinding?.sugarEditTextPasswordConfirm?.setError(getString(R.string.error_sign_up_000))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isUserIdAvailable(): Boolean {
        return viewBinding?.sugarEditTextUserId?.isNotBlank() ?: false
    }

    private fun isPasswordAvailable(): Boolean {
        return viewBinding?.sugarEditTextPassword?.isNotBlank() ?: false
    }
}