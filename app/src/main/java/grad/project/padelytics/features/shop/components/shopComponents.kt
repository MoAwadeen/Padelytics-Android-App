package grad.project.padelytics.features.shop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import grad.project.padelytics.R
import grad.project.padelytics.features.shop.viewModel.ShopViewModel
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.lexendFontFamily
import kotlinx.coroutines.launch

enum class SheetType { CATEGORY, BRAND, SORT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopHeaders(
    viewModel: ShopViewModel,
    selectedCategory: String,
    selectedBrand: String,
    selectedSorting: String
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var currentSheetType by remember { mutableStateOf(SheetType.CATEGORY) }

    val categories = listOf("All", "Rackets", "Shoes", "Bags", "Clothing", "Balls", "Accessories")
    val brands = listOf("All", "Babolat", "Bullpadel", "Wilson", "Adidas", "NOX", "Qshop", "Asics", "Star vie", "Puma", "Head", "S SIUX", "Camewin", "Tecnifibre")
    val sortOptions = listOf("Price: High to Low", "Price: Low to High", "Avg. Customer Review", "Featured", "Best Sellers")

    var tempSelection by remember { mutableStateOf("All") }

    if (showSheet) {
        val currentList = when (currentSheetType) {
            SheetType.CATEGORY -> categories
            SheetType.BRAND -> brands
            SheetType.SORT -> sortOptions
        }

        val currentValue = when (currentSheetType) {
            SheetType.CATEGORY -> selectedCategory
            SheetType.BRAND -> selectedBrand
            SheetType.SORT -> selectedSorting
        }

        tempSelection = currentValue

        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(width = 32.dp, height = 4.dp)
                        .background(
                            color = BlueDark,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        ) {
            BottomSheet(
                items = currentList,
                selectedItem = tempSelection,
                onItemSelected = { tempSelection = it },
                onSave = {
                    scope.launch {
                        sheetState.hide()
                        showSheet = false

                        when (currentSheetType) {
                            SheetType.CATEGORY -> {
                                viewModel.updateFilters(
                                    category = tempSelection,
                                    brand = selectedBrand,
                                    sort_by = selectedSorting
                                )
                            }
                            SheetType.BRAND -> {
                                viewModel.updateFilters(
                                    category = selectedCategory,
                                    brand = tempSelection,
                                    sort_by = selectedSorting
                                )
                            }
                            SheetType.SORT -> {
                                viewModel.updateFilters(
                                    category = selectedCategory,
                                    brand = selectedBrand,
                                    sort_by = tempSelection
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Category",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = BlueDark
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    currentSheetType = SheetType.CATEGORY
                    showSheet = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                modifier = Modifier
                    .width(106.dp)
                    .height(41.dp),
                shape = RoundedCornerShape(22.dp)
            ) {
                Text(
                    text = selectedCategory,
                    fontSize = 12.sp,
                    color = BlueDark,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(2.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Brand",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = BlueDark
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    currentSheetType = SheetType.BRAND
                    showSheet = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                modifier = Modifier
                    .width(106.dp)
                    .height(41.dp),
                shape = RoundedCornerShape(22.dp)
            ) {
                Text(
                    text = selectedBrand,
                    fontSize = 12.sp,
                    color = BlueDark,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(2.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sort by",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = BlueDark
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    currentSheetType = SheetType.SORT
                    showSheet = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenLight),
                modifier = Modifier
                    .width(106.dp)
                    .height(41.dp),
                shape = RoundedCornerShape(22.dp)
            ) {
                Text(
                    text = selectedSorting,
                    fontSize = 12.sp,
                    color = BlueDark,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopHeadersPreview(){
    ShopHeaders(ShopViewModel(), selectedCategory = "All", selectedBrand = "All", selectedSorting = "Relevance")
}

@Composable
fun BottomSheet(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    onSave: () -> Unit
) {
    var tempSelected by remember { mutableStateOf(selectedItem) }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
            .fillMaxWidth()
            .height(400.dp)
    ) {
        items(items) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { tempSelected = item
                        onItemSelected(tempSelected)
                        onSave()}
                    .padding(vertical = 10.dp)
            ) {
                RadioButton(
                    selected = tempSelected == item,
                    onClick = { tempSelected = item
                        onItemSelected(tempSelected)
                        onSave()},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = GreenLight,
                        unselectedColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = item,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = lexendFontFamily
                    ),
                    color = BlueDark
                )
            }
        }
        /*
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        onItemSelected(tempSelected)
                        onSave()
                    },
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenLight)
                ) {
                    Text(
                        text = "Save",
                        fontSize = 20.sp,
                        color = BlueDark,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
         */
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetPreview(){
    BottomSheet(items = listOf("ALL", "Shoes", "Balls"), selectedItem = "All", onItemSelected = {}, onSave = {})
}

@Composable
fun ShopProduct(
    viewModel: ShopViewModel,
    productId: String,
    productImage: String,
    productName: String,
    productRating: String,
    productNumRating: String,
    productPrice: String,
    productDelivery: String,
    productUrl: String
) {
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        viewModel.checkIfFavorite(productId) { result ->
            isFavorite = result
        }
    }

    val ratingValue = productRating.toFloatOrNull() ?: 0f
    val starImages = List(5) { index ->
        val fullStars = ratingValue.toInt()
        val hasHalfStar = ratingValue - fullStars >= 0.5f

        when {
            index < fullStars -> R.drawable.rating
            index == fullStars && hasHalfStar -> R.drawable.rating
            else -> R.drawable.no_rating
        }
    }

    Box(modifier = Modifier.fillMaxWidth().height(110.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color.White.copy(alpha = 0.005f)
                    )
                    .drawBehind {
                        drawRect(
                            color = Color.White.copy(alpha = 0.05f),
                            topLeft = Offset(2.dp.toPx(), size.height - 8.dp.toPx()),
                            size = Size(size.width - 6.dp.toPx(), 8.dp.toPx())
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(74.dp)
                        .height(74.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    model = productImage,
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = productName,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Blue
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = productRating,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = BlueDark
                        )
                    )

                    starImages.forEach { starImage ->
                        Image(
                            painter = painterResource(starImage),
                            contentDescription = "Star Rating",
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    val ratingNum = if (productNumRating == "") "" else "($productNumRating)"

                    Text(
                        text = ratingNum,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = BlueDark
                        )
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = productDelivery,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = BlueDark
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = productPrice,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = BlueDark
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (isFavorite) {
                                viewModel.removeFavoriteProduct(productId) { success ->
                                    if (success) {
                                        isFavorite = false
                                    }
                                }
                            } else {
                                viewModel.saveFavoriteProduct(
                                    productId,
                                    productName,
                                    productPrice,
                                    productImage,
                                    productUrl,
                                    productRating,
                                    productNumRating,
                                ) { success ->
                                    if (success) {
                                        isFavorite = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                if (isFavorite) R.drawable.fav_selected else R.drawable.fav_unselected
                            ),
                            contentDescription = "Favorite",
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopProductPreview(){
    ShopProduct(ShopViewModel(),
        productId = "1",
        productImage = "",
        productName = "productName",
        productRating = "4.5",
        productNumRating = "200",
        productDelivery = "Free Delivery",
        productPrice = "1000 EGP",
        productUrl = ""
    )
}
