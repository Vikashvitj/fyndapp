package com.demoapp.encoraitunes.data_classes

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull


@Keep
data class TopSongs(
    @SerializedName("feed")
    val feed: Feed
)

@Keep
data class Feed(
    @SerializedName("author")
    val author: Author,
    @SerializedName("entry")
    val entry: List<Entry>,
    @SerializedName("icon")
    val icon: Icon,
    @SerializedName("id")
    val id: Id,
    @SerializedName("link")
    val link: List<Link>,
    @SerializedName("title")
    val title: Title,
    @SerializedName("updated")
    val updated: Updated
)

@Keep
data class Author(
    @SerializedName("name")
    val name: Name,
    @SerializedName("uri")
    val uri: Uri
)

@Parcelize
@Keep
@Entity(tableName = "entry_song", primaryKeys = ["idattributeimId"])
data class Entry @JvmOverloads constructor(
    @SerializedName("category")
    @Embedded(prefix = "category")
    val category: Category? = null,
    @Embedded(prefix = "id")
    @SerializedName("id")
    @NotNull
    val id: Id = Id(Attributes(), ""),
    @Embedded(prefix = "imArtist")
    @SerializedName("im:artist")
    val imArtist: ImArtist? = null,
    @Embedded(prefix = "imCollection")
    @SerializedName("im:collection")
    val imCollection: ImCollection? = null,
    @Embedded(prefix = "imImage")
    @SerializedName("im:image")
    @TypeConverters(DataConverter::class)
    var imImage: ArrayList<ImImage>? = arrayListOf(),
    @Embedded(prefix = "imName")
    @SerializedName("im:name")
    val imName: ImName? = null,
    @Embedded(prefix = "imReleaseDate")
    @SerializedName("im:releaseDate")
    val imReleaseDate: ImReleaseDate? = null,
    @Embedded(prefix = "link")
    @SerializedName("link")
    @TypeConverters(DataLinkConverter::class)
    val link: ArrayList<Link>? = arrayListOf(),
    @Embedded(prefix = "title")
    @SerializedName("title")
    val title: Title? = null,
    var imageArt: String? = null,
    var playLink: String? = null,
) : Parcelable

@Parcelize
@Keep
data class Icon @JvmOverloads constructor(
    @ColumnInfo(name = "iconLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class Updated @JvmOverloads constructor(
    @ColumnInfo(name = "updatedLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class Name @JvmOverloads constructor(
    @ColumnInfo(name = "nameLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class Uri @JvmOverloads constructor(
    @ColumnInfo(name = "uriLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class Category @JvmOverloads constructor(
    @Embedded(prefix = "catAttributes")
    @SerializedName("attributes")
    val attributes: Attributes = Attributes()
) : Parcelable


@Parcelize
@Keep
data class Id @JvmOverloads constructor(
    @SerializedName("attributes")
    @Embedded(prefix = "attribute")
    val attributes: Attributes = Attributes(),
    @ColumnInfo(name = "idLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class ImArtist @JvmOverloads constructor(
    @Embedded(prefix = "artistAttr")
    @SerializedName("attributes")
    val attributes: Attributes = Attributes(),
    @ColumnInfo(name = "imArtistLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class ImCollection @JvmOverloads constructor(
    @Embedded(prefix = "imContentType")
    @SerializedName("im:contentType")
    val imContentType: ImContentType? = null,
    @Embedded(prefix = "imName")
    @SerializedName("im:name")
    val imName: ImName? = null,
    @Embedded(prefix = "link")
    @SerializedName("link")
    val link: Link? = null
) : Parcelable


@Parcelize
@Keep
data class ImImage @JvmOverloads constructor(
    @Embedded(prefix = "imageAttr")
    @SerializedName("attributes")
    val attributes: Attributes = Attributes(),
    @ColumnInfo(name = "imImageLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class ImReleaseDate @JvmOverloads constructor(
    @Embedded(prefix = "imReleaseAttr")
    @SerializedName("attributes")
    val attributes: Attributes = Attributes(),
    @ColumnInfo(name = "imReleaseLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class Title @JvmOverloads constructor(
    @ColumnInfo(name = "titleLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable

@Parcelize
@Keep
data class Attributes @JvmOverloads constructor(
    @ColumnInfo(name = "attScheme")
    @SerializedName("scheme")
    val scheme: String = "",
    @SerializedName("href")
    val href: String = "",
    @SerializedName("im:assetType")
    val imAssetType: String = "",
    @SerializedName("rel")
    val rel: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("im:id")
    val imId: String = "",
    @ColumnInfo(name = "attLabel")
    @SerializedName("label")
    val label: String = "",
    @SerializedName("amount")
    val amount: String = "",
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("height")
    val height: String = "",
    @SerializedName("term")
    val term: String = ""
) : Parcelable

@Parcelize
@Keep
data class ImContentType @JvmOverloads constructor(
    @Embedded(prefix = "imContentAttr")
    @SerializedName("attributes")
    val attributes: Attributes = Attributes()
) : Parcelable

@Parcelize
@Keep
data class ImName @JvmOverloads constructor(
    @ColumnInfo(name = "imLabel")
    @SerializedName("label")
    val label: String = "",
) : Parcelable

@Parcelize
@Keep
data class Link @JvmOverloads constructor(
    @Embedded(prefix = "linkAttr")
    @SerializedName("attributes")
    val attributes: Attributes = Attributes(),
    @Embedded(prefix = "imDuration")
    @SerializedName("im:duration")
    val imDuration: ImDuration? = null
) : Parcelable

@Parcelize
@Keep
data class ImDuration @JvmOverloads constructor(
    @ColumnInfo(name = "imDurationLabel")
    @SerializedName("label")
    val label: String = ""
) : Parcelable
