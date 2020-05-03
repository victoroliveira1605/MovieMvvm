package br.com.victorhgo.movieapp.presentation.upcoming

import android.content.Intent
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import br.com.victorhgo.movieapp.R
import br.com.victorhgo.movieapp.data.model.Movie
import br.com.victorhgo.movieapp.presentation.detail.DetailActivity
import br.com.victorhgo.movieapp.util.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.upcoming_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class UpcomingFragment : Fragment() {
    private val upcomingViewModel: UpcomingViewModel by sharedViewModel()
    private val upcomingAdapter: UpcomingAdapter by inject()
    lateinit var upcomingGridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upcoming_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUpcomingRecyclerView()
        upcomingViewModel.getUpcoming()

        upcomingViewModel.upcomingList.observe(viewLifecycleOwner, Observer {
            if (it != null) updateUpcomingList(it)
        })

        upcomingViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                progressUpcoming.visibility = View.VISIBLE
            } else {
                progressUpcoming.visibility = View.INVISIBLE
            }
        })

        upcomingViewModel.errorConnection.observe(viewLifecycleOwner, Observer { hasError ->
            if (hasError) {
                upcomingRecyclerView.visibility = View.INVISIBLE
                layoutInternetErrorUpcoming.visibility = View.VISIBLE
            } else {
                upcomingRecyclerView.visibility = View.VISIBLE
                layoutInternetErrorUpcoming.visibility = View.INVISIBLE
            }
        })

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            textViewErrorMessageUpcoming.text = it
        })


        upcomingAdapter.setOnClickUpcomingListener {
            startActivity(Intent(activity, DetailActivity::class.java).apply {
                putExtra("movie", it)
            })
        }

        upcoming_fragment_search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                upcomingAdapter.filter.filter(s.toString())
            }
        })

    }


    private fun setupUpcomingRecyclerView() {
        upcomingGridLayoutManager =
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(activity, 3)
            } else {
                GridLayoutManager(activity, 4)
            }

        upcomingRecyclerView.apply {
            adapter = upcomingAdapter
            setHasFixedSize(true)
            this.layoutManager = upcomingGridLayoutManager
        }

        upcomingRecyclerView.addOnScrollListener(
            object : EndlessRecyclerViewScrollListener(upcomingGridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    if (upcoming_fragment_search.text.isNullOrEmpty()) {
                        upcomingViewModel.page = page + 1
                        upcomingViewModel.getUpcoming()
                    }
                }
            }
        )
    }

    private fun updateUpcomingList(movieList: List<Movie>) {
        upcomingAdapter.addUpcoming(movieList)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UpcomingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
