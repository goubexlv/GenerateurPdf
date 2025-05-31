package com.daccvo.di

import com.daccvo.repository.CvRepository
import com.daccvo.repository.CvRepositoryImpl
import com.daccvo.repository.GeneratePdfRepository
import com.daccvo.repository.GeneratePdfRepositoryImpl
import org.koin.dsl.module

val KoinModule = module {
    single<GeneratePdfRepository> { GeneratePdfRepositoryImpl() }
    single<CvRepository> { CvRepositoryImpl() }
}