package dev.elbullazul.linkguardian.backends

enum class BackendTypes {
    None,
    Linkwarden,
//    Karakeep (aka Hoarder)
}

fun enumToInt(type: BackendTypes): Int {
    if (type == BackendTypes.Linkwarden)
        return 1

    return 0
}

fun intToEnum(int: Int): BackendTypes {
    if (int == 1)
        return BackendTypes.Linkwarden

    return BackendTypes.None
}