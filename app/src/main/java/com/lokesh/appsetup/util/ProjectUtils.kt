package com.lokesh.appsetup.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.coroutineScope
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.lokesh.appsetup.BuildConfig
import com.lokesh.appsetup.R
import com.lokesh.appsetup.application.App
import com.lokesh.appsetup.commonlistener.ClickListener
import com.lokesh.appsetup.util.encryption.Base64_Local
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.*
import java.text.NumberFormat
import java.text.ParseException
import java.util.*
import java.util.regex.Pattern

const val BUNDLE_DATA = "BUNDLE_DATA"
val globalLiveInternetCheck: MutableLiveData<Boolean> = MutableLiveData(false)

fun getStringResource(@StringRes res: Int) = App.getAppContext().getString(res)

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.startActivityWithParams(
    activity: Class<*>,
    bundleData: Bundle? = null,
    isSingleTop: Boolean = false,
    isNewTask: Boolean = false,
    isClearTop: Boolean = false,
    isClearTask: Boolean = false
) {
    startActivity(
        Intent(this, activity).putExtra(BUNDLE_DATA, bundleData).apply {
            if (isSingleTop) flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            if (isNewTask) flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (isClearTop) flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (isClearTask) flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    )
}

fun showSnackBar(
    message: String,
    isError: Boolean = false,
    activity: Activity,
    isEngagementNeeded: Boolean = false,
    listener: ClickListener<String>? = null
) {
    val context = App.getAppContext()
    val font = ResourcesCompat.getFont(context, R.font.roboto_regular)
    val sb = if (isEngagementNeeded) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_INDEFINITE
        ).setActionTextColor(ContextCompat.getColor(context, R.color.white))
            .setAction("OK") { listener?.onClick("") }
    } else {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    }
    sb.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
        maxLines = 3
        typeface = font
    }
    if (isError) {
        sb.setActionTextColor(ContextCompat.getColor(context, R.color.white)).show()
    } else {
        sb.setActionTextColor(ContextCompat.getColor(context, R.color.white)).setAction("Action", null).show()
    }
}

fun Activity.hideKeyboard() {
    this.currentFocus?.let { view ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.getRealPathFromURI(contentURI: Uri): String? {
    val cursor = contentResolver.query(contentURI, null, null, null, null)
    return if (cursor == null) {
        contentURI.path
    } else {
        cursor.moveToFirst()
        val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        cursor.getString(index)
    }
}

fun Context.fileFromContentUri(contentUri: Uri): File {
    val fileExtension = getFileExtension(this, contentUri)
    val fileName =
        "Doc_" + System.currentTimeMillis() + if (fileExtension != null) ".$fileExtension" else ""
    val tempFile = File(cacheDir, fileName)
    tempFile.createNewFile()

    try {
        val oStream = FileOutputStream(tempFile)
        val inputStream = contentResolver.openInputStream(contentUri)

        inputStream?.let {
            copy(inputStream, oStream)
        }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}

fun Context.isEmailValid(email: String): Boolean {
    val matcher = Patterns.EMAIL_ADDRESS.matcher(email)
    return matcher.matches()
}

fun Context.isValidMobile(phone: String): Boolean {
    val regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]")
    return Patterns.PHONE.matcher(phone).matches() && !regex.matcher(phone).find()
}

@SuppressLint("HardwareIds")
fun Context.getDeviceUniqueId(): String {
    val deviceId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    return deviceId.ifEmpty { " " }
}

fun getDeviceModel(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.lowercase().startsWith(manufacturer.lowercase())) {
        getCapitalizeDeviceModel(model)
    } else {
        getCapitalizeDeviceModel(manufacturer) + " " + model
    }
}

fun getCapitalizeDeviceModel(model: String): String {
    return if (!TextUtils.isEmpty(model)) {
        val firstLetter = model[0]
        if (Character.isUpperCase(firstLetter)) {
            model
        } else {
            Character.toUpperCase(firstLetter).toString() + model.substring(1)
        }
    } else {
        ""
    }
}

fun getDeviceTimezoneId(): String {
    return TimeZone.getDefault().id
}

fun roundDecimalUpToTwo(float: Double?): String {
    if (float == null) return "0.00"
    return String.format(Locale.getDefault(), "%.2f", float)
}

fun EditText.getFloat(): Float {
    return try {
        NumberFormat.getInstance().parse(this.text.toString())!!.toFloat()
    } catch (e: ParseException) {
        0.0f
    }
}

fun ImageView.loadImage(url: String, isCircle: Boolean = false) {
    this.load(url) {
        crossfade(true)
        placeholder(R.drawable.placeholder)
        if (isCircle) transformations(CircleCropTransformation())
    }
}

fun View.goneView() {
    visibility = GONE
}

fun View.hideView() {
    visibility = INVISIBLE
}

fun View.showView() {
    visibility = VISIBLE
}

fun TextView.disableButton() {
    this.isEnabled = false
    Handler(Looper.getMainLooper()).postDelayed({
        this.isEnabled = true
    }, 200)
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    showToast("$text Copied")
}

fun Context.openPDFDocument(pdfFilePath: String?) {
    if (pdfFilePath != null && !TextUtils.isEmpty(pdfFilePath)) {
        val uri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            File(pdfFilePath)
        )
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(uri, "application/pdf")
        pdfIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        pdfIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val viewerIntent = Intent.createChooser(pdfIntent, "View PDF")
        startActivity(viewerIntent)
    } else {
        showToast(getString(R.string.pdf_not_found))
    }
}

