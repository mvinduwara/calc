package com.calcapp.ui.theme;

import javafx.scene.Scene;

import java.util.Objects;

public class ThemeManager {

    public enum Theme { DARK, LIGHT }

    private static Theme current = Theme.DARK;

    public static void apply(Scene scene, Theme theme) {
        current = theme;
        scene.getStylesheets().removeIf(s -> s.contains("theme-"));
        String cssFile = theme == Theme.DARK ? "theme-dark.css" : "theme-light.css";
        String resource = Objects.requireNonNull(
                ThemeManager.class.getResource("/com/calcapp/ui/styles/" + cssFile)
        ).toExternalForm();
        scene.getStylesheets().add(resource);
    }

    public static Theme getCurrent() { return current; }
}