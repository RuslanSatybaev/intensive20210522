package main;

import main.model.Message;
import main.model.User;
import main.repos.MessageRepository;
import main.repos.UserRepository;
import main.response.AddMessageResponse;
import main.response.AuthResponse;
import main.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

    @PostMapping(path = "/users")
    public HashMap<String, Boolean> addUser(HttpServletRequest request) {
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

    @GetMapping(path = "/users")
    public Iterable<User> showUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/messages")
    public AddMessageResponse addMessage(HttpServletRequest request) {
        String text = request.getParameter("text");

        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);

        Date time = new Date();
        Message message = new Message();
        message.setTime(time);
        message.setUser(user);
        message.setMessage(text);
        messageRepository.save(message);

        AddMessageResponse response = new AddMessageResponse();
        response.setResult(true);
        response.setTime(formatter.format(time));
        return response;
    }

    @GetMapping(path = "/auth")
    public AuthResponse auth() {
        AuthResponse response = new AuthResponse();
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        response.setResult(user != null);
        if (user != null) {
            response.setName(user.getName());
        }
        return response;
    }

    @GetMapping(path = "/messages")
    public List<MessageResponse> getMessages() {
        ArrayList<MessageResponse> messagesList = new ArrayList<>();
        Iterable<Message> messages = messageRepository.findAll();
        for (Message message : messages) {
            MessageResponse messageItem = new MessageResponse();
            messageItem.setName(message.getUser().getName());
            messageItem.setTime(formatter.format(message.getTime()));
            messageItem.setText(message.getMessage());
            messagesList.add(messageItem);
        }
        return messagesList;
    }

    private String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
