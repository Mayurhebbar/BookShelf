package com.example.bookshelf.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelf.databinding.ActivityBookShelfBinding
import com.example.bookshelf.network.models.BookShelfInfo
import com.example.bookshelf.network.models.ContentItemsPerYear
import com.example.bookshelf.network.models.YearInfo
import com.example.bookshelf.ui.adapters.ContentAdapter
import com.example.bookshelf.ui.adapters.YearsAdapter
import com.example.bookshelf.utils.PrefUtil
import com.example.bookshelf.utils.Utils
import com.example.bookshelf.viewmodel.BaseViewModelFactory
import com.example.bookshelf.viewmodel.BookShelfViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class BookShelfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookShelfBinding
    private var bookShelfViewModel : BookShelfViewModel? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var yearsAdapter: YearsAdapter? = null
    private var contentAdapter: ContentAdapter? = null
    private var selectedYearPosition: Int = 0
    private var bookList : List<BookShelfInfo?>? = null
    private var TAG = BookShelfActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookShelfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookShelfViewModel = ViewModelProvider(
            this, BaseViewModelFactory {
                BookShelfViewModel(Utils.provideBookShelfDataRepository())
            })[BookShelfViewModel::class.java]

        fetchBookList()
        observerBookList()
        showProgressContainer(true)
        handleClickListeners()
    }

    private fun handleClickListeners(){
        binding.tvLogout.setOnClickListener {
            PrefUtil.getInstance(this).saveLastLogin(false)
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchBookList(){
        bookShelfViewModel?.getBookList()
    }

    private fun showProgressContainer(show : Boolean){
        binding.progressContainer.isVisible = show
        binding.toolBarContainer.isVisible = !show
        binding.rvServiceList.isVisible = !show
    }

    private fun observerBookList(){
        bookShelfViewModel?.bookListSuccessData?.observe(this, Observer { bookList ->
            this.bookList = bookList
            val yearInfo :MutableList<YearInfo> = mutableListOf()
            val contentList : MutableList<ContentItemsPerYear> = mutableListOf()
            val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            val years = bookList?.map {
                dateFormat.format(Date(it?.publishedChapterDate?.times(1000) ?: 0)).toInt()
            }?.distinct()

            // Sort the years in descending order
            val yearInOrder = years?.sortedDescending()
            yearInOrder?.forEachIndexed { index, year ->
                if(index == 0){
                    yearInfo.add(YearInfo(year.toString(), true))
                }
                else {
                    yearInfo.add(YearInfo(year.toString(), false))
                }
            }

            val yearMap = mutableMapOf<Int, List<BookShelfInfo?>?>()
            val finalList = mutableListOf<BookShelfInfo?>()
            yearInOrder?.forEach { year ->
                val itemsForYear = bookList.filter { dateFormat.format(Date(it?.publishedChapterDate?.times(
                    1000
                ) ?: 0)).toInt() == year }
                yearMap[year] = itemsForYear
            }
            yearInOrder?.forEach { year ->
                yearMap[year]?.let {
                    finalList.addAll(it)
                    contentList.add(ContentItemsPerYear(it))
                }
            }

            binding.tvTitles.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                yearsAdapter = YearsAdapter(this@BookShelfActivity, yearInfo) {
                    selectedYearPosition = it
                    setYearSelected()
                    val selectedYear = yearInfo[selectedYearPosition]
                    for ((index, bookShelfInfo) in finalList.withIndex()) {
                        val bookShelfYear = dateFormat.format(Date(bookShelfInfo?.publishedChapterDate?.times(1000) ?: 0)).toInt().toString()
                        if(selectedYear.year == bookShelfYear) {
                            binding.rvServiceList.layoutManager?.scrollToPosition(index)
                            break
                        }
                    }
                }
                adapter = yearsAdapter
            }

            linearLayoutManager = LinearLayoutManager(this)
            binding.rvServiceList.apply {
                layoutManager = linearLayoutManager
                contentAdapter = ContentAdapter(finalList){bookShelfInfo ->
                    bookShelfInfo?.let {
                        BookDetailActivity.start(
                            this@BookShelfActivity,
                            bookTitle = bookShelfInfo.title,
                            bookScore = bookShelfInfo.score.toString(),
                            publishedDate = bookShelfInfo.publishedChapterDate.toString(),
                            bookImage = bookShelfInfo.image)
                    }
                }
                adapter = contentAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        linearLayoutManager?.let { lm ->
                            val index = lm.findLastVisibleItemPosition()
                            if(index < (finalList.size-1)) {
                                val getCurrentBookItem = finalList[index]
                                val visibleYear = dateFormat.format(Date(getCurrentBookItem?.publishedChapterDate?.times(1000) ?: 0)).toInt().toString()
                                val year = yearInfo[selectedYearPosition]
                                if(visibleYear != year.year){
                                    for ((yearIndex, specifiedYear) in yearInfo.withIndex()) {
                                        if(specifiedYear.year == visibleYear){
                                            selectedYearPosition = yearIndex
                                            setYearSelected()
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }
            showProgressContainer(false)
        })

        bookShelfViewModel?.bookListFailureData?.observe(this, Observer { _ ->
            Log.d(TAG, "book list api error")
        })
    }

    private fun setYearSelected() {
        yearsAdapter?.updateList(selectedYearPosition)
    }
}