package com.android.presentation.adapter.viewholder

import com.android.base.BaseViewHolder
import com.caner.common.ext.use
import com.android.data.model.Image
import com.android.data.model.remote.BackdropItem
import com.android.presentation.databinding.RecyclerviewMovieImageBinding

class MovieImagesViewHolder constructor(
    private val imageBinding: RecyclerviewMovieImageBinding
) : BaseViewHolder<BackdropItem, RecyclerviewMovieImageBinding>(imageBinding) {

    override fun bind() {
        getRowItem()?.apply {
            imageBinding.use {
                image = Image(getRowItem()?.file_path)
            }
        }
    }
}