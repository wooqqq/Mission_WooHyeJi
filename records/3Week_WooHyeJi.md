# 3Week_WooHyeJi.md

## Title: [3Week] 우혜지

### 미션 요구사항 분석 & 체크리스트

---
**[필수 미션]**
- [x] 호감취소 및 호감사유 변경 쿨타임 적용
  - [x] 호감표시를 한 후 개별호감표시건에 대해서, 3시간동안은 호감취소와 호감사유 변경을 할 수 없도록 작업
  - [x] 호감사유 변경을 한 후 개별호감표시건에 대해서, 3시간동안은 호감취소와 호감사유 변경을 할 수 없도록 작업
- [x] 네이버 클라우드 플랫폼을 통한 배포 (도메인 없이, IP로 접속)
  - [x] `https://서버IP:포트/` 형태로 접속이 가능
  - [x] 운영서버에서는 각종 소셜 로그인, 인스타 아이디 연결이 안되어도 됨
- [x] 페이스북 로그인 기능 구현
- [x] 인스타그램 연동

**[선택 미션]**
- [ ] 알림기능 구현
  - [ ] 호감표시를 받았거나, 본인에 대한 호감사유가 변경된 경우 알림페이지에서 확인 가능
  - [ ] 각각의 알림은 `readDate`가 `null`이고, 사용자가 알림을 읽으면 `readDate`가 `현재날짜`로 세팅되어야 함



### 3주차 미션 요약

---

**[접근 방법]**
1. 호감취소 및 호감사유 변경 쿨타임 적용
    1) AppConfig.java에서 `setLikeablePersonModifyCoolTime` 메서드를 만들어줌
   2) application.yml에서 `likeablePerson`에 `modifyCoolTime`을 설정
      - 기본적으로 second 단위로 되어있기 때문에 3시간을 설정해주려면 `#{60 * 60 * 3}`으로 해야함
   3) Ut.java에 쿨타임 관련 메서드 적용
   4) 호감취소 및 호감사유 변경 시 잔여시간 표시
      - LikeablePerson.java에서 작업. `getModifyUnlockDateRemainStrHuman`으로 적용되어 있었음
      - `currentTime`을 현재 시간, `modifyUnlockDate`는 호감등록 시 저장된 시간으로 설정
      - `Duration.between(A, B)` 사용하여 duration에 잔여 시간 저장
        - `Duration.between(A, B)`는 A - B, 즉 A 시간과 B 시간 사이의 차이값을 구해줌
      - 시간, 분, 초로 나타내기 위해 `hours`, `minutes`, `seconds`에 각각 해당하는 값 저장
      - `result` 변수에 `"%d시간 %d분 %d초"` 저장 후 return
2. 네이버 클라우드 플랫폼을 통한 배포 (도메인 없이, IP로 접속)
   1) 1번 미션 구현 후 git으로 push 후 3Week 브랜치를 배포
   2) 배포용 데이터베이스를 생성 후 application-prod.yml과 연결
   3) 가비아 사이트에서 `lionlike.site` 도메인 구매
   4) DNSZi 사이트를 이용하여 도메인 관리
   5) www.lionlike.site 를 통해 배포 성공


**[특이사항]**
- 필수미션은 강사님 코드를 보고 진행하여 수월하게 할 수 있었음
- 이번 3주차 미션은 필수미션 구현뿐만 아니라 2주차 미션 이후 기능들을 스프링 이벤트로 따로 분리시키는 리팩토링 진행 
  
  => 따로 코드를 클론하지 않고 직접 작성하여서 시간이 많이 걸림
- 배포 과정에서 배포용 데이터베이스와 연결이 되지 않아 빌드가 된 상태지만 실행이 되지 않았었음
  
  => application-prod.yml에서 배포용 데이터베이스를 관리하는데, 이 파일에서 username과 password를 바꾸지 않아 발생한 일이었음. 이 부분을 수정하니 정상적으로 작동
- 2WeekFix 에서 스프링 이벤트로 분리하는 과정에서 GramgramApplication.java를 실행시키고 크롬에서 확인했을 때 인텔리제이에서의 실행은 정상적이었으나 크롬에서는 실행이 될 때도 있고 안될 때도 있었음 

  => 아직 원인을 찾지 못했음
- 배포 후 소셜 로그인 기능이 원활하게 작동되지 않아 다른 기능들도 제대로 구현이 되는지 확인이 불가한 상태

  => 카카오 로그인에 의하면 다음과 같이 에러 원인이 뜸

    ![image](https://user-images.githubusercontent.com/118504257/236112785-03fa2865-71e8-4e11-9c9d-018f24d3e094.png)

**[Refactoring]**
- 배포 후 www.lionlike.site 로 접속하면 소셜 로그인이 안되는 문제를 해결
  - 원인은 소셜 로그인을 시도했을 때 http://www.lionlike.site Redirect URI로 연결이 됨

    => http 가 아니라 https 로 변경이 되어야 함
  - `custom.site.baseUrl`을 https 로 설정해줌
  - https로 설정됨과 동시에 application.yml의 중복도 제거해줌