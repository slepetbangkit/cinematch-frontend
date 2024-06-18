package com.slepetbangkit.cinematch.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.slepetbangkit.cinematch.data.remote.response.HomeResponse
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HomeViewModel(
//    private val sessionRepository: SessionRepository
): ViewModel() {
    private val _response = MutableLiveData<HomeResponse>()
    val response: LiveData<HomeResponse> = _response

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
        private fun putTempHomeData(): HomeResponse {
            val gson = Gson()
            val homeData = gson.fromJson(
                """
                    {
                        "error": false,
                        "data": {
                            "recommended": [
                                {
                                    "tmdb_id": 1022789,
                                    "title": "Inside Out 2",
                                    "poster_url": "https://image.tmdb.org/t/p/original//vpnVM9B6NMmQpWeZvzLvDESb2QY.jpg"
                                },
                                {
                                    "tmdb_id": 653346,
                                    "title": "Kingdom of the Planet of the Apes",
                                    "poster_url": "https://image.tmdb.org/t/p/original//gKkl37BQuKTanygYQG1pyYgLVgf.jpg"
                                },
                                {
                                    "tmdb_id": 1001311,
                                    "title": "Under Paris",
                                    "poster_url": "https://image.tmdb.org/t/p/original//qZPLK5ktRKa3CL4sKRZtj8UlPYc.jpg"
                                },
                                {
                                    "tmdb_id": 573435,
                                    "title": "Bad Boys: Ride or Die",
                                    "poster_url": "https://image.tmdb.org/t/p/original//nP6RliHjxsz4irTKsxe8FRhKZYl.jpg"
                                },
                                {
                                    "tmdb_id": 150540,
                                    "title": "Inside Out",
                                    "poster_url": "https://image.tmdb.org/t/p/original//2H1TmgdfNtsKlU9jKdeNyYL5y8T.jpg"
                                },
                                {
                                    "tmdb_id": 823464,
                                    "title": "Godzilla x Kong: The New Empire",
                                    "poster_url": "https://image.tmdb.org/t/p/original//z1p34vh7dEOnLDmyCrlUVLuoDzd.jpg"
                                },
                                {
                                    "tmdb_id": 929590,
                                    "title": "Civil War",
                                    "poster_url": "https://image.tmdb.org/t/p/original//sh7Rg8Er3tFcN9BpKIPOMvALgZd.jpg"
                                },
                                {
                                    "tmdb_id": 955555,
                                    "title": "The Roundup: No Way Out",
                                    "poster_url": "https://image.tmdb.org/t/p/original//lW6IHrtaATxDKYVYoQGU5sh0OVm.jpg"
                                },
                                {
                                    "tmdb_id": 614933,
                                    "title": "Atlas",
                                    "poster_url": "https://image.tmdb.org/t/p/original//bcM2Tl5HlsvPBnL8DKP9Ie6vU4r.jpg"
                                },
                                {
                                    "tmdb_id": 626412,
                                    "title": "Alienoid: Return to the Future",
                                    "poster_url": "https://image.tmdb.org/t/p/original//4RClncz0GTKPZzSAcAalHCw0h3g.jpg"
                                },
                                {
                                    "tmdb_id": 719221,
                                    "title": "Tarot",
                                    "poster_url": "https://image.tmdb.org/t/p/original//gAEUXC37vl1SnM7PXsHTF23I2vq.jpg"
                                },
                                {
                                    "tmdb_id": 1086747,
                                    "title": "The Watchers",
                                    "poster_url": "https://image.tmdb.org/t/p/original//vZVEUPychdvZLrTNwWErr9xZFmu.jpg"
                                },
                                {
                                    "tmdb_id": 1011985,
                                    "title": "Kung Fu Panda 4",
                                    "poster_url": "https://image.tmdb.org/t/p/original//kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg"
                                },
                                {
                                    "tmdb_id": 746036,
                                    "title": "The Fall Guy",
                                    "poster_url": "https://image.tmdb.org/t/p/original//aBkqu7EddWK7qmY4grL4I6edx2h.jpg"
                                },
                                {
                                    "tmdb_id": 1115623,
                                    "title": "The Last Kumite",
                                    "poster_url": "https://image.tmdb.org/t/p/original//zDkaJgsPoSqa2cMe2hW2HAfyWwO.jpg"
                                },
                                {
                                    "tmdb_id": 38700,
                                    "title": "Bad Boys for Life",
                                    "poster_url": "https://image.tmdb.org/t/p/original//y95lQLnuNKdPAzw9F9Ab8kJ80c3.jpg"
                                },
                                {
                                    "tmdb_id": 1136318,
                                    "title": "Battle Over Britain",
                                    "poster_url": "https://image.tmdb.org/t/p/original//8htJ7keZTwa08aC9OKyiqaq1cNJ.jpg"
                                },
                                {
                                    "tmdb_id": 786892,
                                    "title": "Furiosa: A Mad Max Saga",
                                    "poster_url": "https://image.tmdb.org/t/p/original//iADOJ8Zymht2JPMoy3R7xceZprc.jpg"
                                },
                                {
                                    "tmdb_id": 974635,
                                    "title": "Hit Man",
                                    "poster_url": "https://image.tmdb.org/t/p/original//1126gjlBf4hTm9Sgf0ox3LGVEBt.jpg"
                                },
                                {
                                    "tmdb_id": 1219685,
                                    "title": "Un père idéal",
                                    "poster_url": "https://image.tmdb.org/t/p/original//4xJd3uwtL1vCuZgEfEc8JXI9Uyx.jpg"
                                }
                            ],
                            "friends": [
                                {
                                    "tmdb_id": 9799,
                                    "title": "The Fast and the Furious",
                                    "poster_url": "https://image.tmdb.org/t/p/original//gqY0ITBgT7A82poL9jv851qdnIb.jpg",
                                    "username": "sulthanftr",
                                    "user_profile": null
                                },
                                {
                                    "tmdb_id": 9615,
                                    "title": "The Fast and the Furious: Tokyo Drift",
                                    "poster_url": "https://image.tmdb.org/t/p/original//46xqGOwHbh2TH2avWSw3SMXph4E.jpg",
                                    "username": "sulthanftr",
                                    "user_profile": null
                                }
                            ],
                            "verdict": [
                                {
                                    "review_id": "0c7039af-a133-4ae3-a40a-ef44dbda2555",
                                    "tmdb_id": 244,
                                    "title": "King Kong",
                                    "poster_url": "https://image.tmdb.org/t/p/original//lHlnxKL5GbgRibyRFI7n1Ey850i.jpg",
                                    "username": "sulthanftr",
                                    "user_profile": null
                                }
                            ],
                            "top_rated": [
                                {
                                    "tmdb_id": 278,
                                    "title": "The Shawshank Redemption",
                                    "poster_url": "https://image.tmdb.org/t/p/original//9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg"
                                },
                                {
                                    "tmdb_id": 238,
                                    "title": "The Godfather",
                                    "poster_url": "https://image.tmdb.org/t/p/original//3bhkrj58Vtu7enYsRolD1fZdja1.jpg"
                                },
                                {
                                    "tmdb_id": 240,
                                    "title": "The Godfather Part II",
                                    "poster_url": "https://image.tmdb.org/t/p/original//hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg"
                                },
                                {
                                    "tmdb_id": 424,
                                    "title": "Schindler's List",
                                    "poster_url": "https://image.tmdb.org/t/p/original//sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg"
                                },
                                {
                                    "tmdb_id": 389,
                                    "title": "12 Angry Men",
                                    "poster_url": "https://image.tmdb.org/t/p/original//ow3wq89wM8qd5X7hWKxiRfsFf9C.jpg"
                                },
                                {
                                    "tmdb_id": 19404,
                                    "title": "Dilwale Dulhania Le Jayenge",
                                    "poster_url": "https://image.tmdb.org/t/p/original//lfRkUr7DYdHldAqi3PwdQGBRBPM.jpg"
                                },
                                {
                                    "tmdb_id": 129,
                                    "title": "Spirited Away",
                                    "poster_url": "https://image.tmdb.org/t/p/original//39wmItIWsg5sZMyRUHLkWBcuVCM.jpg"
                                },
                                {
                                    "tmdb_id": 155,
                                    "title": "The Dark Knight",
                                    "poster_url": "https://image.tmdb.org/t/p/original//qJ2tW6WMUDux911r6m7haRef0WH.jpg"
                                },
                                {
                                    "tmdb_id": 496243,
                                    "title": "Parasite",
                                    "poster_url": "https://image.tmdb.org/t/p/original//7IiTTgloJzvGI1TAYymCfbfl3vT.jpg"
                                },
                                {
                                    "tmdb_id": 497,
                                    "title": "The Green Mile",
                                    "poster_url": "https://image.tmdb.org/t/p/original//8VG8fDNiy50H4FedGwdSVUPoaJe.jpg"
                                },
                                {
                                    "tmdb_id": 372058,
                                    "title": "Your Name.",
                                    "poster_url": "https://image.tmdb.org/t/p/original//q719jXXEzOoYaps6babgKnONONX.jpg"
                                },
                                {
                                    "tmdb_id": 680,
                                    "title": "Pulp Fiction",
                                    "poster_url": "https://image.tmdb.org/t/p/original//d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg"
                                },
                                {
                                    "tmdb_id": 122,
                                    "title": "The Lord of the Rings: The Return of the King",
                                    "poster_url": "https://image.tmdb.org/t/p/original//rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg"
                                },
                                {
                                    "tmdb_id": 13,
                                    "title": "Forrest Gump",
                                    "poster_url": "https://image.tmdb.org/t/p/original//arw2vcBveWOVZr6pxd9XTd1TdQa.jpg"
                                },
                                {
                                    "tmdb_id": 769,
                                    "title": "GoodFellas",
                                    "poster_url": "https://image.tmdb.org/t/p/original//aKuFiU82s5ISJpGZp7YkIr3kCUd.jpg"
                                },
                                {
                                    "tmdb_id": 429,
                                    "title": "The Good, the Bad and the Ugly",
                                    "poster_url": "https://image.tmdb.org/t/p/original//bX2xnavhMYjWDoZp1VM6VnU1xwe.jpg"
                                },
                                {
                                    "tmdb_id": 12477,
                                    "title": "Grave of the Fireflies",
                                    "poster_url": "https://image.tmdb.org/t/p/original//k9tv1rXZbOhH7eiCk378x61kNQ1.jpg"
                                },
                                {
                                    "tmdb_id": 346,
                                    "title": "Seven Samurai",
                                    "poster_url": "https://image.tmdb.org/t/p/original//8OKmBV5BUFzmozIC3pPWKHy17kx.jpg"
                                },
                                {
                                    "tmdb_id": 11216,
                                    "title": "Cinema Paradiso",
                                    "poster_url": "https://image.tmdb.org/t/p/original//9JhfVOveaY00o8njQu2Xrp4YWud.jpg"
                                },
                                {
                                    "tmdb_id": 637,
                                    "title": "Life Is Beautiful",
                                    "poster_url": "https://image.tmdb.org/t/p/original//74hLDKjD5aGYOotO6esUVaeISa2.jpg"
                                }
                            ]
                        }
                    }
                """.trimIndent(),
                HomeResponse::class.java
            )
            return homeData
        }
    }
}