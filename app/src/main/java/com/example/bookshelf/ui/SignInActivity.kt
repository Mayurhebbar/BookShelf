package com.example.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelf.databinding.ActivitySignInBinding
import com.example.bookshelf.viewmodel.SignInViewModel
import com.example.bookshelf.livedata.Result
import com.example.bookshelf.utils.PrefUtil
import com.example.bookshelf.utils.Utils
import com.example.bookshelf.viewmodel.BaseViewModelFactory

class SignInActivity: AppCompatActivity() {

    private var binding : ActivitySignInBinding? = null
    private var viewModel: SignInViewModel? = null
    private val TAG = SignInActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        viewModel = ViewModelProvider(
            this, BaseViewModelFactory {
                SignInViewModel(Utils.provideUserCaseImpl(this))
            })[SignInViewModel::class.java]

        handleClickListeners()
    }

    private fun handleClickListeners(){
        binding?.btnSignIn?.setOnClickListener {
            binding?.tvLoginError?.isVisible = false
            setUpViewModel()
        }

        binding?.btnRedirectToSignUpPage?.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding?.passwordCheckBox?.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked){
                binding?.etPassWord?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding?.etPassWord?.length()?.let { binding?.etPassWord?.setSelection(it) }
            }
            else{
                binding?.etPassWord?.transformationMethod = PasswordTransformationMethod.getInstance()
                binding?.etPassWord?.length()?.let { binding?.etPassWord?.setSelection(it) }
            }
        }

        binding?.etPassWord?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding?.passwordCheckBox?.isVisible = p0?.isNotEmpty() == true
                binding?.tvShowPassword?.isVisible = p0?.isNotEmpty() == true
            }

            override fun afterTextChanged(p0: Editable?) {
                //do nothing
            }

        })
    }

    private fun setUpViewModel(){
        viewModel?.getUserLoginDataStatus(
            binding?.etEmail?.text.toString(),
            binding?.etPassWord?.text.toString()
        )

        viewModel?.getUserLoginDataStatus?.observe(this, Observer {
            when (it.status) {
                Result.SUCCESS -> {
                    Log.d(TAG, "it.Success and user name is = ${it.data?.name} and data is ${it.data}")
                    if (it.data != null) {
                        binding?.tvLoginError?.isVisible = false
                        PrefUtil.getInstance(this).saveLastLoginID(it.data.id)
                        PrefUtil.getInstance(this).saveLastLogin(true)
                        val intent = Intent(this, BookShelfActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d(TAG, "User does not exist in database.")
                        binding?.tvLoginError?.isVisible = true
                    }

                }
                Result.ERROR -> {
                    binding?.tvLoginError?.isVisible = true
                    Log.d(TAG, "login failed = ${it.error}")
                    Toast.makeText(this, "Invalid Login Credentials", Toast.LENGTH_LONG).show()
                }

                Result.LOADING -> {
                    Log.d(TAG, "login validation in process")
                }
            }
        })
    }

}