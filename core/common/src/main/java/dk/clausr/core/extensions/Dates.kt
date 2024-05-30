package com.trifork.familystart.core.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDate.Companion.now(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
fun LocalDate.toEpochMilliseconds(): Long = this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
fun LocalDate.Companion.fromEpochMilliseconds(millis: Long): LocalDate =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date

fun LocalDate.isBeforeNow(): Boolean = this.toJavaLocalDate().isBefore(java.time.LocalDate.now())

fun Instant?.toMediumFormat(): String? = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    .format(
        this?.toJavaInstant()?.atZone(TimeZone.currentSystemDefault().toJavaZoneId())
            ?.toLocalDateTime(),
    )
