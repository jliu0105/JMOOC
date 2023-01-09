package com.course.system.controller.admin;

import com.course.server.dto.PageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.dto.RoleDto;
import com.course.server.service.RoleService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class RoleController {

    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);
    public static final String BUSINESS_NAME = "role";

    @Resource
    private RoleService roleService;

    @PostMapping("/list")
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        roleService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto save(@RequestBody RoleDto roleDto) {
        ValidatorUtil.require(roleDto.getName(), "role");
        ValidatorUtil.length(roleDto.getName(), "role", 1, 50);
        ValidatorUtil.require(roleDto.getDesc(), "description");
        ValidatorUtil.length(roleDto.getDesc(), "description", 1, 100);

        ResponseDto responseDto = new ResponseDto();
        roleService.save(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        roleService.delete(id);
        return responseDto;
    }

    @PostMapping("/save-resource")
    public ResponseDto saveResource(@RequestBody RoleDto roleDto) {
        LOG.info("save role resource association start");
        ResponseDto<RoleDto> responseDto = new ResponseDto<>();
        roleService.saveResource(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    @GetMapping("/list-resource/{roleId}")
    public ResponseDto listResource(@PathVariable String roleId) {
        LOG.info("loading resource start");
        ResponseDto responseDto = new ResponseDto<>();
        List<String> resourceIdList = roleService.listResource(roleId);
        responseDto.setContent(resourceIdList);
        return responseDto;
    }

    /**
     * @param roleDto
     */
    @PostMapping("/save-user")
    public ResponseDto saveUser(@RequestBody RoleDto roleDto) {
        LOG.info("save role user association start");
        ResponseDto<RoleDto> responseDto = new ResponseDto<>();
        roleService.saveUser(roleDto);
        responseDto.setContent(roleDto);
        return responseDto;
    }

    /**
     * @param roleId
     */
    @GetMapping("/list-user/{roleId}")
    public ResponseDto listUser(@PathVariable String roleId) {
        LOG.info("start listing user");
        ResponseDto responseDto = new ResponseDto<>();
        List<String> userIdList = roleService.listUser(roleId);
        responseDto.setContent(userIdList);
        return responseDto;
    }
}
