package com.autotests.lichessbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
public class LogsPageController {

    private static final Path LOG_FILE_PATH = Path.of("logs", "lichess-backend.log");
    private static final int MAX_LINES = 200;

    @GetMapping("/logs-page")
    public String logsPage(Model model) {
        log.info("Opening logs page");

        List<String> logs = readLastLogLines();

        model.addAttribute("logs", logs);

        return "logs";
    }

    private List<String> readLastLogLines() {
        try {
            if (!Files.exists(LOG_FILE_PATH)) {
                return List.of("Log file does not exist yet.");
            }

            List<String> lines = Files.readAllLines(LOG_FILE_PATH);

            int fromIndex = Math.max(0, lines.size() - MAX_LINES);

            return lines.subList(fromIndex, lines.size());
        } catch (IOException exception) {
            log.error("Failed to read log file", exception);
            return Collections.singletonList("Failed to read log file: " + exception.getMessage());
        }
    }
}