package com.course.business.controller.web;

import com.course.server.dto.MemberCourseDto;
import com.course.server.dto.ResponseDto;
import com.course.server.service.MemberCourseService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("webMemberCourseController")
@RequestMapping("/web/member-course")
public class MemberCourseController {

    private static final Logger LOG = LoggerFactory.getLogger(MemberCourseController.class);
    public static final String BUSINESS_NAME = "member course sign up";

    @Resource
    private MemberCourseService memberCourseService;

    @PostMapping("/enroll")
    public ResponseDto enroll(@RequestBody MemberCourseDto memberCourseDto) {
        ValidatorUtil.require(memberCourseDto.getMemberId(), "member id");
        ValidatorUtil.require(memberCourseDto.getCourseId(), "courseid");

        ResponseDto responseDto = new ResponseDto();
        memberCourseDto = memberCourseService.enroll(memberCourseDto);
        responseDto.setContent(memberCourseDto);
        return responseDto;
    }

    @PostMapping("/get-enroll")
    public ResponseDto getEnroll(@RequestBody MemberCourseDto memberCourseDto) {
        ResponseDto responseDto = new ResponseDto();
        memberCourseDto = memberCourseService.getEnroll(memberCourseDto);
        responseDto.setContent(memberCourseDto);
        return responseDto;
    }
}
