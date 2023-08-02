# StockApp

# 개발내용
- 코틀린과 젯팩 컴포즈를 활용하여 실시간 주식 화면을 개발
- 네이티브가 아닌 웹뷰를 통해서도 실시간 주식 화면을 볼 수 있도록 웹 프론트 화면을 개발
  (웹뷰 자바스크립트 인터페이스를 통해 네이티브와 연동)
- 실시간 주식 외에 별도로 API(매수,매도 ...)를 구하기가 어려우니, 
  실제 매수,매도 등의 서버 연동은 생략하고, Room DB를 통해 데이터를 처리하려 예정..

# 사용 (예정) 기술 
- Clean Architecture(Data-Domain-Presentation)
- Jetpack Compose
- WebView(HTML,CSS,JavaScript)
- Dagger Hilt
- Coroutine
- LiveData & Flow
- WebSocket
- RoomDB
- Chart??
- Google Material Design
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
# 비고 & 특이사항
  * 메인화면에서 KRW/BTC/USDT에 따라 실시간 코인 정보 LazyColumn으로 실시간 업데이트 처리
