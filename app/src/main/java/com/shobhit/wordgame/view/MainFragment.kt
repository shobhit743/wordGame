package com.shobhit.wordgame.view


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

import com.shobhit.wordgame.R
import com.shobhit.wordgame.apiCalls.ApiService
import com.shobhit.wordgame.model.IdName
import com.shobhit.wordgame.utils.CommonUtils
import com.shobhit.wordgame.viewModel.DaggerViewModelFactory
import com.shobhit.wordgame.viewModel.MainViewModel
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import java.text.BreakIterator
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(), FlexAdapter.OnBlankClickListener, SelectedWordDialogAdapter.SelectedWordDialogClick {


  override fun onSelectedWordDialog(idName: IdName) {
  }

  override fun onBlackClick(idName: IdName, position: Int) {
    showDialogBlank(position)
  }


  fun showDialogBlank(position: Int) {
    val alertDialogBuilder = AlertDialog.Builder(activity!!)
    alertDialogBuilder.setItems(mainViewModel.selectedWordsListMap.values.toTypedArray(),
        DialogInterface.OnClickListener { dialog, which ->
          val idNameSelectedWord = IdName()
          idNameSelectedWord.isBlank = true
          idNameSelectedWord.isValueInserted = true
          idNameSelectedWord.correctName = mList[position].correctName
          idNameSelectedWord.id = mList[position].id
          idNameSelectedWord.name = mainViewModel.selectedWordsListMap.values.toTypedArray()[which]
          if (mainViewModel.selectedWordsListMap.containsKey(mList[position].id) && mainViewModel.selectedWordsListMap.values.toTypedArray()[which] == mList[position].correctName) {
            mainViewModel.gameScore++
            Toast.makeText(activity, mainViewModel.gameScore.toString(), Toast.LENGTH_LONG).show()
          } else {
            if (mainViewModel.gameScore != 0) {
              mainViewModel.gameScore--
            }
            Toast.makeText(activity, mainViewModel.gameScore.toString(), Toast.LENGTH_LONG).show()
          }
          mList.removeAt(position)
          mList.add(position, idNameSelectedWord)
          flexAdapter.notifyItemChanged(position)
        })
    val alertDialog = alertDialogBuilder.create()
    alertDialog.show()
  }

  @Inject
  lateinit var viewModelFactory: DaggerViewModelFactory

  @Inject
  lateinit var apiService: ApiService
  private lateinit var flexAdapter: FlexAdapter
  private var mList: MutableList<IdName> = mutableListOf()
  private lateinit var mainViewModel: MainViewModel
  private lateinit var flexRecyclerView: RecyclerView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    AndroidSupportInjection.inject(this)
    val view = inflater.inflate(R.layout.fragment_main, container, false)
    initViews(view)
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    setUpViewModel(savedInstanceState)
  }

   private fun setUpViewModel(savedInstanceState: Bundle?) {
    mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
     mList = mainViewModel.getmList()
     flexAdapter = FlexAdapter(activity!!, mList, this)
     flexRecyclerView.adapter = flexAdapter
     mainViewModel.gameMainText.observe(this, Observer<String> {
      if(savedInstanceState==null) {
        setUpData(it)
      } else {
          flexAdapter.notifyDataSetChanged()
      }
    })
  }

  private fun setUpData(questionData: String?) {
    CommonUtils.getListOfSentences(BreakIterator.getSentenceInstance(), questionData).forEachIndexed { index, s ->
      if (CommonUtils.getListOfWords(s).isNotEmpty()) {
        mainViewModel.wordsMap.put(index, CommonUtils.getListOfWords(s))
        mainViewModel.sentenceMap.put(index, s)
      }
    }
    val wordStrMap = mainViewModel.wordsMap
    for (index in wordStrMap.keys) {
      if (wordStrMap[index]!!.isNotEmpty()) {
        val randomNumber = CommonUtils.getRandomDoubleBetweenRange(1, wordStrMap[index]!!.size - 1)
        if (wordStrMap!!.isNotEmpty()) {
          mainViewModel.selectedWordsListMap.put(index, mainViewModel.wordsMap[index]!![randomNumber])
        }
      }
    }
    var sentenceOffset = 0
    for (index in mainViewModel.sentenceMap.keys) {
      var offset = 0
      val sentence = mainViewModel.sentenceMap[index]
      if (sentence!!.isNotEmpty() && sentence.length > 1) {
        val word = mainViewModel.selectedWordsListMap[index]
        offset = sentence.indexOf(word!!)
        if (sentence.isNotEmpty() && offset != -1) {
          val idNameSentence = IdName()
          idNameSentence.id = -1
          idNameSentence.isBlank = false
          idNameSentence.name = sentence.substring(0, offset - 1)
          idNameSentence.correctName = sentence.substring(0, offset - 1)
          val idNameWord = IdName()
          idNameWord.id = index
          idNameWord.isBlank = true
          idNameWord.name = sentence.substring(offset, offset + word.length)
          idNameWord.correctName = sentence.substring(offset, offset + word.length)
          val idNameRemainingText = IdName()
          idNameRemainingText.isBlank = false
          idNameRemainingText.id = -1
          idNameRemainingText.name = sentence.substring(offset + word.length, sentence.length - 1)
          idNameRemainingText.correctName = sentence.substring(offset + word.length, sentence.length - 1)
          mList.add(idNameSentence)
          mList.add(idNameWord)
          mList.add(idNameRemainingText)
        }
      }
      sentenceOffset += sentence.length
    }
    flexAdapter.notifyDataSetChanged()
  }

  private fun initViews(view: View) {
    flexRecyclerView = view.findViewById(R.id.flex_recycler)
    val flexLayoutManager = FlexboxLayoutManager(activity)
    flexLayoutManager.flexWrap = FlexWrap.WRAP
    flexLayoutManager.flexDirection = FlexDirection.ROW
    flexLayoutManager.alignItems = AlignItems.FLEX_START
    flexRecyclerView.layoutManager = flexLayoutManager
  }

   fun getGameScore():Int{
    return mainViewModel.gameScore
  }

}