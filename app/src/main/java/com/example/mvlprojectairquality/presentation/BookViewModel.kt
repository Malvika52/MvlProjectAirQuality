package com.example.mvlprojectairquality.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvlprojectairquality.domain.BookRepository
import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.model.BookRequest
import com.example.mvlprojectairquality.model.BookResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BookViewModel @Inject constructor(private val repo: BookRepository) :
    ViewModel() {

    private val _bookList = MutableStateFlow<List<Book>>(emptyList())
    val bookList = _bookList.asStateFlow()

    private val _aDetails = MutableStateFlow<Book?>(null)
    val aDetails = _aDetails.asStateFlow()

    private val _bDetails = MutableStateFlow<Book?>(null)
    val bDetails = _bDetails.asStateFlow()

    private val _clickCount = MutableStateFlow(0)
    val clickCount = _clickCount.asStateFlow()

    private val _price = MutableStateFlow(0.0)
    val price = _price.asStateFlow()

    fun onBookButtonClicked() {
        _clickCount.value++
    }

    private val _bookingList =
        MutableStateFlow<List<BookResponse>>(emptyList())

    val bookingList = _bookingList.asStateFlow()


    init {
        loadData()
    }

    fun saveA(book: Book) {
        val updatedBook = book.copy(
            nickname = _aDetails.value?.nickname.orEmpty()
        )

        _aDetails.value = updatedBook

        viewModelScope.launch {
            repo.saveA(updatedBook)
        }
    }



    fun saveB(book: Book) {
        val updatedBook = book.copy(
            nickname = _bDetails.value?.nickname.orEmpty()
        )

        _bDetails.value = updatedBook

        viewModelScope.launch {
            repo.saveB(updatedBook)
        }
    }

    fun fetchBookList() {
        viewModelScope.launch {
            _bookList.value = repo.fetchBookList()
            Log.i("Malvika in VM", "bookList = $bookList")
        }
    }

    fun editNickName(label: String, nickName : String, onDone: () -> Unit){

        if (label == "A") {
            _aDetails.update {
                it?.copy(nickname = nickName)
            }
        } else {
            _bDetails.update {
                it?.copy(nickname = nickName)
            }
        }
        viewModelScope.launch {
            repo.editNickName(label, nickName)
            _bookList.update { list ->
                list.map {
                    if (it.label == label) {
                        it.copy(nickname = nickName)
                    } else {
                        it
                    }
                }
            }
            onDone()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _bookList.value = repo.fetchBookList()
            _aDetails.value = repo.getA()
            _bDetails.value = repo.getB()
        }
    }

    fun book(onSuccess: () -> Unit) {

        viewModelScope.launch {

            val response = repo.book(
                BookRequest(
                    a = _aDetails.value!!,
                    b = _bDetails.value!!
                )
            )

            _bookList.value = listOf(
                response.a,
                response.b
            )

            _price.value = response.price

            onSuccess()
        }
    }

    fun fetchBookings() {

        viewModelScope.launch {
            _bookingList.value = repo.getBookings()
            Log.d("BOOKINGS", "Bookings = ${_bookingList.value}")
        }
    }
}

