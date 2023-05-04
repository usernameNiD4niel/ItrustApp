package com.rey.itrustapplication.userchatbot;

public class ChatBotStarlaProfileModel {

    String answer, question, requestedQuestion;
    boolean hasAnswer;

    public ChatBotStarlaProfileModel(String answer, String question) {
        this.answer = answer;
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public ChatBotStarlaProfileModel(String requestedQuestion, boolean hasAnswer) {
        this.requestedQuestion = requestedQuestion;
        this.hasAnswer = hasAnswer;
    }

    public boolean getHasAnswer(){return hasAnswer;}

    public String getRequestedQuestion() {
        return requestedQuestion;
    }

    public String getQuestion() {
        return question;
    }
}
