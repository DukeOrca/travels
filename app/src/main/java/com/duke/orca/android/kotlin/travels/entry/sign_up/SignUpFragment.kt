package com.duke.orca.android.kotlin.travels.entry.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.base.BaseFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentSignUpBinding
import com.duke.orca.android.kotlin.travels.entry.EntryActivity
import com.duke.orca.android.kotlin.travels.entry.EntryViewModel
import com.duke.orca.android.kotlin.travels.main.view.MainActivity

class SignUpFragment: BaseFragment() {
    private var viewBinding: FragmentSignUpBinding? = null
    private val viewModel: EntryViewModel by activityViewModels()

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

    private fun initializeView() {
        viewBinding?.materialButtonSignUp?.setOnClickListener {
            if (isUserIdAvailable().not()) {
                viewBinding?.sugarEditTextUserId?.setError(getString(R.string.error_sign_up_000))
                return@setOnClickListener
            }

            val email = viewBinding?.sugarEditTextUserId?.text() ?: return@setOnClickListener

            if (isPasswordAvailable().not()) {
                viewBinding?.sugarEditTextPassword?.setError(getString(R.string.error_sign_up_001))
                return@setOnClickListener
            }

            val password = viewBinding?.sugarEditTextPassword?.text() ?: return@setOnClickListener

            if (viewBinding?.sugarEditTextPassword?.text() == viewBinding?.sugarEditTextPasswordConfirm?.text()) {
                viewModel.signUp(email, password, {
                    viewModel.loginWithEmail(it.data.email, password, {
                        startMainActivity()
                    }, { message ->
                        showToast(message)
                    })
                }, {
                    showToast(it?.message ?: getString(R.string.error_sign_up_003))
                })
            } else {
                viewBinding?.sugarEditTextPasswordConfirm?.setError(getString(R.string.error_sign_up_002))
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

    private fun startMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(intent)
    }
}