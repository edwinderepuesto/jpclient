# JP Client
 JSONPlaceholder Client written in Kotlin

## Highlights: ##
- Kotlin, MVVM, coroutines.
- Hilt for dependency injection.
- Ktor for networking, Kotlin-based.
- Room for local storage. Posts are cached once received from API.
- Shows loading status for API requests.
- Can dismiss one or all posts.
- Swipe to refresh.
- Adaptive layout. Supports both phones (single pane) and tablets (two-pane).
- Post can be marked as favorite
  - Favorite posts are move to the top of the list
  - Favorite status is also persisted, even if the app is killed.
- Coroutine-safe unit tests.
