package com.agp.mymoment.ui.search

import android.util.Log
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agp.mymoment.model.DBM
import com.agp.mymoment.model.classes.Post
import com.agp.mymoment.model.classes.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class SearchScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel(){


    var item: Int = 0
    var openImageView: Boolean by savedStateHandle.saveable{ mutableStateOf(false) }
    var searchText: String by savedStateHandle.saveable { mutableStateOf("") }
    var posts:List<Post> by savedStateHandle.saveable { mutableStateOf(mutableListOf()) }
    var users:Map<String,User> by savedStateHandle.saveable { mutableStateOf(mutableMapOf()) }
    var isPopLaunched = false


    fun resetImageView(){
            openImageView = false
            isPopLaunched = false
    }

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

    fun amIFollowing(uid: String): Flow<Boolean?> = flow {
        val following = DBM.isFollowing(DBM.getLoggedUserUid(), uid)
        emit(following)
    }

    fun isHeFollowing(uid: String): Flow<Boolean?> = flow {

        val following = DBM.isFollowing(uid, DBM.getLoggedUserUid())
        emit(following)

    }

    fun getPfp(uid: String):Flow<String?> = flow{
        val pfp:String = DBM.getPFP(uid)
        Log.i("GetPFP", pfp)
        emit(pfp)
    }

    fun getUser(uid: String):Flow<User> = flow{
        DBM.getUserData(uid).collect{
            emit(it)
        }
    }
}
