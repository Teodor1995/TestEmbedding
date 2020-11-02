package com.fedor_zavalnyj.testembedding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun MainActivity.addFragment(
    fragment: Fragment,
    addToBackStack: Boolean = true
) {
    supportFragmentManager.inTransaction {
        add(R.id.root_container, fragment)
        if (addToBackStack)
            addToBackStack(fragment::class.simpleName)
    }
}


fun MainActivity.replaceFragment(
    fragment: Fragment,
    addToBackStack: Boolean = true
) {
    supportFragmentManager.inTransaction {
        replace(R.id.root_container, fragment)
        if (addToBackStack)
            addToBackStack(fragment::class.simpleName)
    }
}