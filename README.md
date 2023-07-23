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
  * [참고 URL](https://sas-study.tistory.com/432)
  * [참고 URL2](https://velog.io/@heetaeheo/OkHttp-WebSocket)
  * [참고 URL3](https://itnext.io/websockets-in-android-with-okhttp-and-viewmodel-776a9eed67b5)
- 2023년 7월 23일
  1. 웹소켓 로직 적용(테스트 확인 필요)
  2. Presentation의 ViewModel
     ->Domain의 UseCase,Repository(Interface)
     ->Data의 Repository(Implements),RemoteDataSource로 향하는 user request scene 적용
     추후, 응답 데이터를 받아 Presentation 영역으로의 reponse scene 적용 예정
# 비고 & 특이사항
