package com.example.soul

import android.net.Uri


interface FirstAppFireBaseInterface {
    abstract suspend fun sendComplain(data:FirstAppFireStoreDataClass,image: Uri):Result<String>
}