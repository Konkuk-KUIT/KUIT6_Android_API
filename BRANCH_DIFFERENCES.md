# practice-only 브랜치와 JeongIlhyuk/week7 브랜치 차이점 정리

## 변경 요약
- **추가된 파일**: 4개
- **수정된 파일**: 10개
- **삭제된 파일**: 1개

---

## 1. 추가된 파일 (A)

### 1.1 `app/src/main/java/com/example/kuit6_android_api/di/AppContainer.kt`
**새로 생성된 파일**
- 의존성 주입을 위한 AppContainer 클래스 추가
- ApiService와 PostRepository를 한 곳에서 관리
- Repository 패턴 구현을 위한 의존성 관리

**주요 내용:**
```kotlin
class AppContainer {
    val apiService: ApiService = RetrofitClient.apiService
    val postRepository: PostRepository = PostRepositoryImpl(apiService)
}
```

### 1.2 `app/src/main/java/com/example/kuit6_android_api/ui/post/viewmodel/PostCreateViewModel.kt`
**새로 생성된 파일**
- PostViewModel을 기능별로 분리한 ViewModel 중 하나
- 게시글 생성 관련 로직 담당
- PostCreateUiState를 통한 상태 관리

**주요 기능:**
- `createPost()`: 게시글 생성
- `uploadImage()`: 이미지 업로드
- `clearUploadedImageUrl()`: 업로드된 이미지 URL 초기화

### 1.3 `app/src/main/java/com/example/kuit6_android_api/ui/post/viewmodel/PostDetailViewModel.kt`
**새로 생성된 파일**
- 게시글 상세 조회 및 삭제 관련 ViewModel
- PostDetailUiState를 통한 상태 관리

**주요 기능:**
- `getPostDetail()`: 게시글 상세 조회
- `deletePost()`: 게시글 삭제

### 1.4 `app/src/main/java/com/example/kuit6_android_api/ui/post/viewmodel/PostEditViewModel.kt`
**새로 생성된 파일**
- 게시글 수정 관련 ViewModel
- PostEditUiState를 통한 상태 관리

**주요 기능:**
- `getPostDetail()`: 게시글 상세 조회
- `updatePost()`: 게시글 수정
- `uploadImage()`: 이미지 업로드
- `clearUploadedImageUrl()`: 업로드된 이미지 URL 초기화

---

## 2. 삭제된 파일 (D)

### 2.1 `app/src/main/java/com/example/kuit6_android_api/ui/post/viewmodel/PostViewModel.kt`
**삭제된 파일**
- 기존의 통합 PostViewModel이 기능별로 분리됨
- PostCreateViewModel, PostDetailViewModel, PostEditViewModel로 분리
- ApiService를 직접 참조하던 구조에서 Repository 패턴으로 변경

---

## 3. 수정된 파일 (M)

### 3.1 `app/src/main/java/com/example/kuit6_android_api/data/repository/PostRepository.kt`

**변경 사항:**
1. **Import 추가:**
   - `PostCreateRequest` 추가
   - `okhttp3.MultipartBody` 추가

2. **인터페이스 포맷팅:**
   - `PostRepository{` → `PostRepository {` (공백 추가)

3. **메서드 추가:**
   - `getPostDetail(postId: Long): Result<PostResponse>` - 게시글 상세 조회
   - `createPost(author, title, content, imageUrl): Result<PostResponse>` - 게시글 생성
   - `updatePost(postId, title, content, imageUrl): Result<PostResponse>` - 게시글 수정
   - `deletePost(postId: Long): Result<Unit>` - 게시글 삭제
   - `uploadImage(file: MultipartBody.Part): Result<String>` - 이미지 업로드

4. **파일 끝 개행 추가**

### 3.2 `app/src/main/java/com/example/kuit6_android_api/data/repository/PostRepositoryImpl.kt`

**변경 사항:**
1. **Import 추가:**
   - `PostCreateRequest` 추가
   - `BaseResponse` 추가
   - `okhttp3.MultipartBody` 추가

2. **클래스 포맷팅:**
   - `PostRepositoryImpl (` → `PostRepositoryImpl(` (공백 제거)
   - `): PostRepository {` → `) : PostRepository {` (공백 추가)

