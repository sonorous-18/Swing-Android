package com.uiel.swap.ui.subscribe

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.uiel.swap.design_system.SwapColor
import com.uiel.swap.design_system.SwapTypography
import com.uiel.swap.design_system.button.SwapColoredButton
import com.uiel.swap.viewmodel.subscribe.SubscribeDetailViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun SubscribeDetailScreen(
    subscribeId: Long,
    viewModel: SubscribeDetailViewModel = viewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedColor = remember { mutableStateOf(uiState.detail?.colors?.firstOrNull()) }

    LaunchedEffect(Unit) {
        viewModel.getSubscribeDetail(subscribeId)
        Log.d("TEST1", uiState.detail?.colors?.firstOrNull().toString())
    }

    LaunchedEffect(Unit) {
        delay(100)
        selectedColor.value = uiState.detail?.colors?.firstOrNull()
    }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(SwapColor.gray0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = SwapColor.gray800
                    )
                }
                Text(
                    text = uiState.detail?.title ?: "Ace",
                    style = SwapTypography.TitleLarge,
                    color = SwapColor.gray800
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .background(SwapColor.gray200)
            ) {
                val thumbnailUrl = selectedColor.value?.thumbnail ?: ""
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = thumbnailUrl,
                    contentDescription = null,
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .shadow(4.dp, CircleShape)
                        .background(SwapColor.gray0, RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        val colorName = ProductColor.fromName(selectedColor.value?.color ?: "")?.colorName ?: "Midnight Black"
                        Text(
                            text = colorName,
                            style = SwapTypography.BodyLarge,
                            color = SwapColor.gray800
                        )
                        Spacer(modifier = Modifier.width(50.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            uiState.detail?.colors?.forEach { color ->
                                val productColor = ProductColor.fromName(color.color)
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            productColor?.colorValue ?: Color.Gray,
                                            CircleShape
                                        )
                                        .border(
                                            2.dp,
                                            if (selectedColor.value?.id == color.id) SwapColor.main500 else Color.Transparent,
                                            CircleShape
                                        )
                                        .clickable {
                                            selectedColor.value = color
                                        }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(text = "어반바이크", style = SwapTypography.BodyLarge, color = SwapColor.gray600)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = uiState.detail?.title ?: "Ace", style = SwapTypography.HeadlineSmall, color = SwapColor.gray800)
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "월 구독료", style = SwapTypography.BodyMedium, color = SwapColor.gray600)
                    Text(text = "${uiState.detail?.price ?: 0}원", style = SwapTypography.TitleMedium, color = SwapColor.gray800)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "배송 정보", style = SwapTypography.BodyMedium, color = SwapColor.gray600)
                    Text(text = "11/06 이후 5영업일 이내 순차 배송", style = SwapTypography.BodyMedium, color = SwapColor.gray800)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "비대면 영업배송",
                        style = SwapTypography.TitleSmall,
                        color = SwapColor.main500
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.reviews.forEach { review ->
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .border(1.dp, SwapColor.gray300, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = review.username,
                                style = SwapTypography.BodyLarge,
                                color = SwapColor.gray800
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = review.comment,
                                style = SwapTypography.BodyMedium,
                                color = SwapColor.gray600
                            )
                        }
                    }
                }
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = uiState.detail?.detailLink,
                    contentDescription = null,
                    imageLoader = ImageLoader(LocalContext.current),
                    contentScale = ContentScale.FillWidth,
                )
            }
        }

        SwapColoredButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            text = "이용하기",
            onClick = {},
            small = false,
        )
    }
}
