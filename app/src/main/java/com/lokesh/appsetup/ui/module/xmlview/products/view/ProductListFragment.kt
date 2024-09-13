package com.lokesh.appsetup.ui.module.xmlview.products.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lokesh.appsetup.R
import com.lokesh.appsetup.base.BaseFragment
import com.lokesh.appsetup.base.ErrorBody
import com.lokesh.appsetup.databinding.FragmentProductListBinding
import com.lokesh.appsetup.model.Categories
import com.lokesh.appsetup.model.Product
import com.lokesh.appsetup.ui.module.xmlview.products.adapter.ProductsAdapter
import com.lokesh.appsetup.ui.module.xmlview.products.model.GetProductsResponse
import com.lokesh.appsetup.ui.module.xmlview.products.viewmodel.ProductViewModel
import com.lokesh.appsetup.util.BACK_CATEGORY_DATA
import com.lokesh.appsetup.util.BACK_PRODUCT_DATA
import com.lokesh.appsetup.util.BACK_PRODUCT_INFO_TO_PRODUCT_LIST
import com.lokesh.appsetup.util.BACK_PRODUCT_LIST_TO_CATEGORY_LIST
import com.lokesh.appsetup.util.CATEGORY_NAME
import com.lokesh.appsetup.util.NetworkManager
import com.lokesh.appsetup.util.PRODUCT
import com.lokesh.appsetup.util.PRODUCT_DATA
import com.lokesh.appsetup.util.datastore.DataStoreKeys
import com.lokesh.appsetup.util.globalLiveInternetCheck
import com.lokesh.appsetup.util.onClick
import com.lokesh.appsetup.util.prefs.PrefsKeys
import com.lokesh.appsetup.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : BaseFragment<FragmentProductListBinding>() {

    private var category: Categories? = null
    private val viewModel: ProductViewModel by viewModels()
    private var productsAdapter: ProductsAdapter? = null

    override fun inflateLayout(layoutInflater: LayoutInflater, container: ViewGroup?, attachToContext: Boolean): FragmentProductListBinding =
        FragmentProductListBinding.inflate(layoutInflater, container, attachToContext)

    override fun initCreate() {
        super.initCreate()
        navigateFragmentData()
        buttonInit()
        setBaseObservers()
        backFragmentData()
        setupRecycler()
        callApi()
        printLastDataStoreResponse()
    }

    private fun printLastDataStoreResponse() {
        /** This is how you can fetch easily from Data Store */
        val lastResponse = dataStoreUtils.getObjectData(DataStoreKeys.PRODUCT_INFO, GetProductsResponse::class.java)
        Log.d("Logger", "DataStore: ${Gson().toJson(lastResponse)}")

        /** This is how you can fetch easily from Shared Pref */
        val lastResponsePref = prefUtils.getObjectData(PrefsKeys.PRODUCT_INFO, GetProductsResponse::class.java)
        Log.d("Logger", "Pref: ${Gson().toJson(lastResponsePref)}")

        /** TypeTokenExample */
        val typeToken = object : TypeToken<List<Product>>() {}.type
        val gsonData = prefUtils.getData(PrefsKeys.PRODUCT_INFO_OFFLINE, "")
        val res = Gson().fromJson<ArrayList<Product>>(gsonData, typeToken)
        Log.d("Logger", "Pref: ${Gson().toJson(res)}")
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
            viewModel.getProductsByCategory(true, category?.slug ?: "")
            viewModel.getProducts.observe(viewLifecycleOwner, productsObserver)
        } else {
            viewModel.getProductsByCategoryOffline(true, category?.slug ?: "")
            viewModel.getProductsOffline.observe(viewLifecycleOwner, productsOfflineObserver)
        }
    }

    private val productsObserver = Observer<GetProductsResponse?> {
        if (it != null) {
            /** This is how you can store easily in Data Store */
            dataStoreUtils.putObjectData(DataStoreKeys.PRODUCT_INFO, it)

            /** This is how you can store easily in Shared Pref */
            prefUtils.putObjectData(PrefsKeys.PRODUCT_INFO, it)

            productsAdapter?.fillProduct(it.products)
            showSnackBar("Products has been loaded from online", false, requireActivity())
        } else if (!TextUtils.isEmpty(it?.message)) viewModel.error.value = ErrorBody(message = it?.message)
    }

    private val productsOfflineObserver = Observer<List<Product>?> {
        println("Lokesh -> " + Gson().toJson(it))
        if (!it.isNullOrEmpty() && !NetworkManager.internetAvailable) {
            /** This is how you can store easily in Shared Pref */
            prefUtils.putObjectData(PrefsKeys.PRODUCT_INFO_OFFLINE, it)

            productsAdapter?.fillProduct(it)
            showSnackBar("Products has been loaded from offline", false, requireActivity())
        }
    }

    private fun setupRecycler() {
        productsAdapter = ProductsAdapter(requireContext()) {
            navigateProductDesc(it)
        }
        binding.productRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productsAdapter
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

        binding.btnRefresh onClick {
            viewModel.reset()
            callApi()
        }
    }

    /** Update current fragment with data from navigated fragment */
    private fun navigateFragmentData() {
        val bundle = this.arguments
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                category = bundle.getParcelable(CATEGORY_NAME, Categories::class.java)
            } else {
                category = bundle.getParcelable(CATEGORY_NAME)
            }
        }
    }

    /** Update current fragment with data from previous fragment */
    @SuppressLint("SetTextI18n")
    private fun backFragmentData() {
        setFragmentResultListener(BACK_PRODUCT_INFO_TO_PRODUCT_LIST) { _, bundle ->
            val info = bundle.getString(BACK_PRODUCT_DATA, "-")
            binding.lastProduct.text = "${getString(R.string.last_product)} $info"
        }
    }

    private fun navigateProductDesc(product: Product) {
        val args = Bundle().also {
            it.putString(PRODUCT_DATA, product.title)
            it.putParcelable(PRODUCT, product)
        }
        findNavController().navigate(R.id.forward_productListFragment_to_productInfoFragment, args)
    }

    private fun backPress() {
        val bundle = Bundle()
        bundle.putString(BACK_CATEGORY_DATA, category?.name ?: "")
        setFragmentResult(BACK_PRODUCT_LIST_TO_CATEGORY_LIST, bundle)
        findNavController().navigateUp()
    }
}