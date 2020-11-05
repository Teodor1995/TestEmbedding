package com.fedor_zavalnyj.testembedding

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import kotlinx.android.synthetic.main.second_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun FragmentActivity.addFragment(
    fragment: Fragment,
    addToBackStack: Boolean = true
) {
    supportFragmentManager.inTransaction {
        add(R.id.root_container, fragment)
        if (addToBackStack)
            addToBackStack(fragment::class.simpleName)
    }
}


inline fun FragmentManager.inTransactionFr(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun Fragment.addView(
    fragment: Fragment,
    addToBackStack: Boolean = true,
) {
    childFragmentManager.inTransactionFr {
        replace(R.id.viewContainer, fragment, fragment::class.simpleName)
        if (addToBackStack)
            addToBackStack(fragment::class.simpleName)
    }


}