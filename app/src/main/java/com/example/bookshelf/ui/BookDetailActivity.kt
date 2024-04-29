package com.example.bookshelf.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookshelf.R
import com.example.bookshelf.database.Book
import com.example.bookshelf.database.Users
import com.example.bookshelf.databinding.ActivityBookDetailBinding
import com.example.bookshelf.livedata.Result
import com.example.bookshelf.ui.adapters.TagsAdapter
import com.example.bookshelf.utils.PrefUtil
import com.example.bookshelf.utils.Utils
import com.example.bookshelf.viewmodel.BaseViewModelFactory
import com.example.bookshelf.viewmodel.SignInViewModel
import com.example.bookshelf.viewmodel.SignUpViewModel
import com.squareup.picasso.Picasso

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBookDetailBinding
    private var signInViewModel: SignInViewModel? = null
    private var signUpViewModel: SignUpViewModel? = null
    private var tagsAdapter : TagsAdapter? = null
    private var tagsList : MutableList<String>? = null
    private var isBookMarked : Boolean = false
    private var user : Users? = null
    private var index: Int? = null
    private var bookTitle : String? = null
    private val TAG = BookDetailActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInViewModel = ViewModelProvider(
            this, BaseViewModelFactory {
                SignInViewModel(Utils.provideUserCaseImpl(this))
            })[SignInViewModel::class.java]

        signUpViewModel = ViewModelProvider(
            this, BaseViewModelFactory {
                SignUpViewModel(Utils.provideUserCaseImpl(this))
            })[SignUpViewModel::class.java]

        fetchUserDetails()
        observeUserDetail()
        handleClickListeners()
        generateLoremIpsum()
        initializeAdapter()
        initView()
    }

    private fun initView(){
        bookTitle = intent.getStringExtra(mBookTitle)
        binding.tvBookTitle.text = bookTitle
        binding.tvBookScore.text = intent.getStringExtra(mBookScore)
        binding.tvBookPublishedDate.text = intent.getStringExtra(mPublishedDate)
        Picasso.get()
            .load(intent.getStringExtra(mBookImage))
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .fit()
            .centerCrop()
            .into(binding.ivBook)
    }

    private fun initializeAdapter(){
        binding.rvTagsList.apply {
            tagsAdapter = TagsAdapter()
            layoutManager = LinearLayoutManager(this@BookDetailActivity)
            adapter = tagsAdapter
        }
    }

    private fun generateLoremIpsum(){
        binding.tvLorem.text = Utils.generateLoremIpsum()
    }

    private fun handleBookMark(){
        if(isBookMarked){
            binding.ivBookMark.setImageResource(R.drawable.ic_black_star)
        }
        else{
            binding.ivBookMark.setImageResource(R.drawable.ic_white_star)
        }
    }

    private fun handleClickListeners(){
        binding.ivBookMark.setOnClickListener {
            isBookMarked = !isBookMarked
            handleBookMark()
            updateUserInDb()
        }

        binding.ivTag.setOnClickListener {
            if(binding.etTags.isVisible){
                binding.ivTag.setImageResource(R.drawable.ic_plus)
                binding.etTags.isVisible = false
                binding.btnAddTags.isVisible = false
            }
            else{
                binding.ivTag.setImageResource(R.drawable.ic_minus)
                binding.etTags.isVisible = true
                binding.btnAddTags.isVisible = true
            }
        }

        binding.btnAddTags.setOnClickListener {
            if(binding.etTags.isVisible && binding.etTags.text.isNotEmpty()){
                tagsList?.let {
                    binding.rvTagsList.isVisible = true
                    it.add(0, binding.etTags.text.toString())
                    updateUserInDb()
                    tagsAdapter?.updateList(it)
                } ?: run {
                    binding.rvTagsList.isVisible = true
                    tagsList = mutableListOf()
                    tagsList!!.add(0, binding.etTags.text.toString())
                    updateUserInDb()
                    tagsAdapter?.updateList(tagsList!!)
                }
                Log.d(TAG, "tags list is $tagsList and it's size is ${tagsList?.size}")
            }
        }
    }

    private fun updateUserInDb() {
        index = user?.booksList?.indexOfFirst { it.bookTitle == bookTitle }
        if (index != null && index != -1) {
            user?.booksList?.set(index!!, getBookItemFromDb())
        }
        else{
            if(user?.booksList == null) {
                user?.booksList = mutableListOf()
            }
            user?.booksList?.add(0, getBookItemFromDb())
        }
        user?.let {
            signUpViewModel?.insertUserData(
                Users(
                    id = it.id,
                    password = it.password,
                    name = it.name,
                    email = it.email,
                    country = it.country,
                    booksList = it.booksList
                )
            )
        }

        signUpViewModel?.insertUsersDataStatus?.observe(this, Observer {
            when (it.status) {
                Result.SUCCESS -> {
                    Log.d(TAG,"successfully updated user information${it.data}")
                }
                Result.LOADING -> {
                    Log.d(TAG,"loading updating user information")
                }
                Result.ERROR -> {
                    Log.d(TAG,"failed to update user information")
                }
            }
        })
    }

    private fun getBookItemFromDb(): Book {
        return Book(
            bookTitle = bookTitle,
            isBookMarked = isBookMarked,
            annotations = tagsList
        )
    }

    private fun fetchUserDetails(){
        signInViewModel?.getUserProfileData(PrefUtil.getInstance(this).getUserId())
    }

    private fun observeUserDetail(){
        signInViewModel?.getUserProfileDataStatus?.observe(this, Observer {
            when (it.status) {
                Result.SUCCESS -> {
                    Log.d(TAG, "user details success and name is ${it.data?.name} and book list size is ${it.data?.booksList}")
                    user = it.data
                    val book =
                        it.data?.booksList?.firstOrNull { bookList -> bookTitle == bookList.bookTitle }
                    book?.let { books ->
                        tagsList = books.annotations?.toMutableList()
                        if(tagsList.isNullOrEmpty()){
                            binding.rvTagsList.isVisible = false
                        }
                        else{
                            binding.rvTagsList.isVisible = true
                            tagsAdapter?.updateList(tagsList!!)
                        }
                        isBookMarked = book.isBookMarked == true
                        handleBookMark()
                    } ?: run {
                        binding.rvTagsList.isVisible = false
                    }
                }
                Result.ERROR -> {
                    Log.d(TAG, "user details failed")
                    binding.rvTagsList.isVisible = false
                }
                Result.LOADING -> {
                    Log.d(TAG, "user details in process")
                }
            }
        })
    }

    companion object {
        private const val mBookTitle = "bookTitle"
        private const val mBookScore = "bookScore"
        private const val mPublishedDate = "publishedDate"
        private const val mBookImage = "bookImage"

        fun start(context: Context, bookTitle: String?, bookScore: String?, publishedDate : String?, bookImage : String?) {
            val intent = Intent(context, BookDetailActivity::class.java).apply {
                putExtra(mBookTitle, bookTitle)
                putExtra(mBookScore, bookScore)
                putExtra(mPublishedDate, publishedDate)
                putExtra(mBookImage, bookImage)
            }
            context.startActivity(intent)
        }
    }
}