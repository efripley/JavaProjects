package ernie.ripley;

import static kiss.API.*;
import java.io.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.terminal.*;

public class App{

  Terminal terminal;

	void run() throws IOException {
    terminal = TerminalFacade.createTerminal();
    terminal.enterPrivateMode();
    pause(5);
    terminal.exitPrivateMode();
	}
}
