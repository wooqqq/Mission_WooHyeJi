package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/likeablePerson")
@RequiredArgsConstructor
public class LikeablePersonController {
    private final Rq rq;
    private final LikeablePersonService likeablePersonService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like")
    public String showLike() {
        return "usr/likeablePerson/like";
    }

    @AllArgsConstructor
    @Getter
    public static class LikeForm {
        private final String username;
        private final int attractiveTypeCode;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like")
    public String like(@Valid LikeForm likeForm) {
        RsData<LikeablePerson> rsData = likeablePersonService.like(rq.getMember(), likeForm.getUsername(), likeForm.getAttractiveTypeCode());

        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/likeablePerson/list", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showList(Model model) {
        InstaMember instaMember = rq.getMember().getInstaMember();

        // 인스타인증을 했는지 체크
        if (instaMember != null) {
            List<LikeablePerson> likeablePeople = likeablePersonService.findByFromInstaMemberId(instaMember.getId());
            model.addAttribute("likeablePeople", likeablePeople);
        }

        return "usr/likeablePerson/list";
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        LikeablePerson likeablePerson = likeablePersonService.findById(id).orElse(null);

        RsData canActorDeleteRsData = likeablePersonService.canDelete(rq.getMember(), likeablePerson);

        if (canActorDeleteRsData.isFail()) return rq.historyBack(canActorDeleteRsData);

        RsData deleteRsData = likeablePersonService.delete(likeablePerson);

        if (deleteRsData.isFail()) return rq.historyBack(deleteRsData);

        // 호감상대 삭제 후 "/likeablePerson/list" 로 돌아가기
        return rq.redirectWithMsg("/likeablePerson/list", deleteRsData);
    }
}
