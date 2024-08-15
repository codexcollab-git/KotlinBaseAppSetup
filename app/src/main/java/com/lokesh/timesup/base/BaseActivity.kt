package com.lokesh.timesup.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
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

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    @Inject
    lateinit var prefUtils: PrefUtils
    @Inject
    lateinit var dataStoreUtils: DataStoreUtils

    lateinit var binding: VB
    var dialog: CustomDialogBuilder? = null
    var loaderDialog: CustomDialogBuilder? = null

    private var networkManager: NetworkManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        binding = inflateLayout(layoutInflater)
        setContentView(binding.root)
        initNetwork()
        initCreate()
    }

    abstract fun inflateLayout(layoutInflater: LayoutInflater): VB

    private fun initNetwork() {
        networkManager = NetworkManager(this)
        networkManager!!.register().observe(this, networkObserver)
    }

    open fun initCreate() {}

    val errorObserver = Observer<ErrorBody?> {
        it?.let {
            dialog = showErrorDialog(it.message ?: getString(R.string.api_error), getString(R.string.dialog_ok), dialog)
        }
    }

    val loadingObserver = Observer<Boolean> {
        if (it) loaderDialog = showLoaderDialog(prevDialog = loaderDialog)
        else loaderDialog?.dialogInstance?.dismissLoader()
    }

    private val networkObserver = Observer<Boolean?> {
        it?.let {
            globalLiveInternetCheck.value = it
            NetworkManager.internetAvailable = it
            observeNetwork(it)
        }
    }

    open fun observeNetwork(isNetworkAvailable: Boolean) {}

    override fun onDestroy() {
        super.onDestroy()
        networkManager?.unRegister()
        if (loaderDialog != null && loaderDialog?.dialogInstance?.getLoaderDialog()?.isShowing == true) loaderDialog?.dialogInstance?.dismissLoader()
    }
}
