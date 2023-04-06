/*
Name: Xing Yang Goh
ID: 1001969
 */
package com.project.application.common;

public class ServerError {
    public enum ErrorCodes {
        WORD_NOT_PROVIDED,
        WORD_NOT_FOUND,
        INVALID_WORD_FORMAT,
        MEANING_NOT_PROVIDED,
        WORD_ALREADY_EXISTS,
        UNEXPECTED_OPERATION
    }

    public static String ErrorResponse(String error) {
        switch (error) {
            case "WORD_NOT_PROVIDED":
                return "No word specified to the operation";
            case "WORD_NOT_FOUND":
                return "Given word does not exist in the dictionary";
            case "INVALID_WORD_FORMAT":
                return "Given word is in an invalid format";
            case "MEANING_NOT_PROVIDED":
                return "No meaning provided for the given word";
            case "WORD_ALREADY_EXISTS":
                return "Given word already exists in the dictionary";
            case "UNEXPECTED_OPERATION":
                return "Unexpected operation provided";
            default:
                return "Unexpected error code provided";
        }
    }
}