3. **getPosts() 메서드 개선:**
   - 반환 타입 명시: `BaseResponse<List<PostResponse>>`
   - 에러 메시지 변경: "게시긆 불러오기 실패" → "게시글 목록 조회 실패"
   - 포맷팅 개선 (공백 추가)

4. **새로운 메서드 구현 추가:**
   - `getPostDetail()`: 게시글 상세 조회 구현
   - `createPost()`: 게시글 생성 구현
   - `updatePost()`: 게시글 수정 구현
   - `deletePost()`: 게시글 삭제 구현
   - `uploadImage()`: 이미지 업로드 구현

5. **파일 끝 개행 추가**

### 3.3 `app/src/main/java/com/example/kuit6_android_api/ui/navigation/NavGraph.kt`

**변경 사항:**
1. **Import 추가:**
   - `PostCreateViewModel` 추가
   - `PostDetailViewModel` 추가
   - `PostEditViewModel` 추가

2. **PostDetailScreen에서 Factory를 통해 Repository를 ViewModel에 주입:**
   ```kotlin
   viewModel = viewModel(factory = postViewModelFactory { PostDetailViewModel(it) })
   ```
   - Factory가 AppContainer에서 Repository를 가져와 ViewModel 생성자에 전달

3. **PostCreateScreen에서 Factory를 통해 Repository를 ViewModel에 주입:**
   ```kotlin
   viewModel = viewModel(factory = postViewModelFactory { PostCreateViewModel(it) })
   ```
   - Factory가 AppContainer에서 Repository를 가져와 ViewModel 생성자에 전달

4. **PostEditScreen에서 Factory를 통해 Repository를 ViewModel에 주입:**
   ```kotlin
   viewModel = viewModel(factory = postViewModelFactory { PostEditViewModel(it) })
   ```
   - Factory가 AppContainer에서 Repository를 가져와 ViewModel 생성자에 전달

### 3.4 `app/src/main/java/com/example/kuit6_android_api/ui/post/screen/PostCreateScreen.kt`

**변경 사항:**
1. **Import 변경:**
   - `PostViewModel` → `PostCreateViewModel`
   - `postViewModelFactory` 추가

2. **ViewModel 타입 변경:**
   - `PostViewModel` → `PostCreateViewModel`
   - Factory를 통해 Repository를 ViewModel에 주입

3. **상태 접근 방식 변경:**
   - `viewModel.isUploading` → `uiState.isUploading`
   - `viewModel.uploadedImageUrl` → `uiState.uploadedImageUrl`
   - `val uiState = viewModel.uiState` 추가

4. **주석 추가:**
   - Repository 주입 방식에 대한 설명 주석 추가

### 3.5 `app/src/main/java/com/example/kuit6_android_api/ui/post/screen/PostDetailScreen.kt`

**변경 사항:**
1. **Import 변경:**
   - `PostViewModel` → `PostDetailViewModel`
   - `postViewModelFactory` 추가

2. **ViewModel 타입 변경:**
   - `PostViewModel` → `PostDetailViewModel`
   - Factory를 통해 Repository를 ViewModel에 주입

3. **상태 접근 방식 변경:**
   - `viewModel.postDetail` → `uiState.postDetail`
   - `val uiState = viewModel.uiState` 추가

4. **LaunchedEffect 주석 해제:**
   - `viewModel.getPostDetail(postId)` 호출 활성화

5. **주석 추가:**
   - Repository 주입 방식에 대한 설명 주석 추가

### 3.6 `app/src/main/java/com/example/kuit6_android_api/ui/post/screen/PostEditScreen.kt`

**변경 사항:**
1. **Import 변경:**
   - `PostViewModel` → `PostEditViewModel`
   - `postViewModelFactory` 추가

2. **ViewModel 타입 변경:**
   - `PostViewModel` → `PostEditViewModel`
   - Factory를 통해 Repository를 ViewModel에 주입

3. **상태 접근 방식 변경:**
   - `viewModel.postDetail` → `uiState.postDetail`
   - `viewModel.uploadedImageUrl` → `uiState.uploadedImageUrl`
   - `viewModel.isUploading` → `uiState.isUploading`
   - `val uiState = viewModel.uiState` 추가

4. **주석 추가:**
   - Repository 주입 방식에 대한 설명 주석 추가

### 3.7 `app/src/main/java/com/example/kuit6_android_api/ui/post/screen/PostListScreen.kt`

