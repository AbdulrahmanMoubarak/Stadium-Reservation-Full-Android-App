package com.training.fakeactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.training.ui.common.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FakeTestActivity : AppCompatActivity() {
    val CONTENT_VIEW_ID = 10101010
    lateinit var fragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var frame = FrameLayout(this)
        frame.setId(CONTENT_VIEW_ID)
        setContentView(
            frame, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        fragment = LoginFragment()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(CONTENT_VIEW_ID, fragment).commit()
    }

    fun setTestFragment(fragment : Fragment){
        this.fragment = fragment
    }
}