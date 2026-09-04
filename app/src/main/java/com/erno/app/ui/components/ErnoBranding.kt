package com.erno.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.erno.app.R

/**
 * Official ERNO Logo component.
 * IMPORTANT: This uses the official image asset 'erno_logo' from res/drawable.
 * Ensure the image file is placed in app/src/main/res/drawable/erno_logo.png
 */
@Composable
fun ErnoLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.erno_logo),
        contentDescription = "ERNO Logo",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

// These are now marked as Deprecated and should be removed once all screens are updated.
@Deprecated(
    message = "Use ErnoLogo() which contains the official wordmark",
    replaceWith = ReplaceWith("ErnoLogo(modifier)")
)
@Composable
fun ErnoWordmark(modifier: Modifier = Modifier) {
    ErnoLogo(modifier = modifier)
}

@Deprecated(
    message = "Use ErnoLogo() which contains the official vertical layout",
    replaceWith = ReplaceWith("ErnoLogo(modifier)")
)
@Composable
fun ErnoBrandingVertical(modifier: Modifier = Modifier) {
    ErnoLogo(modifier = modifier)
}

@Deprecated(
    message = "Use ErnoLogo() which contains the official horizontal branding",
    replaceWith = ReplaceWith("ErnoLogo(modifier)")
)
@Composable
fun ErnoBrandingHorizontal(modifier: Modifier = Modifier) {
    ErnoLogo(modifier = modifier)
}
