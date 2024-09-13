package com.lokesh.appsetup.ui.module.xmlview.categories.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lokesh.appsetup.R
import com.lokesh.appsetup.base.BaseFragment
import com.lokesh.appsetup.databinding.FragmentCategoriesListBinding
import com.lokesh.appsetup.model.Categories
import com.lokesh.appsetup.ui.module.xmlview.categories.adapter.CategoriesAdapter
import com.lokesh.appsetup.ui.module.xmlview.categories.viewmodel.CategoriesViewModel
import com.lokesh.appsetup.util.BACK_CATEGORY_DATA
import com.lokesh.appsetup.util.BACK_PRODUCT_LIST_TO_CATEGORY_LIST
import com.lokesh.appsetup.util.CATEGORY_NAME
import com.lokesh.appsetup.util.NetworkManager
import com.lokesh.appsetup.util.globalLiveInternetCheck
import com.lokesh.appsetup.util.onClick
import com.lokesh.appsetup.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesListFragment : BaseFragment<FragmentCategoriesListBinding>() {

    private val viewModel: CategoriesViewModel by viewModels()
    private var categoryAdapter: CategoriesAdapter? = null

    override fun inflateLayout(layoutInflater: LayoutInflater, container: ViewGroup?, attachToContext: Boolean): FragmentCategoriesListBinding =
        FragmentCategoriesListBinding.inflate(layoutInflater, container, attachToContext)

    override fun initCreate() {
        super.initCreate()
        buttonInit()
        setBaseObservers()
        backFragmentData()
        setupRecycler()
        callApi()
    }

    private fun setBaseObservers() {
        viewModel.loading.observe(viewLifecycleOwner, loadingObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
        globalLiveInternetCheck.observe(viewLifecycleOwner, internetObserve)
    }

    private val internetObserve = Observer<Boolean?> {
        if (it != null) {
            if (it) {
                binding.offlineRibbionTxt.text = getString(R.string.online)
            } else {
                binding.offlineRibbionTxt.text = getString(R.string.offline)
            }
        }
    }

    private fun callApi() {
        if (NetworkManager.internetAvailable) {
            viewModel.getCategories(true)
            viewModel.getCategories.observe(viewLifecycleOwner, categoriesObserver)
        } else {
            viewModel.getCategoriesOffline(true)
            viewModel.getCategoriesOffline.observe(viewLifecycleOwner, categoriesOfflineObserver)
        }
    }

    private fun setupRecycler() {
        categoryAdapter = CategoriesAdapter(requireContext()) {
            navigateProductList(it)
        }
        binding.categoryRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoryAdapter
        }
    }

    private val categoriesObserver = Observer<ArrayList<Categories>?> {
        if (!it.isNullOrEmpty() && NetworkManager.internetAvailable) {
            categoryAdapter?.fillCategories(it)
            showSnackBar("Categories have been loaded from online", false, requireActivity())
        }
    }

    private val categoriesOfflineObserver = Observer<List<Categories>?> {
        if (!it.isNullOrEmpty() && !NetworkManager.internetAvailable) {
            categoryAdapter?.fillCategories(it)
            showSnackBar("Categories have been loaded from offline", false, requireActivity())
        }
    }

    private fun buttonInit() {
        binding.btnRefresh onClick {
            viewModel.reset()
            callApi()
        }
    }

    /** Update current fragment with data from previous fragment */
    @SuppressLint("SetTextI18n")
    private fun backFragmentData() {
        setFragmentResultListener(BACK_PRODUCT_LIST_TO_CATEGORY_LIST) { _, bundle ->
            val info = bundle.getString(BACK_CATEGORY_DATA, "-")
            binding.lastCategory.text = "${getString(R.string.last_category)} $info"
        }
    }

    private fun navigateProductList(category: Categories) {
        val args = Bundle().also { it.putParcelable(CATEGORY_NAME, category) }
        findNavController().navigate(R.id.forward_categoriesListFragment_to_productListFragment, args)
    }
}