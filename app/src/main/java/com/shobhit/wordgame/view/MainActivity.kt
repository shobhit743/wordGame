package com.shobhit.wordgame.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.shobhit.wordgame.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  private lateinit var mToolbar:Toolbar
  private  var alertDialog:AlertDialog? = null
  private val TAG = "MAINFRAGMENT"
  private lateinit var txtSubmit :TextView
  private lateinit var ivReplay:ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    mToolbar = findViewById(R.id.mToolbar)
    setSupportActionBar(mToolbar)
    initViews()
    if(savedInstanceState==null) {
    loadMainFragment()
    }
  }


  override fun supportFragmentInjector(): AndroidInjector<Fragment> {
    return dispatchingAndroidInjector
  }

  private fun initViews(){
    ivReplay = findViewById(R.id.iv_replay)
    ivReplay.setOnClickListener {
      ivReplay.visibility = View.GONE
      txtSubmit.visibility = View.VISIBLE
      loadMainFragment()
    }
    txtSubmit= findViewById(R.id.txt_submit)
    txtSubmit.setOnClickListener {
      showGameEndDialog(getMainFragment()?.getGameScore().toString())
    }
  }

  private fun getMainFragment() : MainFragment? {
    val mainFragment = supportFragmentManager.findFragmentByTag(TAG)
    if(mainFragment!=null){
      return mainFragment as MainFragment
    }
    return null
  }


  private fun showGameEndDialog(gameScore:String?){
    val alertDialogBuilder = AlertDialog.Builder(this)
    val view = layoutInflater.inflate(R.layout.play_again_layout,null)
    val btnPlayAgain :Button = view.findViewById(R.id.btn_play_again)
    val btnCompareAnswer : Button = view.findViewById(R.id.compare_answer)
    btnCompareAnswer.setOnClickListener {
      alertDialog?.dismiss()
      getMainFragment()?.setUpShowAdapterAnswer()
      txtSubmit.visibility = View.GONE
      ivReplay.visibility = View.VISIBLE
    }
    val txtGameScore:TextView = view.findViewById(R.id.txt_game_score)
    txtGameScore.text = if (gameScore!=null) gameScore else "0"
    btnPlayAgain.setOnClickListener {
      alertDialog?.dismiss()
      loadMainFragment()
       }
    alertDialogBuilder.setView(view)
    alertDialog = alertDialogBuilder.create()
    alertDialog?.show()
  }

  private fun loadMainFragment(){
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(R.animator.fragment_slide_left_enter,
            R.animator.fragment_slide_left_exit,
            R.animator.fragment_slide_right_enter,
            R.animator.fragment_slide_right_exit)
        .replace(R.id.frame_container, MainFragment(),TAG).commit()
  }

  override fun onDestroy() {
    super.onDestroy()
    if(alertDialog!=null&&alertDialog!!.isShowing) {
      alertDialog!!.dismiss()
    }
  }






}
