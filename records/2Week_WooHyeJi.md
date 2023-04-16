# 2Week_WooHyeJi.md

## Title: [2Week] 우혜지

### 미션 요구사항 분석 & 체크리스트

---
- [x] [필수] 호감표시 할 때 에외처리 케이스 3가지 처리
    - [x] 한명의 인스타 회원이 다른 인스타회원에게 중복으로 호감표시를 할 수 없습니다.
        - ex) user1이 user2에게 호감 표시 (사유 : 외모)
          user1이 user2에게 다시 호감 표시 (사유 : 외모)
          이 경우 처리되면 안된다. (`rq.historyBack`)
    - [x] 한명의 인스타 회원이 11명 이상의 호감상대를 등록할 수 없습니다.
        - 만일 11명 이상의 호감표시를 했을 시 처리되면 안된다. (`rq.historyBack`)
    - [x] 중복 호감표시가 발생했을 때 기존의 사유와 다른 사유로 호감을 표시하는 경우에는 성공으로 처리한다.
        - ex) user1이 user2에게 호감 표시 (사유 : 외모)
          user1이 user2에게 다시 호감 표시 (사유 : 성격)
          이 경우에는 새 호감상대로 등록되지 않고, 기존 호감표시에서 사유만 수정된다.
- [ ] [선택] 네이버 로그인
    - [x] 네이버 로그인으로 가입 및 로그인 처리가 가능하여야 한다. (스프링 OAuth2 클라이언트)
    - [x] 네이버 로그인으로 가입한 회원의 providerTypeCode : NAVER
    - [ ] SQL 내 member 테이블의 username에 개인의 이름과 성별 등의 개인정보는 포함하지 않아야 한다.
- [ ] [UI] 디자인 변경


### 2주차 미션 요약

---

**[접근 방법]**
1. **예외처리 케이스 3가지 추가**
    1) 호감표시 중복 불가능
        - LikeablePersonService.java 의 `like` 메소드에서 처리했던 호감표시 가능 여부 확인 기능을 `canLike` 메소드로 따로 분리시켜줌
        - LikeablePersonService.java 의 `like` 메소드에서 `stream().anyMatch()`를 통해 확인 후 이미 호감표시된 사용자일 경우 실패 처리
    2) 한명의 인스타 회원이 11명 이상의 호감표시 등록 불가능
        - LikeablePersonService.java 의 `like` 메소드 안에서 `fromInstaMember.getFromLikeablePeople().size()`로 확인
        - 예상은 `fromInstaMember.getFromLikeablePeople().size()`가 11 이상일 때 실패가 떠야하는데, `>= 11`로 조건을 설정하면 11개도 등록이 가능함
        - 임시방편으로 `>= 10`으로 설정해놓았으나, 아직 풀리지 않음
    3) 호감표시 중복 발생 시 다른 attractiveTypeCode 가질 시 성공 처리 -> 수정
        - 1)에서 확인했던 호감표시 중복 여부를 체크하는 기능을 LikeablePerson 속성의 `existingLikeablePerson`으로 만들어줌
        - 여기에서 `existingLikeablePerson`이 `null`이 아니라면 이미 호감표시가 완료된 사용자라는 의미
        - 위와 같은 상황을 조건으로 둔 후 true 라면 `attractiveTypeCode`가 다른지 확인
        - 다르다면 기존의 값이 아니라 새로 입력받은 `attractiveTypeCode` 값을 save 시킴
        - 만일 `attractiveTypeCode`가 같다면 이미 호감표시를 했던 동일한 likeablePerson이 존재하므로 실패 처리

2. **네이버 로그인**
    1) 네이버 개발자 센터(https://developers.naver.com/main/)에서 내 애플리케이션 등록
    2) application.yml에 네이버 registration 및 provider 작성
    3) application-secret.yml에 네이버 clientId 및 client-secret 작성
    4) application-secret.yml.default에 네이버 클라이언트 폼 작성


    

**[특이사항]**
1. 구현하지 못한 부분
    - 네이버 로그인 기능을 구현하였으나 SQL의 `member` 테이블의 `username`에 개인의 이름, 성별 등의 개인정보가 포함되지 않아야한다는 조건을 구현하지 못했음
    - 아직 네이버 로그인 기능이 완벽하게 해결되지 않았다고 생각하여 NotProd.java에 네이버 로그인 정보에 관한 부분은 추가하지 않음
    - 수업시간을 토대로 UI를 변경해보고 싶었으나 미션 수행 기간 내에는 시간이 부족하다고 생각이 들어 3주차 미션으로 넘어가기 전 리팩토링 때 구현해봐야 할 것 같음


2. 기능 구현은 마쳤으나 이해되지 않는 부분
    - 호감표시 인원수 제한에서 왜 `>= 11`이 아니라 `>= 10`으로 해야 10명까지만 등록이 가능한지?

**[Refactoring]**
1. 호감표시 중복 관련 테스트케이스 추가
    - LikeablePersonServiceTests.java 새로 생성하여 service 관련 테스트케이스 다룸(t1, t2)
2. 인스타회원 호감표시 최대 인원수를 10명으로 제한
    - application.yml에 호감표시 max 값을 지정하고 AppConfig를 통해 제한하기 위해 AppConfig.java 생성
    - LikeablePersonService.java의 canLike 메소드에 호감표시 가능여부 확인 기능 추가 및 수정
      - AppConfig를 사용하여 `likeablePersonFromMax` 값을 통해 호감표시 최대 인원수 제한
    - LikeablePersonControllerTests.java 에 테스트케이스 추가
3. 호감표시 중복이지만 attractiveTypeCode가 다른 경우 수정 기능
    - LikeablePersonService.java 에서 modifyAttractive 메소드 추가