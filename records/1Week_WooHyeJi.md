# 1Week_WooHyeJi.md

## Title: [1Week] 우혜지

### 미션 요구사항 분석 & 체크리스트

---
- [x] [필수] 호감상대 삭제
    - [x] 삭제 버튼을 눌렀을 시 해당 항목은 삭제되어야 한다.
    - [x] 삭제를 처리하기 전 해당 항목에 대한 소유권이 본인(로그인한 사람)에게 있는지 체크해야한다.
    - [x] 삭제 후 다시 호감목록 페이지로 돌아와야 한다. (`rq.redirectWithMsg` 사용)
- [x] [선택] 구글 로그인
    - [x] 구글 로그인으로 가입 및 로그인 처리가 가능하여야 한다. (스프링 OAuth2 클라이언트)
    - [x] 구글 로그인으로 가입한 회원의 providerTypeCode : GOOGLE

### 1주차 미션 요약

---

**[접근 방법]**
1. **호감상대 삭제**
    - 호감목록에서 삭제하려고 하는 항목은 LikeablePersonRepository 의 id 를 기준으로 찾야야한다.
        - LikeablePersonService 에서 `findById` 함수를 통해 해당 repository 에서 id를 가져온다.
    - LikeablePersonService 에서 `delete` 함수를 만든다.
        - 존재하지 않는 항목을 삭제하려고 했을 때 실패
        - 로그인한 사람에게 삭제 권한이 없는 경우 실패
    - LikeablePersonController 에서 해당 항목의 삭제버튼을 눌러 `"/delete/{id}"` 경로로 들어갔을 때 delete 구현
        - `"/delete/{id}"` 에서 id는 LikeablePersonRepository 에서 가져온 id
        - 호감상대 삭제가 실패한다면 `rq.historyBack` 을 통해 돌아간다.
    - 호감상대를 삭제한 후 `rq.redirectWithMsg` 를 통해 "/likeablePerson/list"로 돌아간다.
2. **구글 로그인**
    - 아직 구현하지 못함

**[특이사항]**
- 호감상대 삭제에 대한 테스트케이스는 실행되지만 GramgramApplication을 실행했을 때에는 list에서 삭제가 되지 않는 오류가 발생.
    
    -> LikeablePersonService.java 의 delete에 `@Transactional` 추가하니 삭제 가능 


**[Refactoring]**
1. **호감상대 삭제**
    - 호감취소 관련 변수명 정리
      - `RsData` -> `deleteRsData`
    - 호감상대 등록시 예외처리 케이스 메서드 새로 만들어서 분리
    - `@OneToMany` 사용하여 내가(or나를) 좋아하는 사람의 명단 양방향 관계 설정
      - 부작용이 생겨 오류 수정 (호감표시 페이지 작동 X)
    - 테스트케이스(t6, t7, t8) 수정
2. **카카오/구글 로그인**
    - 기존 카카오 clientId가 git에 노출됨
        -> application-secret.yml 새로 생성하여 정보 이동
    - 구글 프로젝트 생성 후 application.yml에 추가
        -> 카카오 로그인과 마찬가지로 clientId와 비밀번호는 application-secret.yml에서 관리
    - 협업 시 application-secret.yml은 git에 노출되지 않음
        -> 폼 제공을 위해 applicaion-secret.yml.default 문서 추가