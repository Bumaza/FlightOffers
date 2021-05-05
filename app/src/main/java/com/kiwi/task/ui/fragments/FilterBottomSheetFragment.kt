package com.kiwi.task.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kiwi.task.R
import com.kiwi.task.databinding.BottomSheetFilterBinding
import com.kiwi.task.viewmodels.TopOffersViewModel

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var rootView: View
    lateinit var viewModel: TopOffersViewModel
    lateinit var binding: BottomSheetFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_filter, container, false)
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews(){
        binding.viewModel = viewModel
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.onFilterClose()
    }

    companion object{
        @JvmStatic
        fun newInstance(viewModel: TopOffersViewModel) : FilterBottomSheetFragment {
            val fragment = FilterBottomSheetFragment()
            fragment.viewModel = viewModel
            return fragment
        }
    }

}