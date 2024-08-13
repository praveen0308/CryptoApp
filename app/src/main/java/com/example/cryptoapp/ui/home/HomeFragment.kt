package com.example.cryptoapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.databinding.FragmentHomeBinding
import com.example.cryptoapp.model.CoinModel
import com.example.cryptoapp.ui.adapters.CoinListAdapter
import com.example.cryptoapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), CoinListAdapter.CoinListAdapterListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()


    private lateinit var coinListAdapter: CoinListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchCoins()
        setupRvList()
        setupObservers()
        binding.swiperefresh.setOnRefreshListener {

            // on below line we are setting is refreshing to false.
            binding.swiperefresh.isRefreshing = false
            viewModel.fetchCoins()
        }
        binding.searchView.apply {
            onActionViewExpanded()
            clearFocus()
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                coinListAdapter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                coinListAdapter.filter(newText)
                return false
            }
        })
    }

    private fun setupRvList() {
        binding.rvList.apply {
            coinListAdapter = CoinListAdapter(this@HomeFragment)
            setHasFixedSize(true)
            val mLayoutManager =LinearLayoutManager(context)
            layoutManager = mLayoutManager
            val dividerItemDecoration = DividerItemDecoration(
                requireContext(),
                mLayoutManager.orientation
            )
            addItemDecoration(dividerItemDecoration)


            adapter = coinListAdapter
        }
    }

    private fun setupObservers() {
        viewModel.coinList.observe(viewLifecycleOwner) { result ->

            binding.loader.isVisible = result.status == Status.LOADING
            when (result.status) {
                Status.LOADING -> {
                    ///
                }

                Status.SUCCESS -> {
                    result._data?.let { items ->
                        coinListAdapter.setCoinList(items)
                    }

                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()

                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(coin: CoinModel) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(coin.id))
    }

}