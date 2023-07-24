package com.runnertest;

import io.cucumber.core.cli.Main;

public class ParallelTest {
    public static void main(String[] args) throws Throwable {
        // Provide the command-line arguments
        String[] argv = {
                "--glue", "com.steps",
                "--plugin", "pretty",
                "--threads", "5",
                "Features/"
        };

        // Run the Cucumber features using the CLI
        Main.run(argv, Thread.currentThread().getContextClassLoader());
    }
}
