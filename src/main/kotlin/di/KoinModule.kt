package com.daccvo.di

import com.daccvo.Services.TemplateCv1
import com.daccvo.Services.TemplateCv2
import com.daccvo.Services.TemplateCv3
import com.daccvo.Services.TemplateCv4
import com.daccvo.repository.CvRepository
import com.daccvo.repository.CvRepositoryImpl
import com.daccvo.repository.GeneratePdfRepository
import com.daccvo.repository.GeneratePdfRepositoryImpl
import org.koin.dsl.module

val KoinModule = module {
    single<GeneratePdfRepository> { GeneratePdfRepositoryImpl() }
    single<CvRepository> { CvRepositoryImpl(get(),get(),get(),get() ) }
    single { TemplateCv1() }
    single { TemplateCv2() }
    single { TemplateCv3() }
    single { TemplateCv4() }

}