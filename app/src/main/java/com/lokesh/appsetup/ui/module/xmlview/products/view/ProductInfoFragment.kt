package com.lokesh.appsetup.ui.module.xmlview.products.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lokesh.appsetup.R
import com.lokesh.appsetup.base.BaseFragment
import com.lokesh.appsetup.databinding.FragmentProductInfoBinding
import com.lokesh.appsetup.model.Product
import com.lokesh.appsetup.util.BACK_PRODUCT_DATA
import com.lokesh.appsetup.util.BACK_PRODUCT_INFO_TO_PRODUCT_LIST
import com.lokesh.appsetup.util.PRODUCT
import com.lokesh.appsetup.util.PRODUCT_DATA
import com.lokesh.appsetup.util.prefs.PrefUtils
import com.lokesh.appsetup.util.globalLiveInternetCheck
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductInfoFragment : BaseFragment<FragmentProductInfoBinding>() {

    @Inject
    lateinit var pref: PrefUtils
    private var productTitle: String = ""
    private var product: Product? = null

    override fun inflateLayout(layoutInflater: LayoutInflater, container: ViewGroup?, attachToContext: Boolean): FragmentProductInfoBinding =
        FragmentProductInfoBinding.inflate(layoutInflater, container, attachToContext)

    override fun initCreate() {
        super.initCreate()
        navigateFragmentData()
        buttonInit()
        setBaseObservers()
    }

    private fun setBaseObservers() {
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

    private fun buttonInit() {
        /** Back Button Click */
        /*binding.prev onClick {
            backPress()
        }*/

        /** Android Back Gesture / System Back Button Click */
        backPressHandle(viewLifecycleOwner) {
            backPress()
        }
    }

    /** Update current fragment with data from navigated fragment */
    private fun navigateFragmentData() {
        val bundle = this.arguments
        if (bundle != null) {
            productTitle = bundle.getString(PRODUCT_DATA, "-")
            product = bundle.getParcelable(PRODUCT)
            updateUI()
        }
    }

    private fun updateUI() {
        if (product != null) {
            Glide.with(requireContext()).load(product?.thumbnail).into(binding.image)
            binding.product = product
        }
    }

    private fun backPress() {
        val bundle = Bundle()
        bundle.putString(BACK_PRODUCT_DATA, productTitle)
        setFragmentResult(BACK_PRODUCT_INFO_TO_PRODUCT_LIST, bundle)
        findNavController().navigateUp()
    }
}