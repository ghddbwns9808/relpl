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


## 팀원소개


<div align="center"> 
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
</div>
<br/><br/><br/><br/><br/><br/>

## 기능, 동작 화면
| ![](https://blog.kakaocdn.net/dn/cFQYGA/btsEQcVmwqk/XBAiK3PaIrOFFqdxnMO7n0/img.gif) | ![](https://blog.kakaocdn.net/dn/dc8LEm/btsENTIGM1o/iTlPzy48Uq02Kr9SSGea7K/img.gif) | ![](https://blog.kakaocdn.net/dn/ypeXN/btsES6T48Tp/M8ah0LfARY1H6s7ZZbHnw1/img.gif) |
| :---------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------: |
|                                       홈화면                                        |                                        랭킹                                         |                                      제보하기                                      |

<br><br>

| ![](https://blog.kakaocdn.net/dn/cObp2V/btsEQSCftHU/fKzMRsrRIyqg5VetYgkx10/img.gif) | ![](https://blog.kakaocdn.net/dn/bhwzJC/btsESn2O2GQ/XPNKrl8onodshuD7XKZKK1/img.gif) | ![](https://blog.kakaocdn.net/dn/bgjUve/btsEQK5koKr/J0pbZ0tSLTS9SIlkR5zoT1/img.gif) |
| :---------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: |
|                                  거리 릴레이 조회                                   |                                  경로 릴레이 조회                                   |                                      경로 찾기                                      |

<br><br>

| ![](https://blog.kakaocdn.net/dn/y0dVt/btsEVexSDrJ/3MWoHRnUvan9aKe8u0jvAK/img.gif) | ![](https://blog.kakaocdn.net/dn/KHHhM/btsEPKY2tjh/eUtYnUkTqsHEbfcdPtONXk/img.gif) | ![](https://blog.kakaocdn.net/dn/bUZxX9/btsERmb2KzY/uS7Gomk2Tf4ktrLOuMN9j1/img.gif) |
| :--------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------: |
|                                    릴레이 중단                                     |                                    릴레이 기록                                     |                                  릴레이 상세 기록                                   |

<br/><br/><br/><br/><br/>

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

### [피그마 링크](https://www.figma.com/file/TaVoVQpe1XfXl5K0w5JQ8f/%EB%B0%B8%EB%9F%B0%EC%8A%A4%EA%B2%8C%EC%9E%84?type=design&node-id=0%3A1&mode=design&t=pWdO1poXzxwOOz6e-1)

<br/><br/><br/>

## 개발 환경

- Android Studio : Giraffe 2022.3.1 Patch 2(or 3)
- Gradle JDK : jbr-17(JetBrains Runtime version 17.0.6)
- Android Gradle Plugin Version : 8.1.3
- Gradle Version : 8.1
- Kotlin version : 1.8.0
