package com.graduate.api.auth;


import com.graduate.api.auth.exceptions.AuthErrorEex;
import com.graduate.api.auth.service.AuthService;
import com.graduate.common.BaseJsonData;
import com.graduate.config.secruity.JwtAuthenticationRequest;
import com.graduate.config.secruity.JwtAuthenticationResponse;
import com.graduate.timer.MessageTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final  String tokenHeader = "Authorization" ;

    private final String tokenHead = "SAS";

    @Autowired
    private AuthService authService;

    @Autowired
    private MessageTask task;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseJsonData createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException{
        BaseJsonData<HashMap<String,Object>> data = new BaseJsonData<>();
        HashMap<String,Object> map = new HashMap<>();
        try {
            final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            data.setCode(1);
            data.setMsg("登陆成功");
            map.put("token",tokenHead+" "+token);
            data.setData(map);
        }catch (UsernameNotFoundException userNoFoundEex){
           data.setMsg(userNoFoundEex.getMessage());

        }catch (AuthErrorEex authErrorEex){
            data.setMsg("密码错误！");
        }
        data.setData(map);

        return data;

    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        if(!token.startsWith(tokenHead)){
            return  ResponseEntity.badRequest().body(null);
        }
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }


}
