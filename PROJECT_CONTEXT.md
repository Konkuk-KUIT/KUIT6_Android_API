# 프로젝트 컨텍스트 및 개발 가이드라인

## 브랜치 구조

### practice-only 브랜치
- **의미**: 미션 반영 전 상태
- **내용**: 강의자가 실습한 코드를 그대로 구현한 것
- **역할**: 기준점(baseline)으로 사용
- **중요**: 이 브랜치의 코드 스타일과 패턴을 크게 벗어나지 않아야 함

### JeongIlhyuk/week7 브랜치
- **의미**: 미션 반영 후 상태
- **내용**: 미션 요구사항을 반영한 코드
- **역할**: 미션 완료 버전

---

## 미션 요구사항 (체크리스트)

- [x] 수동 주입(App Container) 구현
- [x] Repository 패턴 사용
- [x] ViewModel 분리
    - PostEditViewModel
    - PostDetailViewModel (삭제까지 같이 하시면 됩니다)
    - PostCreateViewModel
- [x] UiState 구현
- [x] PostViewModel 삭제
- [x] PostListViewModel의 refresh 함수를 메인화면에서 sideEffect로 항상 실행시키기

---

## 개발 원칙

### ⚠️ 중요 사항
1. **미션 요구사항에 벗어나는 불필요한 변경 금지**
   - 미션에서 요구하지 않은 변경사항은 만들지 않기
   - 예: StateFlow → mutableStateOf 변경은 미션 요구사항이 아님 (단순히 선택된 방식)

2. **practice-only 브랜치의 코드 스타일 유지**
   - 강의자가 실습한 코드의 패턴과 스타일을 존중
   - 불필요한 리팩토링이나 스타일 변경 지양

3. **변경사항 검증**
   - 모든 변경사항이 미션 요구사항과 직접적으로 연관되어 있는지 확인
   - practice-only 브랜치와 비교하여 불필요한 차이점이 없는지 확인

---

## 브랜치 비교 시 주의사항

### 비교 방법
```bash
# practice-only (미션 전) vs JeongIlhyuk/week7 (미션 후)
git diff practice-only JeongIlhyuk/week7
```

### 확인해야 할 사항
1. 변경사항이 미션 요구사항과 직접 연관이 있는가?
2. practice-only의 코드 스타일을 크게 벗어나지 않는가?
3. 불필요한 리팩토링이나 개선이 포함되어 있지 않은가?

---

## 참고사항

- practice-only 브랜치는 강의자의 실습 코드이므로, 이를 기준으로 미션만 반영해야 함
- 미션 요구사항 외의 "개선"이나 "최적화"는 지양
- 코드 스타일, 네이밍, 구조는 practice-only 브랜치를 따르는 것을 원칙으로 함

