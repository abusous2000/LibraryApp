package com.sample.libraryapplication.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(){
    private val TAG = "BaseFragment"
    private var persistingView: View? = null

    private fun persistingView(view: View): View {
        val root = persistingView
        if (root == null) {
            persistingView = view
            return view
        } else {
            (root.parent as? ViewGroup)?.removeView(root)
            return root
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {
        val p = if (persistingView == null) onCreatePersistentView(inflater, container, savedInstanceState) else persistingView // prevent inflating
        if (p != null) {
            return persistingView(p)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    protected open fun onCreatePersistentView(inflater: LayoutInflater,
                                              container: ViewGroup?,
                                              savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (persistingView != null) {
            onPersistentViewCreated(view, savedInstanceState)
        }
    }

    protected open fun onPersistentViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onPersistentViewCreated: ")
    }
}