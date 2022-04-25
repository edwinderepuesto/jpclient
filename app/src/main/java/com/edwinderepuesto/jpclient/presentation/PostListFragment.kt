package com.edwinderepuesto.jpclient.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.edwinderepuesto.jpclient.R
import com.edwinderepuesto.jpclient.common.MyResult
import com.edwinderepuesto.jpclient.data.dto.Post
import com.edwinderepuesto.jpclient.databinding.FragmentPostListBinding
import com.edwinderepuesto.jpclient.databinding.ItemPostBinding
import com.edwinderepuesto.jpclient.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link PostDetailsFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */

class PostListFragment : Fragment() {
    private var _binding: FragmentPostListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val adapter = SimpleItemRecyclerViewAdapter(
            emptyList(),
            itemDetailFragmentContainer,
        )

        binding.itemList.adapter = adapter

        val viewModel = MainViewModel()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { result ->
                    when (result) {
                        is MyResult.Success -> {
                            adapter.updateDataSet(result.data)
                            binding.statusTextView.text = getString(R.string.done)
                        }
                        is MyResult.Loading -> {
                            binding.statusTextView.text =
                                getString(
                                    if (result.loading)
                                        R.string.fetching_posts
                                    else
                                        R.string.idle
                                )
                        }
                        is MyResult.Error -> {
                            binding.statusTextView.text = result.errorMessage
                        }
                    }
                }
            }
        }
    }

    class SimpleItemRecyclerViewAdapter(
        private var values: List<Post>,
        private val itemDetailFragmentContainer: View?
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.PostItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {

            val binding =
                ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PostItemViewHolder(binding)

        }

        override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
            val item = values[position]
            holder.titleTextView.text = item.title
            holder.bodyTextView.text = item.body

            holder.itemView.setOnClickListener { itemView ->
                val bundle = Bundle()
                bundle.putString(
                    PostDetailsFragment.ARG_POST_ID,
                    item.id
                )
                bundle.putString(
                    PostDetailsFragment.ARG_POST_TITLE,
                    item.title
                )
                bundle.putString(
                    PostDetailsFragment.ARG_POST_BODY,
                    item.body
                )
                bundle.putString(
                    PostDetailsFragment.ARG_POST_USER_ID,
                    item.userId
                )

                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.fragment_item_detail, bundle)
                } else {
                    itemView.findNavController().navigate(R.id.show_item_detail, bundle)
                }
            }
        }

        override fun getItemCount() = values.size

        @SuppressLint("NotifyDataSetChanged")
        fun updateDataSet(newData: List<Post>) {
            values = newData
            notifyDataSetChanged()
        }

        inner class PostItemViewHolder(binding: ItemPostBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val titleTextView: TextView = binding.titleTextView
            val bodyTextView: TextView = binding.bodyTextView
            val favoriteIndicatorView: TextView = binding.favoriteIndicatorView
            val dismissPostButton: TextView = binding.dismissPostButton
            val thumbnailImageView: ImageView = binding.thumbnailImageView
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}