package com.freshdigitable.upnpsample

import android.util.Log
import net.mm2d.upnp.util.asIterable
import org.w3c.dom.Element

data class PvrRes(
    var channelNum: Int = 0,
    var channelList: String = "",
    var channelPropertyList: String = ""
) {
    companion object {
        fun create(res: Map<String, String>): PvrRes = res.toList().filter { it.first == "Result" }
            .map { it.second.toXmlElements().childNodes.asIterable() }
            .flatten()
            .map { it as Element }
            .fold(PvrRes()) { r, elem ->
                r.apply {
                    when (elem.tagName) {
                        "channelNum" -> channelNum = elem.textContent.toInt()
                        "channelList" -> channelList = elem.textContent
                        "channelPropertyList" -> channelPropertyList = elem.textContent
                        else -> Log.d(TAG, "unknown tag> ${elem.tagName}:${elem.textContent}")
                    }
                }
            }

        private val TAG = PvrRes::class.java.simpleName
    }
}