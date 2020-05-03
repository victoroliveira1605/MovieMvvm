package br.com.victorhgo.movieapp.presentation.popular

import android.content.Intent
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
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
import kotlinx.android.synthetic.main.popular_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class PopularFragment : Fragment() {
    private val popularViewModel: PopularViewModel by sharedViewModel()
    private val popularAdapter: PopularAdapter by inject()
    lateinit var popularGridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popular_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupPopularRecyclerView()
        popularViewModel.getPopular()

        popularViewModel.listPopular.observe(viewLifecycleOwner, Observer {
            if (it != null) updatePopularList(it)
        })

        popularViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                progressPopular.visibility = View.VISIBLE
            } else {
                progressPopular.visibility = View.INVISIBLE
            }
        })

        popularViewModel.errorConnection.observe(viewLifecycleOwner, Observer { hasError ->
            if (hasError) {
                popularRecyclerView.visibility = View.INVISIBLE
                layoutInternetErrorPopular.visibility = View.VISIBLE
            } else {
                popularRecyclerView.visibility = View.VISIBLE
                layoutInternetErrorPopular.visibility = View.INVISIBLE
            }
        })

        popularViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            textViewErrorMessagePopular.text = it
        })


        popularAdapter.setOnClickPopularListener {
            startActivity(Intent(activity, DetailActivity::class.java).apply {
                putExtra("movie", it)
            })
        }
    }


    private fun setupPopularRecyclerView() {
        popularGridLayoutManager =
            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(activity, 3)
            } else {
                GridLayoutManager(activity, 4)
            }

        popularRecyclerView.apply {
            adapter = popularAdapter
            setHasFixedSize(true)
            this.layoutManager = popularGridLayoutManager
        }

        popularRecyclerView.addOnScrollListener(
            object : EndlessRecyclerViewScrollListener(popularGridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    popularViewModel.page = page + 1
                    popularViewModel.getPopular()
                }
            }
        )
    }

    private fun updatePopularList(movieList: List<Movie>) {
        popularAdapter.addPopularMovies(movieList)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PopularFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
