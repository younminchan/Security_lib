package com.lotte.dcx.security

import kotlinx.coroutines.CoroutineExceptionHandler

//TODO: internal: 같은 패키지 내부만 접근 가능
internal object Utils {
    /** Coroutine Handler */
    val handler = CoroutineExceptionHandler { _, exception ->
        //SLog.e("coroutineHandler", "Caught $exception with suppressed ${exception.suppressed.contentToString()}")
    }
}