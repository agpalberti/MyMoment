package com.agp.mymoment.ui.search

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.agp.mymoment.model.DBM
import com.agp.mymoment.model.classes.Post
import com.agp.mymoment.model.classes.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class SearchScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel(){


    var searchText: String by savedStateHandle.saveable { mutableStateOf("") }
    var posts:List<Post> by savedStateHandle.saveable { mutableStateOf(mutableListOf()) }
    var users:List<User> by savedStateHandle.saveable { mutableStateOf(mutableListOf()) }


    fun updateScreen(){
        updatePosts(); updateUsers()
    }
    fun updatePosts(){
        viewModelScope.launch {
            DBM.getAllExplorePosts().collect(){
                Log.i("Buscar", "$it")
               posts = it
            } }
    }

    fun updateUsers(){
        viewModelScope.launch {
            DBM.getAllUsers().collect(){
                users = it
            }
        }
    }
}
