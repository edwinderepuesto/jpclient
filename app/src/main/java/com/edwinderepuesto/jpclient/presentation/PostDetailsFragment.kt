package com.edwinderepuesto.jpclient.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.edwinderepuesto.jpclient.databinding.FragmentPostDetailsBinding
import com.edwinderepuesto.jpclient.presentation.viewmodel.MainViewModel
import com.edwinderepuesto.jpclient.presentation.viewmodel.MainViewModelFactory

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [PostListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class PostDetailsFragment : Fragment() {
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    private var postId: Int = -1
    private var postUserId: Int = -1
    private var postTitle: String = ""
    private var postBody: String = ""

    private var _binding: FragmentPostDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            postId = it.getInt(ARG_POST_ID)
            postUserId = it.getInt(ARG_POST_USER_ID)
            postTitle = it.getString(ARG_POST_TITLE).toString()
            postBody = it.getString(ARG_POST_BODY).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelFactory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        updateContent()

        viewModel.fetchComments(postId)

        return rootView
    }

    private fun updateContent() {
        binding.detailTitleTextView.text = postTitle
        binding.detailBodyTextView.text = postBody
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_POST_ID = "ARG_POST_ID"
        const val ARG_POST_TITLE = "ARG_POST_TITLE"
        const val ARG_POST_BODY = "ARG_POST_BODY"
        const val ARG_POST_USER_ID = "ARG_POST_USER_ID"
    }
}