package ecma.demo.educenter.telegram.tgService;

import ecma.demo.educenter.behavior.CRUDable;
import ecma.demo.educenter.behavior.Readable;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.payload.ReqStudent;
import ecma.demo.educenter.projections.ResGroupsWithStudentsBalance;
import ecma.demo.educenter.projections.ResStudentWithBalance;
import ecma.demo.educenter.service.GroupService;
import ecma.demo.educenter.service.StudentService;
import ecma.demo.educenter.service.UserService;
import ecma.demo.educenter.telegram.storehouse.Constant;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

@Service
public class MessageService {
    private final Map<Long, User> userChatId = new HashMap<>();
    private final Map<Long, Constant> state = new HashMap<>();
    private final Map<Long, UUID> groupId = new HashMap<>();
    private final Map<Long, ResGroupsWithStudentsBalance> resGroup = new HashMap<>();
    private final ButtonService buttonService;

    private final UserService userService;
    private final Readable groupService;
    private final CRUDable studentService;

    public MessageService(UserService userService, ButtonService buttonService, GroupService groupService, StudentService studentService) {
        this.userService = userService;
        this.buttonService = buttonService;
        this.groupService = groupService;
        this.studentService = studentService;
    }

    public SendMessage replyTo(Message message) {
        Long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("");

        if (message.hasText()) {
            String text = message.getText();
            if (text.equals("/start")) {
                sendMessage.setReplyMarkup(buttonService.contactButton());
                sendMessage.setText("Botdan foydalanish uchun telefon raqamingiz zarur bo`ladi!");
                this.state.put(chatId, Constant.START);
            } else if (this.state.get(chatId).equals(Constant.GROUP)) {
                sendGroup(sendMessage, text, chatId);
            } else if (this.state.get(chatId).equals(Constant.SAVE_NEW_STUDENT)) {
                addStudents(chatId, text);
                this.state.put(chatId, Constant.GROUP);
                sendGroup(sendMessage, String.valueOf(this.groupId.get(chatId)), chatId);
            }
        } else if (message.hasContact()) {
            logInWithPhoneNumber(sendMessage, chatId, message.getContact().getPhoneNumber());
        } else if (message.hasPhoto()) {
            sendMessage.setText("Rasmlar bilan ishlash funksiyasi hozircha ishga tushirilmagan.");
        } else if (message.hasReplyMarkup()) {
            sendMessage.setText("Noto`g`ri xabar!");
            sendMessage.setReplyToMessageId(message.getMessageId());
        } else {
            sendMessage.setText("Noto`g`ri xabar!");
            sendMessage.setReplyToMessageId(message.getMessageId());
        }
        return sendMessage;
    }

    private void logInWithPhoneNumber(SendMessage sendMessage, Long chatId, String phoneNumber) {
        phoneNumber = (!phoneNumber.startsWith("+") ? "+" : "") + phoneNumber;
        User user = getUser(phoneNumber);
        if (user != null) {
            userChatId.put(chatId, user);

            sendMessage.setText("<b>" + user.getLastName() + " " +
                    user.getFirstName() + "</b>" +
                    " sizni yana bir bor ko`rib turganimdan bag`oyatda xursandman!");
            sendMessage.setReplyMarkup(buttonService.homeButton());
        } else {
            sendMessage.setText("Siz foydalanuvchilar ro`yhatida yo`qsiz.");
        }
    }

    private User getUser(String phoneNumber) {
        try {
            return userService.getByPhoneNumber(phoneNumber);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private void sendGroup(SendMessage sendMessage, String data, Long chatId) {
        ApiResponse answerList = groupService.read(null, true);
        List<ResGroupsWithStudentsBalance> groupList = (List<ResGroupsWithStudentsBalance>) answerList.getObject();
        for (ResGroupsWithStudentsBalance group : groupList) {
            if (group.getName().equals(data) || String.valueOf(group.getId()).equals(data)) {
                sendMessage.setReplyMarkup(buttonService.studentsKeyboard(group.getStudents(), group.getId().toString()));
                sendMessage.setText(group.getName());
                this.resGroup.put(chatId, group);
                break;
            }
        }
    }

    private void addStudents(Long chatId, String text) {
        ReqStudent reqStudent;
        List<UUID> groupIdList;
        String[] students = text.split("\n\n");
        for (String student : students) {
            String[] studentInfo = student.split("\n");
            if (student.length() < 2) throw new ArrayIndexOutOfBoundsException();
            reqStudent = new ReqStudent();

            reqStudent.setLastName(studentInfo[0]);
            reqStudent.setFirstName(studentInfo[1]);
            reqStudent.setPhoneNumber(studentInfo.length > 2 ? studentInfo[2] : "");
            reqStudent.setParentsNumber(studentInfo.length > 3 ? studentInfo[3] : "");
            reqStudent.setAddress(studentInfo.length > 4 ? studentInfo[4] : "");

            groupIdList = new ArrayList<>();
            groupIdList.add(this.groupId.get(chatId));
            reqStudent.setGroupIdList(groupIdList);
            studentService.create(reqStudent);
        }
    }

    public SendMessage replyTo(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());

        String data = callbackQuery.getData();
        if (data.equals("GROUPS")) {
            ApiResponse answerList = groupService.read(null, true);
            List<ResGroupsWithStudentsBalance> groupList = (List<ResGroupsWithStudentsBalance>) answerList.getObject();
            sendMessage.setReplyMarkup(buttonService.groupsKeyboard(groupList));
            sendMessage.setText("Kerakli guruhni tanlang");

            this.state.put(chatId, Constant.GROUP);
        } else if (data.equals("SEARCH")) {
            sendMessage.setText("Bu tugma hozircha aktivlashtirilmagan.");
        } else if (data.endsWith(Constant.NEW_STUDENT.name())) {
            data = data.split(" ")[0];
            this.groupId.put(chatId, UUID.fromString(data));
            this.state.put(chatId, Constant.SAVE_NEW_STUDENT);

            sendMessage.enableHtml(true);
            sendMessage.setText("\uD83D\uDC47 <b>Ma'lumotlarni quyidagi tartibda kiriting</b> \uD83D\uDC47 \n" +
                    "<b>Familiya: </b>\n" +
                    "<b>Ism: </b>\n" +
                    "<b>Telefon raqami: </b>\n" +
                    "<b>Ota-onasining tel raqami: </b>\n" +
                    "<b>Manzil: </b>");
        } else if (this.state.get(chatId).equals(Constant.GROUP)) {
            try {
                UUID studentId = UUID.fromString(data);
                for (ResStudentWithBalance student : resGroup.get(chatId).getStudents()) {
                    if (student.getId().equals(studentId)) {
                        sendMessage.setText(
                                student.getLastName() + "\n" +
                                        student.getFirstName() + "\n" +
                                        (student.getPhoneNumber() != null ? student.getPhoneNumber() + "\n" : "") +
                                        (student.getParentsNumber() != null ? student.getParentsNumber() + "\n" : "") +
                                        (student.getAddress() != null ? student.getAddress() + "\n" : "") +
                                        (student.getBalance() != null ? student.getBalance() : ""));
                        sendMessage.setReplyMarkup(buttonService.selectedStudent());
                        break;
                    }
                }

            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
            }
        }
        return sendMessage;
    }
}
