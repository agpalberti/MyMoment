package com.agp.mymoment.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.agp.mymoment.model.DBM
import com.agp.mymoment.model.classes.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel(){


    var date:String by savedStateHandle.saveable{ mutableStateOf("")}

    suspend fun getPFP(uid: String): String {
        return DBM.getPFP(uid)
    }
    fun getUserData(userUID: String = DBM.getLoggedUserUid()): Flow<User> = flow {
        DBM.getUserData(userUID).collect{
            emit(it)
        }
    }
}