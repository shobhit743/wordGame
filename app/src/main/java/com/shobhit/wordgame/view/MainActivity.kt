package com.shobhit.wordgame.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.shobhit.wordgame.R
import com.shobhit.wordgame.utils.CommonUtils
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import com.shobhit.wordgame.utils.CommonUtils.revealShow
import android.graphics.Color
import android.graphics.drawable.ColorDrawable


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,MainFragment.EnableFloatingActionListener {


  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  private lateinit var mToolbar: Toolbar
  private var dialog: Dialog? = null
  private val TAG = "MAINFRAGMENT"
  private lateinit var ivReplay: ImageView
  private lateinit var fabSubmit: FloatingActionButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    mToolbar = findViewById(R.id.mToolbar)
    setSupportActionBar(mToolbar)
    initViews()
    if (savedInstanceState == null) {
      loadMainFragment()
    }
  }

  override fun onEnableFloatingAction() {
   showFloatingButton()
  }

  @SuppressLint("RestrictedApi")
  private fun showFloatingButton(){
    fabSubmit.visibility = View.VISIBLE
  }

  override fun onDestroy() {
    super.onDestroy()
    if (dialog != null && dialog!!.isShowing) {
      dialog!!.dismiss()
    }
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> {
    return dispatchingAndroidInjector
  }

  private fun initViews() {
    fabSubmit = findViewById(R.id.fab)
    hideFloatingButton()
    fabSubmit.setOnClickListener {
      showGameEndDialog(getMainFragment()?.getGameScore()?.toString())
    }
    ivReplay = findViewById(R.id.iv_replay)
    ivReplay.setOnClickListener {
      ivReplay.visibility = View.GONE
      hideFloatingButton()
      loadMainFragment()
    }
  }

  private fun getMainFragment(): MainFragment? {
    val mainFragment = supportFragmentManager.findFragmentByTag(TAG)
    if (mainFragment != null) {
      return mainFragment as MainFragment
    }
    return null
  }

  @SuppressLint("RestrictedApi")
  private fun hideFloatingButton(){
    fabSubmit.visibility = View.GONE
  }


  private fun showGameEndDialog(gameScore: String?) {
    val view = View.inflate(this, R.layout.play_again_layout, null)
    val dialog = Dialog(this, R.style.MyAlertDialogStyle)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(view);
    val btnPlayAgain: Button = view.findViewById(R.id.btn_play_again)
    val btnCompareAnswer: Button = view.findViewById(R.id.compare_answer)
    btnCompareAnswer.setOnClickListener {
      CommonUtils.revealShow(view, false, dialog, fabSubmit)
      getMainFragment()?.setUpShowAdapterAnswer()
      hideFloatingButton()
     ivReplay.visibility = View.VISIBLE
    }

    val txtGameScore: TextView = view.findViewById(R.id.txt_game_score)
    txtGameScore.text = gameScore ?: "0"
    btnPlayAgain.setOnClickListener {
      CommonUtils.revealShow(view, false, dialog, fabSubmit)
      loadMainFragment()
    }
    dialog.setOnShowListener { revealShow(view, true, dialog, fabSubmit) }
    dialog.setOnKeyListener { dialogInterface, i, keyEvent -> false }
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show();
  }

  private fun loadMainFragment() {
      hideFloatingButton()
      supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.animator.fragment_slide_left_enter,
            R.animator.fragment_slide_left_exit,
            R.animator.fragment_slide_right_enter,
            R.animator.fragment_slide_right_exit)
        .replace(R.id.frame_container, MainFragment(), TAG).commit()
  }

}
