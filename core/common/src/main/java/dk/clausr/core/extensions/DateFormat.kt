package dk.clausr.core.extensions

import android.text.format.DateUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

fun Instant.getCustomRelativeTimeSpanString(locale: Locale = Locale.getDefault()): String {
    val currentTime = Clock.System.now()

    val timeDifference = currentTime - this

    val targetTime = this.toLocalDateTime(TimeZone.currentSystemDefault())

    return when {
        timeDifference <= 1.hours -> {
            DateUtils.getRelativeTimeSpanString(
                this.toEpochMilliseconds(),
                currentTime.toEpochMilliseconds(),
                DateUtils.SECOND_IN_MILLIS,
            ).toString()
        }

        timeDifference <= 1.days -> {
            targetTime
                .toJavaLocalDateTime()
                .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale))
        }

        timeDifference <= 7.days -> {
            targetTime.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        }

        else -> {
//            val flags = if (targetTime.year == currentTime.toLocalDateTime(TimeZone.currentSystemDefault()).year) {
//                DateUtils.FORMAT_SHOW_DATE
//            } else {
//                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
//            }
            // TODO Add year if in another year
            targetTime
                .toJavaLocalDateTime()
                .format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale),
                )
        }
    }
}
