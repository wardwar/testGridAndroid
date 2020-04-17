package app.by.wildan.testgridandroid.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.by.wildan.testgridandroid.EventObserver
import app.by.wildan.testgridandroid.R
import app.by.wildan.testgridandroid.data.entity.User
import app.by.wildan.testgridandroid.databinding.ActivityMainBinding
import app.by.wildan.testgridandroid.decorator.DecoratorRecyclerViewHorizontal
import app.by.wildan.testgridandroid.dialog.LoadingDialog
import app.by.wildan.testgridandroid.extension.dp
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val queryFilter = HashMap<String, String>()
    private var isLoading = true
    private val pictureAdapter = ContentAdapter()
    private val categoryAdapter = CategoryAdapter()
    private var filterBy = ""
    private var currentPage = 1
    private var totalPage = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUserBinding()
        setupCategory()
        setupContent()
        setupLoading()
        setupErrorHandle()
        setupActionButton()
    }

    private fun setupUserBinding() {
        val user = User(
            "Wildan Angga Rahman",
            "Android Developer",
            "219",
            "2.1K",
            "2"
        )

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.user = user
    }

    private fun setupCategory() {
        listCategory.apply {
            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

            val dp16 = 16.dp
            val dp8 = 8.dp
            val decorator = DecoratorRecyclerViewHorizontal(
                dp16,
                dp16,
                dp16,
                dp16,
                dp8,
                dp8
            )
            addItemDecoration(decorator)
        }
        with(categoryAdapter) {
            notifyDataSetChanged(viewModel.categoryList)
            addOnItemClickListener { position, category ->
                filterBy = category.toLowerCase()
                currentPage = 1
                queryFilter["query"] = filterBy
                queryFilter["page"] = currentPage.toString()
                viewModel.getContent(queryFilter)
            }
        }
    }

    private fun setupContent() {
        queryFilter["query"] = categoryAdapter.getCurrentSelectedPostiion()
        queryFilter["page"] = currentPage.toString()
        viewModel.getContent(queryFilter)

        listPictures.apply {
            adapter = pictureAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 3)

        }

        viewModel.contentResponse.observe(this, Observer {
            totalPage = it?.total_pages ?: -1
            if (currentPage > 1) {
                pictureAdapter.appendData(it.results ?: emptyList())
            } else {
                pictureAdapter.notifyDataSetChanged(it.results ?: emptyList())
            }

            isLoading = true

        })
        initScrollListener()
    }

    private fun initScrollListener() {

        nestedScrollParent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    val layoutManager = listPictures.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (isLoading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount && currentPage < totalPage) {
                            currentPage += 1
                            queryFilter["page"] = currentPage.toString()
                            viewModel.getContent(queryFilter)
                        }
                    }
                }
            }
        })
    }

    private fun setupLoading() {
        val loading = LoadingDialog(this)
        viewModel.loadingPerform.observe(this, EventObserver {
            loading.perform(it)
        })

    }

    private fun setupErrorHandle() {
        viewModel.errorHandle.observe(this, EventObserver {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setupActionButton() {
        buttonFollow.setOnClickListener {
            followMe()
        }
    }

    private fun followMe() {
        val uri: Uri = Uri.parse("https://www.instagram.com/appbywildan")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)
        likeIng.setPackage("com.instagram.android")
        try {
            startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/appbywildan/")
                )
            )
        }
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
}
