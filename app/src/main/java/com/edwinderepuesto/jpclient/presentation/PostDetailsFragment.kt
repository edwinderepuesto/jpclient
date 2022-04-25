package com.edwinderepuesto.jpclient.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.edwinderepuesto.jpclient.databinding.FragmentPostDetailsBinding

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [PostListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class PostDetailsFragment : Fragment() {
    private var postId: String = ""
    private var postTitle: String = ""
    private var postBody: String = ""
    private var postUserId: String = ""

    private var _binding: FragmentPostDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            postId = it.getString(ARG_POST_ID).toString()
            postTitle = it.getString(ARG_POST_TITLE).toString()
            postBody = it.getString(ARG_POST_BODY).toString()
            postUserId = it.getString(ARG_POST_USER_ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        val rootView = binding.root

        updateContent()

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