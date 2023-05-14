package com.agp.mymoment.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel(){





    fun getPosts() {
        viewModelScope.launch {  }
    }

}