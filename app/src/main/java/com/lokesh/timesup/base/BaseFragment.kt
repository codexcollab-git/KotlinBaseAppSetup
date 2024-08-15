package com.lokesh.timesup.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.lokesh.timesup.R
import com.lokesh.timesup.customdialog.CustomDialogBuilder
import com.lokesh.timesup.util.datastore.DataStoreUtils
import com.lokesh.timesup.util.NetworkManager
import com.lokesh.timesup.util.prefs.PrefUtils
import com.lokesh.timesup.util.globalLiveInternetCheck
import com.lokesh.timesup.customdialog.util.showErrorDialog
import com.lokesh.timesup.customdialog.util.showLoaderDialog
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    @Inject
    lateinit var prefUtils: PrefUtils
    @Inject
    lateinit var dataStoreUtils: DataStoreUtils

    lateinit var binding: VB
    var dialog: CustomDialogBuilder? = null
    var loaderDialog: CustomDialogBuilder? = null
    var networkManager: NetworkManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateLayout(inflater, container, false)
        initNetwork()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCreate()
    }

    abstract fun inflateLayout(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToContext: Boolean
    ): VB

    open fun initCreate() {
    }

    fun backPressHandle(viewLifeCycle: LifecycleOwner, function: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifeCycle, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                function.invoke()
            }
        })
    }

    val errorObserver = Observer<ErrorBody?> {
        it?.let {
            dialog = requireActivity().showErrorDialog(it.message ?: getString(R.string.api_error), getString(R.string.dialog_ok), dialog)
        }
    }

    val loadingObserver = Observer<Boolean> {
        if (it) loaderDialog = requireActivity().showLoaderDialog(prevDialog = loaderDialog)
        else loaderDialog?.dialogInstance?.dismissLoader()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (loaderDialog != null && loaderDialog?.dialogInstance?.getLoaderDialog()?.isShowing == true) loaderDialog?.dialogInstance?.dismissLoader()
    }

    override fun onDetach() {
        super.onDetach()
        if (loaderDialog != null && loaderDialog?.dialogInstance?.getLoaderDialog()?.isShowing == true) loaderDialog?.dialogInstance?.dismissLoader()
    }

    private fun initNetwork() {
        networkManager = NetworkManager(requireActivity())
        networkManager!!.register().observe(viewLifecycleOwner, networkObserver)
    }

    private val networkObserver = Observer<Boolean?> {
        it?.let {
            globalLiveInternetCheck.value = it
            NetworkManager.internetAvailable = it
            observeNetwork(it)
        }
    }

    open fun observeNetwork(isNetworkAvailable: Boolean) {}
}
