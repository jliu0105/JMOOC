package com.course.business.controller.web;

import com.alibaba.fastjson.JSON;
import com.course.server.dto.LoginMemberDto;
import com.course.server.dto.MemberDto;
import com.course.server.dto.ResponseDto;
import com.course.server.dto.SmsDto;
import com.course.server.enums.SmsUseEnum;
import com.course.server.exception.BusinessException;
import com.course.server.service.MemberService;
import com.course.server.service.SmsService;
import com.course.server.util.UuidUtil;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController("webMemberController")
@RequestMapping("/web/member")
public class MemberController {

    private static final Logger LOG = LoggerFactory.getLogger(MemberController.class);
    public static final String BUSINESS_NAME = "member";

    @Resource
    private MemberService memberService;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Resource
    private SmsService smsService;

    @PostMapping("/register")
    public ResponseDto register(@RequestBody MemberDto memberDto) {
        ValidatorUtil.require(memberDto.getMobile(), "phone_number");
        ValidatorUtil.length(memberDto.getMobile(), "phone_number", 11, 11);
        ValidatorUtil.require(memberDto.getPassword(), "password");
        ValidatorUtil.length(memberDto.getName(), "nickname", 1, 50);
        ValidatorUtil.length(memberDto.getPhoto(), "avatar_url", 1, 200);

        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));

        SmsDto smsDto = new SmsDto();
        smsDto.setMobile(memberDto.getMobile());
        smsDto.setCode(memberDto.getSmsCode());
        smsDto.setUse(SmsUseEnum.REGISTER.getCode());
        smsService.validCode(smsDto);
        LOG.info("SMS verification code passed!");

        ResponseDto responseDto = new ResponseDto();
        memberService.save(memberDto);
        responseDto.setContent(memberDto);
        return responseDto;
    }

    @PostMapping("/login")
    public ResponseDto login(@RequestBody MemberDto memberDto) {
        LOG.info("user log in start");
        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));
        ResponseDto responseDto = new ResponseDto();

        String imageCode = (String) redisTemplate.opsForValue().get(memberDto.getImageCodeToken());
        LOG.info("Verification code obtained from redis:{}", imageCode);
        if (StringUtils.isEmpty(imageCode)) {
            responseDto.setSuccess(false);
            responseDto.setMessage("verification code outdated");
            LOG.info("user log in failed, verification code outdated");
            return responseDto;
        }
        if (!imageCode.toLowerCase().equals(memberDto.getImageCode().toLowerCase())) {
            responseDto.setSuccess(false);
            responseDto.setMessage("verification is incorrect");
            LOG.info("user log in failed, verification code is incorrect");
            return responseDto;
        } else {
            redisTemplate.delete(memberDto.getImageCodeToken());
        }

        LoginMemberDto loginMemberDto = memberService.login(memberDto);
        String token = UuidUtil.getShortUuid();
        loginMemberDto.setToken(token);
        redisTemplate.opsForValue().set(token, JSON.toJSONString(loginMemberDto), 3600, TimeUnit.SECONDS);
        responseDto.setContent(loginMemberDto);
        return responseDto;
    }

    @GetMapping("/logout/{token}")
    public ResponseDto logout(@PathVariable String token) {
        ResponseDto responseDto = new ResponseDto();
        redisTemplate.delete(token);
        LOG.info("delete from redis token:{}", token);
        return responseDto;
    }

    @GetMapping(value = "/is-mobile-exist/{mobile}")
    public ResponseDto isMobileExist(@PathVariable(value = "mobile") String mobile) throws BusinessException {
        LOG.info("Start checking if phone number exists");
        ResponseDto responseDto = new ResponseDto();
        MemberDto memberDto = memberService.findByMobile(mobile);
        if (memberDto == null) {
            responseDto.setSuccess(false);
        } else {
            responseDto.setSuccess(true);
        }
        return responseDto;
    }

    @PostMapping("/reset-password")
    public ResponseDto resetPassword(@RequestBody MemberDto memberDto) throws BusinessException {
        LOG.info("Start resetting password:");
        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));
        ResponseDto<MemberDto> responseDto = new ResponseDto();

        SmsDto smsDto = new SmsDto();
        smsDto.setMobile(memberDto.getMobile());
        smsDto.setCode(memberDto.getSmsCode());
        smsDto.setUse(SmsUseEnum.FORGET.getCode());
        smsService.validCode(smsDto);
        LOG.info("SMS verification passed");

        memberService.resetPassword(memberDto);

        return responseDto;
    }
}
