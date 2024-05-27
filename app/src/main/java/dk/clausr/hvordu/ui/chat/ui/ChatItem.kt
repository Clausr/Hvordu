package dk.clausr.hvordu.ui.chat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dk.clausr.hvordu.ui.chat.types.ChatItemData
import dk.clausr.hvordu.ui.chat.types.ChatItemDirection
import dk.clausr.hvordu.ui.chat.types.ChatMessage
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ChatItem(
    item: ChatItemData,
    timestamp: OffsetDateTime,
    modifier: Modifier = Modifier,
) {
    if (item is ChatItemData.Empty) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = when (item.direction) {
            is ChatItemDirection.Received -> Arrangement.Start
            ChatItemDirection.Sent -> Arrangement.End
        },
    ) {
//        if (item.direction is ChatItemDirection.Received) {
//            // Profile image
//            Surface(
//                modifier = Modifier
//                    .padding(end = 8.dp)
//                    .size(20.dp),
//                shape = CircleShape,
//                color = MaterialTheme.colorScheme.primary
//            ) {
//                Text(item.direction.senderName.first().toString())
//            }
//        }

        Column(
            horizontalAlignment = when (item.direction) {
                is ChatItemDirection.Received -> Alignment.Start
                ChatItemDirection.Sent -> Alignment.End
            }
        ) {
            when (item) {
                ChatItemData.Empty -> {}
                is ChatItemData.Message -> {
                    ChatMessage(item)
                }
            }

            Text(
                style = MaterialTheme.typography.labelSmall,
                text = timestamp.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

}