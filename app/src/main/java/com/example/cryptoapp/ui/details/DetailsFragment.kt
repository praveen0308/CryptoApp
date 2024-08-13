package com.example.cryptoapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentDetailsBinding
import com.example.cryptoapp.databinding.FragmentHomeBinding
import com.example.cryptoapp.ui.home.HomeViewModel
import com.example.cryptoapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : DetailsViewModel by viewModels<DetailsViewModel>()

    private val args : DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchCoinDetail(args.coinId)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.coinDetail.observe(viewLifecycleOwner){result->

            binding.loader.isVisible = result.status == Status.LOADING
            when(result.status){
                Status.LOADING -> {
                    ///
                }
                Status.SUCCESS -> {
                    result._data?.let {detail->
                        binding.apply {
                            Glide.with(this@DetailsFragment)
                                .load(detail.logo)
                                .into(itemLogo)

                            itemName.text = detail.name
                            itemSymbol.text = detail.symbol
                            itemRank.text = "Rank ${detail.rank}"
//                            parentInfo.text = "Parent ${detail.}"
                            itemTypeStatus.text = "Type: ${detail.type} | Active: ${if (detail.isActive) "Yes" else "No"}"
                            itemDescription.text = detail.description

                            // Set tags
                            itemTags.text = "Tags: ${detail.tags.joinToString { it.name }}"

                            // Set team information
                            teamInfo.text = "Team: ${detail.team.joinToString { "${it.name} - ${it.position}" }}"

                            // Set links
                            itemExplorer.text = "Explorer: ${detail.links?.explorer?.firstOrNull() ?: "N/A"}"
                            itemWebsite.text = "Website: ${detail.links?.website?.firstOrNull() ?: "N/A"}"
                            // Set other links similarly

                            // Load whitepaper thumbnail and set the link
                            detail.whitepaper?.let {
                                Glide.with(root.context)
                                    .load(it.thumbnail)
                                    .into(whitepaperThumbnail)

                                whitepaperLink.text = "Whitepaper"
                                whitepaperLink.setOnClickListener { _ ->
                                    // Open whitepaper link
                                    it.link?.let { it1 -> openLink(it1) }
                                }
                            }
                        }
                    }

                }
                Status.ERROR -> Toast.makeText(requireContext(),result.message,Toast.LENGTH_LONG).show()
            }

        }
    }
    fun openLink( url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}