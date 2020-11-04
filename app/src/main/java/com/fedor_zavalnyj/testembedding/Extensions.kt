package com.fedor_zavalnyj.testembedding

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

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

fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    addToBackStack: Boolean = true
) {
    (this as AppCompatActivity).supportFragmentManager.inTransaction {
        replace(R.id.root_container, fragment)
        if (addToBackStack)
            addToBackStack(fragment::class.simpleName)
    }
}