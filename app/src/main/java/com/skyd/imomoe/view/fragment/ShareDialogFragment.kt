package com.skyd.imomoe.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skyd.imomoe.R
import com.skyd.imomoe.databinding.FragmentShareDialogBinding
import com.skyd.imomoe.util.Share.SHARE_LINK
import com.skyd.imomoe.util.Share.SHARE_QQ
import com.skyd.imomoe.util.Share.SHARE_WECHAT
import com.skyd.imomoe.util.Share.SHARE_WEIBO
import com.skyd.imomoe.util.Share.share

open class ShareDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentShareDialogBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var shareContent: String
    private lateinit var attachedActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let { act ->
            attachedActivity = act
            mBinding.tvToQq.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_QQ)
                dismiss()
            }
            mBinding.tvToWechat.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_WECHAT)
                dismiss()
            }
            mBinding.tvToWeibo.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_WEIBO)
                dismiss()
            }
            mBinding.tvCopyLink.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_LINK)
                dismiss()
            }
            mBinding.tvCancelShare.setOnClickListener {
                dismiss()
            }
        }
    }

    fun setShareContent(shareContent: String): BottomSheetDialogFragment {
        this.shareContent = shareContent
        return this
    }
}