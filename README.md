# StockApp

# 개발내용
- 코틀린과 젯팩 컴포즈를 활용하여 실시간 주식 화면을 개발
- 네이티브가 아닌 웹뷰를 통해서도 실시간 주식 화면을 볼 수 있도록 웹 프론트 화면을 개발
  (웹뷰 자바스크립트 인터페이스를 통해 네이티브와 연동)
- 실시간 주식 외에 별도로 API(매수,매도 ...)를 구하기가 어려우니, 
  실제 매수,매도 등의 서버 연동은 생략하고, Room DB를 통해 데이터를 처리하려 예정..

# 사용 기술 
- Clean Architecture(Data-Domain-Presentation)
- Jetpack Compose
- WebView(HTML,CSS,JavaScript) (사용 예정)
- Dagger Hilt
- Coroutine
- LiveData & Flow
- WebSocket
- RoomDB
- Chart?? (사용 예정)
- Google Material Design (사용 예정)
- Retrofit

# 개발일지
- 2023년 7월 19일,21일
  1. Clean Architecture(Data-Domain-Presentation) 적용을 위한 모듈 생성  
  * [참고 URL1](https://heegs.tistory.com/61)
  * [참고 URL2](https://jungwoon.github.io/android/2021/04/12/Android-CleanArchitecture.html)
  * [참고 URL3](https://mashup-android.vercel.app/mashup-11th/heejin/useCase/useCase/)
  2. Dagger Hilt 스터디
  * [참고 URL1](https://developer.android.com/training/dependency-injection/hilt-android?hl=ko#setup)
  * [참고 URL2](https://velog.io/@hhi-5258/Hilt%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%9C-DIDependency-Injection)
  * [참고 URL3](https://jkroh.tistory.com/33)
  * [참고 URL4](https://junyoung-developer.tistory.com/195)
  * [참고 URL5](https://developer.android.com/codelabs/android-hilt#0)
  * [참고 URL6](https://thinking-face.tistory.com/entry/Hilt-2-Dependency-Injection-with-Hilt)
  3. WebSocket 스터디
  * [참고 URL1](https://sas-study.tistory.com/432)
  * [참고 URL2](https://velog.io/@heetaeheo/OkHttp-WebSocket)
  * [참고 URL3](https://itnext.io/websockets-in-android-with-okhttp-and-viewmodel-776a9eed67b5)
  * [참고 URL4](https://docs.upbit.com/reference/test-and-request-sample)
- 2023년 7월 23일
  1. 웹소켓 로직 적용(테스트 확인 필요)
  2. Presentation의 ViewModel
     ->Domain의 UseCase,Repository(Interface)
     ->Data의 Repository(Implements),RemoteDataSource로 향하는 user request scene 적용
     추후, 응답 데이터를 받아 Presentation 영역으로의 response scene 적용 예정
- 2023년 7월 27일
  1. 웹소켓 동작 확인을 위해 업비트 웹소켓 URL을 활용하여 비트코인 원화 실시간 데이터 수신 로직 적용
  2. 가상화폐 마켓코드를 얻어오기 위해 Retrofit API 통한 서버 연동 로직 적용
     (코루틴으로 retrofit 사용시 enqueue 메소드를 적용할 필요가 없음)
  * [참고 URL1](https://it4edu.tistory.com/159)
  * [참고 URL2](https://notepad96.tistory.com/194)
  * [참고 URL3](https://velog.io/@jeongminji4490/Retrofit-Call-vs-Response-and-Kotlin-Result)
  * [참고 URL4](https://codechacha.com/ko/android-coroutine-retrofit/)
  * [참고 URL5](https://seokzoo.tistory.com/4)
- 2023년 7월 29일
  1. jetpack compose navigation 적용(navigation 각 화면 composable UI는 추후 개발 예정)
  2. jetpack compose hilt viewmodel 적용(hiltViewModel) - navigation 각 화면마다 각자의 ViewModel을 가진다.
  * [참고 URL](https://velog.io/@wlsrhkd4023/Compose-hiltViewModel%EA%B3%BC-viewModel-%EC%B0%A8%EC%9D%B4)
- 2023년 7월 30일
  1. Retrofit API로 가상화폐 마켓코드를 얻어온 후 회신 데이터를 Flow Stream으로 Emit(배출)
  2. Data와 Domain Layer는 서로 다른 모델을 사용하도록 함. Data 모델은 Mapper를 통해 Domain 모델로 변환됨
     (Data 모델을 Domain Layer에서 직접 쓸경우 빌드 에러가 난다. Domain은 Data 모듈을 dependency로 가지고 있지 않기 때문에..)
- 2023년 7월 31일
  1. 앱 시작시 Intro 화면에서 가상화폐 마켓코드를 서버로부터 받아서(Retrofit 통신) RoomDB에 저장하도록 로직 적용
- 2023년 8월 1일
  1. 앱 시작시 마켓코드를 받아 RoomDB에 저장 성공시 메인화면으로 진입하도록 로직 적용
  2. Compose LiveData dependency 적용
- 2023년 8월 2일
  1. 메인화면 Scaffold,TopAppBar,NavigationBar 적용
  2. 업비트 웹화면 캡쳐(윈도우+shift+s)하여 화면 구성 및 컬러 분석(캡쳐 파일을 그림판으로 열어 스포이트로 색상값 추출)
  3. 메인 화면 KRW,BTC,USDT 탭버튼 구성
- 2023년 8월 3일
  1. RoomDB Insert 수정(@Insert DAO에서 다수 데이터를 insert 해야할때에는 param을 list가 아닌 vararg로 받아야 한다. 배열을 인자로 사용할시에는 *배열로 spread화 시켜서 insert함수에 전달한다)
  2. RoomDB Insert 가 inMemoryDataBase에서는 오동작 하는 것 같아서, 일반 DataBaseBuilder로 DB 인스턴스 생성하도록 수정
  3. 메인 화면 진입시 DB에 저장되어 있던 마켓코드 불러오는 로직 적용. 이에 따라 서버에서 수신 받을시에는 GetCoinMarketCodeAllFromRemoteUseCase, DB에서 불러올시에는 GetCoinMarketCodeAllFromLocalUseCase 처리하도록 구분시켰음.
  4. Room DB 처리시 Flow 로직 수정(DAO 함수에 suspend 적용, Channel 제거)
- 2023년 8월 5일
  1. 메인화면 진입시 Room DB에서 얻어온 마켓코드들을 KRW/BTC/USDT 그룹으로 구분하여 저장
  2. 메인화면에서 KRW/BTC/USDT 탭을 클릭할 때마다 해당 마켓으로 웹소켓 통신 하도록 로직 적용
- 2023년 8월 7일
  1. 메인화면에서 KRW 기준으로 현재가 조회 API를 통해 LazyColumn으로 코인 현재가 리스트 출력 로직 적용
- 2023년 8월 8일
  1. 메인화면에서 KRW 실시간 코인 정보 LazyColumn으로 실시간 업데이트 처리
- 2023년 8월 9일
  1. 메인화면 실시간 데이터 스트림 처리는 liveData보다는 sharedFlow가 쫌 더 부드러운 듯 하여 sharedFlow 적용
- 2023년 8월 10일
  1. 메인화면 ViewModel에 이벤트 리스너 인터페이스를 추가함. 추후 필요한 이벤트 처리 하도록 준비
  2. 메인화면 진입시 초기화(마켓코드 수신 및 실시간 코인정보 웹소켓 접속..)를 마치면, 자동으로 KRW 탭을 선택하도록 적용
- 2023년 8월 11일
  1. MainViewModel을 BaseRealTimeViewModel과 분리(확장 및 재사용을 위해)
  2. 메인화면 실시간 코인 정보 리스트 항목 클릭시 상세화면으로 이동 처리(navController.navigate). 상세화면은 추후 개발 예정
  3. 메인화면 실시간 코인 정보 수신 처리 개선 중..(실시간 데이터가 연속으로 똑같은게 들어올때가 있어서 ,이전 데이터를 저장했다가 새로 데이터가 들어올때 비교해서 다를때에만 emit하도록 함)
  4. Navigation에서 다음 화면으로 reference argument를 넘길때, currentBackStackEntry.savedStateHandle.set메소드를 이용하는데,
     set 메소드 내부를 보니, ACCEPTABLE_CLASSES 배열 안에 정의된 자료형이 아니면, IllegalArgumentException을 발생시킨다.
     이에 따라, Navigation에서 reference argument로써 데이터 클래스를 넘기기 위해 Parcelable을 적용함.
- 2023년 8월 12일
  1. 상세화면 진입시 해당 마켓코드로 웹소켓 통신(실시간 호가)하도록 로직 적용
  2. 상세화면 UI 프레임 구성 - 메인화면 처럼 Scaffold 기준으로 세웠으며, 컨텐츠 영역에 Tab,LazyColumn,Input Component(Slot Composable)을 구성함
- 2023년 8월 14일
  1. 상세화면 진입시 해당 마켓코드로 호가 조회(Retrofit API) 후 웹소켓 통신(실시간 호가)하여 LazyColumn으로 실시간 호가 리스트 업데이트 처리 로직 적용
  2. 웹소켓 통신 파라미터 수정 - 요청할 파라미터 타입(ticker,orderbook,trade)을 다수로 넘길 수 있도록 로직 수정 -> 상세화면에서 실시간으로 현재가와 호가를 동시에 받을 수 있게 함
- 2023년 8월 15일
  1. 상세화면에서 현재가 와 호가 정보를 실시간으로 받아서 화면에 업데이트 하도록 로직 적용 
- 2023년 8월 16일,17일
  1. 상세화면 주문 매수 탭 Compose UI 개발 중
  2. 상세화면 주문 매수/매도 탭에 수량/가격 필드 클릭시 보일 ModalBottomSheet 적용 [참고URL](https://workspace-dev.medium.com/hello-modalbottomsheet-b8fa11bb6423)
- 2023년 8월 20일
  1. ModalBottomSheet을 사용하려 했으나, 하단 소프트키와 겹치는 현상이 있고, ModalBottomSheet 자체 버그(아직 실험단계의 컴포넌트이니까..)인 듯 하여 AnimatedVisibility를 활용하여 BottomSheet를 비슷하게 만들어 봤음..
- 2023년 8월 21일
  1. 상세화면 호가 탭 구성 - Compose에서 예전 AndroidView를 사용해보려 함 [참고 URL](https://developer.android.com/jetpack/compose/migrate/interoperability-apis/views-in-compose?hl=ko)
  2. 상세화면 호가 탭에 AndroidView를 구성하고 웹뷰를 배치 - 추후, 호가 정보는 웹으로 개발한 소스를 GitHub(무료호스팅)에 배포하여 웹뷰로 볼 수 있도록 할 예정
# 비고 & 특이사항
  1. 메인화면에서 KRW/BTC/USDT에 따라 실시간 코인 정보 LazyColumn으로 실시간 업데이트 처리
  - MainViewModel 에서 실시간 코인 정보 보내면 Main에서 늦게 받는 경우가 있는 이슈 처리 필요
  - 메인에서 flow collect 해서 실시간 데이터를 받는 것까지는 괜찮은데, 이를 composable에서 state 변수가 몇몇 받아내지 못하고 있다.. 
  2. Room DB에서 DAO 함수 반환형을 Flow를 사용하도록 로직 수정 
  - [참고 URL](https://hungseong.tistory.com/33)
  - 이 건은 취소 한다. DAO 함수(Select 쿼리문) 반환형을 Flow로 적용하면, DB데이터가 바뀔때마다 자동으로 호출되는데,
    현재 앱에서는 굳이 필요할 것 같지 않아 추후, 필요시에 검토해서 적용하려 함. 
  - 반환형을 Flow로 적용하니까, collect 후에 onCompletion 메소드가 호출되지 않는게 찜찜하다..
  3. plugin을 compose로 찾아보니, 몇몇 쓸만한 것들이 눈에 띈다. 
  - Compose Modifiers Playground -> compose modifier 들을 바꿔가며 각 속성들이 뭐하는 것인지 알려줌
  - HTML to compose web converter -> HTML 코드를 compose 코드로 컨버젼 해주는 것
  4. 디자인 탭에서 육안으로 보이는 것을 더블 클릭하여, 해당 컴포넌트 소스 위치를 알 수 있지만..
    디자인 탭에서는 컴포넌트들이 겹치거나 하는 등으로 잘 보이지 않고, 실제 화면상에서만 확인할 수 있는 소스 상의 composable 함수들이 어디에 위치하는지 궁금할때엔, 
    Layout Inspector를 적극 활용하자.
  - Layout Inspector에 보이는 컴포넌트 트리 상에서 원하는 컴포넌트를 더블 클릭하면 해당 composable 함수로 이동해준다.
  5. viewModel이 있는 Composable 경우 preview 탭에 컴포넌트가 그려지지 않는데, 해결방안 찾아볼 것
  - 현재로써는 ViewModel을 쓰지 않는 TestActivity 한개 생성해서 거기서 Composable UI 작성해서 확인해보며 개발하고 있믐..
    쫌더 기술적인 개발 방안을 잦아볼 필요가 있음..
  - interface나 abstract class를 이용하여 fake ViewModel을 활용하는 것도 괜찮은 것 같다.
    [참고 URL](https://witcheryoon.tistory.com/316)
  6. webView content 개발시에는 예전 xml 사용하는 방식으로 compose에 붙여볼 생각인데, [참고 URL](https://developer.android.com/jetpack/compose/interop/interop-apis?hl=ko)을 보며 해볼 예정.
  7. compose layout align은 [참고 URL](https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#Column(androidx.compose.ui.Modifier,androidx.compose.foundation.layout.Arrangement.Vertical,androidx.compose.ui.Alignment.Horizontal,kotlin.Function1))