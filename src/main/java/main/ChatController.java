package main;

import main.model.User;
import main.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@RestController
public class ChatController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(path = "/api/auth")
    public HashMap<String, Boolean> auth() {
        HashMap<String, Boolean> response = new HashMap<>();
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        response.put("result", user != null);
        return response;
    }

    @PostMapping(path = "/api/users")
    public HashMap<String, Boolean> adduser(HttpServletRequest request) {
        String name = request.getParameter("name");
        String sessionId = getSessionId();

        User user = new User();
        user.setName(name);
        user.setRegTime(new Date());
        user.setSessionId(sessionId);

        userRepository.save(user);

        HashMap<String, Boolean> response = new HashMap<>();
        response.put("result", true);
        return response;
    }

    private String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
