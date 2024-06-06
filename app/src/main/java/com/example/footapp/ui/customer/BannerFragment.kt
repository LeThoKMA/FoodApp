package com.example.footapp.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.footapp.R

class BannerFragment() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.banner_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val img = view.findViewById<ImageView>(R.id.img_banner)
        arguments?.takeIf { it.containsKey("banner") }.apply {
            Glide.with(view.rootView.context)
                .load(this?.getString("banner"))
//                .centerCrop().override(466, 592)
                .into(img)
        }
    }
}
