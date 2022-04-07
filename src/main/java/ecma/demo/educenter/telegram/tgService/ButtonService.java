package ecma.demo.educenter.telegram.tgService;

import ecma.demo.educenter.projections.ResGroupsWithStudentsBalance;
import ecma.demo.educenter.projections.ResStudentWithBalance;
import ecma.demo.educenter.telegram.storehouse.Constant;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ButtonService {
    public InlineKeyboardMarkup homeButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> list = new ArrayList<>();

        InlineKeyboardButton groupBtn = new InlineKeyboardButton();
        InlineKeyboardButton searchBtn = new InlineKeyboardButton();

        groupBtn.setText("Guruhlar");
        groupBtn.setCallbackData("GROUPS");

        searchBtn.setText("Izlash");
        searchBtn.setCallbackData("SEARCH");

        list.add(groupBtn);
        list.add(searchBtn);

        lists.add(list);

        inlineKeyboardMarkup.setKeyboard(lists);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboardMarkup contactButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("Share Contact...");
        button.setRequestContact(true);
        row.add(button);
        rows.add(row);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup groupsKeyboard(List<ResGroupsWithStudentsBalance> groups) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton btn;

        for (ResGroupsWithStudentsBalance group : groups) {
            btn = new KeyboardButton();
            btn.setText(group.getName());
            row.add(btn);
            if (row.size() == 3) {
                rows.add(row);
                row = new KeyboardRow();
            }
        }
        rows.add(row);
        row = new KeyboardRow();
        btn = new KeyboardButton();
        btn.setText("➕ Yangi guruh qo`shish ➕");
        row.add(btn);
        rows.add(row);
        replyKeyboardMarkup.setInputFieldPlaceholder("Salom");
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup studentsKeyboard(List<ResStudentWithBalance> students, String groupId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> list;
        InlineKeyboardButton btn;

        for (ResStudentWithBalance student : students) {
            list = new ArrayList<>();
            btn = new InlineKeyboardButton();
            btn.setText(student.getLastName() + " " + student.getFirstName() + (
                    student.getBalance() != null && student.getBalance() < 0 ? " \uD83D\uDD34" : " \uD83D\uDFE2"));
            btn.setCallbackData(student.getId().toString());
            list.add(btn);
            lists.add(list);
        }


        list = new ArrayList<>();
        btn = new InlineKeyboardButton();
        btn.setText("➕ Yangi o`quvchi qo`shish ➕");
        btn.setCallbackData(groupId + " "+ Constant.NEW_STUDENT);
        list.add(btn);
        lists.add(list);
        inlineKeyboardMarkup.setKeyboard(lists);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup myButton(String text, String query) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsList = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(query);
        buttonsList.add(button);
        rows1.add(buttonsList);
        inlineKeyboardMarkup.setKeyboard(rows1);
        return inlineKeyboardMarkup;
    }

//        public InlineKeyboardMarkup oneRowButtons(List<ReqOneRowButtons> reqOneRowButtons){
//            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//            List<List<InlineKeyboardButton>> rows1 = new ArrayList<>();
//            List<InlineKeyboardButton> buttonsList=  new ArrayList<>();
//            for (ReqOneRowButtons reqOneRowButton : reqOneRowButtons) {
//                InlineKeyboardButton button = new InlineKeyboardButton();
//                button.setText(reqOneRowButton.getText());
//                button.setCallbackData(reqOneRowButton.getQuery());
//                buttonsList.add(button);
//            }
//            rows1.add(buttonsList);
//            inlineKeyboardMarkup.setKeyboard(rows1);
//            return inlineKeyboardMarkup;
//        }
}