infix fun View.onClick(click: () -> Unit) {
    setOnClickListener { click() }
}

fun ArrayList<*>.indexExists(index: Int): Boolean {
    return index >= 0 && index < this.size
}

fun String.splitIn2(): Pair<String, String> {
    var firstName = ""
    var lastName = ""
    val arr = this.split(" ").toTypedArray()
    if (arr.size < 2) {
        firstName = arr[0]
        lastName = ""
    } else if (arr.size == 2) {
        firstName = arr[0]
        lastName = arr[1]
    } else if (arr.size > 2) {
        val stringBuilder = StringBuilder()
        arr.forEachIndexed { index, s ->
            if (index != 0) stringBuilder.append("$s ")
        }
        firstName = arr[0]
        lastName = stringBuilder.toString()
    }
    return Pair(firstName, lastName)
}

fun encodeToBase64(image: Bitmap, compressFormat: CompressFormat?, quality: Int): String? {
    val byteArrayOS = ByteArrayOutputStream()
    if (compressFormat != null) {
        image.compress(compressFormat, quality, byteArrayOS)
    }
    return Base64_Local.encodeToString(byteArrayOS.toByteArray(), Base64_Local.DEFAULT)
}

fun decodeBase64(input: String?): Bitmap? {
    val decodedBytes: ByteArray = Base64_Local.decode(input, 0)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun ImageView.setTint(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

fun getRandomUUID(): String {
    return UUID.randomUUID().toString()
}

fun getTotalDeviceSize(): Long {
    val totalSize: Long = File(App.getAppContext().filesDir.absoluteFile.toString()).totalSpace
    return totalSize / (1024 * 1024)
}

fun getAvailableDeviceSize(): Long {
    val availableSize: Long = File(App.getAppContext().filesDir.absoluteFile.toString()).freeSpace
    return availableSize / (1024 * 1024)
}

fun Context.getTimeGap(timestamp: Long): String? {
    var convTime: String? = null
    val suffix = getString(R.string.ago)
    val calendar = Calendar.getInstance(Locale.getDefault())
    calendar.timeInMillis = timestamp
    val pasTime1 = calendar.time as Date
    val nowTime = Date()

    val diff: Long = nowTime.time - pasTime1.time
    val diffSeconds = diff / 1000
    val diffMinutes = diff / (60 * 1000) % 60
    val diffHours = diff / (60 * 60 * 1000) % 24
    val diffDays = diff / (24 * 60 * 60 * 1000)

    if (diffDays > 0) {
        convTime = if (diffDays == 1L) {
            diffDays.toString() + " " + getString(R.string.day) + " " + suffix
        } else {
            diffDays.toString() + " " + getString(R.string.days) + " " + suffix
        }
    } else {
        if (diffHours > 0) {
            convTime = if (diffHours == 1L) {
                diffHours.toString() + " " + getString(R.string.hour) + " " + suffix
            } else {
                diffHours.toString() + " " + getString(R.string.hours) + " " + suffix
            }
        } else {
            if (diffMinutes > 0) {
                convTime = if (diffMinutes == 1L) {
                    diffMinutes.toString() + " " + getString(R.string.minute) + " " + suffix
                } else {
                    diffMinutes.toString() + " " + getString(R.string.minutes) + " " + suffix
                }
            } else {
                if (diffSeconds >= 0) {
                    convTime = if (diffSeconds == 0L || diffSeconds == 1L) {
                        diffSeconds.toString() + " " + getString(R.string.second) + " " + suffix
                    } else {
                        diffSeconds.toString() + " " + getString(R.string.seconds) + " " + suffix
                    }
                }
            }
        }
    }
    return convTime
}

fun Context.getRoomDatabaseSize(dbName: String): Long {
    val dbFolderPath = filesDir.absolutePath.replace("files", "databases")
    val dbFile = File("$dbFolderPath/$dbName")
    // Check if database file exist.
    if (!dbFile.exists()) throw Exception("${dbFile.absolutePath} doesn't exist")
    return dbFile.length()
}

fun getDeviceDateFormatFromFormat(format: String): String {
    var dateFormat = "dd/MMM/yyyy"
    when (format) {
        DD_MM_YYYY -> dateFormat = "dd/MM/yyyy"
        DD_MMM_YYYY -> dateFormat = "dd/MMM/yyyy"
        MM_DD_YYYY -> dateFormat = "MM/dd/yyyy"
        MMM_DD_YYYY -> dateFormat = "MMM/dd/yyyy"
    }
    return dateFormat
}

fun getDeviceTimeFormatFromFormat(format: String): String {
    var timeFormat = "hh:mm aa"
    when (format) {
        HH_MM_12 -> timeFormat = "hh:mm aa"
        HH_MM_SS_12 -> timeFormat = "hh:mm:ss aa"
        HH_MM_24 -> timeFormat = "HH:mm"
        HH_MM_SS_24 -> timeFormat = "HH:mm:ss"
    }
    return timeFormat
}

fun addCommaFormat(number: Double?): String {
    if (number == null) return "0.00"
    return String.format(Locale.getDefault(), "%,.2f", number)
}

fun ImageView.setTint(context: Context, colorRes: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

fun getUniqueNo(): Int {
    return (Date().time / 1000L % Int.MAX_VALUE).toInt()
}

fun checkIfFloatHasDecimal(f: Float): Boolean {
    return f - f.toInt() != 0f
}

// Check if value is between 0 to 99.9 and not contains 2 decimal and not contain 2 values after decimal and also invalidate 0. values.
// ^(?:\b([0-9]|[1-9][0-9])\b|\b([0-9]|[1-9][0-9])\b\d*(\.?[0-9]*))$
fun checkIfValidVAT(f: Float): Boolean {
    val regex = "^(?:\\b([0-9]|[1-9][0-9])\\b|\\b([0-9]|[1-9][0-9])\\b\\d*(\\.?[0-9]*))\$".toRegex()
    return regex.matches(f.toString())
}

fun roundDecimalUpToOne(float: Float?): String {
    if (float == null) return "0.0"
    return String.format(Locale.getDefault(), "%.1f", float)
}

fun roundDecimalUpToOneFloat(float: Float?): Float {
    if (float == null) return 0.0f
    val value = float.toString().split(".")
    val trimDecimal = value[1].substring(0, 1)
    return "${value[0]}.$trimDecimal".toFloat()
}

fun checkIfValidFloat(value: String): Pair<Boolean, Float> {
    return try {
        val conversion = value.toFloat()
        Pair(true, conversion)
    } catch (e: Exception) {
        Pair(false, 0.0f)
    }
}

fun safeCall(function: () -> Unit) {
    try {
        function.invoke()
    } catch (e: Exception) {
        e.localizedMessage?.let { Log.e("safeCall", it) }
    }
}

fun View.hideKeyboard() {
    val inputMethodManager = App.getAppContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

private var txtChangeJob: Job? = null

fun EditText.setOnTextUpdate(delay: Long = 500L, lifecycle: Lifecycle, function: (String) -> Unit) {
    fun txtChangedFlow(): Flow<String> = channelFlow {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(ch: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(ch: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(str: Editable?) {
                trySend(str.toString())
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }
    txtChangeJob?.cancel()
    txtChangeJob = txtChangedFlow()
        .debounce(delay) /** 0.5 sec */
        .onEach { function(it) }
        .launchIn(lifecycle.coroutineScope)
}

fun doInBackground(function: () -> Unit) {
    Thread {
        function.invoke()
    }.start()
}
