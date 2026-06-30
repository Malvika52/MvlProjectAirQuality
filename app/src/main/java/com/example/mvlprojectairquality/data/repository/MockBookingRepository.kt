package com.example.mvlprojectairquality.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mvlprojectairquality.domain.BookRepository
import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.model.BookRequest
import com.example.mvlprojectairquality.model.BookResponse
import kotlinx.coroutines.delay

class MockBookingRepository : BookRepository {

    val bookList = mutableListOf<Book>()

    private val bookings = mutableListOf<BookResponse>()

    override suspend fun saveA(book: Book) {
//        if (bookList.isEmpty()) {
//            bookList.add(book)
//        } else {
//            bookList[0] = book
//        }

        val old = getA()

        val updated = old.copy(
            locationName = book.locationName,
            aqi = book.aqi,
            lat = book.lat,
            long = book.long
        )

        if (bookList.isEmpty()) {
            bookList.add(updated)
        } else {
            bookList[0] = updated
        }
    }

    override suspend fun saveB(book: Book) {

        val old = getB()

        val updated = old.copy(
            locationName = book.locationName,
            aqi = book.aqi,
            lat = book.lat,
            long = book.long
        )

        if (bookList.size >= 2) {
            bookList[1] = updated
        } else {
            bookList.add(updated)
        }
    }

    override suspend fun fetchBookList(): List<Book> {
        Log.i("Malvika", "BookList111 = $bookList")
        return bookList
    }

    override suspend fun book(
        request: BookRequest
    ): BookResponse {
        delay(1000)       // Simulate network delay
        val response =  BookResponse(
            a = request.a,
            b = request.b,
            price = 10000.0
        )

        bookings.add(response)
        return response


    }

    override suspend fun getBookings(): List<BookResponse> {
        Log.i("BOOKINGS", "booking = $bookings")
        return bookings
    }

    override suspend fun getA(): Book {
        return bookList.getOrElse(
            0,
            { Book("", 1, "", 55.55, 55.55, "A") })
    }

    override suspend fun getB(): Book {
        return bookList.getOrElse(
            1,
            { Book("", 1, "", 55.55, 55.55, "A") })
    }

    override suspend fun editNickName(label: String, nickName: String) {
        if (label == "A") {
            bookList[0] = bookList[0].copy(nickname = nickName)
        }
        if (label == "B") {
            bookList[1] = bookList[1].copy(nickname = nickName)
        }

        Log.d("BOOK", "Repository = $bookList")
    }


}