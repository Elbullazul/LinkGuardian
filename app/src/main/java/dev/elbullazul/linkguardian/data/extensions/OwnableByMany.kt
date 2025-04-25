package dev.elbullazul.linkguardian.data.extensions

import dev.elbullazul.linkguardian.data.generic.Data

interface OwnableByMany {
    val ownershipData: List<Data>
}