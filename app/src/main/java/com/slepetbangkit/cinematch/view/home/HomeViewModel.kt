package com.slepetbangkit.cinematch.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomeViewModel(
//    private val sessionRepository: SessionRepository
): ViewModel() {
    private val _response = MutableLiveData<HomeData>()
    val response: LiveData<HomeData> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        viewModelScope.launch {
            getHomeData()
        }
    }

    private suspend fun getHomeData() {
        try {
            _isLoading.value = true
            _response.value = putTempHomeData()
        } catch (e: HttpException) {
//            if (e.code() == 401) {
//                withContext(Dispatchers.IO) {
//                    sessionRepository.refresh()
//                }
//                getHomeData()
//            } else {
//                _error.value = "An error occurred"
//            }
            _error.value = "An error occured"
        } finally {
            _isLoading.value = false
        }
    }

    companion object {
        data class HomeData(
            @SerializedName("recommended")
            val recommended: List<RecommendedItem>,
            val friends: List<FriendsItem>,
            val verdict: List<VerdictItem>,
            val toprated: List<TopRatedItem>
        )

        data class RecommendedItem(
            @SerializedName("poster_url")
            val posterUrl: String
        )

        data class FriendsItem(
            @SerializedName("poster_url")
            val posterUrl: String,

            @SerializedName("profile_picture")
            val profilePicture: String
        )

        data class VerdictItem(
            @SerializedName("poster_url")
            val posterUrl: String,

            @SerializedName("title")
            val title: String,

            @SerializedName("release_date")
            val releaseDate: String,

            @SerializedName("verdict")
            val verdict: String,

            @SerializedName("profile_picture")
            val profilePicture: String,

            @SerializedName("username")
            val username: String
        )

        data class TopRatedItem(
            @SerializedName("poster_url")
            val posterUrl: String
        )

        private fun putTempHomeData(): HomeData {
            val gson = Gson()
            val homeData = gson.fromJson(
                "{" +
                        "\"recommended\":[" +
                            "{" +
                                "\"poster_url\": \"abc\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"def\"" +
                            "}" +
                        "]" +
                        ",\"friends\":[" +
                            "{" +
                                "\"poster_url\":\"ghi\"," +
                                "\"profile_picture\":\"jkl\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"mno\"," +
                                "\"profile_picture\":\"pqr\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"stu\"," +
                                "\"profile_picture\":\"vwx\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"yza\"," +
                                "\"profile_picture\":\"bcd\"" +
                            "}" +
                        "]" +
                        ",\"verdict\":[" +
                            "{" +
                                "\"poster_url\":\"efg\"," +
                                "\"title\":\"hij\"," +
                                "\"release_date\":\"klm\"," +
                                "\"verdict\":\"Lorem ipsum dolor sit amet\"," +
                                "\"profile_picture\":\"qrs\"," +
                                "\"username\":\"tuv\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"wxy\"," +
                                "\"title\":\"zab\"," +
                                "\"release_date\":\"cde\"," +
                                "\"verdict\":\"Lorem ipsum dolor sit amet\"," +
                                "\"profile_picture\":\"ijk\"," +
                                "\"username\":\"lmn\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"opq\"," +
                                "\"title\":\"rst\"," +
                                "\"release_date\":\"uvw\"," +
                                "\"verdict\":\"Lorem ipsum dolor sit amet\"," +
                                "\"profile_picture\":\"abc\"," +
                                "\"username\":\"def\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"ghi\"," +
                                "\"title\":\"jkl\"," +
                                "\"release_date\":\"mno\"," +
                                "\"verdict\":\"Lorem ipsum dolor sit amet\"," +
                                "\"profile_picture\":\"stu\"," +
                                "\"username\":\"vwx\"" +
                            "}" +
                        "]" +
                        ",\"toprated\":[" +
                            "{" +
                                "\"poster_url\":\"yza\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"bcd\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"efg\"" +
                            "}," +
                            "{" +
                                "\"poster_url\":\"hij\"" +
                            "}" +
                        "]" +
                    "}",
                HomeData::class.java
            )
            return homeData
        }
    }
}