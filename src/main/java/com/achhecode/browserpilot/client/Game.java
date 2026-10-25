package com.achhecode.browserpilot.client;

public enum Game {

    ZIP(
            "ZIP",
            "/api/pinpointanswer/zip/solve",
            "/api/linkedin-games/zip/solve"
    ),
    WEND(
            "WEND",
            "/api/pinpointanswer/wend/solve",
            "/api/linkedin-games/wend/solve"
    ),
    TANGO(
            "TANGO",
            "/api/pinpointanswer/tango/solve",
            "/api/linkedin-games/tango/solve"
    ),
    QUEENS(
            "QUEENS",
            "/api/pinpointanswer/queens/solve",
            "/api/linkedin-games/queens/solve"
    ),
    PATCHES(
            "PATCHES",
            "/api/pinpointanswer/patches/solve",
            "/api/linkedin-games/patches/solve"
    ),
    MINI_SUDOKU(
            "MINI SUDOKU",
            "/api/pinpointanswer/mini-sudoku/solve",
            "/api/linkedin-games/mini-sudoku/solve"
    );

    private final String displayName;
    private final String pinpointPath;
    private final String linkedinPath;

    Game(String displayName, String pinpointPath, String linkedinPath) {
        this.displayName = displayName;
        this.pinpointPath = pinpointPath;
        this.linkedinPath = linkedinPath;
    }

    public String getPinpointPath() {
        return pinpointPath;
    }

    public String getLinkedinPath() {
        return linkedinPath;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
