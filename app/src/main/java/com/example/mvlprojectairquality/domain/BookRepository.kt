package com.example.mvlprojectairquality.domain

import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.model.BookRequest
import com.example.mvlprojectairquality.model.BookResponse

interface BookRepository {
    suspend fun saveA(book: Book)

    suspend fun saveB(book: Book)

    suspend fun fetchBookList() : List<Book>

    suspend fun book(
        request: BookRequest
    ): BookResponse

    suspend fun getBookings(): List<BookResponse>

    suspend fun editNickName(label : String, nickName : String)

    suspend fun getA()  :Book

    suspend fun getB() : Book
}