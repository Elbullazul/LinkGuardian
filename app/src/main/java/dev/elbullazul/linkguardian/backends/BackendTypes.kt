package dev.elbullazul.linkguardian.backends

enum class BackendTypes {
    None,
    Linkwarden,
//    Karakeep (aka Hoarder)
}

fun EnumToInt(type: BackendTypes): Int {
    if (type == BackendTypes.Linkwarden)
        return 1

    return 0
}

fun IntToEnum(int: Int): BackendTypes {
    if (int == 1)
        return BackendTypes.Linkwarden

    return BackendTypes.None
}