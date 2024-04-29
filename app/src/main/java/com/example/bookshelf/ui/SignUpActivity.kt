package com.example.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelf.R
import com.example.bookshelf.database.Users
import com.example.bookshelf.databinding.ActivitySignUpBinding
import com.example.bookshelf.livedata.Result
import com.example.bookshelf.utils.PrefUtil
import com.example.bookshelf.utils.Utils
import com.example.bookshelf.viewmodel.BaseViewModelFactory
import com.example.bookshelf.viewmodel.BookShelfViewModel
import com.example.bookshelf.viewmodel.SignUpViewModel

class SignUpActivity: AppCompatActivity() , AdapterView.OnItemSelectedListener {

    private var binding : ActivitySignUpBinding? = null
    private var viewModel: SignUpViewModel? =  null
    private var bookShelfViewModel : BookShelfViewModel? = null
    private val TAG = SignUpActivity::class.java.simpleName
    private var isEmailValid = false
    private var isUserNameValid = false
    private var isPasswordValid = false
    private var defaultCountry : String = "India (IN)"
    private val mutableCountryList : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        viewModel = ViewModelProvider(
            this, BaseViewModelFactory {
                SignUpViewModel(Utils.provideUserCaseImpl(this))
            })[SignUpViewModel::class.java]
        bookShelfViewModel = ViewModelProvider(
            this, BaseViewModelFactory {
                BookShelfViewModel(Utils.provideBookShelfDataRepository())
            })[BookShelfViewModel::class.java]

        fetchCountryList()
        handleListeners()
        observeCountryList()
        observeDefaultCountry()
    }

    private fun fetchCountryList(){
        bookShelfViewModel?.fetchCountryList()
    }

    private fun fetchDefaultCountry(){
        bookShelfViewModel?.getDefaultCountry()
    }

    private fun observeCountryList(){
        bookShelfViewModel?.countryListSuccessData?.observe(this, Observer { countryList ->
            countryList.data.forEach { (code, country) ->
                mutableCountryList.add("${country.country} ($code)")
            }
            val ad: ArrayAdapter<String> = ArrayAdapter(
                this,
                R.layout.item_country_list,
                mutableCountryList)
            ad.setDropDownViewResource(
                R.layout.item_drop_down_country_list)

            binding?.countryListView?.isVisible = true
            binding?.countryListView?.onItemSelectedListener = this
            binding?.countryListView?.adapter = ad
            fetchDefaultCountry()

        })

        bookShelfViewModel?.countryListFailureData?.observe(this, Observer { _ ->
            Log.d(TAG, "country list error")
            binding?.countryListView?.isVisible = false
            binding?.tvCountryError?.isVisible = false
        })
    }

    private fun observeDefaultCountry(){
        bookShelfViewModel?.defaultCountrySuccessData?.observe(this, Observer { defaultCounty ->
            val defaultCountryText = "${defaultCounty.country} (${defaultCounty.countryCode})"
            mutableCountryList.forEachIndexed { index, country ->
                if(country == defaultCountryText){
                    binding?.countryListView?.setSelection(index)
                    return@forEachIndexed
                }
            }
        })

        bookShelfViewModel?.defaultCountryFailureData?.observe(this, Observer { _ ->
            //getting failure because of forbidden from website
            Log.d(TAG, "default country list error")
            if(binding?.countryListView?.isVisible == true){
                mutableCountryList.forEachIndexed { index, country ->
                    if(country == defaultCountry){
                        //setting default country as India if api failure happens
                        binding?.countryListView?.setSelection(index)
                        return@forEachIndexed
                    }
                }
            }
        })
    }

    private fun handleListeners(){
        handleEmail()
        handleUserName()
        handlePassword()
        handleBtnSignUp()
    }

    private fun showProgressContainer(show : Boolean){
        binding?.progressContainer?.isVisible = show
        binding?.mainContainer?.isVisible = !show
    }

    private fun handleBtnSignUp(){
        binding?.btnSignUp?.setOnClickListener {
            if(validateDetails()){
                val users = Users(
                    email = binding!!.etEmail.text.toString(),
                    name = binding!!.etUserName.text.toString(),
                    password = binding!!.etPassWord.text.toString(),
                    country = defaultCountry

                )
                viewModel?.insertUserData(users)
                viewModel?.insertUsersDataStatus?.observe(this, Observer {
                    when (it.status) {
                        Result.SUCCESS -> {
                            showProgressContainer(false)
                            PrefUtil.getInstance(this).saveLastLogin(true)
                            PrefUtil.getInstance(this).saveLastLoginID(it.data!!)
                            val intent = Intent(this, BookShelfActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            Log.d(TAG,"created account data is ${it.data}")
                            Toast.makeText(this, "SuccessFully Account Created and Logged In", Toast.LENGTH_LONG).show()
                            finish()
                        }
                        Result.LOADING -> {
                            showProgressContainer(true)
                        }
                        Result.ERROR -> {
                            Log.d(TAG, "inserting user details failed in db : ${it.error}")
                            Toast.makeText(this, "Something went wrong try again later", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
            else{
                Toast.makeText(this, "Please Check All Details", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateDetails() :Boolean{
        return isEmailValid && isUserNameValid && isPasswordValid
    }

    private fun handlePassword() {
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
                p0?.toString()?.let {
                    if (it.isNotEmpty()) {
                        isPasswordValid = viewModel?.isPasswordValid(it) == true
                        binding?.tvPassWordError?.isVisible = !isPasswordValid
                    } else {
                        isPasswordValid = false
                        binding?.tvPassWordError?.isVisible = !isPasswordValid
                    }
                }
            }

        })
    }

    private fun handleEmail(){
        binding?.etEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.toString()?.let {
                    if (it.isNotEmpty()) {
                        binding?.tvEmailError?.text = getString(R.string.email_error)
                        isEmailValid = viewModel?.isValidEmail(it) == true
                        binding?.tvEmailError?.isVisible = !isEmailValid
                    } else {
                        binding?.tvEmailError?.text = getString(R.string.email_empty_error)
                        isEmailValid = false
                        binding?.tvEmailError?.isVisible = !isEmailValid
                    }
                }
            }

        })
    }

    private fun handleUserName(){
        binding?.etUserName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.toString()?.let {
                    if (it.isNotEmpty()) {
                        isUserNameValid = true
                        binding?.tvUserNameError?.isVisible = !isUserNameValid
                    } else {
                        isUserNameValid = false
                        binding?.tvUserNameError?.isVisible = !isUserNameValid
                    }
                }
            }

        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        binding?.countryListView?.setSelection(position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //do nothing
    }
}