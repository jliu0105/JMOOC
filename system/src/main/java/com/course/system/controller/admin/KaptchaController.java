package com.course.system.controller.admin;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin/kaptcha")
public class KaptchaController {
    public static final String BUSINESS_NAME = "kaptcha";

    @Qualifier("getDefaultKaptcha")
    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Resource
    public RedisTemplate redisTemplate;

    @GetMapping("/image-code/{imageCodeToken}")
    public void imageCode(@PathVariable(value = "imageCodeToken") String imageCodeToken, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception{
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            String createText = defaultKaptcha.createText();

            redisTemplate.opsForValue().set(imageCodeToken, createText, 300, TimeUnit.SECONDS);

            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Define the response output type as image/jpeg type, use the response output stream to output the byte array of the picture
        byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
