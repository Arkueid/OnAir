package com.arkueid.onair.ui.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arkueid.onair.domain.entity.Anime
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import javax.inject.Inject

class FollowingViewModel @Inject constructor(
    private val gson: Gson,
    private val mmkv: MMKV,
) : ViewModel() {

    companion object {
        private const val KEY_FOLLOWING_ANIME = "following_anime"
    }

    private val _followingAnimeList = MutableLiveData(emptyList<Anime>())
    val followedAnimeList: LiveData<List<Anime>> = _followingAnimeList

    init {
        val followingAnimeJson = mmkv.decodeString(KEY_FOLLOWING_ANIME)
        if (followingAnimeJson != null) {
            _followingAnimeList.value =
                gson.fromJson(followingAnimeJson, Array<Anime>::class.java).toList()
        }
    }

    fun addFollowedAnime(anime: Anime) {
        _followingAnimeList.value = _followingAnimeList.value?.plus(anime)
        mmkv.encode(KEY_FOLLOWING_ANIME, gson.toJson(_followingAnimeList.value!!))
    }

    fun removeFollowedAnime(anime: Anime) {
        _followingAnimeList.value = _followingAnimeList.value?.minus(anime)
        mmkv.encode(KEY_FOLLOWING_ANIME, gson.toJson(_followingAnimeList.value!!))
    }

    fun isFollowed(anime: Anime): Boolean {
        return _followingAnimeList.value?.contains(anime) ?: false
    }
}
