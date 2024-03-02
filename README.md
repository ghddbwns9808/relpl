# 팀 GDD (D201)
<br><br><br>

 <h1 align="center">
릴플🍀
</h1>

<div align="center">
<img src="https://blog.kakaocdn.net/dn/o6NbZ/btsEP7zFkus/EvoxawE97IKX8CEcse8zJ0/tfile.svg" width="400px">
</div>

<div>
<h4>🏃‍♂️ 플로깅을 함께 해요<h4> 

<h4>🎯 목표 거리, 목표 경로를 바탕으로 플로깅 릴레이를 생성 할 수 있습니다<h4>

<h4>🤝 다른 유저가 생성한 릴레이에 참여 할 수 있습니다<h4>

<h4>🥇 누적 거리를 바탕으로 순위를 확인하세요<h4>
</div>
<br/>

## 개요

- 한 줄 요약 : *relpl* 프로젝트는 **위치 기반 릴레이 플로깅이 가능**한 **Android 모바일 어플리케이션**입니다.

- 기획의도 : 효율적인 플로깅이 가능하도록 돕고, 지속적인 플로깅을 위한 동기 부여가 가능하도록 하고자 했습니다.

- 개발 인원 및 기간
  
  - 개발 인원 : Android 2명, BackEnd 4명
  
  - 개발 기간 : 2024.01.03 ~ 2024.02.16 (총 45일, Business Day 31일)

- 주요 기능
  
  - 유저 관련 기능
  
  - 릴레이 플로깅 관련 기능
  
  - 제보, 보상 관련 기능

