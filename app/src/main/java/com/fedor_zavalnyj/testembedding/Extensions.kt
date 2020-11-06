package com.fedor_zavalnyj.testembedding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**fragment*/
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


/**view*/
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