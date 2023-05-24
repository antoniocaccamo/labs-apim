package it.antoniocaccamo.labs.apim.client;

import it.antoniocaccamo.labs.apim.client.command.LabsApimClientCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication @RequiredArgsConstructor
public class LabsApimClientApplication  implements CommandLineRunner, ExitCodeGenerator{

    private int exitCode = 0;
    private final IFactory factory;

    private final LabsApimClientCommand command;

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(LabsApimClientApplication.class, args)));
    }
    
    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(command, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
    
}
