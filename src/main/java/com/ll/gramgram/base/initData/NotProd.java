package com.ll.gramgram.base.initData;

import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.standard.util.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Value("${custom.security.oauth2.client.registration.kakao.devUserOauthId}")
    private String kakaoDevUserOAuthId;

    @Value("${custom.security.oauth2.client.registration.naver.devUserOauthId}")
    private String naverDevUserOAuthId;

    @Value("${custom.security.oauth2.client.registration.google.devUserOauthId}")
    private String googleDevUserOAuthId;

    @Value("${custom.security.oauth2.client.registration.facebook.devUserOauthId}")
    private String facebookDevUserOAuthId;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            InstaMemberService instaMemberService,
            LikeablePersonService likeablePersonService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Member memberAdmin = memberService.join("admin", "1234").getData();
                Member memberUser1 = memberService.join("user1", "1234").getData();
                Member memberUser2 = memberService.join("user2", "1234").getData();
                Member memberUser3 = memberService.join("user3", "1234").getData();
                Member memberUser4 = memberService.join("user4", "1234").getData();
                Member memberUser5 = memberService.join("user5", "1234").getData();
                Member memberUser11 = memberService.join("user11", "1234").getData();
                Member memberUser12 = memberService.join("user12", "1234").getData();
                Member memberUser13 = memberService.join("user13", "1234").getData();

                Member memberUser6ByKakao = memberService.whenSocialLogin("KAKAO", "KAKAO__%s".formatted(kakaoDevUserOAuthId)).getData();
                Member memberUser7ByGoogle = memberService.whenSocialLogin("GOOGLE", "GOOGLE__%s".formatted(googleDevUserOAuthId)).getData();
                Member memberUser8ByNaver = memberService.whenSocialLogin("NAVER", "NAVER__%s".formatted(naverDevUserOAuthId)).getData();
                Member memberUser9ByFacebook = memberService.whenSocialLogin("FACEBOOK", "FACEBOOK__%s".formatted(facebookDevUserOAuthId)).getData();

                instaMemberService.connect(memberUser2, "insta_user2", "M");
                instaMemberService.connect(memberUser3, "insta_user3", "W");
                instaMemberService.connect(memberUser4, "insta_user4", "M");
                instaMemberService.connect(memberUser5, "insta_user5", "W");
                instaMemberService.connect(memberUser11, "insta_user11", "W");
                instaMemberService.connect(memberUser12, "insta_user12", "M");
                instaMemberService.connect(memberUser13, "insta_user13", "W");


                // 원활한 테스트와 개발을 위해서 자동으로 만들어지는 호감이 삭제, 수정이 가능하도록 쿨타임해제
                LikeablePerson likeablePersonToInstaUser4 = likeablePersonService.like(memberUser3, "insta_user4", 1).getData();
                Ut.reflection.setFieldValue(likeablePersonToInstaUser4, "modifyUnlockDate", LocalDateTime.now().minusSeconds(1));
                LikeablePerson likeablePersonToInstaUser100 = likeablePersonService.like(memberUser3, "insta_user100", 2).getData();
                Ut.reflection.setFieldValue(likeablePersonToInstaUser100, "modifyUnlockDate", LocalDateTime.now().minusSeconds(1));
                LikeablePerson likeablePersonToInstaUserAbcd = likeablePersonService.like(memberUser3, "insta_user_abcd", 2).getData();

                // 나의 호감 목록 조회를 위해 추가
                likeablePersonService.like(memberUser2, "wooqq__", 1).getData();
                likeablePersonService.like(memberUser4, "wooqq__", 2).getData();
                likeablePersonService.like(memberUser5, "wooqq__", 3).getData();
                likeablePersonService.like(memberUser11, "wooqq__", 2).getData();
                likeablePersonService.like(memberUser12, "wooqq__", 3).getData();
                likeablePersonService.like(memberUser13, "wooqq__", 1).getData();
            }
        };
    }
}