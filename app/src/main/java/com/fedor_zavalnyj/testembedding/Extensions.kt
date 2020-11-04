package com.fedor_zavalnyj.testembedding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import io.flutter.embedding.android.FlutterFragment

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

fun FlutterFragment.addView(
    fragment: Fragment,
    addToBackStack: Boolean = true
) {
    childFragmentManager.inTransactionFr {
        add(R.id.viewContainer, fragment)
        if (addToBackStack)
            addToBackStack(fragment::class.simpleName)
    }
}