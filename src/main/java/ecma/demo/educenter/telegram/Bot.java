package ecma.demo.educenter.telegram;

import ecma.demo.educenter.telegram.tgService.MessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {
    private final MessageService messageService;

    public Bot(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public String getBotUsername() {
        return "sevn_plus_bot";
    }
//    public String getBotUsername() {
//        return "TesterBootBot";
//    }

    @Override
    public String getBotToken() {
        return "5205766434:AAFYGg3AhrLiuOvHuSGezNLsaavtu7Brnhw";
    }
//    public String getBotToken() {
//        return "1173193780:AAGIUlsOalrZ5IgfQ3eOw25Cruo9Ho6B7Nw";
//    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                SendMessage sendMessage = messageService.replyTo(update.getMessage());
                if (sendMessage.getText().endsWith("yo`qsiz.")) {
                    deleteClientMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId());
                }
                send(sendMessage);
            } else if (update.hasCallbackQuery()) {
                send(messageService.replyTo(update.getCallbackQuery()));

            } else if (update.hasInlineQuery()) {

            } else if (update.hasChosenInlineQuery()) {

            } else if (update.hasEditedMessage()) {

            } else {

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            deleteClientMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(SendMessage sendMessage) {
        try {
            if(sendMessage.getParseMode()==null)
                sendMessage.setParseMode(ParseMode.HTML);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteClientMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

