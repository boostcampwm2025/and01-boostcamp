# Memorip
나만의 장소 저장 및 공유 서비스

## 프로젝트 소개

이 프로젝트는 다음과 같은 니즈를 가진 사람들이 모여 기획한
나만의 여행 장소 기록·공유 서비스입니다.

1. 여행 장소 정보는 필요하지만, 직접 하나하나 찾아보기 번거로운 사람
2. 광고성 정보가 아닌, 실제 사람들이 남긴 자연스러운 장소 후기를 원하는 사람
3. 나만의 소중한 장소를 저장하고, 필요하다면 다른 사람과 공유하고 싶은 사람
4. 여러 장소를 바탕으로 여행 계획을 쉽고 직관적으로 세우고 싶은 사람

이 서비스는 사용자들의 실제 경험을 기반으로
장소를 기록하고, 공유하고, 계획까지 이어질 수 있도록 돕는 것을 목표로 합니다.


<img width="1100" alt="image" src="https://github.com/user-attachments/assets/22a4b0cc-be77-42a2-9147-9bf7d8c15b64" />


## 정적 데이터 생성 자동화

> 변경 빈도가 낮은 지역/행정구역 데이터를 서버 API 대신 정적 리소스로 관리하여  
> 네트워크 의존도를 제거하고 즉시 응답 가능한 구조 설계

<img width="1867" height="641" alt="image" src="https://github.com/user-attachments/assets/6c20e42c-b5e2-496d-9da5-6b596ce4fd36" />




### 문제
지역 필터링 시 매번 서버 API 호출이 발생하여 응답 지연과 불필요한 트래픽 비용 증가

<img width="953" height="781" alt="image" src="https://github.com/user-attachments/assets/2f152e11-a246-41be-a797-aa899f5cd54e" />



### 해결
행정구역 데이터를 JSON 정적 리소스로 내장하고, 스크립트 기반 자동 생성으로 빌드 시점에 포함

<img width="1190" height="750" alt="image" src="https://github.com/user-attachments/assets/dfa35a47-50da-49e6-a3eb-b6dfd8cf2730" />



---

### 데이터 구조 설계

> 지역 depth(시/도/구/동 등)를 고정하지 않고 트리 구조로 표현하여 단계 수와 무관하게 확장 가능한 구조 설계

| 항목 | 설명 |
| --- | --- |
| Tree 기반 파싱 | JSON → Tree 재귀 탐색으로 변환|
| Depth 자유도 | 행정 단계 추가 시 변환 코드 수정 불필요 |
| 재사용성 | 해외 지역/다른 카테고리 데이터에도 동일 구조 적용 가능 |

---

### 처리 흐름

```
원본 데이터
   ↓
변환 스크립트
   ↓
JSON 자동 생성
   ↓
앱 빌드 시 포함
   ↓
클라이언트 지역 필터 기능에 사용
```



## 주요 기능
| 기능 | 설명 | 영상 |
| --- | --- | --- |
| **원하는 장소 사진들을 한눈에** | 사용자가 원하는 장소를 쉽게 찾을 수 있는 검색과 필터링 기능 | <img src="https://github.com/user-attachments/assets/0d79dd02-5585-44ea-a434-c969b604406a" width="150" /> <img src="https://github.com/user-attachments/assets/b1e9013c-0b97-400f-85cc-231f4f67044b" width="150" /> |
| **간편한 앱 사용** | 기존 앱들의 UI와 달리 텍스트를 최소화하여 간단한 인터랙션으로 동작 | <img src="https://github.com/user-attachments/assets/79a85ad2-7995-40a2-a474-9010203f195f" width="150" /> |
| **취향에 맞게 가져와 사용하는 장소 경험 공유 시스템** | 익명 공유를 통해 개인정보 노출 부담 없이 순수하게 장소에 대한 경험을 공유 | <img src="https://github.com/user-attachments/assets/1a705c9f-a12d-496b-aed1-b6e46f0eb42f" width="150" /> |

## 프로젝트 구조

## 팀원 소개

| 권동현 | 서호준 | 임현정 | 홍원택 |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/GwonDongHyeon21.png" width="150" height="150" style="border-radius:100%" > | <img src="https://github.com/uselessnaming.png" width="150" height="150" style="border-radius:100%" > | <img src="https://github.com/HJunng.png" width="150" height="150" style="border-radius:100%" > | <img src="https://github.com/hoyadong1.png" width="150" height="150" style="border-radius:100%" > |
| [@GwonDongHyeon21](https://github.com/GwonDongHyeon21) | [@uselessnaming](https://github.com/uselessnaming) | [@HJunng](https://github.com/HJunng) | [@hoyadong1](https://github.com/hoyadong1) |

## 기술 스택
![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=android&logoColor=white)
![MVVM](https://img.shields.io/badge/MVVM-000000)

![Coroutines](https://img.shields.io/badge/Coroutines-0095D5)
![Flow](https://img.shields.io/badge/Flow-0095D5)
![Hilt](https://img.shields.io/badge/Hilt-59666C)

![Firebase](https://img.shields.io/badge/Firebase%20App%20Distribution-FFCA28?logo=firebase&logoColor=black)
