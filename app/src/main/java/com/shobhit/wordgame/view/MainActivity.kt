package com.shobhit.wordgame.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
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
  private lateinit var alertDialog:AlertDialog
  private val TAG = "MAINFRAGMENT"

  override fun supportFragmentInjector(): AndroidInjector<Fragment> {
    return dispatchingAndroidInjector
  }
  private lateinit var txtSubmit :TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    mToolbar = findViewById(R.id.mToolbar)
    setSupportActionBar(mToolbar)
    txtSubmit= findViewById(R.id.txt_submit)
    txtSubmit.setOnClickListener {
      val mainFragment = supportFragmentManager.findFragmentByTag(TAG)
      if(mainFragment!=null){
        showGameEndDialog((mainFragment as MainFragment).getGameScore().toString())
      }
    }
    if(savedInstanceState==null) {
      supportFragmentManager.beginTransaction().replace(R.id.frame_container, MainFragment(),TAG).commit()
    }

  }

  private fun showGameEndDialog(gameScore:String){
    val alertDialogBuilder = AlertDialog.Builder(this)
    val view = layoutInflater.inflate(R.layout.play_again_layout,null)
    val btnPlayAgain :Button = view.findViewById(R.id.btn_play_again)
    var txtGameScore:TextView = view.findViewById(R.id.txt_game_score)
    txtGameScore.text = gameScore
    btnPlayAgain.setOnClickListener {
      alertDialog.dismiss()
      supportFragmentManager.beginTransaction().replace(R.id.frame_container, MainFragment(),TAG).commit()
    }
    alertDialogBuilder.setView(view)
    alertDialog = alertDialogBuilder.create()
    alertDialog.show()
  }






}
