package com.duke.orca.android.kotlin.travels.entry.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.base.BaseFragment
import com.duke.orca.android.kotlin.travels.databinding.FragmentLoginBinding
import com.duke.orca.android.kotlin.travels.entry.EntryActivity
import com.duke.orca.android.kotlin.travels.entry.EntryViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment: BaseFragment() {
    private var viewBinding: FragmentLoginBinding? = null
    private val viewModel: EntryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)

        initializeView()

        return viewBinding?.root
    }

    override fun initializeToolbar() {
        (requireActivity() as EntryActivity).supportActionBar?.apply {
            title = getString(R.string.login)
            setDisplayHomeAsUpEnabled(true)
            setHasOptionsMenu(true)
        }
    }

    override fun initializeView() {
        viewBinding?.materialButtonLogin?.setOnClickListener {
            if (isUserIdAvailable().not()) {
                viewBinding?.sugarEditTextUserId?.setError(getString(R.string.error_login_000))
                return@setOnClickListener
            }

            if (isPasswordAvailable().not()) {
                viewBinding?.sugarEditTextPassword?.setError(getString(R.string.error_login_001))
                return@setOnClickListener
            }
        }

        viewBinding?.loginButtonFacebook?.setOnClickListener {
            viewModel.loginWithFacebook(
                requireActivity(), { token ->
                    showToast("onSuccess")
                }, {
                    Timber.e(it)
                }
            )
        }

        viewBinding?.loginButtonGoogle?.setOnClickListener {
            viewModel.loginWithGoogle(
                requireActivity(), {

                }, {
                    Timber.e(it)
                }
            )
        }

        viewBinding?.loginButtonKakao?.setOnClickListener {
            viewModel.loginWithKakao(
                requireContext(), { token ->
                    showToast("onSuccess")
                }, {
                    Timber.e(it)
                }
            )
        }

        viewBinding?.loginButtonNaver?.setOnClickListener {
            viewModel.loginWithNaver(requireActivity(), { accessToken, refreshToken, expiresAt, tokenType ->

            }, { errorCode, errorDesc ->
                Timber.e("errorCode $errorCode")
                Timber.e("errorDesc $errorDesc")
            })
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