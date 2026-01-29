package com.andone.memorip.presentation.screen.user.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.screen.user.component.UserProfileSectionDimen.PROFILE_IMAGE_ICON_PADDING
import com.andone.memorip.presentation.screen.user.component.UserProfileSectionDimen.PROFILE_IMAGE_ICON_SIZE
import com.andone.memorip.presentation.screen.user.component.UserProfileSectionDimen.PROFILE_IMAGE_SIZE
import com.andone.memorip.presentation.screen.user.model.UserUiModel
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object UserProfileSectionDimen {
    val PROFILE_IMAGE_SIZE = 80.dp

    val PROFILE_IMAGE_ICON_SIZE = 28.dp

    val PROFILE_IMAGE_ICON_PADDING = 6.dp
}

@Composable
fun UserProfileSection(
    user: UserUiModel,
    onEditClick: () -> Unit,
    onProfileImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = MemoripPadding.AppHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(PROFILE_IMAGE_SIZE)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onProfileImageClick
                )
        ) {
            Surface(
                modifier = Modifier.matchParentSize(),
                shape = CircleShape,
                border = BorderStroke(
                    width = MemoripLineWidth.Small,
                    color = MemoripTheme.colors.primary
                )
            ) {
                val imageUrl = user.profileImgUrl
                val pngUrl = imageUrl.replace("svg", "png")

                if (imageUrl.isNotBlank()) {
                    MemoripImage(
                        imageUrl = pngUrl,
                        contentDescription = stringResource(R.string.login_user_profile_image),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(PROFILE_IMAGE_ICON_SIZE),
                shape = CircleShape,
                color = MemoripTheme.colors.primary,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_camera_alt),
                    contentDescription = null,
                    modifier = Modifier.padding(all = PROFILE_IMAGE_ICON_PADDING)
                )
            }
        }

        Spacer(modifier = Modifier.width(width = MemoripSpace.SpaceXXXLarge))

        Column(verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MemoripTheme.typography.headlineBold24,
                )

                Spacer(modifier = Modifier.weight(weight = 1f))

                IconButton(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_edit),
                        contentDescription = null,
                    )
                }
            }

            user.email?.let {
                Text(
                    text = it,
                    style = MemoripTheme.typography.bodyBold14,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserProfileSectionPreview() {
    MemoripTheme {
        UserProfileSection(
            user = DummyData.dummyUser,
            onEditClick = {},
            onProfileImageClick = {}
        )
    }
}
