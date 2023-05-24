package it.antoniocaccamo.labs.apim.client.command;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Component @Slf4j
@Command(name = "labs-api-client",
        mixinStandardHelpOptions = true,
        subcommands = {LabsApimClientCommandWithSecret.class, LabsApimClientCommandWithCertificate.class}
)
public class LabsApimClientCommand implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        CommandLine.usage(this, System.out);
        return 2;
    }


}
