package com.android.moviedb.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.base.BaseFragment
import com.caner.common.ext.observeWith
import com.caner.common.Constants
import com.caner.common.utils.DataStoreUtils
import com.android.moviedb.databinding.FragmentProfileBinding
import com.android.presentation.vm.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    @Inject
    lateinit var storeUtils: DataStoreUtils

    private val viewModel: ProfileViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        initObservers()

        lifecycleScope.launch {
            storeUtils.getData(Constants.ACCESS_TOKEN_DATA_STORE).collect { sessionId ->
                if (sessionId == null) {
                    viewModel.getNewTokenWithDataStore()
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.newSessionLiveData.observeWith(viewLifecycleOwner) {
            lifecycleScope.launch {
                storeUtils.saveData(Constants.ACCESS_TOKEN_DATA_STORE, it)
            }
        }
    }

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate
}