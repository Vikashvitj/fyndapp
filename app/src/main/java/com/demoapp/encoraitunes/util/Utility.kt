package com.demoapp.encoraitunes.util

import androidx.collection.ArrayMap
import androidx.lifecycle.ViewModel
import com.demoapp.encoraitunes.util.viewmodel.ViewModelFactory

object Utility {

    /**
     * Used to create ViewModelFactory so we can pass argument in viewModel constructor
     * @param classType Class<out ViewModel> class of ViewModel
     * @param viewModel ViewModel object of viewModel
     * @return ViewModelFactory
     */
    fun getViewModelFactory(
        classType: Class<out ViewModel>,
        viewModel: ViewModel
    ): ViewModelFactory {
        val arrayMap = ArrayMap<Class<out ViewModel>, ViewModel>()
        arrayMap[classType] = viewModel
        return ViewModelFactory(arrayMap)
    }
}