## 📝 변경 사항
Pinterest/Instagram 스타일의 Bento 레이아웃 시스템을 구현하여 그룹 이미지를 동적이고 미적으로 표현하는 그룹 뷰 컴포넌트를 추가했습니다.

## 🎯 작업 내용

### 1. Domain Layer - 레이아웃 시스템 구축
- **`GroupItem.kt`**: 그리드 아이템의 span 정보와 이미지 데이터를 담는 데이터 클래스
  - `colSpan`, `rowSpan`: 아이템이 차지할 그리드 크기
  - `imageUrl`: 표시할 이미지 URL
  - `overNumber`: 7개 초과 이미지 개수 표시

- **`LayoutBlock.kt`**: 레이아웃 패턴을 정의하는 내부 데이터 클래스
  - `index`: 이미지 배열에서의 인덱스
  - `isOverflowTarget`: 오버플로우 카운트 표시 대상 여부

- **`GroupLayoutBuilder.kt`**: 핵심 레이아웃 빌더 로직 (69줄)
  - `buildBento5x3Items()`: 이미지 리스트를 받아 GroupItem 리스트로 변환
  - 이미지 개수(1~7개)에 따라 최적화된 7가지 레이아웃 패턴 제공
  - 7개 초과 이미지는 오버플로우 카운트로 표시

**레이아웃 패턴 설계 특징:**
```kotlin
// 1개: 전체 화면 (5x3)
// 2개: 3:2 비율 분할
// 3개: 메인 이미지 + 2개 사이드
// 4개: 메인 이미지 + 3개 소형 그리드
// 5개: 균형잡힌 비대칭 레이아웃
// 6개: 5개 패턴 + 추가 소형 블록
// 7개: 최대 밀도 레이아웃 + 오버플로우
```

### 2. Presentation Layer - UI 컴포넌트

- **`GroupLayout.kt`**: Custom Layout Composable (125줄)
  - 5x3 그리드 기반의 Custom Layout 구현
  - 동적 셀 배치 알고리즘으로 겹침 방지
  - `occupied` 배열로 그리드 점유 상태 추적
  - 각 아이템의 rowSpan/colSpan에 맞춰 정확한 위치 계산

**핵심 로직:**
```kotlin
// 1. 그리드 점유 여부 확인
fun canPlace(r: Int, c: Int, rs: Int, cs: Int): Boolean

// 2. 배치 후 그리드 마킹
fun mark(r: Int, c: Int, rs: Int, cs: Int)

// 3. 셀 크기 계산
val cellWidth = constraints.maxWidth / columns
val cellHeight = constraints.maxWidth / rows
```

- **`ImageCard.kt`**: 이미지 표시 컴포넌트 (70줄)
  - Coil을 사용한 비동기 이미지 로딩
  - 2dp padding으로 카드 간격 설정
  - 오버플로우 카운트 오버레이 (검은색 30% 투명도)
  - ContentScale.Crop으로 이미지 비율 유지

- **`GroupView.kt`**: 메인 컴포저블 (58줄)
  - 그룹명 + 이미지 레이아웃 통합
  - buildBento5x3Items로 도메인 레이어 연결
  - 8dp 수직 간격으로 깔끔한 배치

### 3. 의존성 추가
- **Coil Compose**: `2.7.0` - 비동기 이미지 로딩
- **Foundation Layout**: `1.10.0` - Layout API 지원

## 🏗️ 아키텍처 설계

```
Domain Layer (순수 Kotlin)
└── 레이아웃 로직 및 데이터 구조
    ├── GroupItem (UI 데이터 모델)
    ├── LayoutBlock (레이아웃 패턴)
    └── GroupLayoutBuilder (패턴 생성)

Presentation Layer (Jetpack Compose)
└── UI 컴포넌트
    ├── GroupLayout (커스텀 레이아웃)
    ├── ImageCard (개별 카드)
    └── GroupView (통합 뷰)
```

**Clean Architecture 준수:**
- Domain Layer는 Android/Compose 의존성 없음
- Presentation은 Domain의 데이터만 사용
- 단방향 데이터 흐름 유지

## 🔗 관련 이슈
Closes #6

## 📸 스크린샷 (선택사항)


## 💬 리뷰어에게

**주요 검토 포인트:**

1. **레이아웃 알고리즘 (GroupLayout.kt)**
   - `canPlace`와 `mark` 함수의 그리드 충돌 방지 로직
   - 셀 크기 계산 방식 (정사각형 셀 기준)
   - 배치 순서에 따른 최적화 가능성

2. **패턴 설계 (GroupLayoutBuilder.kt)**
   - 7가지 레이아웃 패턴의 미적 균형
   - 오버플로우 처리 방식 (7개 초과)
   - 패턴 확장 가능성 (추후 다른 그리드 사이즈)

3. **성능 고려사항**
   - Coil의 이미지 캐싱 전략
   - Custom Layout의 recomposition 최적화
   - 대량 이미지 처리 시 메모리 관리

4. **향후 개선 방향**
   - 애니메이션 추가 (클릭, 전환)
   - 다양한 그리드 사이즈 지원 (3x3, 4x4 등)
   - 이미지 로딩 상태 처리
   - 에러 핸들링 및 플레이스홀더
