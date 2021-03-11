package com.skyd.imomoe.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skyd.imomoe.App
import com.skyd.imomoe.R
import com.skyd.imomoe.bean.AnimeCoverBean
import com.skyd.imomoe.bean.ClassifyBean
import com.skyd.imomoe.config.Api
import com.skyd.imomoe.util.JsoupUtil
import com.skyd.imomoe.util.ParseHtmlUtil.parseLpic
import com.skyd.imomoe.util.ParseHtmlUtil.parseTers
import com.skyd.imomoe.util.Util.showToastOnThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.select.Elements
import java.lang.Exception
import java.util.*


class ClassifyViewModel : ViewModel() {
    var isRequesting = false
    var classifyTabList: MutableList<ClassifyBean> = ArrayList()        //上方分类数据
    var mldClassifyTabList: MutableLiveData<Boolean> = MutableLiveData()
    var classifyList: MutableList<AnimeCoverBean> = ArrayList()       //下方tv数据
    var mldClassifyList: MutableLiveData<Boolean> = MutableLiveData()

    fun getClassifyTabData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val document = JsoupUtil.getDocument(Api.MAIN_URL + "/a/")
                val areaElements: Elements = document.getElementsByClass("area")
                classifyTabList.clear()
                for (i in areaElements.indices) {
                    val areaChildren: Elements = areaElements[i].children()
                    for (j in areaChildren.indices) {
                        when (areaChildren[j].className()) {
                            "ters" -> {
                                classifyTabList.addAll(parseTers(areaChildren[j]))
                            }
                        }
                    }
                }
                mldClassifyTabList.postValue(true)
            } catch (e: Exception) {
                classifyTabList.clear()
                mldClassifyTabList.postValue(false)
                e.printStackTrace()
                (App.context.getString(R.string.get_data_failed) + "\n" + e.message).showToastOnThread()
            }
        }
    }

    fun getClassifyData(partUrl: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (isRequesting) return@launch
                isRequesting = true
                val url = Api.MAIN_URL + partUrl
                val document = JsoupUtil.getDocument(url)
                val areaElements: Elements = document.getElementsByClass("area")
                classifyList.clear()
                for (i in areaElements.indices) {
                    val areaChildren: Elements = areaElements[i].children()
                    for (j in areaChildren.indices) {
                        when (areaChildren[j].className()) {
                            "fire l" -> {
                                classifyList.addAll(parseLpic(areaChildren[j], url))
                            }
                        }
                    }
                }
                mldClassifyList.postValue(true)
            } catch (e: Exception) {
                classifyList.clear()
                mldClassifyList.postValue(false)
                e.printStackTrace()
                (App.context.getString(R.string.get_data_failed) + "\n" + e.message).showToastOnThread()
            }
        }
    }

    companion object {
        const val TAG = "ClassifyViewModel"
    }
}