- 데모 시연 영상 : [D201 UCC (youtube.com)](https://www.youtube.com/watch?v=hv8uWsDKsy0)

---
<br/><br/><br/>

## 팀원소개

<center>  
<table>
  <tr>
    <td style="text-align: center;"><img src="https://blog.kakaocdn.net/dn/d2Sec4/btsETi7NKEO/kQCJxQQVCoJq529c02jVIK/img.jpg" width="200" alt="정철주"></td>
    <td style="text-align: center;"><img src="https://blog.kakaocdn.net/dn/pSIdE/btsEVfJvBJ4/sdNN7OCrkarJYApZ1bU9y0/img.jpg" width="200" alt="홍유준"></td>
    <td style="text-align: center;"><img src="https://blog.kakaocdn.net/dn/pbmw2/btsEQRwAVlS/CeeGY7kZ8mW9r8qoxQaEI0/img.jpg" width="200" alt="김성훈"></td>
  </tr>
  <tr>
    <td style="text-align: center;">🍺 정철주</td>
    <td style="text-align: center;">☕ 홍유준</td>
    <td style="text-align: center;">🍜 김성훈</td>
  </tr>

  <tr>
    <td style="text-align: center;">App(Android)</td>
    <td style="text-align: center;">App(Android)</td>
    <td style="text-align: center;">Back-End(Spring)</td>
  </tr>
</table>
<table>
  <tr>
    <td style="text-align: center;"><img src="https://blog.kakaocdn.net/dn/dtTZ9t/btsETYBeX6j/JX9QbCz3TMgTUYpPUqXmDK/img.jpg" width="200" alt="김효주"></td>
    <td style="text-align: center;"><img src="https://blog.kakaocdn.net/dn/tBheL/btsETYuubJI/slJ3w1jFvcEzUJg0QFEKD1/img.jpg" width="200" alt="송민석"></td>
    <td style="text-align: center;"><img src="https://blog.kakaocdn.net/dn/6a0VG/btsETkq17SE/KyCUURrp4Cnn70PPHCzCU0/img.jpg" width="200" alt="최재성"></td>
  </tr>
  <tr>
    <td style="text-align: center;">😺 김효주</td>
    <td style="text-align: center;">🐸 송민석</td>
    <td style="text-align: center;">⚽ 최재성</td>
  </tr>

  <tr>
    <td style="text-align: center;">Back-End(Spring)</td>
    <td style="text-align: center;">Back-End(Spring)</td>
    <td style="text-align: center;">Back-End(Spring)</td>
  </tr>
</table>
</center>
<br/><br/><br/><br/><br/><br/>

## 기능, 동작 화면
| ![](https://blog.kakaocdn.net/dn/cFQYGA/btsEQcVmwqk/XBAiK3PaIrOFFqdxnMO7n0/img.gif) | ![](https://blog.kakaocdn.net/dn/dc8LEm/btsENTIGM1o/iTlPzy48Uq02Kr9SSGea7K/img.gif) | ![](https://blog.kakaocdn.net/dn/ypeXN/btsES6T48Tp/M8ah0LfARY1H6s7ZZbHnw1/img.gif) |
| :---------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------: |
|                                       홈화면                                        |                                        랭킹                                         |                                      제보하기                                      |

<br><br>

| ![](https://blog.kakaocdn.net/dn/cObp2V/btsEQSCftHU/fKzMRsrRIyqg5VetYgkx10/img.gif) | ![](https://github.com/ghddbwns9808/relpl/assets/65646370/b23e9a64-7de6-4a2b-873d-88b9b5d6fa86) | ![](https://blog.kakaocdn.net/dn/bgjUve/btsEQK5koKr/J0pbZ0tSLTS9SIlkR5zoT1/img.gif) |
| :---------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: |
|                                  거리 릴레이 조회                                   |                                  경로 릴레이 조회                                   |                                      경로 찾기                                      |

<br><br>

| ![](https://blog.kakaocdn.net/dn/y0dVt/btsEVexSDrJ/3MWoHRnUvan9aKe8u0jvAK/img.gif) | ![](https://blog.kakaocdn.net/dn/KHHhM/btsEPKY2tjh/eUtYnUkTqsHEbfcdPtONXk/img.gif) | ![](https://blog.kakaocdn.net/dn/bUZxX9/btsERmb2KzY/uS7Gomk2Tf4ktrLOuMN9j1/img.gif) |
| :--------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: |
|                                    릴레이 중단                                     |                                    릴레이 기록                                     |                                  릴레이 상세 기록                                   |

<br/><br/><br/><br/><br/>
 <h1 align="center">
안드로이드
</h1>

## 모듈 구조

![](https://blog.kakaocdn.net/dn/bZwEPB/btsETn92g33/p3AurXvRdgHGWxs5H7UJ4k/img.png)


### 기술
- Android: <span style="color:yellowgreen"> Hilt, Jetpack AAC(ViewModel, Room, DataBinding), Foreground Service </span>
- Kotlin : <span style="color:blueviolet"> Coroutine, Flow </span>
- Library : <span style="color:orange"> Retrofit, Glide, Naver Map, Google Location, Firebase(FCM, Auth), Zxing</span>
- UI Library : <span style="color:skyblue"> StickyTimeLine, Pager Dots Indicator
, Floating Action Button Speed Dial, Lottie </span>
- Architecture : <span style="color:gray"> MVVM, MultiModule, CleanArchitecture</span>

<br/><br/><br/>

### [피그마 링크](https://www.figma.com/file/CNQG7GcXmt00S7lykwbBrp/GDD?type=design&node-id=931%3A329&mode=design&t=4O22DC2FBjH0mhY5-1)

<br/><br/><br/>

## 개발 환경

- Android Studio : Giraffe 2022.3.1 Patch 2
- Gradle JDK : jbr-17(JetBrains Runtime version 17.0.6)
- Android Gradle Plugin Version : 8.1.3
- Gradle Version : 8.1
- Kotlin version : 1.8.0
## Android Role & Responsibility (R&R)

#### 정철주
- 앱 구조 구성, 모듈화
- 릴레이 진행, 중단
  - 포그라운드 서비스 활용 위치 추적
- 랭킹
- 제보하기, 제보기록
- 포인트 내역, 포인트 사용
- JWT 토큰 대응

#### 홍유준
- 앱 디자인
- 회원가입
- 거리, 경로 릴레이 조회
- 거리, 경로 릴레이 참여
- 유저정보
  - 프로필, 회원정보 수정
- 내 플로깅 기록, 상세 기록
- JWT 토큰 대응

<br/><br/><br/><br/><br/>

 <h1 align="center">
백엔드
</h1>

## 설계

### 시스템 아키텍쳐
![](https://blog.kakaocdn.net/dn/SIWQC/btsEVOToYqw/9lDCjOphFtE0zlKzCFSWBK/img.png)

### ERD 다이어그램
![](https://blog.kakaocdn.net/dn/IV5N0/btsERlFbYuc/Du9wAf3ra1WO7tSL3uvfb0/img.png)

- [요구사항 명세서](https://docs.google.com/spreadsheets/d/1ZayfMIxvD49W1cAYiXdvZzePvYK2iXs2G8KfIWohgio/edit?usp=sharing)

- [API 명세서](https://grand-check-7ba.notion.site/API-33f7e2f1f2e14a1683b82493a37beebd?pvs=4)

---

## 사용한 기술

### 사용한 라이브러리

- Spring Boot Data JPA, MongoDB, Redis
- Project Lombok
- PostgreSQL JDBC Driver
- Spring Boot Starter Test
- Spring Security Test
- SpringDoc OpenAPI
- Hibernate Spatial
- Jakarta Annotations & Persistence API
- Spring Cloud AWS
- Jasypt
- Spring Boot Starter Security
- JSON Web Token (JWT)
- Spring Boot Starter Cache
- Firebase Admin SDK
- OkHttp

### 디렉토리 구조
![](https://blog.kakaocdn.net/dn/4igCf/btsERklWOxL/quhS0fCz2XSxIo3ZIkJ9J1/img.png)

![](https://blog.kakaocdn.net/dn/tsVZV/btsES6AMKdH/gdLQvDmJSmXtwRswgXhDe1/img.png)

## Back-End Role & Responsibility (R&R)

#### 김성훈

- 인프라 (CI/CD)

- 릴레이 플로깅 생성 기능

- Tmap API 를 활용한 초기 도로 DB 구축

- 경로 추천 기능

#### 최재성

- jwt , Spring Security 를 이용한 회원가입 API 개발

- jwt , Spring Security 를 이용한 로그인 API 개발

- 릴레이 플로깅 참여 API 개발

- 릴레이 플로깅 중단 API 개발

#### 김효주

- Redis 를 활용한 실시칸 랭킹 API 개발

- PostGreSQL 활용하여 Amazon S3 사진 업로드 가능한 프로필 설정 API 개발

- PostGreSQL 활용하여 프로필 사진 변경이 가능한 마이페이지 API 개발

- mongoDB, PostGreSQL, Spring Data 를 활용한 내 플로깅 기록 보기 API 개발

#### 송민석

- 포인트 관련 기능

- 플로깅 장소 제보 기능

- 릴레이 플로깅 정보 조회 기능
