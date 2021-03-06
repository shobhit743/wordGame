package com.shobhit.wordgame.view


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

import com.shobhit.wordgame.R
import com.shobhit.wordgame.model.IdName
import com.shobhit.wordgame.utils.CommonUtils
import com.shobhit.wordgame.viewModel.DaggerViewModelFactory
import com.shobhit.wordgame.viewModel.MainViewModel
import dagger.android.support.AndroidSupportInjection
import java.text.BreakIterator
import java.util.HashMap
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(), FlexAdapter.OnBlankClickListener {


  @Inject
  lateinit var viewModelFactory: DaggerViewModelFactory

  private lateinit var flexAdapter: FlexAdapter
  private lateinit var progressBar: ProgressBar
  private var mList: MutableList<IdName> = mutableListOf()
  private lateinit var mainViewModel: MainViewModel
  private lateinit var flexRecyclerView: RecyclerView
  private lateinit var txtGameTitle: TextView
  private var wordsMap = HashMap<Int, Array<String>>()
  private var selectedWordsListMap = HashMap<Int, String>()
  private var sentenceMap = HashMap<Int, String>()
  private var mListSelected: MutableList<IdName> = mutableListOf()
  var alertDialog: AlertDialog? = null
  private lateinit var enableFloatingActionListener: EnableFloatingActionListener


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

  override fun onBlackClick(idName: IdName, position: Int) {
    showDialogBlank(position)
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if(context is EnableFloatingActionListener){
      enableFloatingActionListener = context
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    if (alertDialog != null && alertDialog!!.isShowing) {
      alertDialog?.dismiss()
    }
  }



  private fun initViews(view: View) {
    progressBar = view.findViewById(R.id.progressbar)
    progressBar.visibility = View.VISIBLE
    txtGameTitle = view.findViewById(R.id.txt_game_title)
    flexRecyclerView = view.findViewById(R.id.flex_recycler)
    val flexLayoutManager = FlexboxLayoutManager(activity)
    flexLayoutManager.flexWrap = FlexWrap.WRAP
    flexLayoutManager.flexDirection = FlexDirection.ROW
    flexLayoutManager.alignItems = AlignItems.FLEX_START
    flexRecyclerView.layoutManager = flexLayoutManager
  }

  private fun setUpViewModel(savedInstanceState: Bundle?) {
    mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    mList = mainViewModel.getmList()
    flexAdapter = FlexAdapter(activity!!, mList, this, false)
    flexRecyclerView.adapter = flexAdapter
    mainViewModel.gameTitle.observe(this, Observer<String> {
      txtGameTitle.text = it
    })
    mainViewModel.gameMainText.observe(this, Observer<String> {
      if (savedInstanceState == null) {
        setUpData(it)
      } else {
        flexAdapter.notifyDataSetChanged()
      }
      progressBar.visibility = View.GONE
    })
  }


  private fun showDialogBlank(position: Int) {
    val alertDialogBuilder = AlertDialog.Builder(activity!!)
    val view = LayoutInflater.from(activity).inflate(R.layout.selected_word_layout, null)
    alertDialogBuilder.setView(view)
    val selectedWordAdapter = SelectedWordDialogAdapter(activity!!, mListSelected, object : SelectedWordDialogAdapter.SelectedWordDialogClick {
      override fun onSelectedWordDialog(idName: IdName) {
        updateAdapterDataOnSelect(idName, position)
      }
    })
    val recyclerView: RecyclerView = view.findViewById(R.id.selected_word_recycler)
    recyclerView.adapter = selectedWordAdapter
    recyclerView.layoutManager = LinearLayoutManager(activity)
    selectedWordAdapter.notifyDataSetChanged()
    alertDialog = alertDialogBuilder.create()
    alertDialog?.show()
  }


  private fun updateAdapterDataOnSelect(idName: IdName, position: Int) {
    alertDialog?.dismiss()
    idName.isBlank = true
    idName.isValueInserted = true
    idName.correctName = mList[position].correctName
    idName.id = mList[position].id
    if (selectedWordsListMap.containsKey(mList[position].id) && idName.name == mList[position].correctName) {
      mainViewModel.gameScore++
      idName.correctAnswer = flexAdapter.ANSWER_IS_CORRECT
    } else {
      if (mainViewModel.gameScore != 0) {
        mainViewModel.gameScore--
      }
      idName.correctAnswer = flexAdapter.ANSWER_IS_WRONG
    }
    mList.removeAt(position)
    mList.add(position, idName)
    flexAdapter.notifyItemChanged(position)
  }

  private fun setUpData(questionData: String?) {
    if(questionData != null) {
      enableFloatingActionListener.onEnableFloatingAction()
      CommonUtils.getListOfSentences(BreakIterator.getSentenceInstance(), questionData).forEachIndexed { index, s ->
        if (CommonUtils.getListOfWords(s).isNotEmpty()) {
          wordsMap.put(index, CommonUtils.getListOfWords(s))
          sentenceMap.put(index, s)
        }
      }
      val wordStrMap = wordsMap
      for (index in wordsMap.keys) {
        if (wordsMap[index]!!.isNotEmpty()) {
          val randomNumber = CommonUtils.getRandomDoubleBetweenRange(1, wordStrMap[index]!!.size - 1)
          if (wordsMap.isNotEmpty() && wordsMap.containsKey(index)) {
            selectedWordsListMap.put(index, wordsMap[index]!![randomNumber])
          }
        }
      }
      randomiseSelectedWords()
      setUpQuestionTextUI()
    } else {
      txtGameTitle.text = ""
      Toast.makeText(activity,"Error Fetching Data",Toast.LENGTH_LONG).show()
    }
  }

  private fun setUpQuestionTextUI() {
    var sentenceOffset = 0
    for (index in sentenceMap.keys) {
      var offset = 0
      val sentence = sentenceMap[index]
      if (sentence!!.isNotEmpty() && sentence.length > 1) {
        val word = selectedWordsListMap[index]
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

  private fun randomiseSelectedWords() {
    for ((key, value) in selectedWordsListMap) {
      val idName = IdName()
      idName.id = key
      idName.name = value
      idName.correctName = value
      mListSelected.add(idName)
    }
    CommonUtils.randomize(mListSelected)
  }

  fun getGameScore(): Int {
    return mainViewModel.gameScore
  }

  fun setUpShowAdapterAnswer() {
    flexRecyclerView.adapter = FlexAdapter(activity!!, mList, this, true)
  }

  interface EnableFloatingActionListener{
    fun onEnableFloatingAction()
  }
}