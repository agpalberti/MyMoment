package com.agp.mymoment.ui.image

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ImageViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    ViewModel() {

    var enableOptions by savedStateHandle.saveable{mutableStateOf(false)}
    var user by savedStateHandle.saveable{mutableStateOf(User("","","", emptyList(), emptyList(),
        emptyList()
    ))}
    var post by savedStateHandle.saveable { mutableStateOf( Post("", emptyList(),"",)) }

    var isLiked by savedStateHandle.saveable{ mutableStateOf(false) }


    fun getCurrentUserUid() = DBM.getLoggedUserUid()

    fun like(userUid: String) {
        viewModelScope.launch {
            DBM.likePost(post.date?:"", userUid)
            updatePostInfo(post.download_link?:"",userUid)
        }
    }

    fun dislike(userUid: String) {
        viewModelScope.launch {
            DBM.dislikePost(post.date?:"",userUid)
            updatePostInfo(post.download_link?:"",userUid)
        }
    }

    fun updatePostInfo(url: String, userUId: String){

        viewModelScope.launch{
            user = DBM.getUserData(userUid = userUId).first()?:user
            post = user.posts?.find { it.download_link == url }?:post
            isLiked = post.likes?.contains(
                DBM.getLoggedUserUid()
            )?:false
        }
    }

    fun deletePost() {

        viewModelScope.launch {
            DBM.deletePost(post.date?:"")
        }

    }
}