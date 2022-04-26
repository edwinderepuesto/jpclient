package com.edwinderepuesto.jpclient.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.edwinderepuesto.jpclient.R
import com.edwinderepuesto.jpclient.common.MyResult
import com.edwinderepuesto.jpclient.data.dto.PostComment
import com.edwinderepuesto.jpclient.databinding.FragmentPostDetailsBinding
import com.edwinderepuesto.jpclient.databinding.ItemCommentBinding
import com.edwinderepuesto.jpclient.presentation.viewmodel.MainViewModel
import com.edwinderepuesto.jpclient.presentation.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [PostListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class PostDetailsFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentPostDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelFactory = MainViewModelFactory(requireActivity())
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val adapter = CommentRecyclerViewAdapter(emptyList())

        binding.commentsRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedPost.collect { selectedPost ->
                    binding.detailsTitleTextView.text =
                        selectedPost?.title ?: getString(R.string.click_post_for_details)
                    binding.detailsBodyTextView.text = selectedPost?.body ?: ""
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.commentsState.collect { result ->
                    when (result) {
                        is MyResult.Success -> {
                            adapter.updateCommentsDataSet(result.data)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collect { result ->
                    when (result) {
                        is MyResult.Loading -> {
                            binding.detailsUserTextView.text =
                                getString(
                                    if (result.loading)
                                        R.string.fetching_user_info
                                    else
                                        R.string.idle
                                )
                        }
                        is MyResult.Success -> {
                            binding.detailsUserTextView.text =
                                "${result.data.name} (${result.data.username} - ${result.data.email})"
                        }
                        is MyResult.Error -> {
                            binding.detailsUserTextView.text = result.errorMessage
                        }
                    }
                }
            }
        }

        viewModel.fetchSelectedPostDetails()

        return rootView
    }

    class CommentRecyclerViewAdapter(private var values: List<PostComment>) :
        RecyclerView.Adapter<CommentRecyclerViewAdapter.CommentItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemViewHolder {

            val binding =
                ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CommentItemViewHolder(binding)

        }

        override fun onBindViewHolder(holder: CommentItemViewHolder, position: Int) {
            val item = values[position]
            holder.commentBodyTextView.text = item.body
        }

        override fun getItemCount() = values.size

        @SuppressLint("NotifyDataSetChanged")
        fun updateCommentsDataSet(newData: List<PostComment>) {
            values = newData
            notifyDataSetChanged()
        }

        inner class CommentItemViewHolder(binding: ItemCommentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val commentBodyTextView: TextView = binding.commentBodyTextView
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}