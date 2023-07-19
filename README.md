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
- 2023년 7월 19일
  1. Clean Architecture(Data-Domain-Presentation) 적용을 위한 모듈 생성  
  * [참고 URL1](https://heegs.tistory.com/61)
  * [참고 URL2](https://jungwoon.github.io/android/2021/04/12/Android-CleanArchitecture.html)
  2. Dagger Hilt 스터디
  * [참고 URL1](https://developer.android.com/training/dependency-injection/hilt-android?hl=ko#setup)
  * [참고 URL2](https://velog.io/@hhi-5258/Hilt%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%9C-DIDependency-Injection)
  * [참고 URL3](https://jkroh.tistory.com/33)
  3. WebSocket 스터디
  * [참고 URL](https://sas-study.tistory.com/432)
  * [참고 URL2](https://velog.io/@heetaeheo/OkHttp-WebSocket)
# 비고 & 특이사항
