package dk.clausr.koncert.ui.parallax

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dk.clausr.koncert.R
import dk.clausr.koncert.utils.sensors.SensorData

// https://twitter.com/philipcdavis/status/1499436970556071949?ref_src=twsrc%5Etfw%7Ctwcamp%5Etweetembed%7Ctwterm%5E1529777425785073665%7Ctwgr%5E7240d45859f5dd3e4645a0bda6788a2bc6ce4ff0%7Ctwcon%5Es3_&ref_url=https%3A%2F%2Fcdn.embedly.com%2Fwidgets%2Fmedia.html%3Ftype%3Dtext2Fhtmlkey%3Da19fcc184b9711e1b4764040d3dc5c07schema%3Dtwitterurl%3Dhttps3A%2F%2Ftwitter.com%2F_sur4j_%2Fstatus%2F1529777425785073665image%3Dhttps3A%2F%2Fi.embed.ly%2F1%2Fimage3Furl3Dhttps253A252F252Fabs.twimg.com252Ferrors252Flogo46x38.png26key3Da19fcc184b9711e1b4764040d3dc5c07
// https://proandroiddev.com/parallax-effect-with-sensormanager-using-jetpack-compose-a735a2f5811b
@Composable
fun ParallaxComp(
    modifier: Modifier = Modifier,
    depthMultiplier: Int = 20,
    data: SensorData?
) {
    val roll by remember(data?.roll) { derivedStateOf { (data?.roll ?: 0f) * depthMultiplier } }
    val pitch by remember(data?.pitch) { derivedStateOf { (data?.pitch ?: 0f) * depthMultiplier } }

    Box(modifier = modifier) {
        // Glow Shadow
        // Has quicker offset change and in opposite direction to the image card
        Image(
            painter = painterResource(id = R.drawable.rocket_cover),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = -(roll * 1.5).dp.roundToPx(),
                        y = (pitch * 2).dp.roundToPx()
                    )
                }
                .width(250.dp)
                .height(250.dp)
                .align(Alignment.Center)
                .blur(radius = 40.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )

//        // Edge (used to give depth to card when tilted)
//        // SLightly slower offset change, than card
//        Box(
//            modifier = Modifier
//                .offset {
//                    IntOffset(
//                        x = (roll * 0.9).dp.roundToPx(),
//                        y = -(pitch * 0.9).dp.roundToPx()
//                    )
//                }
//                .width(300.dp)
//                .height(300.dp)
//                .align(Alignment.Center)
//                .background(
//                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
//                    shape = RoundedCornerShape(16.dp)
//                )
//        )

        // Image card
        // Image inside has a parallax shift in the opposite direction
        Image(
            painter = painterResource(id = R.drawable.rocket_cover),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = roll.dp.roundToPx(),
                        y = -pitch.dp.roundToPx()
                    )
                }
                .width(300.dp)
                .height(300.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(16.dp)),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            alignment = BiasAlignment(
                horizontalBias = (roll * 0.005).toFloat(),
                verticalBias = 0f
            )
        )
    }
}

@Preview
@Composable
fun ParallaxCompPreview() {
    ParallaxComp(data = SensorData(0f, 0f))
}