**변경 사항:**
1. **Import 변경:**
   - `PostViewModel` 제거
   - `PostListUiState` 제거
   - `collectAsState` 제거
   - `DisposableEffect`, `LocalLifecycleOwner`, `Lifecycle`, `LifecycleEventObserver` 추가
   - `postViewModelFactory` 추가

2. **Factory를 통한 Repository 주입 추가:**
   - Factory가 AppContainer에서 Repository를 가져와 ViewModel 생성자에 전달
   - 기본값으로 ViewModel 생성

3. **상태 관리 방식 변경:**
   - `StateFlow` 기반 → `mutableStateOf` 기반으로 변경
   - `collectAsState()` 제거, 직접 `uiState` 접근

4. **Lifecycle 관리 추가:**
   - `DisposableEffect`를 사용한 Lifecycle 이벤트 감지
   - `ON_RESUME` 이벤트 시 자동 새로고침

5. **UI 구조 단순화:**
   - `PostListUiState`의 sealed class 구조 제거
   - Loading, Success, Error 상태 분기 제거
   - 단순한 LazyColumn으로 변경

6. **LaunchedEffect 추가:**
   - 초기 로드 시 `viewModel.refresh()` 호출

### 3.8 `app/src/main/java/com/example/kuit6_android_api/ui/post/viewmodel/PostListViewModel.kt`

**변경 사항:**
1. **Import 변경:**
   - `PostListUiState` 제거 (파일 내부로 이동)
   - `MutableStateFlow`, `StateFlow`, `asStateFlow` 제거
   - `mutableStateOf`, `getValue`, `setValue` 추가
   - `PostResponse` 추가

2. **상태 관리 방식 변경:**
   - `StateFlow` 기반 → `mutableStateOf` 기반으로 변경
   - `_uiState`와 `uiState` 분리 구조 제거

3. **PostListUiState 정의 변경:**
   - sealed class에서 data class로 변경
   - Loading, Success, Error 상태 제거
   - 단순히 `posts: List<PostResponse>`만 포함

4. **생성자 파라미터 이름 변경:**
   - `postRepository` → `repository`

5. **초기화 로직 변경:**
   - `init` 블록 제거
   - `loadPosts()` 메서드 제거
   - `refresh()` 메서드로 통합

6. **에러 처리 단순화:**
   - Error 상태 제거, 실패 시 빈 리스트로 설정

7. **파일 끝 개행 추가**

### 3.9 `app/src/main/java/com/example/kuit6_android_api/ui/post/viewmodel/PostViewModelFactory.kt`

**변경 사항:**
1. **주석 추가:**
   - Repository 패턴과 수동 주입(App Container)에 대한 설명 주석 추가
   - Factory 패턴 사용 목적 설명

### 3.10 `gradle/libs.versions.toml`

**변경 사항:**
1. **AGP 버전 업데이트:**
   - `agp = "8.13.0"` → `agp = "8.13.1"`

---

## 주요 아키텍처 변경 사항

### 1. Repository 패턴 도입
- ApiService를 직접 참조하던 구조에서 Repository를 통한 추상화
- 의존성 주입을 통한 테스트 용이성 향상

### 2. ViewModel 분리
- 단일 `PostViewModel`을 기능별로 분리:
  - `PostListViewModel`: 목록 조회
  - `PostCreateViewModel`: 게시글 생성
  - `PostDetailViewModel`: 상세 조회 및 삭제
  - `PostEditViewModel`: 게시글 수정

### 3. UiState 구현
- 각 ViewModel에 UiState data class 추가 (PostCreateUiState, PostDetailUiState, PostEditUiState)
- PostListViewModel은 practice-only와 동일하게 StateFlow + sealed class 기반 PostListUiState 유지
- UI 상태를 UiState로 명확히 정의하여 상태 관리 일관성 향상

### 4. 의존성 주입 구조
- `AppContainer`를 통한 중앙 집중식 의존성 관리
- Factory 패턴을 통한 ViewModel 생성 시 Repository 주입
- ViewModel이 Repository를 파라미터로 받아 사용 (의존성 주입)

### 5. Lifecycle 관리 개선
- `LaunchedEffect`를 사용한 초기 로드 시 refresh 함수 실행 (미션 요구사항)
- PostListScreen에서 화면 진입 시 자동으로 데이터 새로고침

