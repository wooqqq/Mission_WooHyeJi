# 4Week_WooHyeJi.md

## Title: [4Week] 우혜지

### 미션 요구사항 분석 & 체크리스트

---
**[필수 미션]**
- [x] 네이버 클라우드 플랫폼을 통한 배포, 도메인, HTTPS 까지 적용
  - [x] `https://도메인/` 형태로 접속이 가능
  - [x] 운영서버에서 각종 소셜로그인, 인스타 아이디 연결이 잘 되어야 함
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링 기능 구현
  - [x] 내가 받은 호감리스트에서 특정 성별을 가진 사람에게서 받은 호감만 필터링해서 볼 수 있음

**[선택 미션]**
- [ ] 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동 배포 진행
  - [ ] 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포 진행
- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링 기능 구현
  - [x] 내가 받은 호감리스트에서 특정 호감사유의 호감만 필터링해서 볼 수 있음
- [ ] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬 기능
  - [ ] 아래 케이스대로 작동
    - 최신순(기본)
      - 가장 최근에 받은 호감표시를 우선적으로 표시
    - 날짜순
      - 가장 오래전에 받은 호감표시를 우선적으로 표시
    - 인기 많은 순
      - 가장 인기가 많은 사람들의 호감표시를 우선적으로 표시
    - 인기 적은 순
      - 가장 인기가 적은 사람들의 호감표시를 우선적으로 표시
    - 성별순
      - 여성에게 받은 호감표시를 먼저 표시하고, 그 다음 남자에게 받은 호감표시를 후에 표시
      - 2순위 정렬조건으로는 최신순
    - 호감 사유순
      - 외모 때문에 받은 호감표시를 먼저 표시, 그 다음 성격, 마지막으로 능력 때문에 받은 호감표시를 후에 표시
      - 2순위 정렬조건으로는 최신순



### 4주차 미션 요약

---

**[접근 방법]**
1. **배포 - 도메인, HTTPS 까지 적용**
  - https://www.lionlike.site 로 접속하면 원하는 기능 모두 적용되어 나타남

2. **내가 받은 호감리스트에서 성별 필터링 기능 구현**
  - 소셜로그인이 모두 본인 아이디(wooqq__)와 연결되어있어 호감표시를 따로 할 수 없음
    - NotProd.java에서 wooqq__에게 호감표시를 한 케이스 3가지 추가시킴
    - SQL에서 likeable_person 테이블 조회 시 추가된 것을 확인함
  - LikeablePersonService.java 에서 `listByGender`를 통해 기능 구현(`.stream()` 사용)
  - LikeablePersonController.java 에서 service 의 메서드를 이용

3. **내가 받은 호감리스트에서 호감사유 필터링 기능 구현**
  - 성별 필터링 기능을 구현한 `listByGender`를 `listByGroup`으로 변경 후 호감사유 필터링 기능 추가
  - LikeablePersonController.java 에도 `attractiveTypeCode`와 `sortCode` 추가
  - https://localhost 에서 호감사유에 따라 필터링 기능이 구현된 것을 확인함

4. **내가 받은 호감리스트에서 정렬 기능**
  - LikeablePersonService.java 에서 Stream 사용하여 정렬
  - 성별 별로 정렬을 해보았으나, 제대로 구현되지 않음



**[특이사항]**
- https://localhost 를 통해 성별 필터링 기능이 구현된 것을 확인했으나, 도메인(https://www.lionlike.site)으로 접속했을 때는 NotProd.java에 추가한 내용이 뜨지 않음
    
    => 멘토님께 여쭤본 결과, NotProd.java 는 test와 dev 환경에서 사용. 배포한 환경은 prod 환경으로 실행하는 중이기 때문에 NotProd.java에 추가한 정보가 뜨지 않는것이 맞았음

- 도메인에서는 프로그램이 보이지만, 인텔리제이 실행 후 https://localhost 로 확인하면 알 수 없는 에러가 종종 뜸
  (이전에도 이러한 에러가 보였던 적이 있지만 시간이 좀 지나고 정상적으로 뜸)
  - This application has no explicit mapping for /error, so you are seeing this as a fallback.
  - There was an unexpected error (type=Internal Server Error, status=500).
    An error happened during template parsing (template: "class path resource [templates/usr/member/me.html]")
    
  => 크롬에서 로그인 했던 세션이 남아있어서 에러가 났었음. 쿠키 삭제하니 바로 정상 작동


**[Refactoring]**
