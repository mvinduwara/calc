package com.calcapp.ui;

import com.calcapp.core.Calculator;
import com.calcapp.core.HistoryManager;
import com.calcapp.core.MemoryStore;
import com.calcapp.core.model.CalculationResult;
import com.calcapp.core.model.HistoryEntry;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Label expressionLabel;
    @FXML private Label resultLabel;
    @FXML private Label memoryIndicator;
    @FXML private Label modeLabel;
    @FXML private VBox scientificPanel;
    @FXML private VBox historyPanel;
    @FXML private VBox historyList;
    @FXML private ScrollPane historyScroll;
    @FXML private Button toggleScientificBtn;
    @FXML private Button toggleHistoryBtn;
    @FXML private Button degRadBtn;

    private final Calculator calculator = new Calculator();
    private final HistoryManager historyManager = new HistoryManager();
    private final MemoryStore memoryStore = new MemoryStore();
    private final DecimalFormat df = new DecimalFormat("#.##########");

    private String currentInput = "";
    private String expression = "";
    private boolean justCalculated = false;
    private boolean scientificVisible = false;
    private boolean historyVisible = false;
    private boolean isDegrees = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resultLabel.setText("0");
        expressionLabel.setText("");
        memoryIndicator.setVisible(false);
        updateModeLabel();
    }

    public void initKeyboardShortcuts(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        String text = event.getText();

        if (text.matches("[0-9]")) {
            appendDigit(text);
        } else if (text.equals(".")) {
            appendDecimal();
        } else if (text.equals("+")) {
            appendOperator("+");
        } else if (text.equals("-")) {
            appendOperator("−");
        } else if (text.equals("*")) {
            appendOperator("×");
        } else if (text.equals("/")) {
            event.consume();
            appendOperator("÷");
        } else if (text.equals("%")) {
            handlePercent();
        } else if (code == KeyCode.ENTER || code == KeyCode.EQUALS) {
            calculate();
        } else if (code == KeyCode.BACK_SPACE) {
            backspace();
        } else if (code == KeyCode.ESCAPE) {
            allClear();
        }
    }

    @FXML
    private void handleDigit(javafx.event.ActionEvent e) {
        Button btn = (Button) e.getSource();
        animateButton(btn);
        appendDigit(btn.getText());
    }

    @FXML
    private void handleDecimal(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        appendDecimal();
    }

    @FXML
    private void handleOperator(javafx.event.ActionEvent e) {
        Button btn = (Button) e.getSource();
        animateButton(btn);
        appendOperator(btn.getText());
    }

    @FXML
    private void handleEquals(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        calculate();
    }

    @FXML
    private void handleAllClear(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        allClear();
    }

    @FXML
    private void handleClearEntry(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        clearEntry();
    }

    @FXML
    private void handleBackspace(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        backspace();
    }

    @FXML
    private void handlePercent(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        handlePercent();
    }

    @FXML
    private void handleNegate(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        negate();
    }

    @FXML
    private void handleMemoryStore(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        try {
            double val = Double.parseDouble(currentInput.isEmpty() ? "0" : currentInput);
            memoryStore.store(val);
            memoryIndicator.setVisible(true);
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void handleMemoryRecall(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        double val = memoryStore.recall();
        currentInput = formatNumber(val);
        expression = currentInput;
        resultLabel.setText(currentInput);
        justCalculated = false;
    }

    @FXML
    private void handleMemoryAdd(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        try {
            double val = Double.parseDouble(currentInput.isEmpty() ? "0" : currentInput);
            memoryStore.add(val);
            memoryIndicator.setVisible(true);
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void handleMemorySubtract(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        try {
            double val = Double.parseDouble(currentInput.isEmpty() ? "0" : currentInput);
            memoryStore.subtract(val);
            memoryIndicator.setVisible(true);
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void handleMemoryClear(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        memoryStore.clear();
        memoryIndicator.setVisible(false);
    }

    @FXML
    private void handleSin(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("sin");
    }

    @FXML
    private void handleCos(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("cos");
    }

    @FXML
    private void handleTan(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("tan");
    }

    @FXML
    private void handleAsin(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("asin");
    }

    @FXML
    private void handleAcos(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("acos");
    }

    @FXML
    private void handleAtan(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("atan");
    }

    @FXML
    private void handleLog(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("log");
    }

    @FXML
    private void handleLn(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("ln");
    }

    @FXML
    private void handleSquare(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("square");
    }

    @FXML
    private void handleCube(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("cube");
    }

    @FXML
    private void handleSqrt(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("sqrt");
    }

    @FXML
    private void handleCbrt(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("cbrt");
    }

    @FXML
    private void handlePower(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        appendOperator("^");
    }

    @FXML
    private void handleFactorial(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("factorial");
    }

    @FXML
    private void handleAbs(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        applyUnaryFunction("abs");
    }

    @FXML
    private void handlePi(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        insertConstant(String.valueOf(Math.PI));
    }

    @FXML
    private void handleE(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        insertConstant(String.valueOf(Math.E));
    }

    @FXML
    private void handleOpenParen(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        appendToExpression("(");
    }

    @FXML
    private void handleCloseParen(javafx.event.ActionEvent e) {
        animateButton((Button) e.getSource());
        appendToExpression(")");
    }

    @FXML
    private void handleToggleDegRad(javafx.event.ActionEvent e) {
        isDegrees = !isDegrees;
        degRadBtn.setText(isDegrees ? "DEG" : "RAD");
        calculator.setDegrees(isDegrees);
        updateModeLabel();
    }

    @FXML
    private void handleToggleScientific(javafx.event.ActionEvent e) {
        scientificVisible = !scientificVisible;
        scientificPanel.setVisible(scientificVisible);
        scientificPanel.setManaged(scientificVisible);
        toggleScientificBtn.setText(scientificVisible ? "STD" : "SCI");
    }

    @FXML
    private void handleToggleHistory(javafx.event.ActionEvent e) {
        historyVisible = !historyVisible;
        historyPanel.setVisible(historyVisible);
        historyPanel.setManaged(historyVisible);
        toggleHistoryBtn.getStyleClass().toggle("active");
        if (historyVisible) refreshHistoryList();
    }

    @FXML
    private void handleClearHistory(javafx.event.ActionEvent e) {
        historyManager.clear();
        historyList.getChildren().clear();
    }

    private void appendDigit(String digit) {
        if (justCalculated) {
            currentInput = digit;
            expression = digit;
            justCalculated = false;
        } else {
            if (currentInput.equals("0") && !digit.equals(".")) {
                currentInput = digit;
            } else {
                currentInput += digit;
            }
        }
        resultLabel.setText(currentInput.isEmpty() ? "0" : currentInput);
        expressionLabel.setText(expression.isEmpty() ? currentInput : expression + currentInput.charAt(currentInput.length() - 1));
    }

    private void appendDecimal() {
        if (justCalculated) {
            currentInput = "0.";
            expression = "0.";
            justCalculated = false;
        } else if (!currentInput.contains(".")) {
            if (currentInput.isEmpty()) currentInput = "0";
            currentInput += ".";
        }
        resultLabel.setText(currentInput);
    }

    private void appendOperator(String op) {
        if (!currentInput.isEmpty() || justCalculated) {
            expression = (expression.isEmpty() ? currentInput : expression) + " " + op + " ";
            currentInput = "";
            expressionLabel.setText(expression);
            justCalculated = false;
        }
    }

    private void appendToExpression(String token) {
        expression += token;
        currentInput += token;
        expressionLabel.setText(expression);
        resultLabel.setText(currentInput);
    }

    private void insertConstant(String value) {
        currentInput = value;
        expression = value;
        resultLabel.setText(formatNumber(Double.parseDouble(value)));
        expressionLabel.setText(expression);
        justCalculated = true;
    }

    private void calculate() {
        String fullExpr = expression + currentInput;
        if (fullExpr.isEmpty()) return;

        try {
            CalculationResult result = calculator.evaluate(fullExpr);
            String formatted = formatNumber(result.value());
            historyManager.add(new HistoryEntry(fullExpr, formatted));
            expressionLabel.setText(fullExpr + " =");
            resultLabel.setText(formatted);
            expression = formatted;
            currentInput = formatted;
            justCalculated = true;
            if (historyVisible) refreshHistoryList();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void applyUnaryFunction(String fn) {
        String input = currentInput.isEmpty() ? "0" : currentInput;
        try {
            double val = Double.parseDouble(input);
            CalculationResult result = calculator.applyFunction(fn, val);
            String label = fn + "(" + formatNumber(val) + ")";
            String formatted = formatNumber(result.value());
            historyManager.add(new HistoryEntry(label, formatted));
            expressionLabel.setText(label + " =");
            resultLabel.setText(formatted);
            currentInput = formatted;
            expression = formatted;
            justCalculated = true;
            if (historyVisible) refreshHistoryList();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handlePercent() {
        if (!currentInput.isEmpty()) {
            try {
                double val = Double.parseDouble(currentInput) / 100.0;
                currentInput = formatNumber(val);
                resultLabel.setText(currentInput);
            } catch (NumberFormatException ignored) {}
        }
    }

    private void negate() {
        if (!currentInput.isEmpty() && !currentInput.equals("0")) {
            if (currentInput.startsWith("-")) {
                currentInput = currentInput.substring(1);
            } else {
                currentInput = "-" + currentInput;
            }
            resultLabel.setText(currentInput);
        }
    }

    private void allClear() {
        currentInput = "";
        expression = "";
        justCalculated = false;
        resultLabel.setText("0");
        expressionLabel.setText("");
    }

    private void clearEntry() {
        currentInput = "";
        resultLabel.setText("0");
    }

    private void backspace() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            resultLabel.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }

    private void showError(String msg) {
        String display = (msg != null && !msg.isEmpty()) ? msg : "Error";
        resultLabel.setText(display);
        resultLabel.getStyleClass().add("error");
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
            resultLabel.getStyleClass().remove("error");
            allClear();
        }));
        t.play();
    }

    private void refreshHistoryList() {
        historyList.getChildren().clear();
        List<HistoryEntry> entries = historyManager.getAll();
        for (int i = entries.size() - 1; i >= 0; i--) {
            HistoryEntry entry = entries.get(i);
            HBox row = buildHistoryRow(entry);
            historyList.getChildren().add(row);
        }
        Platform.runLater(() -> historyScroll.setVvalue(0));
    }

    private HBox buildHistoryRow(HistoryEntry entry) {
        VBox textBlock = new VBox(2);
        Label exprLbl = new Label(entry.expression());
        exprLbl.getStyleClass().add("history-expr");
        Label resultLbl = new Label(entry.result());
        resultLbl.getStyleClass().add("history-result");
        textBlock.getChildren().addAll(exprLbl, resultLbl);
        HBox.setHgrow(textBlock, Priority.ALWAYS);

        Button useBtn = new Button("↩");
        useBtn.getStyleClass().add("history-use-btn");
        useBtn.setOnAction(ev -> {
            currentInput = entry.result();
            expression = entry.result();
            resultLabel.setText(entry.result());
            expressionLabel.setText(entry.expression() + " =");
            justCalculated = true;
        });

        HBox row = new HBox(8, textBlock, useBtn);
        row.getStyleClass().add("history-row");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void animateButton(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(80), btn);
        st.setToX(0.92);
        st.setToY(0.92);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private String formatNumber(double val) {
        if (Double.isNaN(val)) return "Not a number";
        if (Double.isInfinite(val)) return val > 0 ? "∞" : "-∞";
        if (val == Math.floor(val) && !Double.isInfinite(val) && Math.abs(val) < 1e15) {
            return String.valueOf((long) val);
        }
        return df.format(val);
    }

    private void updateModeLabel() {
        modeLabel.setText(isDegrees ? "DEG" : "RAD");
    }
}