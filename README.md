# JP Client
 JSONPlaceholder Client written in Kotlin
 
https://user-images.githubusercontent.com/26127503/165461676-a42c61fc-c668-467a-90bf-00c0e5e26648.mp4



## Highlights: ##
- Kotlin, MVVM, coroutines/Flow.
- Hilt for dependency injection. Reduced boilerplate over dagger2.
- Ktor for networking. Kotlin-based, optimized for coroutines/Flow.
- Room for local storage, officially supported. 
  - Posts are cached once received from API.
- Shows loading status for API requests. Based on Flow.
- Can dismiss one or all posts.
- Swipe to refresh.
- Adaptive layout. Supports both phones (single pane) and tablets (two-pane).
- Post can be marked as favorite.
  - Favorite posts are move to the top of the list.
  - Favorite status is also persisted, even if the app is killed.
- Coroutine-safe unit tests (kotlinx-coroutines-test).
- Using mockito-inline for increase flexibilty over regular mockito.
