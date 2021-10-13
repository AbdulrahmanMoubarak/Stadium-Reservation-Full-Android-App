package com.training.fakeactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.training.R
import com.training.ui.common.ChangePasswordFragment
import com.training.ui.common.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FakeChangePasswordFragmentTestActivity : AppCompatActivity() {
    val CONTENT_VIEW_ID = 10101010
    lateinit var fragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_change_password_fragment_test)
        var frame = FrameLayout(this)
        frame.setId(CONTENT_VIEW_ID)
        setContentView(
            frame, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        fragment = ChangePasswordFragment()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(CONTENT_VIEW_ID, fragment).commit()
    }

    fun setTestFragment(fragment : Fragment){
        this.fragment = fragment
    }
}