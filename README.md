# LinkGuardian
An Android client for [Linkwarden](https://github.com/linkwarden/linkwarden). Built with Kotlin and Jetpack compose.

<a href="https://apt.izzysoft.de/fdroid/index/apk/dev.elbullazul.linkguardian"><img src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png" alt="Download from IzzyOnDroid" width="200" height="80"/></a>

| | | | |
| --- | --- | -- | -- |
| ![preview-light](https://github.com/Elbullazul/linkguardian/blob/master/res/preview.png) | ![preview-light](https://github.com/Elbullazul/linkguardian/blob/master/res/add-link.png) | ![preview-dark](https://github.com/Elbullazul/linkguardian/blob/master/res/preview-dark.png) | ![preview-light](https://github.com/Elbullazul/linkguardian/blob/master/res/add-link-dark.png) |

### Features (work in progress)
- [x] Login with API token
- [x] Dark theme
- [x] Browse links in the app
- [x] Add links
- [x] Receive external links
- [ ] Search
- [ ] Edit links, tags and collections
- [ ] Offline mode
- [ ] Open archived content

### Dependencies
- [OkHttp](https://github.com/square/okhttp) for making HTTP requests
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) for Json serialization/deserialization
- [Coil](https://github.com/coil-kt/coil) to display remote images

### Architecture
The app will be developed in as simple a way as possible. I'm relatively new to Kotlin, so not all code will follow best practices initially. If you have suggestions, feel free to open a pull request!
